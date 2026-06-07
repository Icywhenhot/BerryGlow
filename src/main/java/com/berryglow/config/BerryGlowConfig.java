package com.berryglow.config;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public record BerryGlowConfig(
	boolean enableGlowBerryEffect,
	int glowBerryDurationSeconds,
	boolean enableGlowingPotions,
	boolean enableGlowingSpectralArrows
) {
	private static final boolean DEFAULT_ENABLE_GLOW_BERRY_EFFECT = true;
	private static final int DEFAULT_GLOW_BERRY_DURATION_SECONDS = 5;
	private static final boolean DEFAULT_ENABLE_GLOWING_POTIONS = true;
	private static final boolean DEFAULT_ENABLE_GLOWING_SPECTRAL_ARROWS = true;

	public static BerryGlowConfig load(Logger logger, String modId) {
		Path configPath = FabricLoader.getInstance().getConfigDir().resolve(modId + ".properties");
		Properties properties = new Properties();

		if (Files.exists(configPath)) {
			try (InputStream inputStream = Files.newInputStream(configPath)) {
				properties.load(inputStream);
			} catch (IOException exception) {
				logger.warn("Failed to read config at {}", configPath, exception);
			}
		}

		BerryGlowConfig config = new BerryGlowConfig(
			getBoolean(properties, "enableGlowBerryEffect", DEFAULT_ENABLE_GLOW_BERRY_EFFECT),
			getPositiveInt(properties, "glowBerryDurationSeconds", DEFAULT_GLOW_BERRY_DURATION_SECONDS),
			getBoolean(properties, "enableGlowingPotions", DEFAULT_ENABLE_GLOWING_POTIONS),
			getBoolean(properties, "enableGlowingSpectralArrows", DEFAULT_ENABLE_GLOWING_SPECTRAL_ARROWS)
		);

		writeDefaults(configPath, config, logger);
		return config;
	}

	private static void writeDefaults(Path configPath, BerryGlowConfig config, Logger logger) {
		Properties properties = new Properties();
		properties.setProperty("enableGlowBerryEffect", Boolean.toString(config.enableGlowBerryEffect()));
		properties.setProperty("glowBerryDurationSeconds", Integer.toString(config.glowBerryDurationSeconds()));
		properties.setProperty("enableGlowingPotions", Boolean.toString(config.enableGlowingPotions()));
		properties.setProperty("enableGlowingSpectralArrows", Boolean.toString(config.enableGlowingSpectralArrows()));

		try {
			Files.createDirectories(configPath.getParent());
			try (OutputStream outputStream = Files.newOutputStream(configPath)) {
				properties.store(outputStream, "BerryGlow configuration");
			}
		} catch (IOException exception) {
			logger.warn("Failed to write config at {}", configPath, exception);
		}
	}

	private static boolean getBoolean(Properties properties, String key, boolean fallback) {
		String value = properties.getProperty(key);
		return value == null ? fallback : Boolean.parseBoolean(value);
	}

	private static int getPositiveInt(Properties properties, String key, int fallback) {
		String value = properties.getProperty(key);

		if (value == null) {
			return fallback;
		}

		try {
			return Math.max(0, Integer.parseInt(value));
		} catch (NumberFormatException exception) {
			return fallback;
		}
	}
}
