plugins {
    // Applies the correct Loom variant for the Minecraft version
    id("dev.kikugie.loom-back-compat")
    id("me.modmuss50.mod-publish-plugin")
}

// DO NOT set group = ...!
version = "${property("mod.version")}+${sc.current.version}"
base.archivesName = "${property("mod.archives_name")}-fabric"

extra["apdGame"] = sc.current.version
apply(from = rootProject.file("gradle/target.gradle.kts"))
val requiredJava = extra["apdJava"] as JavaVersion
val accessWidener = extra["apdAccessWidener"] as File
val clientMixins = extra["apdClientMixins"] as String
val modId = property("mod.id") as String
val modName = property("mod.name") as String
val modVersion = property("mod.version") as String

repositories {
    exclusiveContent {
        forRepository { maven("https://api.modrinth.com/maven") { name = "Modrinth" } }
        filter { includeGroup("maven.modrinth") }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    // Applies Mojang Mappings on obfuscated versions
    loomx.applyMojangMappings()

    // Use `mod{dependency type}` even on 26.1+ - loom-back-compat converts them
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${sc.properties.get<String>("deps.fabric_api")}")

    // Mod Menu config screen integration - compile-time only, optional at runtime
    val modmenuVersion: String = sc.properties["deps.modmenu"]
    modCompileOnly("maven.modrinth:modmenu:$modmenuVersion")
    modLocalRuntime("maven.modrinth:modmenu:$modmenuVersion")
}

loom {
    accessWidenerPath = accessWidener

    runConfigs.all {
        preferGradleTask = true
        generateRunConfig = true
        runDirectory = rootProject.file("run") // Shares the run directory between versions
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

    processResources {
        val props = mapOf(
            "id" to modId,
            "name" to modName,
            "version" to "$modVersion+${sc.current.version}",
            "minecraft" to sc.properties.get<String>("mod.mc_compat"),
            "java" to "JAVA_${requiredJava.majorVersion}",
            "clientMixins" to clientMixins,
        )
        inputs.properties(props)
        filesMatching(listOf("fabric.mod.json", "*.mixins.json")) { expand(props) }

        exclude("META-INF/neoforge.mods.toml", "META-INF/mods.toml", "icon.png")
        // The access widener, generated for this version
        from(accessWidener)
    }

    withType<Jar>().configureEach {
        from(rootProject.file("COPYING"), rootProject.file("COPYING.LESSER"))
        from(rootProject.file("licenses")) { into("licenses") }
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds mod jars and copies results to `build/libs/{mod version}/`"

        inputs.property("version", modVersion)
        // loomx.modJar returns the jar task for the applied loom variant
        from(loomx.modJar.flatMap { it.archiveFile })
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

    file = loomx.modJar.flatMap { it.archiveFile }
    displayName = "$modName ${project.version} (Fabric)"
    version = project.version.toString()
    changelog = providers.environmentVariable("PUBLISH_CHANGELOG").orElse("")
    type = STABLE
    modLoaders.addAll("fabric", "quilt")
    // A secret the repository doesn't have reaches CI as an empty string
    dryRun = providers.environmentVariable("MODRINTH_TOKEN").orNull.isNullOrBlank()

    if (modrinthId.isNotEmpty()) modrinth {
        projectId = modrinthId
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.addAll(compatibleVersions)
        requires("fabric-api")
        optional("modmenu")
    }

    if (curseforgeId.isNotEmpty()) curseforge {
        projectId = curseforgeId
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        minecraftVersions.addAll(compatibleVersions)
        javaVersions.add(requiredJava)
        clientRequired = true
        serverRequired = false
        requires("fabric-api")
        optional("modmenu")
    }
}
