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

package org.ayamemc.ayamepaperdoll.config;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
//? if >=26.1 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?}
//? if <26.1 {
/*import net.minecraft.client.gui.GuiGraphics;
*///?}
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
//? if >=1.21.9 {
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
//?}
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?}
//? if <1.21.11 {
/*import net.minecraft.resources.ResourceLocation;
*///?}
import org.ayamemc.ayamepaperdoll.AyamePaperDoll;
//? if <26.1 {
/*import org.ayamemc.ayamepaperdoll.CommonInterfaceInstances;
*///?}
import org.ayamemc.ayamepaperdoll.config.model.ConfigOption;
import org.ayamemc.ayamepaperdoll.config.model.SimpleNumericOption;
import org.ayamemc.ayamepaperdoll.config.model.SimpleOption;
import org.ayamemc.ayamepaperdoll.config.view.Canvas;
import org.ayamemc.ayamepaperdoll.config.view.Painter;
import org.ayamemc.ayamepaperdoll.config.view.Retextured;
import org.ayamemc.ayamepaperdoll.hud.PaperDollRenderer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoubleSupplier;
import java.util.function.Predicate;

import static org.ayamemc.ayamepaperdoll.AyamePaperDoll.CONFIGS;

/**
 * Settings screen, modelled on the one in Spoty Widget: a live preview of the paper doll on the left,
 * with the corner presets and the visual editor under it, and a searchable, scrolling list of
 * settings on the right. The doll in the HUD is hidden while the screen is open. Each row has a reset button that shows up once the
 * value differs from its default, and rows that do nothing in the current setup are greyed out with
 * a tooltip that says why. The list is laid out by hand, as the vanilla list classes change between
 * versions.
 */
public class ConfigScreen extends Screen {
    private static final String KEY = "config.%s.screen.".formatted(AyamePaperDoll.MOD_ID);
    private static final int HEADER = 24;
    private static final int FOOTER = 30;
    private static final int ROW = 22;
    private static final int SECTION = 18;
    private static final int SCROLL_STEP = 22;
    private static final int NUMBER_BOX = 40;
    private static final int LINE = 0xFF5A5A5A;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int GREY = 0xFFA0A0A0;
    private static final int DIM = 0xFF707070;
    private static final int YELLOW = 0xFFFFFF55;
    private static final int VALID = 0xFFE0E0E0;
    private static final int INVALID = 0xFFFF5555;

    private final Screen lastScreen;
    private final List<? extends ConfigOption<?>> options;
    private final PaperDollRenderer previewHud = PaperDollRenderer.getInstance();

    private final List<Row> rows = new ArrayList<>();
    private String search = "";
    private boolean searchFocused;
    private boolean rebuild;
    private int scroll;
    private int contentHeight;
    private boolean draggingBar;
    private double grabOffset;

    private EditBox searchBox;
    private int listX;
    private int listWidth;
    private int listTop;
    private int listBottom;

    public ConfigScreen(Screen lastScreen, List<? extends ConfigOption<?>> options) {
        super(text("title"));

        // Make sure lastScreen is not another ConfigScreen
        while (lastScreen instanceof ConfigScreen configScreen) {
            lastScreen = configScreen.lastScreen;
        }

        this.lastScreen = lastScreen;
        this.options = options;
    }

    private static MutableComponent text(String key) {
        return Component.translatable(KEY + key);
    }

    /** One entry of the list and the widgets it owns. */
    private static final class Row {
        final String label;
        final String search;
        final int height;
        final boolean section;
        boolean labelled = true;
        BooleanSupplier enabled = () -> true;
        Component tip = Component.empty();
        Component disabledTip = Component.empty();
        BooleanSupplier changed = () -> false;
        Runnable refresh = () -> {
        };
        final List<AbstractWidget> widgets = new ArrayList<>();
        final List<Integer> offsets = new ArrayList<>();
        Button resetButton;
        int y;

        Row(String label, String search, int height, boolean section) {
            this.label = label;
            this.search = search;
            this.height = height;
            this.section = section;
        }

        Row when(BooleanSupplier condition, String why) {
            enabled = condition;
            disabledTip = text("why." + why);
            return this;
        }

