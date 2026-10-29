plugins {
    id("net.neoforged.moddev")
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
