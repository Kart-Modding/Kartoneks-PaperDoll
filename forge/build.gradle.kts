/*
 *     Highly configurable PaperDoll mod. Forked from Extra Player Renderer.
 *     Copyright (C) 2024-2025  LucunJi(Original author), HappyRespawnanchor
 *
 *     This file is part of Ayame PaperDoll.
 *
 *     Ayame PaperDoll is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Ayame PaperDoll is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Ayame PaperDoll.  If not, see <https://www.gnu.org/licenses/>.
 */

plugins {
    id("net.minecraftforge.gradle") version "7.0.40"
}

val common = project(":common")
val mixinConfig = project.property("mod_mixin_config") as String

// Forge has no Architectury transformer, so the common code is compiled straight into this jar.
sourceSets.main {
    java.srcDir(common.file("src/main/java"))
    resources.srcDir(common.file("src/main/resources"))
}

minecraft {
    runs {
        register("client") {
            workingDir.convention(layout.projectDirectory.dir("run"))
            // Mixin picks its config up from the jar manifest in production, but not in dev
            args("--mixin.config=$mixinConfig")
        }
    }
}

repositories {
    minecraft.mavenizer(this)
    maven(fg.forgeMaven)
    maven(fg.minecraftLibsMaven)
    mavenCentral()
}

dependencies {
    implementation(minecraft.dependency("net.minecraftforge:forge:${rootProject.property("forge_version")}"))
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks.named<ProcessResources>("processResources") {
    val placeholders = mapOf(
        "mod_license" to project.findProperty("mod_license"),
        "mod_version" to project.version,
        "mod_id" to project.findProperty("mod_id"),
        "mod_name" to project.findProperty("mod_name"),
        "mod_homepage_url" to project.findProperty("mod_homepage_url"),
        "mod_description" to project.findProperty("mod_description"),
        "mod_issues_url" to project.findProperty("mod_issues_url"),
        "forge_minecraft_version_range" to project.findProperty("forge_minecraft_version_range"),
    )

    inputs.properties(placeholders)

    filesMatching("META-INF/mods.toml") {
        expand(placeholders)
    }
}

tasks.named<Jar>("jar") {
    // Forge's Mixin finds configs through the manifest rather than mods.toml
    manifest.attributes["MixinConfigs"] = mixinConfig
}
