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

package org.ayamemc.ayamepaperdoll.config;

//? if >=26.3 {
import com.mojang.blaze3d.platform.InputConstants;
//?}
//? if >=26.1 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?}
//? if <26.1 {
/*import net.minecraft.client.gui.GuiGraphics;
*///?}
import net.minecraft.client.gui.screens.Screen;
//? if >=1.21.9 {
import net.minecraft.client.input.MouseButtonEvent;
//?}
import net.minecraft.network.chat.Component;
import org.ayamemc.ayamepaperdoll.hud.PaperDollRenderer;
//? if <26.3 {
/*import org.lwjgl.glfw.GLFW;
*///?}

import static org.ayamemc.ayamepaperdoll.AyamePaperDoll.CONFIGS;

public class VisualConfigEditorScreen extends Screen {
    private static final int LINE_COLOR = 0xA6_FFFFFF;
    private static final int BORDER_MARGIN = 10;
    private final Screen lastScreen;
    private final PaperDollRenderer paperDollRenderer = PaperDollRenderer.getInstance();

    protected VisualConfigEditorScreen(Screen lastScreen) {
        super(Component.empty());
        this.lastScreen = lastScreen;
    }

    @Override
    //? if >=26.1 {
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
    //?}
    //? if <26.1 {
    /*public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    *///?}
        // 十字的水平线，- 1能居中点，大概
        //? if >=26.1 {
        graphics.horizontalLine(0, width, (height / 2) - 1, LINE_COLOR);
        //?}
        //? if <26.1 {
        /*guiGraphics.hLine(0, width, (height / 2) - 1, LINE_COLOR);
        *///?}
        // 十字的垂直线
        //? if >=26.1 {
        graphics.verticalLine((width / 2), -1, height, LINE_COLOR);
        //?}
        //? if <26.1 {
        /*guiGraphics.vLine((width / 2), -1, height, LINE_COLOR);
        *///?}

        // 底下的线
        //? if >=26.1 {
        graphics.horizontalLine(0, width, (height - BORDER_MARGIN), LINE_COLOR);
        //?}
        //? if <26.1 {
        /*guiGraphics.hLine(0, width, (height - BORDER_MARGIN), LINE_COLOR);
        *///?}
        // 顶上的线
        //? if >=26.1 {
        graphics.horizontalLine(0, width, BORDER_MARGIN, LINE_COLOR);
        //?}
        //? if <26.1 {
        /*guiGraphics.hLine(0, width, BORDER_MARGIN, LINE_COLOR);
        *///?}
        // 左边的线
        //? if >=26.1 {
        graphics.verticalLine((width - BORDER_MARGIN), -1, height, LINE_COLOR);
        //?}
        //? if <26.1 {
        /*guiGraphics.vLine((width - BORDER_MARGIN), -1, height, LINE_COLOR);
        *///?}
        // 右边的线
        //? if >=26.1 {
        graphics.verticalLine(BORDER_MARGIN, -1, height, LINE_COLOR);
        paperDollRenderer.extractPaperdoll(graphics, a);
        //?}
        //? if <26.1 {
        /*guiGraphics.vLine(BORDER_MARGIN, -1, height, LINE_COLOR);
        paperDollRenderer.render(partialTick, guiGraphics);
        *///?}
    }


    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onClose() {
        // 通过构造新配置屏幕刷新设置中的值

        if (lastScreen instanceof ConfigScreen configScreen) {
            configScreen.onClose();
            //? if >=26.2 {
            this.minecraft.gui.setScreen(new ConfigScreen(lastScreen, CONFIGS.getOptions()));
            //?}
            //? if <26.2 {
            /*this.minecraft.setScreen(new ConfigScreen(lastScreen, CONFIGS.getOptions()));
            *///?}
        } else {
            //? if >=26.2 {
            this.minecraft.gui.setScreen(lastScreen);
            //?}
            //? if <26.2 {
            /*this.minecraft.setScreen(lastScreen);
            *///?}
        }
    }

    @Override
    //? if >=1.21.9 {
    public boolean mouseDragged(MouseButtonEvent mouseButtonEvent, double deltaX, double deltaY) {
    //?}
    //? if <1.21.9 {
    /*public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    *///?}
        boolean onDrag = false;
        //? if >=26.3 {
        if (mouseButtonEvent.button() == InputConstants.MOUSE_BUTTON_LEFT) {
        //?}
        //? if >=1.21.9 && <26.3 {
        /*if (mouseButtonEvent.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
        *///?}
        //? if <1.21.9 {
        /*if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
        *///?}
            final double newOffsetX = CONFIGS.offsetX.getValue() + (deltaX * 0.0015);
            final double newOffsetY = CONFIGS.offsetY.getValue() + (deltaY * 0.0015);
            if (newOffsetX < CONFIGS.offsetX.getMax() && newOffsetY > CONFIGS.offsetY.getMin()) {
                CONFIGS.offsetX.setValue(newOffsetX);
            }
            if (newOffsetY < CONFIGS.offsetY.getMax() && newOffsetX > CONFIGS.offsetX.getMin()) {
                CONFIGS.offsetY.setValue(newOffsetY);
            }
            onDrag = true;
        }
        //? if >=26.3 {
        if (mouseButtonEvent.button() == InputConstants.MOUSE_BUTTON_RIGHT) {
        //?}
        //? if >=1.21.9 && <26.3 {
        /*if (mouseButtonEvent.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
        *///?}
        //? if <1.21.9 {
        /*if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
        *///?}
            final double newRotationY = CONFIGS.rotationY.getValue() + deltaX;
            if (newRotationY < CONFIGS.rotationY.getMax() && newRotationY > CONFIGS.rotationY.getMin()) {
                CONFIGS.rotationY.setValue(newRotationY);
            }
            onDrag = true;
        }
        return onDrag;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY != 0) {
            final double newSize = CONFIGS.size.getValue() + (scrollY / 80);
            if (newSize < CONFIGS.size.getMax() && newSize > CONFIGS.size.getMin()) {
                CONFIGS.size.setValue(newSize);
            }
            return true;
        }
        return false;
    }

    //? if >=1.21.9 {
    @Override
    //?}
    //? if >=26.1 {
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
    //?}
    //? if >=1.21.9 && <26.1 {
    /*public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    *///?}
    //? if >=1.21.9 {
    }

    //?}
}
