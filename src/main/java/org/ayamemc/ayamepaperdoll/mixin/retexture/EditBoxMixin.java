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

package org.ayamemc.ayamepaperdoll.mixin.retexture;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//? if <1.21.5 {
/*import com.mojang.blaze3d.systems.RenderSystem;
*///?}
//? if >=26.3 {
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
//?}
//? if >=1.21.6 && <26.3 {
/*import com.mojang.blaze3d.pipeline.RenderPipeline;
*///?}
//? if >=26.1 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?}
//? if <26.1 {
/*import net.minecraft.client.gui.GuiGraphics;
*///?}
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
//? if >=1.21.3 && <1.21.6 {
/*import net.minecraft.client.renderer.RenderType;
*///?}
import net.minecraft.network.chat.Component;
//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?}
//? if <1.21.11 {
/*import net.minecraft.resources.ResourceLocation;
*///?}
//? if >=1.21.3 {
import net.minecraft.util.ARGB;
//?}
import org.ayamemc.ayamepaperdoll.config.view.Retextured;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//? if >=1.21.3 && <1.21.6 {
/*
import java.util.function.Function;
*///?}

@Mixin(EditBox.class)
public abstract class EditBoxMixin extends AbstractWidget {
    public EditBoxMixin(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    //? if >=26.1 {
    @WrapOperation(method = "extractWidgetRenderState", at = {
    //?}
            //? if >=26.3 {
            @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V")
            //?}
            //? if >=26.1 && <26.3 {
            /*@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V")
            *///?}
    //? if <26.1 {
    /*@WrapOperation(method = "renderWidget", at = {
    *///?}
            //? if >=1.21.11 && <26.1 {
            /*@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V")
            *///?}
            //? if >=1.21.6 && <1.21.11 {
            /*@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIII)V")
            *///?}
            //? if >=1.21.3 && <1.21.6 {
            /*@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIII)V")
            *///?}
            //? if <1.21.3 {
            /*@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V")
            *///?}
    })
    //? if >=26.1 {
    public void drawTransparentTextFieldTexture(GuiGraphicsExtractor instance, RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height, Operation<Void> original) {
    //?}
    //? if >=1.21.11 && <26.1 {
    /*public void drawTransparentTextFieldTexture(GuiGraphics instance, RenderPipeline renderPipeline, Identifier Identifier, int i, int j, int k, int l, Operation<Void> original) {
    *///?}
    //? if >=1.21.6 && <1.21.11 {
    /*public void drawTransparentTextFieldTexture(GuiGraphics instance, RenderPipeline renderPipeline, ResourceLocation resourceLocation, int i, int j, int k, int l, Operation<Void> original) {
    *///?}
    //? if >=1.21.3 && <1.21.6 {
    /*public void drawTransparentTextFieldTexture(GuiGraphics instance, Function<ResourceLocation, RenderType> function, ResourceLocation resourceLocation, int i, int j, int k, int l, Operation<Void> original) {
    *///?}
    //? if <1.21.3 {
    /*public void drawTransparentTextFieldTexture(GuiGraphics instance, ResourceLocation texture, int x, int y, int width, int height, Operation<Void> original) {
    *///?}
        if (this instanceof Retextured retextured) {
            //? if >=1.21.3 {
            int color = ARGB.white(this.alpha);
            //?}
//? if >=1.21.3 && <26.3 {
/*
*///?}
            //? if <1.21.3 {
            /*instance.setColor(1.0f, 1.0f, 1.0f, this.alpha);
            *///?}
//? if >=1.21.5 && <26.3 {
/*//            RenderSystem.enableBlend();
//            RenderSystem.enableDepthTest();
*///?}
            //? if <1.21.5 {
            /*RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            *///?}
//? if >=1.21.3 && <26.3 {
/*
*///?}
            //? if >=26.1 {
            instance.blitSprite(renderPipeline, retextured.retexture(location), x, y, width, height, color);
            //?}
            //? if >=1.21.11 && <26.1 {
            /*instance.blitSprite(renderPipeline, retextured.retexture(Identifier), i, j, k, l, color);
            *///?}
            //? if >=1.21.6 && <1.21.11 {
            /*instance.blitSprite(renderPipeline, retextured.retexture(resourceLocation), i, j, k, l, color);
            *///?}
            //? if >=1.21.3 && <1.21.6 {
            /*instance.blitSprite(function, retextured.retexture(resourceLocation), i, j, k, l, color);
            *///?}
        //? if >=1.21.3 {
        } else {
        //?}
            //? if >=26.1 {
            original.call(instance, renderPipeline, location, x, y, width, height);
            //?}
            //? if >=1.21.11 && <26.1 {
            /*original.call(instance, renderPipeline, Identifier, i, j, k, l);
            *///?}
            //? if >=1.21.6 && <1.21.11 {
            /*original.call(instance, renderPipeline, resourceLocation, i, j, k, l);
            *///?}
            //? if >=1.21.3 && <1.21.6 {
            /*original.call(instance, function, resourceLocation, i, j, k, l);
            *///?}
            //? if <1.21.3 {
            /*original.call(instance, retextured.retexture(texture), x, y, width, height);
            instance.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            return;
            *///?}
        }
        //? if <1.21.3 {
        /*original.call(instance, texture, x, y, width, height);
        *///?}
    }
}