        void add(AbstractWidget widget, int offset) {
            widgets.add(widget);
            offsets.add(offset);
        }
    }

    // --- layout ----------------------------------------------------------------

    @Override
    protected void init() {
        listWidth = Math.max(214, Math.min(320, this.width / 2));
        listX = this.width - listWidth;
        listTop = HEADER + 24;
        listBottom = this.height - FOOTER;

        addRenderableOnly(new Painter(this::paintBase));
        addRenderableOnly(new Painter(this::paintPreview));
        addLeftButtons();

        searchBox = new TextBox(listX + 5, HEADER + 4, listWidth - 10, 16);
        searchBox.setHint(text("search").withStyle(ChatFormatting.DARK_GRAY));
        searchBox.setMaxLength(32);
        searchBox.setValue(search);
        searchBox.setResponder(value -> {
            if (value.equals(search)) return;
            search = value;
            scroll = 0;
            rebuild = true;
        });
        addRenderableWidget(searchBox);
        if (searchFocused) {
            setFocused(searchBox);
            searchBox.setFocused(true);
        }

        addRenderableWidget(button(this.width - 214, this.height - 25, 104, text("reset_all"), this::confirmReset));
        addRenderableWidget(button(this.width - 106, this.height - 25, 100, text("done"), this::onClose));

        addRenderableOnly(new Painter(this::beginList));
        rows.clear();
        buildRows();
        placeRows();
        addRenderableOnly(new Painter(this::endList));
        refresh();
    }

    /** Corner presets and the visual editor button, under the preview. */
    private void addLeftButtons() {
        int buttonWidth = Math.min(220, listX - 16);
        int x = (listX - buttonWidth) / 2;
        int moveY = this.height - FOOTER - 34;
        int half = (buttonWidth - 2) / 2;
        String[] keys = {"top_left", "top_right", "bottom_left", "bottom_right"};
        Configs.Presets[] presets = {CONFIGS.topLeft, CONFIGS.topRight, CONFIGS.bottomLeft, CONFIGS.bottomRight};
        Tooltip presetTip = Tooltip.create(Component.translatable("config.%s.option.presets.desc".formatted(AyamePaperDoll.MOD_ID)));
        for (int i = 0; i < presets.length; i++) {
            Configs.Presets preset = presets[i];
            Button button = button(x + (i % 2) * (half + 2), moveY - 46 + (i / 2) * 22, half,
                    Component.translatable("config.%s.presets.%s".formatted(AyamePaperDoll.MOD_ID, keys[i])), () -> {
                        preset.load();
                        refresh();
                    });
            button.setTooltip(presetTip);
            addRenderableWidget(button);
        }
        Button adjust = button(x, moveY, buttonWidth, text("adjust_location"), this::openEditor);
        boolean inWorld = this.minecraft.level != null;
        adjust.active = inWorld;
        adjust.setTooltip(Tooltip.create(Component.translatable("config.%s.option.%s.desc".formatted(AyamePaperDoll.MOD_ID,
                inWorld ? "visual_config_editor" : "visual_config_editor_not_available"))));
        addRenderableWidget(adjust);
    }

    /** Area of the preview box: x0, y0, x1, y1. */
    private int[] previewBox() {
        return new int[]{8, HEADER + 16, listX - 8, this.height - FOOTER - 34 - 46 - 6};
    }

    private static Button button(int x, int y, int width, Component message, Runnable action) {
        var button = new ConfigWidgetRegistry.ConfigButton(width, 20, message, pressed -> action.run());
        button.setX(x);
        button.setY(y);
        return button;
    }

    private void openEditor() {
        save();
        open(new VisualConfigEditorScreen(this));
    }

    private void confirmReset() {
        open(new ConfirmScreen(confirmed -> {
            if (confirmed) {
                for (var option : options) {
                    if (!option.getCategory().equals(Configs.HIDDEN_CATEGORY)) resetOption(option);
                }
                save();
            }
            open(new ConfigScreen(lastScreen, options));
        }, text("reset_all.title"), text("reset_all.message")));
    }

    private static <T> void resetOption(ConfigOption<T> option) {
        option.setValue(option.getDefaultValue());
    }

    // --- the settings ----------------------------------------------------------

