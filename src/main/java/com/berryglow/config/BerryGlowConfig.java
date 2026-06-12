package com.berryglow.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Mutable, reloadable configuration holder. Values are read live through the
 * accessor methods so config changes made at runtime (e.g. from the Mod Menu
 * screen) take effect immediately for features that query them per-use.
 *
 * <p>Note: {@link #enableGlowingPotions()} is also read once at mod init to
 * register brewing recipes; toggling it requires a game restart to take effect.
 */
public final class BerryGlowConfig {
	public static final int MIN_GLOW_BERRY_DURATION_SECONDS = 0;
	public static final int MAX_GLOW_BERRY_DURATION_SECONDS = 60;

	private static final boolean DEFAULT_ENABLE_GLOW_BERRY_EFFECT = true;
	private static final int DEFAULT_GLOW_BERRY_DURATION_SECONDS = 5;
	private static final boolean DEFAULT_ENABLE_GLOWING_POTIONS = true;
	private static final boolean DEFAULT_ENABLE_GLOWING_SPECTRAL_ARROWS = true;

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private final Path configPath;
	private final Logger logger;

	private boolean enableGlowBerryEffect;
	private int glowBerryDurationSeconds;
	private boolean enableGlowingPotions;
	private boolean enableGlowingSpectralArrows;

	private BerryGlowConfig(Path configPath, Logger logger) {
		this.configPath = configPath;
		this.logger = logger;
	}

	public static BerryGlowConfig load(Logger logger, String modId) {
		Path configPath = FabricLoader.getInstance().getConfigDir().resolve(modId + ".json");
		BerryGlowConfig config = new BerryGlowConfig(configPath, logger);

		JsonObject json = new JsonObject();
		if (Files.exists(configPath)) {
			try (Reader reader = Files.newBufferedReader(configPath)) {
				JsonElement parsed = JsonParser.parseReader(reader);
				if (parsed != null && parsed.isJsonObject()) {
					json = parsed.getAsJsonObject();
				}
			} catch (IOException | JsonParseException exception) {
				logger.warn("Failed to read config at {}", configPath, exception);
			}
		}

		config.enableGlowBerryEffect = getBoolean(json, "enableGlowBerryEffect", DEFAULT_ENABLE_GLOW_BERRY_EFFECT);
		config.glowBerryDurationSeconds = getPositiveInt(json, "glowBerryDurationSeconds", DEFAULT_GLOW_BERRY_DURATION_SECONDS);
		config.enableGlowingPotions = getBoolean(json, "enableGlowingPotions", DEFAULT_ENABLE_GLOWING_POTIONS);
		config.enableGlowingSpectralArrows = getBoolean(json, "enableGlowingSpectralArrows", DEFAULT_ENABLE_GLOWING_SPECTRAL_ARROWS);

		config.save();
		return config;
	}

	public void save() {
		JsonObject json = new JsonObject();
		json.addProperty("enableGlowBerryEffect", enableGlowBerryEffect);
		json.addProperty("glowBerryDurationSeconds", glowBerryDurationSeconds);
		json.addProperty("enableGlowingPotions", enableGlowingPotions);
		json.addProperty("enableGlowingSpectralArrows", enableGlowingSpectralArrows);

		try {
			Files.createDirectories(configPath.getParent());
			try (Writer writer = Files.newBufferedWriter(configPath)) {
				GSON.toJson(json, writer);
			}
		} catch (IOException exception) {
			logger.warn("Failed to write config at {}", configPath, exception);
		}
	}

	public boolean enableGlowBerryEffect() {
		return enableGlowBerryEffect;
	}

	public void setEnableGlowBerryEffect(boolean value) {
		this.enableGlowBerryEffect = value;
	}

	public int glowBerryDurationSeconds() {
		return glowBerryDurationSeconds;
	}

	public void setGlowBerryDurationSeconds(int value) {
		this.glowBerryDurationSeconds = clampDuration(value);
	}

	public boolean enableGlowingPotions() {
		return enableGlowingPotions;
	}

	public void setEnableGlowingPotions(boolean value) {
		this.enableGlowingPotions = value;
	}

	public boolean enableGlowingSpectralArrows() {
		return enableGlowingSpectralArrows;
	}

	public void setEnableGlowingSpectralArrows(boolean value) {
		this.enableGlowingSpectralArrows = value;
	}

	private static int clampDuration(int value) {
		return Math.max(MIN_GLOW_BERRY_DURATION_SECONDS, Math.min(MAX_GLOW_BERRY_DURATION_SECONDS, value));
	}

	private static boolean getBoolean(JsonObject json, String key, boolean fallback) {
		if (!json.has(key) || !json.get(key).isJsonPrimitive()) {
			return fallback;
		}

		return json.get(key).getAsBoolean();
	}

	private static int getPositiveInt(JsonObject json, String key, int fallback) {
		if (!json.has(key) || !json.get(key).isJsonPrimitive()) {
			return fallback;
		}

		try {
			return clampDuration(json.get(key).getAsInt());
		} catch (NumberFormatException exception) {
			return fallback;
		}
	}
}
