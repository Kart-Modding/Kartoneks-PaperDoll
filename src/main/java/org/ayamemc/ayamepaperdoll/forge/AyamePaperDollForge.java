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

//? if forge {
/*package org.ayamemc.ayamepaperdoll.forge;

*///?}
//? if forge && <26.1 {
/*import net.minecraft.client.KeyMapping;
*///?}
//? if forge && >=1.21.6 {
/*import net.minecraft.client.Minecraft;
*///?}
//? if forge {
/*import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
*///?}
//? if forge && >=1.21.6 {
/*import net.minecraftforge.client.event.RegisterPictureInPictureRendererEvent;
*///?}
//? if forge {
/*import net.minecraftforge.client.gui.overlay.ForgeLayeredDraw;
*///?}
//? if forge && <1.21.6 {
/*import net.minecraftforge.common.MinecraftForge;
*///?}
//? if forge {
/*import net.minecraftforge.event.TickEvent;
*///?}
//? if forge && <1.21.6 {
/*import net.minecraftforge.fml.ModLoadingContext;
*///?}
//? if forge {
/*import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.ayamemc.ayamepaperdoll.AyamePaperDoll;
*///?}
//? if forge && <26.1 {
/*import org.ayamemc.ayamepaperdoll.CommonInterfaceInstances;
*///?}
//? if forge {
/*import org.ayamemc.ayamepaperdoll.config.ConfigScreen;
import org.ayamemc.ayamepaperdoll.handler.EventHandler;
*///?}
//? if forge && >=1.21.6 {
/*import org.ayamemc.ayamepaperdoll.hud.ModRenderer;
*///?}
//? if forge {
/*
@Mod(AyamePaperDoll.MOD_ID)
public final class AyamePaperDollForge {
    public AyamePaperDollForge(FMLJavaModLoadingContext context) {
*///?}
        //? if forge && <26.1 {
        /*CommonInterfaceInstances.keyHelper = KeyMapping::getKey;
        *///?}
        //? if forge {
        /*AyamePaperDoll.init();

        *///?}
        //? if forge && >=1.21.9 {
        /*RegisterKeyMappingsEvent.BUS.addListener(AyamePaperDollForge::registerKeyMapping);
        AddGuiOverlayLayersEvent.BUS.addListener(AyamePaperDollForge::registerHudLayer);
        *///?}
        //? if forge && >=1.21.6 && <1.21.9 {
        /*var modBus = context.getModBusGroup();
        RegisterKeyMappingsEvent.getBus(modBus).addListener(AyamePaperDollForge::registerKeyMapping);
        AddGuiOverlayLayersEvent.getBus(modBus).addListener(AyamePaperDollForge::registerHudLayer);
        *///?}
        //? if forge && >=1.21.6 {
        /*RegisterPictureInPictureRendererEvent.BUS.addListener(AyamePaperDollForge::registerPip);
        TickEvent.ClientTickEvent.Post.BUS.addListener(event -> EventHandler.keyPressed());
        *///?}
        //? if forge && <1.21.6 {
        /*context.getModEventBus().addListener(AyamePaperDollForge::registerKeyMapping);
        context.getModEventBus().addListener(AyamePaperDollForge::registerHudLayer);
        MinecraftForge.EVENT_BUS.addListener(AyamePaperDollForge::onClientTick);
        *///?}
        //? if forge && >=1.21.6 {
        /*context.registerExtensionPoint(
        *///?}
        //? if forge && <1.21.6 {
        /*ModLoadingContext.get().registerExtensionPoint(
        *///?}
                //? if forge {
                /*ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(lastScreen -> new ConfigScreen(lastScreen, AyamePaperDoll.CONFIGS.getOptions()))
        );
                *///?}
    //? if forge && >=1.21.6 {
    /*}

    private static void registerPip(RegisterPictureInPictureRendererEvent event) {
    *///?}
        //? if forge && >=26.2 {
        /*event.register(new ModRenderer(Minecraft.getInstance().getEntityRenderDispatcher()));
        *///?}
        //? if forge && >=1.21.6 && <26.2 {
        /*event.register(new ModRenderer(event.getBufferSource(), Minecraft.getInstance().getEntityRenderDispatcher()));
        *///?}
    //? if forge {
    /*}

    private static void registerHudLayer(AddGuiOverlayLayersEvent event) {
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
    *///?}
    //? if forge && <1.21.6 {
/*
    private static void onClientTick(TickEvent.ClientTickEvent.Post event) {
        EventHandler.keyPressed();
    }
    *///?}
//? if forge {
/*}
*///?}
