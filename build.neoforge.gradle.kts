plugins {
    id("net.neoforged.moddev")
    id("me.modmuss50.mod-publish-plugin")
    id("neoforge-mutex")
}

// DO NOT set group = ...!
version = "${property("mod.version")}+${sc.current.version}"
base.archivesName = "${property("mod.archives_name")}-neoforge"

extra["apdGame"] = sc.current.version
apply(from = rootProject.file("gradle/target.gradle.kts"))
val requiredJava = extra["apdJava"] as JavaVersion
val accessTransformer = extra["apdAccessTransformer"] as File
val clientMixins = extra["apdClientMixins"] as String
val modId = property("mod.id") as String
val modName = property("mod.name") as String
val modVersion = property("mod.version") as String

neoForge {
    version = sc.properties.get<String>("deps.neoforge")
    accessTransformers.from(accessTransformer)

    mods {
        register(modId) {
            sourceSet(sourceSets.main.get())
        }
    }

    runs {
        register("client") {
            gameDirectory = rootProject.file("run") // Shares the run directory between versions
            client()
        }
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
            "neoforge" to sc.properties.get<String>("deps.neoforge"),
            "java" to "JAVA_${requiredJava.majorVersion}",
            "clientMixins" to clientMixins,
        )
        inputs.properties(props)
        filesMatching(listOf("META-INF/neoforge.mods.toml", "*.mixins.json")) { expand(props) }

        exclude("fabric.mod.json", "META-INF/mods.toml")
        // META-INF/accesstransformer.cfg, generated for this version
        from(accessTransformer.parentFile.parentFile)
    }

    withType<Jar>().configureEach {
        from(rootProject.file("COPYING"), rootProject.file("COPYING.LESSER"))
        from(rootProject.file("licenses")) { into("licenses") }
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds mod jars and copies results to `build/libs/{mod version}/`"

        inputs.property("version", modVersion)
        from(jar.flatMap { it.archiveFile })
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

    file = tasks.jar.flatMap { it.archiveFile }
    displayName = "$modName ${project.version} (NeoForge)"
    version = project.version.toString()
    changelog = providers.environmentVariable("PUBLISH_CHANGELOG").orElse("")
    type = STABLE
    modLoaders.add("neoforge")
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
