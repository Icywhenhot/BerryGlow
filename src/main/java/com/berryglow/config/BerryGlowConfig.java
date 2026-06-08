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

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public static BerryGlowConfig load(Logger logger, String modId) {
		Path configPath = FabricLoader.getInstance().getConfigDir().resolve(modId + ".json");
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

		BerryGlowConfig config = new BerryGlowConfig(
			getBoolean(json, "enableGlowBerryEffect", DEFAULT_ENABLE_GLOW_BERRY_EFFECT),
			getPositiveInt(json, "glowBerryDurationSeconds", DEFAULT_GLOW_BERRY_DURATION_SECONDS),
			getBoolean(json, "enableGlowingPotions", DEFAULT_ENABLE_GLOWING_POTIONS),
			getBoolean(json, "enableGlowingSpectralArrows", DEFAULT_ENABLE_GLOWING_SPECTRAL_ARROWS)
		);

		writeDefaults(configPath, config, logger);
		return config;
	}

	private static void writeDefaults(Path configPath, BerryGlowConfig config, Logger logger) {
		JsonObject json = new JsonObject();
		json.addProperty("enableGlowBerryEffect", config.enableGlowBerryEffect());
		json.addProperty("glowBerryDurationSeconds", config.glowBerryDurationSeconds());
		json.addProperty("enableGlowingPotions", config.enableGlowingPotions());
		json.addProperty("enableGlowingSpectralArrows", config.enableGlowingSpectralArrows());

		try {
			Files.createDirectories(configPath.getParent());
			try (Writer writer = Files.newBufferedWriter(configPath)) {
				GSON.toJson(json, writer);
			}
		} catch (IOException exception) {
			logger.warn("Failed to write config at {}", configPath, exception);
		}
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
			return Math.max(0, json.get(key).getAsInt());
		} catch (NumberFormatException exception) {
			return fallback;
		}
	}
}
