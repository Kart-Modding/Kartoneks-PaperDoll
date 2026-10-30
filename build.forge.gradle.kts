plugins {
    // Must come before ForgeGradle, which looks for it when it creates the `minecraft` extension
    id("net.minecraftforge.accesstransformers")
    id("net.minecraftforge.gradle")
    // ForgeGradle 7 leaves Jar-in-Jar to Forge's separate plugin
    id("net.minecraftforge.jarjar")
    id("me.modmuss50.mod-publish-plugin")
}

// DO NOT set group = ...!
version = "${property("mod.version")}+${sc.current.version}"
base.archivesName = "${property("mod.archives_name")}-forge"

extra["apdGame"] = sc.current.version
apply(from = rootProject.file("gradle/target.gradle.kts"))
val requiredJava = extra["apdJava"] as JavaVersion
val accessTransformer = extra["apdAccessTransformer"] as File
// Forge 56 and 57 (1.21.6, 1.21.7) have no HUD layer event; a mixin draws the paper doll there instead
val clientMixins = (extra["apdClientMixins"] as String).let {
    if (sc.current.parsed >= "1.21.6" && sc.current.parsed < "1.21.8") "$it,\n        \"forge.ForgeGuiMixin\"" else it
}
val modId = property("mod.id") as String
val modName = property("mod.name") as String
val modVersion = property("mod.version") as String
val mixinConfig = "ayame-paperdoll.mixins.json"

// `<minecraft>-<forge>`, see stonecutter.properties.toml
val forgeVersion: String = sc.properties["deps.forge"]
// Forge's Mixin 0.8.7 knows compatibility levels up to JAVA_21; the game's own classes are Java 25 on 26.x
val mixinJava = "JAVA_${minOf(requiredJava.majorVersion.toInt(), 21)}"

minecraft {
    // Reads META-INF/accesstransformer.cfg from the first resources folder (set below)
    setAccessTransformer(true)

    // 26.1+ ships unobfuscated, older versions need Mojang's mappings applied.
    // Forge runs the game under Mojang's names on all of them, so the jar needs no remapping.
    if (sc.current.parsed < "26.1") mappings("official", forgeVersion.substringBefore('-'))

    runs {
        register("client") {
            workingDir.convention(rootProject.layout.projectDirectory.dir("run")) // Shares the run directory between versions
            // Mixin picks its config up from the jar manifest in production, but not in dev
            args("--mixin.config=$mixinConfig")
        }
    }
}

// The generated META-INF/accesstransformer.cfg goes first among the resources, where ForgeGradle looks
// for it. (Handing ForgeGradle the file directly overflows the stack in its Mavenizer.)
sourceSets.main {
    resources.setSrcDirs(listOf(accessTransformer.parentFile.parentFile) + resources.srcDirs)
}

repositories {
    minecraft.mavenizer(this)
    maven(fg.forgeMaven)
    maven(fg.minecraftLibsMaven)
    mavenCentral()
}

// The jar that ships is jarJar's: it takes the plain jar's name, and the plain jar steps aside as "-slim"
jarJar.register {
    archiveClassifier = null
}

dependencies {
    implementation(minecraft.dependency("net.minecraftforge:forge:$forgeVersion"))

    // MixinExtras ships with Forge from 60 (1.21.10) on. Before that, the mod carries it in its jar.
    val mixinExtras = property("deps.mixinextras") as String
    if (sc.current.parsed < "1.21.10") {
        compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:$mixinExtras")!!)
        implementation("io.github.llamalad7:mixinextras-forge:$mixinExtras")
        "jarJar"("io.github.llamalad7:mixinextras-forge:$mixinExtras")
    } else {
        // Bundled by Forge at runtime, only needed to compile against
        compileOnly("io.github.llamalad7:mixinextras-common:$mixinExtras")
    }
}

java {
    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava

    toolchain {
        languageVersion = JavaLanguageVersion.of(requiredJava.majorVersion)
    }
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    // setSrcDirs above drops the task dependency of Stonecutter's generated resources folder
    named("sourcesJar") { dependsOn("stonecutterGenerate") }

    processResources {
        dependsOn("stonecutterGenerate")
        val props = mapOf(
            "id" to modId,
            "name" to modName,
            "version" to "$modVersion+${sc.current.version}",
            "minecraft" to sc.properties.get<String>("mod.mc_compat"),
            "java" to mixinJava,
            "clientMixins" to clientMixins,
        )
        inputs.properties(props)
        filesMatching(listOf("META-INF/mods.toml", "*.mixins.json")) { expand(props) }

        exclude("fabric.mod.json", "META-INF/neoforge.mods.toml")
    }

    jar {
        archiveClassifier = "slim"
    }

    withType<Jar>().configureEach {
        // Forge's Mixin finds configs through the manifest rather than mods.toml.
        // Set on the jarJar task too, so the shipped jar has it either way.
        manifest.attributes("MixinConfigs" to mixinConfig)
    }

    // Not on the jarJar task: it already carries everything the plain jar does
    withType<Jar>().matching { it.name != "jarJar" }.configureEach {
        from(rootProject.file("COPYING"), rootProject.file("COPYING.LESSER"))
        from(rootProject.file("licenses")) { into("licenses") }
    }

    named("assemble") { dependsOn("jarJar") }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds the mod jar and copies it to `build/libs/{mod version}/`"

        inputs.property("version", modVersion)
        // The jarJar task's output, never the -slim jar
        from(named("jarJar"))
        into(rootProject.layout.buildDirectory.file("libs/$modVersion"))
    }
}

// Uploads this node's jar to Modrinth and CurseForge.
// CI runs it when mod.version changes on main. Without MODRINTH_TOKEN it is a dry run, so a local
// `./gradlew publishMods` only logs what it would upload.
publishMods {
    val modrinthId = findProperty("publish.modrinth")?.toString().orEmpty()
    val curseforgeId = findProperty("publish.curseforge")?.toString().orEmpty()
    val compatibleVersions: List<String> = sc.properties.rawOrNull("mod", "mc_releases")
        ?.asList().orEmpty().map { it.toString() }

    file = tasks.named<Jar>("jarJar").flatMap { it.archiveFile }
    displayName = "$modName ${project.version} (Forge)"
    version = project.version.toString()
    changelog = providers.environmentVariable("PUBLISH_CHANGELOG").orElse("")
    type = STABLE
    modLoaders.add("forge")
    // A secret the repository doesn't have reaches CI as an empty string
    dryRun = providers.environmentVariable("MODRINTH_TOKEN").orNull.isNullOrBlank()

    if (modrinthId.isNotEmpty()) modrinth {
        projectId = modrinthId
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.addAll(compatibleVersions)
    }

    if (curseforgeId.isNotEmpty()) curseforge {
        projectId = curseforgeId
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        minecraftVersions.addAll(compatibleVersions)
        javaVersions.add(requiredJava)
        clientRequired = true
        serverRequired = false
    }
}
