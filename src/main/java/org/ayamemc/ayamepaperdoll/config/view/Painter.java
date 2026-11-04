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

import net.minecraft.client.gui.components.Renderable;
//? if >=26.1 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?}
//? if <26.1 {
/*import net.minecraft.client.gui.GuiGraphics;
*///?}

import java.util.function.Consumer;

/** Draws through a {@link Canvas} at its place in a screen's widget order. */
public final class Painter implements Renderable {
    private final Consumer<Canvas> action;

    public Painter(Consumer<Canvas> action) {
        this.action = action;
    }

    @Override
    //? if >=26.1 {
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
    //?}
    //? if <26.1 {
    /*public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
    *///?}
        action.accept(new Canvas(graphics));
    }
}
