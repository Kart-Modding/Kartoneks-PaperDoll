plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "26.3.x-fabric"

// Builds every node's jar, output lands in build/libs/<mod version>/
tasks.register("chiseledBuild") {
    group = "project"
    dependsOn(stonecutter.tasks.named("buildAndCollect").map { it.values })
}

// Builds one loader's jars for every Minecraft version (CI runs one job per loader)
for (loader in listOf("fabric", "neoforge", "forge")) {
    tasks.register("build${loader.replaceFirstChar(Char::uppercase)}") {
        group = "project"
        dependsOn(subprojects.filter { it.name.endsWith("-$loader") }.map { "${it.path}:buildAndCollect" })
    }
}

// See https://stonecutter.kikugie.dev/wiki/config/params
stonecutter parameters {
    val loader = current.project.substringAfterLast('-')

    // Makes version- and loader-specific sections of `stonecutter.properties.toml` apply
    properties {
        tags(current.project.substringBeforeLast('-'), loader)
    }

    // Loader constants for Stonecutter comments (`//? if forge {`)
    constants {
        match(loader, "fabric", "neoforge", "forge")
    }
}
