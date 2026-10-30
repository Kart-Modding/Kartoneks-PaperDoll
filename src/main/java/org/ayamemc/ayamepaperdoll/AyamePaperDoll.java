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

package org.ayamemc.ayamepaperdoll;

//? if >=26.1 && <26.3 {
/*import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
*///?}
import com.mojang.blaze3d.platform.InputConstants;
//? if >=26.2 && <26.3 {
/*import net.minecraft.client.renderer.BindGroupLayouts;
*///?}
//? if >=26.1 && <26.2 {
/*import com.mojang.blaze3d.shaders.UniformType;
*///?}
//? if >=26.1 && <26.3 {
/*import com.mojang.blaze3d.vertex.DefaultVertexFormat;
*///?}
//? if >=26.2 && <26.3 {
/*import com.mojang.blaze3d.PrimitiveTopology;
*///?}
//? if >=26.1 && <26.2 {
/*import com.mojang.blaze3d.vertex.VertexFormat;
*///?}
import net.minecraft.client.KeyMapping;
//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?}
//? if <1.21.11 {
/*import net.minecraft.resources.ResourceLocation;
*///?}
import org.ayamemc.ayamepaperdoll.config.Configs;
import org.ayamemc.ayamepaperdoll.config.persistence.ConfigPersistence;
import org.ayamemc.ayamepaperdoll.config.persistence.GsonConfigPersistence;
//? if <26.3 {
/*import org.lwjgl.glfw.GLFW;
*///?}
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public final class AyamePaperDoll {
    public static final String MOD_ID = "ayame_paperdoll";
    public static final String MOD_NAME = "Kartoneks Paperdoll";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    //? if >=1.21.6 && <26.1 {
    /*public static boolean identifier = false;
    *///?}
    //? if >=1.21.9 {
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(path("keys"));
    //?}
    public static final KeyMapping SHOW_PAPERDOLL_KEY = new KeyMapping(
            "key.%s.showPaperDoll".formatted(MOD_ID),
            //? if >=26.3 {
            InputConstants.Type.KEYBOARD,
            InputConstants.KEY_F8,
            //?}
            //? if <26.3 {
            /*InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F8,
            *///?}
            //? if >=1.21.9 {
            CATEGORY);
            //?}
            //? if <1.21.9 {
            /*"key.%s.category".formatted(MOD_ID));
            *///?}
    public static final KeyMapping OPEN_CONFIG_GUI = new KeyMapping(
            "key.%s.openConfigGui".formatted(MOD_ID),
            //? if >=26.3 {
            InputConstants.Type.KEYBOARD,
            //?}
            //? if <26.3 {
            /*InputConstants.Type.KEYSYM,
            *///?}
            InputConstants.UNKNOWN.getValue(),
            //? if >=1.21.9 {
            CATEGORY);
            //?}
            //? if <1.21.9 {
            /*"key.%s.category".formatted(MOD_ID));
            *///?}
    public static final Configs CONFIGS = new Configs();
    public static final ConfigPersistence CONFIG_PERSISTENCE = new GsonConfigPersistence(Path.of("config/" + MOD_ID + "_v0.json"));
    //? if >=26.2 && <26.3 {
    /*private static final RenderPipeline.Snippet GUI_TEXTURED_SNIPPET_NEW = RenderPipeline.builder()
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
    *///?}
    //? if >=26.1 && <26.2 {
    /*private static final RenderPipeline.Snippet MATRICES_PROJECTION_SNIPPET = RenderPipeline.builder()
            .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
            .withUniform("Projection", UniformType.UNIFORM_BUFFER)
            .buildSnippet();

    private static final RenderPipeline.Snippet GUI_TEXTURED_SNIPPET_NEW = RenderPipeline.builder(MATRICES_PROJECTION_SNIPPET)
    *///?}
            //? if >=26.1 && <26.3 {
            /*.withVertexShader(path("core/position_color_tex_lightmap"))
            .withFragmentShader(path("core/position_color_tex_lightmap"))
            *///?}
            //? if >=26.2 && <26.3 {
            /*.withBindGroupLayout(BindGroupLayouts.SAMPLER0_SAMPLER2)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            *///?}
            //? if >=26.1 && <26.2 {
            /*.withSampler("Sampler0")
            .withSampler("Sampler2")
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS)
            *///?}
            //? if >=26.1 && <26.3 {
            /*.buildSnippet();
            *///?}
    //? if >=26.1 && <26.3 {
    /*public static final RenderPipeline MOD_PIPELINE = RenderPipeline.builder(GUI_TEXTURED_SNIPPET_NEW)
            .withLocation(path("pipeline/mod_pipeline"))
    *///?}
            //? if >=26.1 && <26.2 {
            /*.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA))
            *///?}
            //? if >=26.1 && <26.3 {
            /*.build();
            *///?}

    //? if >=1.21.11 {
    public static Identifier path(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    //?}
    //? if <1.21.11 {
    /*public static ResourceLocation path(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    *///?}
    }

    public static void init() {
        // Write common init code here.
        CONFIG_PERSISTENCE.load(AyamePaperDoll.CONFIGS.getOptions());
    }
}
