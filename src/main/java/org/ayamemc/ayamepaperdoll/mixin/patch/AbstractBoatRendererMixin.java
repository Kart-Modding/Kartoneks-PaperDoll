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

//? if >=1.21.3 && <26.1 {
/*package org.ayamemc.ayamepaperdoll.mixin.patch;

import com.mojang.blaze3d.vertex.PoseStack;
*///?}
//? if >=1.21.9 && <26.1 {
/*import net.minecraft.client.renderer.SubmitNodeCollector;
*///?}
//? if >=1.21.3 && <1.21.9 {
/*import net.minecraft.client.renderer.MultiBufferSource;
*///?}
//? if >=1.21.3 && <26.1 {
/*import net.minecraft.client.renderer.entity.AbstractBoatRenderer;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
*///?}
//? if >=1.21.9 && <26.1 {
/*import net.minecraft.client.renderer.state.CameraRenderState;
*///?}
//? if >=1.21.3 && <26.1 {
/*import org.ayamemc.ayamepaperdoll.hud.PaperDollRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoatRenderer.class)
public abstract class AbstractBoatRendererMixin {
*///?}
    //? if >=1.21.9 && <26.1 {
    /*@Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/BoatRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At("HEAD"))
    private void modifyRotationYaw(BoatRenderState boatRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
    *///?}
    //? if >=1.21.3 && <1.21.9 {
    /*@Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/BoatRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    private void modifyRotationYaw(BoatRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
    *///?}
        //? if >=1.21.3 && <26.1 {
        /*if (PaperDollRenderer.shouldLockRotationYaw() && poseStack instanceof PaperDollRenderer.LockedPaperDoll) {
        *///?}
            //? if >=1.21.9 && <26.1 {
            /*boatRenderState.yRot = 0.0F;
            *///?}
            //? if >=1.21.3 && <1.21.9 {
            /*renderState.yRot = 0.0F;
            *///?}
        //? if >=1.21.3 && <26.1 {
        /*}
    }
}
        *///?}
