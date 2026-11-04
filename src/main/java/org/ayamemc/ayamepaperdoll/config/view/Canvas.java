/*
 *     Highly configurable PaperDoll mod. Forked from Extra Player Renderer.
 *     Copyright (C) 2024-2026  LucunJi(Original author), HappyRespawnanchor, Kartonek
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

package org.ayamemc.ayamepaperdoll.config.view;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
//? if >=26.1 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?}
//? if <26.1 {
/*import net.minecraft.client.gui.GuiGraphics;
*///?}

/**
 * Thin wrapper over the vanilla draw context with the few calls the settings screen needs, so the
 * screen itself stays the same on every Minecraft version.
 */
public final class Canvas {
    //? if >=26.1 {
    private final GuiGraphicsExtractor ctx;

    public Canvas(GuiGraphicsExtractor ctx) {
        this.ctx = ctx;
    }

    public GuiGraphicsExtractor graphics() {
        return ctx;
    }
    //?}
    //? if <26.1 {
    /*private final GuiGraphics ctx;

    public Canvas(GuiGraphics ctx) {
        this.ctx = ctx;
    }

    public GuiGraphics graphics() {
        return ctx;
    }
    *///?}

    private static Font font() {
        return Minecraft.getInstance().font;
    }

    public void rect(int x, int y, int width, int height, int argb) {
        if (width <= 0 || height <= 0) return;
        ctx.fill(x, y, x + width, y + height, argb);
    }

    public void text(String value, int x, int y, int argb, boolean shadow) {
        //? if >=26.1 {
        ctx.text(font(), value, x, y, argb, shadow);
        //?}
        //? if <26.1 {
        /*ctx.drawString(font(), value, x, y, argb, shadow);
        *///?}
    }

    /** Clips drawing to an area until {@link #endClip()}. */
    public void clip(int x0, int y0, int x1, int y1) {
        ctx.enableScissor(x0, y0, x1, y1);
    }

    public void endClip() {
        ctx.disableScissor();
    }

    public int textWidth(String value) {
        return font().width(value);
    }

    /** Shortens a string with an ellipsis so it fits into {@code maxWidth}. */
    public String trim(String value, int maxWidth) {
        Font font = font();
        if (font.width(value) <= maxWidth) return value;
        int ellipsis = font.width("...");
        StringBuilder builder = new StringBuilder();
        int width = 0;
        for (int i = 0; i < value.length(); i++) {
            int charWidth = font.width(String.valueOf(value.charAt(i)));
            if (width + charWidth + ellipsis > maxWidth) break;
            width += charWidth;
            builder.append(value.charAt(i));
        }
        return builder.append("...").toString();
    }
}