    private void buildRows() {
        BooleanSupplier locked = () -> CONFIGS.rotationMode.getValue() == Configs.RotationMode.LOCK;
        BooleanSupplier manual = () -> CONFIGS.poseOffsetMethod.getValue() == Configs.PoseOffsetMethod.MANUAL;

        List<Row> all = new ArrayList<>();
        all.add(section("general"));
        all.add(toggle(CONFIGS.displayPaperDoll));
        all.add(cycle(CONFIGS.rotationMode));
        all.add(textField(CONFIGS.playerName));
        all.add(toggle(CONFIGS.spectatorAutoSwitch));

        all.add(section("position"));
        all.add(decimal(CONFIGS.offsetX, 0.01, ConfigScreen::plain));
        all.add(decimal(CONFIGS.offsetY, 0.01, ConfigScreen::plain));
        all.add(decimal(CONFIGS.size, 0.01, ConfigScreen::plain));
        all.add(toggle(CONFIGS.mirrored));

        all.add(section("model_rotation"));
        all.add(decimal(CONFIGS.rotationX, 0.5, ConfigScreen::degrees));
        all.add(decimal(CONFIGS.rotationY, 0.5, ConfigScreen::degrees));
        all.add(decimal(CONFIGS.rotationZ, 0.5, ConfigScreen::degrees));

        all.add(section("head_and_body"));
        all.add(decimal(CONFIGS.pitch, 0.5, ConfigScreen::degrees));
        all.add(decimal(CONFIGS.pitchRange, 0.5, ConfigScreen::degrees));
        all.add(decimal(CONFIGS.headYaw, 0.5, ConfigScreen::degrees).when(locked, "locked"));
        all.add(decimal(CONFIGS.headYawRange, 0.5, ConfigScreen::degrees).when(locked, "locked"));
        all.add(decimal(CONFIGS.bodyYaw, 0.5, ConfigScreen::degrees).when(locked, "locked"));
        all.add(decimal(CONFIGS.bodyYawRange, 0.5, ConfigScreen::degrees).when(locked, "locked"));

        all.add(section("postures"));
        all.add(cycle(CONFIGS.poseOffsetMethod));
        all.add(decimal(CONFIGS.sneakOffsetY, 0.01, ConfigScreen::plain).when(manual, "manual"));
        all.add(decimal(CONFIGS.swimCrawlOffsetY, 0.01, ConfigScreen::plain).when(manual, "manual"));
        all.add(decimal(CONFIGS.elytraOffsetY, 0.01, ConfigScreen::plain).when(manual, "manual"));

        all.add(section("lighting"));
        all.add(toggle(CONFIGS.useWorldLight));
        all.add(integer(CONFIGS.worldLightMin).when(CONFIGS.useWorldLight::getValue, "world_light"));
        all.add(decimal(CONFIGS.lightDegree, 0.5, ConfigScreen::degrees));

        all.add(section("details"));
        all.add(toggle(CONFIGS.hurtFlash));
        all.add(toggle(CONFIGS.swingHands));
        all.add(toggle(CONFIGS.renderVehicle)
                .when(() -> CONFIGS.poseOffsetMethod.getValue() != Configs.PoseOffsetMethod.FORCE_STANDING, "standing"));

        all.add(section("when_to_show"));
        all.add(toggle(CONFIGS.visibleDuringActivity));
        all.add(toggle(CONFIGS.hideUnderDebug));
        all.add(toggle(CONFIGS.hideOnScreenOpen));
        all.add(toggle(CONFIGS.autoHide));
        all.add(decimal(CONFIGS.autoHideDelay, 0.5, ConfigScreen::seconds).when(CONFIGS.autoHide::getValue, "auto_hide"));

        all.add(section("this_screen"));
        all.add(toggle(CONFIGS.pauseGameOnConfigScreen));
        all.add(toggle(CONFIGS.disableConfigScreenBlur));

        filter(all);
        for (Row row : rows) {
            for (AbstractWidget widget : row.widgets) addRenderableWidget(widget);
            if (row.resetButton != null) addRenderableWidget(row.resetButton);
        }
    }

