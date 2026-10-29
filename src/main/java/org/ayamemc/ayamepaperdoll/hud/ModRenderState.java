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

//? if >=1.21.6 {
package org.ayamemc.ayamepaperdoll.hud;

import net.minecraft.client.gui.navigation.ScreenRectangle;
//?}
//? if >=26.1 {
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
//?}
//? if >=1.21.6 && <26.1 {
/*import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
*///?}
//? if >=1.21.6 {
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public record ModRenderState(
        EntityRenderState renderState,
        Vector3f translation,
        Quaternionf rotation,
//?}
        //? if >=26.1 {
        @Nullable EntityRenderState vehicleRenderState,
        @Nullable Vector3f translation2,
        Quaternionf rotation2,
        //?}
        //? if >=1.21.6 {
        @Nullable Quaternionf overrideCameraAngle,
        int x0,
        int y0,
        int x1,
        int y1,
        //?}
        //? if >=26.3 {
        int offsetX,
        int offsetY,
        //?}
        //? if >=1.21.6 {
        float scale,
        //?}
        //? if >=1.21.6 && <26.1 {
        /*boolean boat,
        *///?}
        //? if >=1.21.6 && <1.21.9 {
        /*int light,
        *///?}
        //? if >=26.3 {
        float lightDegree,
        boolean mirror,
        //?}
        //? if >=1.21.6 {
        @Nullable ScreenRectangle scissorArea,
        @Nullable ScreenRectangle bounds
) implements PictureInPictureRenderState {
    public ModRenderState(
            EntityRenderState renderState,
            Vector3f translation,
            Quaternionf rotation,
        //?}
            //? if >=26.1 {
            @Nullable EntityRenderState vehicleRenderState,
            @Nullable Vector3f translation2,
            Quaternionf rotation2,
            //?}
            //? if >=1.21.6 {
            @Nullable Quaternionf overrideCameraAngle,
            //?}
            //? if >=26.3 {
            int offsetX,
            int offsetY,
            int width,
            int height,
            //?}
            //? if >=1.21.6 && <26.3 {
            /*int x,
            int y,
            *///?}
            //? if >=1.21.6 {
            float scale,
            //?}
            //? if >=1.21.6 && <26.1 {
            /*boolean boat,
            *///?}
        //? if >=1.21.6 && <1.21.9 {
        /*int light,
        *///?}
            //? if >=26.3 {
            float lightDegree,
            boolean mirror
            //?}
            //? if >=1.21.6 && <26.3 {
            /*@Nullable ScreenRectangle scissorArea
            *///?}
    //? if >=1.21.6 {
    ) {
        this(
                renderState,
                translation,
                rotation,
    //?}
                //? if >=26.1 {
                vehicleRenderState,
                translation2,
                rotation2,
                //?}
                //? if >=1.21.6 {
                overrideCameraAngle,
                //?}
                //? if >=26.3 {
                0, 0, width, height, offsetX, offsetY,
                //?}
                //? if >=1.21.6 && <26.3 {
                /*x,
                y,
                x,
                y,
                *///?}
                //? if >=1.21.6 {
                scale,
                //?}
                //? if >=1.21.6 && <26.1 {
                /*boat,
                *///?}
                //? if >=1.21.6 && <1.21.9 {
                /*light,
                *///?}
                //? if >=26.3 {
                lightDegree,
                mirror,
                null,
                PictureInPictureRenderState.getBounds(0, 0, width, height, null)
                //?}
                //? if >=1.21.6 && <26.3 {
                /*scissorArea,
                PictureInPictureRenderState.getBounds(x, y, x, y, scissorArea)
                *///?}
        //? if >=1.21.6 {
        );
    }
}
        //?}
