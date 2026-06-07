package com.berryglow.client;

import com.berryglow.BerryGlow;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;

public class BerryGlowClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		registerGlowingPotionModelPredicate(Items.POTION);
		registerGlowingPotionModelPredicate(Items.SPLASH_POTION);
		registerGlowingPotionModelPredicate(Items.LINGERING_POTION);

		registerPotionColorProvider();
	}

	// The custom glowing potion textures are full-color artwork, so they must not be
	// tinted by the potion's (dull olive) effect color. We replace the vanilla potion
	// color provider, returning white (no tint) for glowing potions while preserving
	// vanilla tinting for every other potion.
	private static void registerPotionColorProvider() {
		ColorProviderRegistry.ITEM.register(
			(stack, tintIndex) -> {
				if (tintIndex > 0 || isGlowingPotion(stack)) {
					return -1;
				}
				return stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getColor();
			},
			Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION
		);
	}

	private static void registerGlowingPotionModelPredicate(Item item) {
		ItemProperties.register(
			item,
			ResourceLocation.fromNamespaceAndPath(BerryGlow.MOD_ID, "glowing"),
			(stack, level, entity, seed) -> isGlowingPotion(stack) ? 1.0F : 0.0F
		);
	}

	private static boolean isGlowingPotion(ItemStack stack) {
		PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);
		if (potionContents == null) {
			return false;
		}

		return potionContents.is(BerryGlow.GLOWING_POTION) || potionContents.is(BerryGlow.LONG_GLOWING_POTION);
	}
}
