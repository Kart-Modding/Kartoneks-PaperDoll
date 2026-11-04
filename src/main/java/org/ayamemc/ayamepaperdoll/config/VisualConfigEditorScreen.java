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
import org.ayamemc.ayamepaperdoll.AyamePaperDoll;
import org.ayamemc.ayamepaperdoll.config.view.Canvas;
import org.ayamemc.ayamepaperdoll.hud.PaperDollRenderer;
//? if <26.3 {
/*import org.lwjgl.glfw.GLFW;
*///?}

import java.util.Locale;

import static org.ayamemc.ayamepaperdoll.AyamePaperDoll.CONFIGS;

public class VisualConfigEditorScreen extends Screen {
    private static final int LINE_COLOR = 0xA6_FFFFFF;
    private static final int BORDER_MARGIN = 10;
    private static final int FRAME = 0x73FFFFFF;
    private static final int FRAME_HOVERED = 0xD9FFFFFF;
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
        paintFrame(new Canvas(graphics), mouseX, mouseY);
        //?}
        //? if <26.1 {
        /*guiGraphics.vLine(BORDER_MARGIN, -1, height, LINE_COLOR);
        paperDollRenderer.render(partialTick, guiGraphics);
        paintFrame(new Canvas(guiGraphics), mouseX, mouseY);
        *///?}
    }

    /**
     * Frames the doll with viewfinder corners and labels it with its position. The box comes from
     * the settings: the doll stands with its feet at the offset point and is about two blocks of
     * Size tall, so it fits any skin without measuring the model.
     */
    private void paintFrame(Canvas canvas, int mouseX, int mouseY) {
        double size = CONFIGS.size.getValue() * height;
        double feetX = CONFIGS.offsetX.getValue() * width;
        double feetY = CONFIGS.offsetY.getValue() * height;
        int x0 = Math.max(1, (int) Math.floor(feetX - 0.6 * size));
        int x1 = Math.min(width - 1, (int) Math.ceil(feetX + 0.6 * size));
        int y0 = Math.max(1, (int) Math.floor(feetY - 2.0 * size));
        int y1 = Math.min(height - 1, (int) Math.ceil(feetY + 0.1 * size));
        if (x1 - x0 < 4 || y1 - y0 < 4) return;
        int w = x1 - x0, h = y1 - y0;

        boolean hovered = mouseX >= x0 && mouseX < x1 && mouseY >= y0 && mouseY < y1;
        // A faint full border, and bright corners like the mod icon
        int edge = hovered ? FRAME_HOVERED : FRAME;
        canvas.rect(x0, y0, w, 1, edge);
        canvas.rect(x0, y1 - 1, w, 1, edge);
        canvas.rect(x0, y0, 1, h, edge);
        canvas.rect(x1 - 1, y0, 1, h, edge);
        int arm = Math.max(3, Math.min(10, Math.min(w, h) / 4));
        int corner = 0xFFFFFFFF;
        canvas.rect(x0 - 1, y0 - 1, arm, 2, corner);
        canvas.rect(x0 - 1, y0 - 1, 2, arm, corner);
        canvas.rect(x1 - arm + 1, y0 - 1, arm, 2, corner);
        canvas.rect(x1 - 1, y0 - 1, 2, arm, corner);
        canvas.rect(x0 - 1, y1 - 1, arm, 2, corner);
        canvas.rect(x0 - 1, y1 - arm + 1, 2, arm, corner);
        canvas.rect(x1 - arm + 1, y1 - 1, arm, 2, corner);
        canvas.rect(x1 - 1, y1 - arm + 1, 2, arm, corner);

        String label = Component.translatable("config.%s.screen.placement".formatted(AyamePaperDoll.MOD_ID),
                format(CONFIGS.offsetX.getValue()), format(CONFIGS.offsetY.getValue()), format(CONFIGS.size.getValue())).getString();
        int labelWidth = canvas.textWidth(label) + 8;
        int labelX = Math.max(2, Math.min(x0, width - labelWidth - 2));
        int labelY = y0 > 16 ? y0 - 14 : Math.min(y1 + 3, height - 14);
        canvas.rect(labelX, labelY, labelWidth, 12, 0xB4000000);
        canvas.text(label, labelX + 4, labelY + 2, 0xFFFFFFFF, true);
    }

    private static String format(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
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
