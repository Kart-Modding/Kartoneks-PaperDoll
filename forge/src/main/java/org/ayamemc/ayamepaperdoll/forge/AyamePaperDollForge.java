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

package org.ayamemc.ayamepaperdoll.forge;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterPictureInPictureRendererEvent;
import net.minecraftforge.client.gui.overlay.ForgeLayeredDraw;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.ayamemc.ayamepaperdoll.AyamePaperDoll;
import org.ayamemc.ayamepaperdoll.config.ConfigScreen;
import org.ayamemc.ayamepaperdoll.handler.EventHandler;
import org.ayamemc.ayamepaperdoll.hud.ModRenderer;

@Mod(AyamePaperDoll.MOD_ID)
public final class AyamePaperDollForge {
    public AyamePaperDollForge(FMLJavaModLoadingContext context) {
        // Run our Forge setup.
        AyamePaperDoll.init();

        RegisterKeyMappingsEvent.BUS.addListener(AyamePaperDollForge::registerKeyMapping);
        RegisterPictureInPictureRendererEvent.BUS.addListener(AyamePaperDollForge::registerPip);
        AddGuiOverlayLayersEvent.BUS.addListener(AyamePaperDollForge::registerHudLayer);
        TickEvent.ClientTickEvent.Post.BUS.addListener(event -> EventHandler.keyPressed());

        context.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(lastScreen -> new ConfigScreen(lastScreen, AyamePaperDoll.CONFIGS.getOptions()))
        );
    }

    private static void registerPip(RegisterPictureInPictureRendererEvent event) {
        event.register(new ModRenderer(Minecraft.getInstance().getEntityRenderDispatcher()));
    }

    private static void registerHudLayer(AddGuiOverlayLayersEvent event) {
        // Same spot as on Fabric: right after the vignette/spyglass/portal overlays
        event.getLayeredDraw().addAbove(
                ForgeLayeredDraw.PRE_SLEEP_STACK,
                AyamePaperDoll.path("ayame_paperdoll_renderer_layer_after_misc_overlays"),
                ForgeLayeredDraw.CAMERA_OVERLAY,
                EventHandler::renderPaperDoll
        );
    }

    private static void registerKeyMapping(RegisterKeyMappingsEvent event) {
        event.register(AyamePaperDoll.SHOW_PAPERDOLL_KEY);
        event.register(AyamePaperDoll.OPEN_CONFIG_GUI);
    }
}
