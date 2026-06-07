package com.berryglow;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BerryGlow implements ModInitializer {
	public static final String MOD_ID = "berryglow";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Minecraft runs at 20 ticks per second.
	public static final int GLOW_BERRY_DURATION_TICKS = 5 * 20;
	public static final int GLOWING_POTION_DURATION_TICKS = 3 * 60 * 20;
	public static final int LONG_GLOWING_POTION_DURATION_TICKS = 8 * 60 * 20;

	public static final Holder.Reference<Potion> GLOWING_POTION = registerPotion("glowing", GLOWING_POTION_DURATION_TICKS);
	public static final Holder.Reference<Potion> LONG_GLOWING_POTION = registerPotion("long_glowing", LONG_GLOWING_POTION_DURATION_TICKS);

	@Override
	public void onInitialize() {
		FabricPotionBrewingBuilder.BUILD.register(builder -> {
			builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(Items.GLOW_BERRIES), GLOWING_POTION);
			builder.registerPotionRecipe(GLOWING_POTION, Ingredient.of(Items.REDSTONE), LONG_GLOWING_POTION);
		});

		LOGGER.info("BerryGlow loaded - glow berries and glowing potions are ready.");
	}

	private static Holder.Reference<Potion> registerPotion(String id, int durationTicks) {
		return Registry.registerForHolder(
			BuiltInRegistries.POTION,
			Identifier.fromNamespaceAndPath(MOD_ID, id),
			new Potion(id, new MobEffectInstance(MobEffects.GLOWING, durationTicks))
		);
	}
}
