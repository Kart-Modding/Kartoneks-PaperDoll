pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") { name = "FabricMC" }
        maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
        maven("https://maven.minecraftforge.net/") { name = "MinecraftForge" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }

    // The NeoForge and Forge buildscripts are applied per node and can't pin plugin versions themselves
    plugins {
        id("net.neoforged.moddev") version "2.0.148"
        id("net.minecraftforge.gradle") version "7.0.40"
        // ForgeGradle 7 leaves Jar-in-Jar to Forge's separate plugin
        id("net.minecraftforge.jarjar") version "0.2.3"
    }
}

plugins {
    // https://stonecutter.kikugie.dev/
    id("dev.kikugie.stonecutter") version "0.9.8"

    // Applies the right Loom for each Minecraft version (remapping before 26.1, plain from 26.1)
    id("dev.kikugie.loom-back-compat") version "0.4.2"

    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

stonecutter {
    create(rootProject) {
        /**
         * One node per loader, named `{project}-{loader}` and built by `build.{loader}.gradle.kts`.
         * [version] is the Minecraft version Stonecutter compares against in `//? if` comments.
         *
         * Quilt has no node of its own: Quilt Loader runs the Fabric jar.
         */
        fun match(project: String, version: String = project) {
            for (loader in listOf("fabric", "neoforge", "forge")) {
                version("$project-$loader", version).buildscript("build.$loader.gradle.kts")
            }
        }

        for (it in listOf("1.21.1", "1.21.3", "1.21.4", "1.21.5", "1.21.6", "1.21.7", "1.21.8",
                          "1.21.9", "1.21.10", "1.21.11")) match(it)
        match("26.1.x", version = "26.1")
        match("26.2.x", version = "26.2")
        match("26.3.x", version = "26.3")
        vcsVersion = "26.3.x-fabric"
    }
}

rootProject.name = "Kartoneks-PaperDoll"
