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

//? if fabric {
package org.ayamemc.ayamepaperdoll.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//?}
//? if fabric && >=26.1 {
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.PictureInPictureRendererRegistry;
//?}
//? if fabric && <26.1 {
/*import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
*///?}
//? if fabric && >=1.21.6 && <26.1 {
/*import net.fabricmc.fabric.api.client.rendering.v1.SpecialGuiElementRegistry;
*///?}
//? if fabric && >=1.21.6 {
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
//?}
//? if fabric && >=1.21.4 && <1.21.6 {
/*import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
*///?}
//? if fabric && <1.21.4 {
/*import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
*///?}
//? if fabric {
import org.ayamemc.ayamepaperdoll.AyamePaperDoll;
//?}
//? if fabric && <26.1 {
/*import org.ayamemc.ayamepaperdoll.CommonInterfaceInstances;
*///?}
//? if fabric {
import org.ayamemc.ayamepaperdoll.handler.EventHandler;
//?}
//? if fabric && >=1.21.6 {
import org.ayamemc.ayamepaperdoll.hud.ModRenderer;
//?}
//? if fabric {

public class AyamePaperDollFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Run our Fabric setup.
//?}
        //? if fabric && >=26.1 {
        KeyMappingHelper.registerKeyMapping(AyamePaperDoll.SHOW_PAPERDOLL_KEY);
        KeyMappingHelper.registerKeyMapping(AyamePaperDoll.OPEN_CONFIG_GUI);
        //?}
        //? if fabric && <26.1 {
        /*CommonInterfaceInstances.keyHelper = KeyBindingHelper::getBoundKeyOf;

        KeyBindingHelper.registerKeyBinding(AyamePaperDoll.SHOW_PAPERDOLL_KEY);
        KeyBindingHelper.registerKeyBinding(AyamePaperDoll.OPEN_CONFIG_GUI);
        *///?}
        //? if fabric {

        AyamePaperDoll.init();

        ClientTickEvents.END_CLIENT_TICK.register((minecraft) ->
                EventHandler.keyPressed()
        );

        //?}
        //? if fabric && >=1.21.6 {
        HudElementRegistry.attachElementAfter(
                VanillaHudElements.MISC_OVERLAYS,
                AyamePaperDoll.path("ayame_paperdoll_renderer_layer_after_misc_overlays"),
                EventHandler::renderPaperDoll
        //?}
        //? if fabric && >=1.21.4 && <1.21.6 {
        /*HudLayerRegistrationCallback.EVENT.register(layeredDrawer ->
                layeredDrawer.attachLayerAfter(
                        IdentifiedLayer.MISC_OVERLAYS,
                        AyamePaperDoll.path("ayame_paperdoll_renderer_layer_after_misc_overlays"),
                        EventHandler::renderPaperDoll)
        *///?}
        //? if fabric && >=1.21.4 {
        );
        //?}
        //? if fabric && <1.21.4 {
        /*HudRenderCallback.EVENT.register((EventHandler::renderPaperDoll));
        *///?}
        //? if fabric && >=26.1 {
        PictureInPictureRendererRegistry.register(context -> new ModRenderer(
        //?}
                //? if fabric && >=26.2 {
                context.minecraft().getEntityRenderDispatcher()
                //?}
                //? if fabric && >=26.1 && <26.2 {
                /*context.bufferSource(), context.minecraft().getEntityRenderDispatcher()
                *///?}
        //? if fabric && >=26.1 {
        ));
        //?}
        //? if fabric && >=1.21.6 && <26.1 {
        /*SpecialGuiElementRegistry.register((context -> new ModRenderer(
                context.vertexConsumers(),context.client().getEntityRenderDispatcher()
        )));
        *///?}
    //? if fabric {
    }
}
    //?}
