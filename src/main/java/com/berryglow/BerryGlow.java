package com.berryglow;

import com.berryglow.config.BerryGlowConfig;
import com.berryglow.recipe.GlowingSpectralArrowRecipe;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BerryGlow implements ModInitializer {
	public static final String MOD_ID = "berryglow";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Minecraft runs at 20 ticks per second.
	public static final int GLOWING_POTION_DURATION_TICKS = 3 * 60 * 20;
	public static final int LONG_GLOWING_POTION_DURATION_TICKS = 8 * 60 * 20;

	public static final BerryGlowConfig CONFIG = BerryGlowConfig.load(LOGGER, MOD_ID);

	public static final Holder.Reference<Potion> GLOWING_POTION = registerPotion("glowing", GLOWING_POTION_DURATION_TICKS);
	public static final Holder.Reference<Potion> LONG_GLOWING_POTION = registerPotion("long_glowing", LONG_GLOWING_POTION_DURATION_TICKS);
	public static final RecipeSerializer<GlowingSpectralArrowRecipe> GLOWING_SPECTRAL_ARROW_RECIPE_SERIALIZER = Registry.register(
		BuiltInRegistries.RECIPE_SERIALIZER,
		Identifier.fromNamespaceAndPath(MOD_ID, "glowing_spectral_arrow"),
		new CustomRecipe.Serializer<>(GlowingSpectralArrowRecipe::new)
	);

	@Override
	public void onInitialize() {
		if (CONFIG.enableGlowingPotions()) {
			FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
				builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(Items.GLOW_BERRIES), GLOWING_POTION);
				builder.registerPotionRecipe(GLOWING_POTION, Ingredient.of(Items.REDSTONE), LONG_GLOWING_POTION);
			});
		}

		LOGGER.info("BerryGlow loaded - glow berries and glowing potions are ready.");
	}

	public static int getGlowBerryDurationTicks() {
		return CONFIG.glowBerryDurationSeconds() * 20;
	}

	private static Holder.Reference<Potion> registerPotion(String id, int durationTicks) {
		return Registry.registerForHolder(
			BuiltInRegistries.POTION,
			Identifier.fromNamespaceAndPath(MOD_ID, id),
			new Potion(id, new MobEffectInstance(MobEffects.GLOWING, durationTicks))
		);
	}
}