    private void filter(List<Row> all) {
        String query = search.trim().toLowerCase(Locale.ROOT);
        int y = 2;
        Row pendingSection = null;
        boolean sectionHit = false;
        for (Row row : all) {
            if (row.section) {
                pendingSection = row;
                sectionHit = !query.isEmpty() && row.search.contains(query);
                if (query.isEmpty()) {
                    row.y = y;
                    y += row.height;
                    rows.add(row);
                    pendingSection = null;
                }
                continue;
            }
            if (!query.isEmpty() && !sectionHit && !row.search.contains(query)) continue;
            if (pendingSection != null) {
                pendingSection.y = y;
                y += pendingSection.height;
                rows.add(pendingSection);
                pendingSection = null;
            }
            row.y = y;
            y += row.height;
            rows.add(row);
        }
        contentHeight = y + 4;
    }

    private int rowX() {
        return listX + 5;
    }

    private int rowWidth() {
        return listWidth - 18;
    }

    private int controlWidth() {
        return Math.min(124, rowWidth() / 2);
    }

    private int controlX() {
        return rowX() + rowWidth() - controlWidth() - 17;
    }

    private Row section(String key) {
        String title = text("section." + key).getString();
        return new Row(title, title.toLowerCase(Locale.ROOT), SECTION, true);
    }

    private Row row(ConfigOption<?> option, String extraSearch) {
        String label = option.getName().getString();
        Row row = new Row(label, (label + " " + extraSearch).toLowerCase(Locale.ROOT), ROW, false);
        // The label may be cut short in a narrow list, so the tooltip repeats it
        row.tip = Component.empty()
                .append(option.getName().copy().withStyle(ChatFormatting.YELLOW))
                .append("\n")
                .append(option.getDescription());
        return row;
    }

    private void withReset(Row row, BooleanSupplier changed, Runnable reset) {
        row.changed = changed;
        Button button = button(rowX() + rowWidth() - 15, 0, 15, Component.empty(), () -> {
            reset.run();
            rebuild = true;
        });
        button.setTooltip(Tooltip.create(text("reset_one")));
        row.resetButton = button;
    }

    private Row toggle(SimpleOption<Boolean> option) {
        Row row = row(option, "");
        Button button = button(controlX(), 0, controlWidth(), onOff(option.getValue()), () -> {
            option.setValue(!option.getValue());
            refresh();
        });
        row.add(button, 0);
        row.refresh = () -> button.setMessage(onOff(option.getValue()));
        withReset(row, () -> !option.isValueDefault(), () -> option.setValue(option.getDefaultValue()));
        return row;
    }

    private static Component onOff(boolean value) {
        String key = "config.%s.%s".formatted(AyamePaperDoll.MOD_ID, value ? "on" : "off");
        return Component.translatable(key).withStyle(value ? ChatFormatting.GREEN : ChatFormatting.RED);
    }

