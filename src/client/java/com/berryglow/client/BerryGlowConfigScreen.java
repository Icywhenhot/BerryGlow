package com.berryglow.client;

import com.berryglow.BerryGlow;
import com.berryglow.config.BerryGlowConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/**
 * Vanilla-widget config screen opened from Mod Menu. Edits a working copy and
 * only writes back to {@link BerryGlow#CONFIG} (and disk) when the player
 * presses Done, so Cancel discards changes.
 */
public class BerryGlowConfigScreen extends Screen {
	private static final int WIDGET_WIDTH = 220;
	private static final int WIDGET_HEIGHT = 20;
	private static final int ROW_SPACING = 24;

	private final Screen parent;
	private final BerryGlowConfig config;

	private boolean enableGlowBerryEffect;
	private int glowBerryDurationSeconds;
	private boolean enableGlowingPotions;
	private boolean enableGlowingSpectralArrows;

	public BerryGlowConfigScreen(Screen parent) {
		super(Component.translatable("berryglow.config.title"));
		this.parent = parent;
		this.config = BerryGlow.CONFIG;

		this.enableGlowBerryEffect = config.enableGlowBerryEffect();
		this.glowBerryDurationSeconds = config.glowBerryDurationSeconds();
		this.enableGlowingPotions = config.enableGlowingPotions();
		this.enableGlowingSpectralArrows = config.enableGlowingSpectralArrows();
	}

	@Override
	protected void init() {
		int x = this.width / 2 - WIDGET_WIDTH / 2;
		int y = 48;

		addRenderableWidget(CycleButton.onOffBuilder(this.enableGlowBerryEffect)
			.withTooltip(value -> Tooltip.create(Component.translatable("berryglow.config.enableGlowBerryEffect.tooltip")))
			.create(x, y, WIDGET_WIDTH, WIDGET_HEIGHT,
				Component.translatable("berryglow.config.enableGlowBerryEffect"),
				(button, value) -> this.enableGlowBerryEffect = value));
		y += ROW_SPACING;

		addRenderableWidget(new DurationSlider(x, y, WIDGET_WIDTH, WIDGET_HEIGHT));
		y += ROW_SPACING;

		addRenderableWidget(CycleButton.onOffBuilder(this.enableGlowingPotions)
			.withTooltip(value -> Tooltip.create(Component.translatable("berryglow.config.enableGlowingPotions.tooltip")))
			.create(x, y, WIDGET_WIDTH, WIDGET_HEIGHT,
				Component.translatable("berryglow.config.enableGlowingPotions"),
				(button, value) -> this.enableGlowingPotions = value));
		y += ROW_SPACING;

		addRenderableWidget(CycleButton.onOffBuilder(this.enableGlowingSpectralArrows)
			.withTooltip(value -> Tooltip.create(Component.translatable("berryglow.config.enableGlowingSpectralArrows.tooltip")))
			.create(x, y, WIDGET_WIDTH, WIDGET_HEIGHT,
				Component.translatable("berryglow.config.enableGlowingSpectralArrows"),
				(button, value) -> this.enableGlowingSpectralArrows = value));

		addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> saveAndClose())
			.bounds(this.width / 2 - WIDGET_WIDTH / 2, this.height - 32, WIDGET_WIDTH / 2 - 4, WIDGET_HEIGHT)
			.build());
		addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> onClose())
			.bounds(this.width / 2 + 4, this.height - 32, WIDGET_WIDTH / 2 - 4, WIDGET_HEIGHT)
			.build());
	}

	private void saveAndClose() {
		config.setEnableGlowBerryEffect(this.enableGlowBerryEffect);
		config.setGlowBerryDurationSeconds(this.glowBerryDurationSeconds);
		config.setEnableGlowingPotions(this.enableGlowingPotions);
		config.setEnableGlowingSpectralArrows(this.enableGlowingSpectralArrows);
		config.save();
		onClose();
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(this.parent);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);
		graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFFFF);
	}

	private class DurationSlider extends AbstractSliderButton {
		private static final int RANGE = BerryGlowConfig.MAX_GLOW_BERRY_DURATION_SECONDS
			- BerryGlowConfig.MIN_GLOW_BERRY_DURATION_SECONDS;

		DurationSlider(int x, int y, int width, int height) {
			super(x, y, width, height, Component.empty(),
				(double) (glowBerryDurationSeconds - BerryGlowConfig.MIN_GLOW_BERRY_DURATION_SECONDS) / RANGE);
			updateMessage();
		}

		@Override
		protected void updateMessage() {
			setMessage(Component.translatable("berryglow.config.glowBerryDurationSeconds", glowBerryDurationSeconds));
		}

		@Override
		protected void applyValue() {
			glowBerryDurationSeconds = BerryGlowConfig.MIN_GLOW_BERRY_DURATION_SECONDS
				+ (int) Math.round(this.value * RANGE);
		}
	}
}
