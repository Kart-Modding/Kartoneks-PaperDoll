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

//? if <1.21.5 {
/*package org.ayamemc.ayamepaperdoll.mixin.patch;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.ayamemc.ayamepaperdoll.mixininterface.BufferSourceMixinInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MultiBufferSource.BufferSource.class)
public abstract class BufferSourceMixin implements BufferSourceMixinInterface {

    @Unique
    private boolean ayame_PaperDoll$forceDisableCulling = false;

    @Override
    public void ayame_PaperDoll$setForceDisableCulling(boolean disableCulling) {
        this.ayame_PaperDoll$forceDisableCulling = disableCulling;
    }

    @WrapOperation(method = "endBatch(Lnet/minecraft/client/renderer/RenderType;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endBatch(Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/BufferBuilder;)V"))
    void disableCulling(MultiBufferSource.BufferSource instance, RenderType layer, BufferBuilder builder, Operation<Void> original) {
        if (this.ayame_PaperDoll$forceDisableCulling) {
            RenderSystem.disableCull();
            original.call(instance, layer, builder);
            RenderSystem.enableCull();
        } else {
            original.call(instance, layer, builder);
        }
    }
}
*///?}
//? if >=1.21.6 && <26.1 {
/*package org.ayamemc.ayamepaperdoll.mixin.patch;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.MultiBufferSource;
*///?}
//? if >=1.21.11 && <26.1 {
/*import net.minecraft.client.renderer.rendertype.RenderType;
*///?}
//? if >=1.21.6 && <1.21.11 {
/*import net.minecraft.client.renderer.RenderType;
*///?}
//? if >=1.21.6 && <26.1 {
/*import org.ayamemc.ayamepaperdoll.AyamePaperDoll;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MultiBufferSource.BufferSource.class)
public class BufferSourceMixin {
*///?}
    //? if >=1.21.11 && <26.1 {
    /*@WrapOperation(method = "endBatch(Lnet/minecraft/client/renderer/rendertype/RenderType;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endBatch(Lnet/minecraft/client/renderer/rendertype/RenderType;Lcom/mojang/blaze3d/vertex/BufferBuilder;)V"))
    *///?}
    //? if >=1.21.6 && <1.21.11 {
    /*@WrapOperation(method = "endBatch(Lnet/minecraft/client/renderer/RenderType;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endBatch(Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/BufferBuilder;)V"))
    *///?}
    //? if >=1.21.6 && <26.1 {
    /*void disableCulling(MultiBufferSource.BufferSource instance, RenderType layer, BufferBuilder builder, Operation<Void> original) {
        if (AyamePaperDoll.identifier) {
            GL11.glFrontFace(GL11.GL_CW);
            original.call(instance, layer, builder);
            GL11.glFrontFace(GL11.GL_CCW);
        } else {
            original.call(instance, layer, builder);
        }
    }
}
    *///?}
