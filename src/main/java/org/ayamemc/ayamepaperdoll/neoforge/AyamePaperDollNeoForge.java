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

//? if neoforge {
/*package org.ayamemc.ayamepaperdoll.neoforge;

*///?}
//? if neoforge && <26.1 {
/*import net.minecraft.client.DeltaTracker;
*///?}
//? if neoforge && <26.2 {
/*import net.minecraft.client.KeyMapping;
*///?}
//? if neoforge && >=1.21.6 {
/*import net.minecraft.client.Minecraft;
*///?}
//? if neoforge && <26.1 {
/*import net.minecraft.client.gui.GuiGraphics;
*///?}
//? if neoforge {
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
*///?}
//? if neoforge && >=1.21.6 {
/*import net.neoforged.bus.api.SubscribeEvent;
*///?}
//? if neoforge {
/*import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
*///?}
//? if neoforge && >=1.21.6 {
/*import net.neoforged.neoforge.client.event.RegisterPictureInPictureRenderersEvent;
*///?}
//? if neoforge {
/*import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.ayamemc.ayamepaperdoll.AyamePaperDoll;
*///?}
//? if neoforge && <26.1 {
/*import org.ayamemc.ayamepaperdoll.CommonInterfaceInstances;
*///?}
//? if neoforge {
/*import org.ayamemc.ayamepaperdoll.config.ConfigScreen;
import org.ayamemc.ayamepaperdoll.handler.EventHandler;
*///?}
//? if neoforge && >=1.21.6 {
/*import org.ayamemc.ayamepaperdoll.hud.ModRenderState;
import org.ayamemc.ayamepaperdoll.hud.ModRenderer;
*///?}
//? if neoforge {
/*
@Mod(value = AyamePaperDoll.MOD_ID, dist = Dist.CLIENT)
public final class AyamePaperDollNeoForge {
    public AyamePaperDollNeoForge(IEventBus modBus) {
        // Run our NeoForge setup.
*///?}
        //? if neoforge && <26.1 {
        /*CommonInterfaceInstances.keyHelper = KeyMapping::getKey;

        *///?}
        //? if neoforge {
        /*AyamePaperDoll.init();

        modBus.addListener(AyamePaperDollNeoForge::registerKeyMapping);
        *///?}
        //? if neoforge && >=1.21.6 {
        /*modBus.addListener(AyamePaperDollNeoForge::registerPip);
        *///?}
        //? if neoforge {
/*
        NeoForge.EVENT_BUS.addListener(AyamePaperDollNeoForge::renderPaperDoll);
        NeoForge.EVENT_BUS.addListener(AyamePaperDollNeoForge::onClientTick);

        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (modContainer, lastScreen) -> new ConfigScreen(lastScreen, AyamePaperDoll.CONFIGS.getOptions())
        );
        *///?}
    //? if neoforge && >=1.21.6 {
    /*}
    @SubscribeEvent // on the mod event bus
    public static void registerPip(RegisterPictureInPictureRenderersEvent event) {
        event.register(
                ModRenderState.class,
    *///?}
                //? if neoforge && >=1.21.6 && <26.3 {
                /*// A factory that takes in the `MultiBufferSource.BufferSource` and returns the PiP renderer
                *///?}
                //? if neoforge && >=26.2 {
                /*()-> new ModRenderer(Minecraft.getInstance().getEntityRenderDispatcher())
                *///?}
                //? if neoforge && >=1.21.6 && <26.2 {
                /*(ctx)-> new ModRenderer(
                        ctx,Minecraft.getInstance().getEntityRenderDispatcher()
                )
                *///?}
        //? if neoforge && >=1.21.6 {
        /*);
        *///?}
//? if neoforge && <1.21.6 {
/*
*///?}
    //? if neoforge {
    /*}

    *///?}
    //? if neoforge && >=1.21.4 {
    /*private static void renderPaperDoll(RenderGuiEvent.Pre event) {
    *///?}
    //? if neoforge && <1.21.4 {
    /*private static void renderPaperDoll(RenderGuiEvent.Post event) {
    *///?}
        //? if neoforge && >=26.1 {
        /*final var guiGraphics = event.getGuiGraphics();
        final var partialTick = event.getPartialTick();
        *///?}
        //? if neoforge && <26.1 {
        /*final GuiGraphics guiGraphics = event.getGuiGraphics();
        final DeltaTracker partialTick = event.getPartialTick();
        *///?}
        //? if neoforge {
        /*EventHandler.renderPaperDoll(guiGraphics, partialTick);
    }

    private static void registerKeyMapping(RegisterKeyMappingsEvent event) {
        event.register(AyamePaperDoll.SHOW_PAPERDOLL_KEY);
        event.register(AyamePaperDoll.OPEN_CONFIG_GUI);
    }

    private static void onClientTick(ClientTickEvent.Post event) {
        EventHandler.keyPressed();
    }
}
        *///?}
