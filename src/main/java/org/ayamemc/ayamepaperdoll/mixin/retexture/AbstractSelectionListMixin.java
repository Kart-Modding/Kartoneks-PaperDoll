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

//? if <1.21.4 {
/*package org.ayamemc.ayamepaperdoll.mixin.retexture;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
*///?}
//? if >=1.21.3 && <1.21.4 {
/*import net.minecraft.client.renderer.RenderType;
*///?}
//? if <1.21.4 {
/*import net.minecraft.resources.ResourceLocation;
import org.ayamemc.ayamepaperdoll.config.view.Retextured;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
*///?}
//? if >=1.21.3 && <1.21.4 {
/*
import java.util.function.Function;
*///?}
//? if <1.21.4 {
/*
@Mixin(AbstractSelectionList.class)
public abstract class AbstractSelectionListMixin {
    @WrapOperation(method = "renderWidget", at = {
*///?}
            //? if >=1.21.3 && <1.21.4 {
            /*@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIII)V")
            *///?}
            //? if <1.21.3 {
            /*@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V")
            *///?}
    //? if <1.21.4 {
    /*})
    *///?}
    //? if >=1.21.3 && <1.21.4 {
    /*public void drawTransparentTextFieldTexture(GuiGraphics instance, Function<ResourceLocation, RenderType> function, ResourceLocation resourceLocation, int i, int j, int k, int l, Operation<Void> original) {
    *///?}
    //? if <1.21.3 {
    /*public void drawTransparentTextFieldTexture(GuiGraphics instance, ResourceLocation sprite, int x, int y, int width, int height, Operation<Void> original) {
    *///?}
        //? if <1.21.4 {
        /*if (this instanceof Retextured retextured) {
        *///?}
            //? if >=1.21.3 && <1.21.4 {
            /*original.call(instance, function, retextured.retexture(resourceLocation), i, j, k, l);
            *///?}
            //? if <1.21.3 {
            /*original.call(instance, retextured.retexture(sprite), x, y, width, height);
            *///?}
        //? if <1.21.4 {
        /*} else {
        *///?}
            //? if >=1.21.3 && <1.21.4 {
            /*original.call(instance, function, resourceLocation, i, j, k, l);
            *///?}
            //? if <1.21.3 {
            /*original.call(instance, sprite, x, y, width, height);
            *///?}
        //? if <1.21.4 {
        /*}

    }
}
        *///?}