    private <E extends Enum<E>> Row cycle(SimpleOption<E> option) {
        E[] values = option.getType().getEnumConstants();
        Component[] names = new Component[values.length];
        StringBuilder searchable = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            names[i] = Component.translatable("config.%s.enum.%s.%s".formatted(
                    AyamePaperDoll.MOD_ID, option.getType().getSimpleName(), values[i].name()));
            searchable.append(names[i].getString()).append(' ');
        }
        Row row = row(option, searchable.toString());
        Button button = button(controlX(), 0, controlWidth(), names[option.getValue().ordinal()], () -> {
            option.setValue(values[(option.getValue().ordinal() + 1) % values.length]);
            refresh();
        });
        row.add(button, 0);
        row.refresh = () -> button.setMessage(names[option.getValue().ordinal()]);
        withReset(row, () -> !option.isValueDefault(), () -> option.setValue(option.getDefaultValue()));
        return row;
    }

    private Row textField(SimpleOption<String> option) {
        Row row = row(option, "");
        TextBox box = new TextBox(controlX(), 0, controlWidth(), 20);
        box.setMaxLength(64);
        box.setValue(option.getValue());
        box.setResponder(option::setValue);
        row.add(box, 0);
        row.refresh = () -> {
            if (!box.isFocused() && !box.getValue().equals(option.getValue())) box.setValue(option.getValue());
        };
        withReset(row, () -> !option.isValueDefault(), () -> option.setValue(option.getDefaultValue()));
        return row;
    }

    private Row decimal(SimpleNumericOption<Double> option, double step, DoubleFunction<String> format) {
        return number(option, option.getMin(), option.getMax(), step, false, option::getValue, option::setValue, format);
    }

    private Row integer(SimpleNumericOption<Integer> option) {
        return number(option, option.getMin(), option.getMax(), 1, true,
                option::getValue, value -> option.setValue((int) Math.round(value)), value -> String.valueOf((int) value));
    }

    /** A slider with a box next to it to type an exact value in (#10). */
    private Row number(ConfigOption<?> option, double min, double max, double step, boolean whole,
                       DoubleSupplier get, DoubleConsumer set, DoubleFunction<String> format) {
        Row row = row(option, "");
        int sliderWidth = controlWidth() - NUMBER_BOX - 2;
        ValueSlider slider = new ValueSlider(controlX(), sliderWidth, 20, min, max, step, get, set, format);
        NumberBox box = new NumberBox(controlX() + sliderWidth + 2, NUMBER_BOX);
        box.listen(typed -> {
            try {
                double value = whole ? Integer.parseInt(typed.trim()) : Double.parseDouble(typed.trim());
                if (value < min || value > max) return false;
                set.accept(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }, () -> box.show(exact(get.getAsDouble())));
        box.show(exact(get.getAsDouble()));
        row.add(slider, 0);
        row.add(box, 0);
        row.refresh = () -> {
            slider.sync();
            if (!box.isFocused()) box.show(exact(get.getAsDouble()));
        };
        double fallback = ((Number) option.getDefaultValue()).doubleValue();
        withReset(row, () -> Math.abs(get.getAsDouble() - fallback) > 1e-6, () -> set.accept(fallback));
        return row;
    }

    private static String exact(double value) {
        return new BigDecimal(value).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

    private static String plain(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }

    private static String seconds(double value) {
        return String.format(Locale.ROOT, "%.1f s", value);
    }

    private static String degrees(double value) {
        return String.format(Locale.ROOT, "%.1f°", value);
    }

    // --- scrolling -------------------------------------------------------------

    private int maxScroll() {
        return Math.max(0, contentHeight - (listBottom - listTop));
    }

    private void placeRows() {
        scroll = Math.max(0, Math.min(scroll, maxScroll()));
        for (Row row : rows) {
            int y = listTop + row.y - scroll;
            boolean inView = y + row.height > listTop && y < listBottom;
            for (int i = 0; i < row.widgets.size(); i++) {
                AbstractWidget widget = row.widgets.get(i);
                widget.setY(y + row.offsets.get(i));
                widget.visible = inView && widget.getY() + widget.getHeight() > listTop && widget.getY() < listBottom;
            }
            if (row.resetButton != null) {
                row.resetButton.setY(y);
                row.resetButton.visible = inView && row.enabled.getAsBoolean() && row.changed.getAsBoolean();
            }
        }
    }

    private void refresh() {
        for (Row row : rows) {
            row.refresh.run();
            boolean enabled = row.enabled.getAsBoolean();
            Component tip = enabled ? row.tip : row.disabledTip;
            for (AbstractWidget widget : row.widgets) {
                widget.active = enabled;
                if (!tip.getString().isEmpty()) widget.setTooltip(Tooltip.create(tip));
            }
        }
        placeRows();
    }

    @Override
    public void tick() {
        super.tick();
        if (rebuild) {
            rebuild = false;
            searchFocused = searchBox.isFocused();
            rebuildWidgets();
        }
        refresh();
    }

    private boolean overList(double mouseX) {
        return mouseX >= listX && mouseX < listX + listWidth;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (!overList(mouseX) || scrollY == 0) return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        scroll -= (int) Math.round(scrollY * SCROLL_STEP);
        placeRows();
        return true;
    }

    private int barX() {
        return listX + listWidth - 7;
    }

    private int knobHeight() {
        int height = listBottom - listTop;
        return Math.max(16, height * height / Math.max(1, contentHeight));
    }

    private int knobY() {
        int travel = listBottom - listTop - knobHeight();
        return listTop + (maxScroll() == 0 ? 0 : travel * scroll / maxScroll());
    }

    private boolean grabBar(double mouseX, double mouseY) {
        if (maxScroll() == 0 || mouseX < barX() || mouseX >= listX + listWidth || mouseY < listTop || mouseY >= listBottom) {
            return false;
        }
        int knob = knobY();
        draggingBar = true;
        grabOffset = mouseY >= knob && mouseY < knob + knobHeight() ? mouseY - knob : knobHeight() / 2.0;
        dragBar(mouseY);
        return true;
    }

    private void dragBar(double mouseY) {
        double travel = listBottom - listTop - knobHeight();
        scroll = (int) Math.round((mouseY - listTop - grabOffset) / travel * maxScroll());
        placeRows();
    }

    //? if >=1.21.9 {
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == 0 && grabBar(event.x(), event.y())) return true;
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
        if (!draggingBar) return super.mouseDragged(event, deltaX, deltaY);
        dragBar(event.y());
        return true;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        draggingBar = false;
        return super.mouseReleased(event);
    }
    //?}
    //? if <1.21.9 {
    /*@Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && grabBar(mouseX, mouseY)) return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!draggingBar) return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        dragBar(mouseY);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggingBar = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }
    *///?}

    // --- painting --------------------------------------------------------------

    /**
     * The live paper doll in a fixed box on the left, with every look setting applied. The doll is
     * moved and sized into the box for this one draw by swapping its position settings, then they
     * are put back; the real position shows in the line under the buttons and in the editor.
     */
    private void paintPreview(Canvas canvas) {
        int[] box = previewBox();
        int w = box[2] - box[0], h = box[3] - box[1];
        canvas.text(text("preview").getString(), box[0], HEADER + 5, WHITE, true);
        String placement = Component.translatable(KEY + "placement", plain(CONFIGS.offsetX.getValue()),
                plain(CONFIGS.offsetY.getValue()), plain(CONFIGS.size.getValue())).getString();
        placement = canvas.trim(placement, listX - 8);
        canvas.text(placement, (listX - canvas.textWidth(placement)) / 2, this.height - FOOTER - 11, GREY, true);
        if (w < 20 || h < 30) return;

        canvas.rect(box[0], box[1], w, h, 0x66000000);
        canvas.rect(box[0], box[1], w, 1, LINE);
        canvas.rect(box[0], box[3] - 1, w, 1, LINE);
        canvas.rect(box[0], box[1], 1, h, LINE);
        canvas.rect(box[2] - 1, box[1], 1, h, LINE);

        String note = this.minecraft.level == null ? text("note.no_world").getString()
                : !CONFIGS.displayPaperDoll.getValue() ? text("note.hidden").getString() : "";
        if (!note.isEmpty()) {
            note = canvas.trim(note, w - 8);
            canvas.text(note, box[0] + (w - canvas.textWidth(note)) / 2, box[1] + h / 2 - 4, YELLOW, true);
            return;
        }

        // The doll stands with its feet at the offset point and is about two blocks of Size tall
        double sizePx = Math.min((h - 12) / 2.1, (w - 12) / 1.3);
        double offsetX = CONFIGS.offsetX.getValue(), offsetY = CONFIGS.offsetY.getValue(), size = CONFIGS.size.getValue();
        CONFIGS.offsetX.setValue((box[0] + w / 2.0) / this.width);
        CONFIGS.offsetY.setValue((box[3] - 6) / (double) this.height);
        CONFIGS.size.setValue(sizePx / this.height);
        try {
            paintDoll(canvas);
        } finally {
            CONFIGS.offsetX.setValue(offsetX);
            CONFIGS.offsetY.setValue(offsetY);
            CONFIGS.size.setValue(size);
        }
    }

    private void paintDoll(Canvas canvas) {
        //? if >=26.1 {
        previewHud.extractPaperdoll(canvas.graphics(), this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true));
        //?}
        //? if >=1.21.3 && <26.1 {
        /*previewHud.render(this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true), canvas.graphics());
        *///?}
        //? if <1.21.3 {
        /*previewHud.render(this.minecraft.getTimer().getGameTimeDeltaPartialTick(true), canvas.graphics());
        *///?}
        //? if <1.21.6 {
        /*// Keep the rest of the screen in front of the doll
        canvas.graphics().pose().translate(0, 0, 200);
        *///?}
    }

    private void paintBase(Canvas canvas) {
        canvas.rect(listX, HEADER, listWidth, listBottom - HEADER, 0xAD000000);
        canvas.rect(0, 0, this.width, HEADER, 0xC7000000);
        canvas.rect(0, this.height - FOOTER, this.width, FOOTER, 0xC7000000);
        canvas.rect(0, HEADER - 1, this.width, 1, LINE);
        canvas.rect(0, this.height - FOOTER, this.width, 1, LINE);
        String title = this.title.getString();
        canvas.text(title, (this.width - canvas.textWidth(title)) / 2, 8, WHITE, true);
    }

    private void beginList(Canvas canvas) {
        canvas.clip(listX, listTop, listX + listWidth, listBottom);
        for (Row row : rows) {
            int y = listTop + row.y - scroll;
            if (y + row.height <= listTop || y >= listBottom) continue;
            if (row.section) {
                canvas.text(row.label, rowX() + (rowWidth() - canvas.textWidth(row.label)) / 2, y + 7, YELLOW, true);
            } else if (row.labelled || !row.widgets.isEmpty()) {
                String label = canvas.trim(row.label, (row.labelled ? controlX() : rowX() + rowWidth()) - rowX() - 6);
                canvas.text(label, rowX() + 2, y + 6, row.enabled.getAsBoolean() ? WHITE : DIM, true);
            }
        }
    }

    private void endList(Canvas canvas) {
        for (Row row : rows) {
            if (row.resetButton != null && row.resetButton.visible) {
                resetIcon(canvas, row.resetButton.getX() + 3, row.resetButton.getY() + 6);
            }
        }
        canvas.endClip();
        if (rows.isEmpty()) {
            String none = text("no_results").getString();
            canvas.text(none, listX + (listWidth - canvas.textWidth(none)) / 2, listTop + 20, GREY, true);
        }
        // Shadow under the search box while the list is scrolled
        if (scroll > 0) {
            for (int i = 0; i < 4; i++) canvas.rect(listX, listTop + i, listWidth - 7, 1, (0x80 - i * 0x1E) << 24);
        }
        if (maxScroll() > 0) {
            int barX = barX();
            canvas.rect(barX, listTop, 6, listBottom - listTop, 0xFF000000);
            canvas.rect(barX, knobY(), 6, knobHeight(), 0xFF808080);
            canvas.rect(barX, knobY(), 5, knobHeight() - 1, 0xFFC0C0C0);
        }
    }

    private static void resetIcon(Canvas canvas, int x, int y) {
        canvas.rect(x + 2, y, 5, 1, WHITE);
        canvas.rect(x + 1, y + 1, 1, 1, WHITE);
        canvas.rect(x + 7, y + 1, 1, 1, WHITE);
        canvas.rect(x, y + 2, 1, 4, WHITE);
        canvas.rect(x + 8, y + 2, 1, 2, WHITE);
        canvas.rect(x + 7, y + 4, 3, 1, WHITE);
        canvas.rect(x + 8, y + 5, 1, 1, WHITE);
        canvas.rect(x + 1, y + 6, 1, 1, WHITE);
        canvas.rect(x + 2, y + 7, 5, 1, WHITE);
    }

    /** In a world the background is left out, so the doll and the world behind it stay sharp. */
    private boolean keepWorldVisible() {
        return this.minecraft.level != null && CONFIGS.disableConfigScreenBlur.getValue();
    }

    //? if >=26.1 {
    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        if (!keepWorldVisible()) super.extractBackground(graphics, mouseX, mouseY, delta);
    }
    //?}
    //? if <26.1 {
    /*@Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (!keepWorldVisible()) super.renderBackground(graphics, mouseX, mouseY, delta);
    }
    *///?}

    @Override
    public boolean isPauseScreen() {
        return CONFIGS.pauseGameOnConfigScreen.getValue();
    }

    /** True while a text box has focus, so its letters don't close the screen. */
    private boolean typing() {
        return getFocused() instanceof EditBox;
    }

    //? if >=1.21.9 {
    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        //? if >=26.1 {
        boolean closeKey = AyamePaperDoll.OPEN_CONFIG_GUI.matches(keyEvent);
        //?}
        //? if <26.1 {
        /*boolean closeKey = keyEvent.key() == CommonInterfaceInstances.keyHelper.getBoundKeyOf(AyamePaperDoll.OPEN_CONFIG_GUI).getValue();
        *///?}
        if (closeKey && !typing()) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyEvent);
    }
    //?}
    //? if <1.21.9 {
    /*@Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == CommonInterfaceInstances.keyHelper.getBoundKeyOf(AyamePaperDoll.OPEN_CONFIG_GUI).getValue() && !typing()) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    *///?}

    private static void save() {
        AyamePaperDoll.CONFIG_PERSISTENCE.save(CONFIGS.getOptions());
    }

    private static void open(Screen screen) {
        //? if >=26.2 {
        Minecraft.getInstance().gui.setScreen(screen);
        //?}
        //? if <26.2 {
        /*Minecraft.getInstance().setScreen(screen);
        *///?}
    }

    @Override
    public void onClose() {
        open(lastScreen);
        save();
    }

    // --- widgets ---------------------------------------------------------------

    /** Text box drawn with the mod's own textures. */
    private static class TextBox extends EditBox implements Retextured {
        TextBox(int x, int y, int width, int height) {
            super(Minecraft.getInstance().font, x, y, width, height, Component.empty());
        }

        @Override
        //? if >=1.21.11 {
        public Identifier retexture(Identifier oldTexture) {
        //?}
        //? if <1.21.11 {
        /*public ResourceLocation retexture(ResourceLocation oldTexture) {
        *///?}
            return AyamePaperDoll.path(oldTexture.getPath());
        }
    }

    /**
     * Box next to a slider to type an exact value in. Values that don't parse or are out of range
     * turn red and aren't applied; leaving the box shows the option's value again.
     */
    private static final class NumberBox extends TextBox {
        private Predicate<String> apply = text -> false;
        private Runnable onUnfocus = () -> {
        };
        private boolean showing;

        NumberBox(int x, int width) {
            super(x, 0, width, 20);
            setMaxLength(12);
            setResponder(text -> {
                if (showing) return;
                setTextColor(apply.test(text) ? VALID : INVALID);
            });
        }

        void listen(Predicate<String> apply, Runnable onUnfocus) {
            this.apply = apply;
            this.onUnfocus = onUnfocus;
        }

        /** Shows a value without applying it again. */
        void show(String text) {
            if (!text.equals(getValue())) {
                showing = true;
                setValue(text);
                showing = false;
            }
            setTextColor(VALID);
        }

        @Override
        public void setFocused(boolean focused) {
            super.setFocused(focused);
            if (!focused) onUnfocus.run();
        }
    }

    /** Slider that shows a formatted value and snaps to a step. */
    private static final class ValueSlider extends AbstractSliderButton implements Retextured {
        private final double min;
        private final double max;
        private final double step;
        private final DoubleSupplier get;
        private final DoubleConsumer set;
        private final DoubleFunction<String> format;

        ValueSlider(int x, int width, int height, double min, double max, double step, DoubleSupplier get,
                    DoubleConsumer set, DoubleFunction<String> format) {
            super(x, 0, width, height, Component.empty(), (Math.max(min, Math.min(max, get.getAsDouble())) - min) / (max - min));
            this.min = min;
            this.max = max;
            this.step = step;
            this.get = get;
            this.set = set;
            this.format = format;
            updateMessage();
        }

        /** Follows changes made elsewhere, such as the number box, a reset or the visual editor. */
        void sync() {
            this.value = (Math.max(min, Math.min(max, get.getAsDouble())) - min) / (max - min);
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            setMessage(Component.literal(format.apply(get.getAsDouble())));
        }

        @Override
        protected void applyValue() {
            double raw = min + this.value * (max - min);
            double snapped = Math.round(raw / step) * step;
            set.accept(Math.max(min, Math.min(max, Math.round(snapped * 10000.0) / 10000.0)));
        }

        @Override
        //? if >=1.21.11 {
        public Identifier retexture(Identifier oldTexture) {
        //?}
        //? if <1.21.11 {
        /*public ResourceLocation retexture(ResourceLocation oldTexture) {
        *///?}
            return AyamePaperDoll.path(oldTexture.getPath());
        }
    }
}
