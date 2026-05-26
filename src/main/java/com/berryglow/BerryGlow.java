package com.berryglow;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BerryGlow implements ModInitializer {
	public static final String MOD_ID = "berryglow";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Glow effect lasts 5 seconds. Minecraft runs at 20 ticks/second.
	public static final int GLOW_DURATION_TICKS = 5 * 20;

	@Override
	public void onInitialize() {
		LOGGER.info("BerryGlow loaded - glow berries now make you glow.");
	}
}
