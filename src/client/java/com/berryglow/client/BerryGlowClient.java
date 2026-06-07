package com.berryglow.client;

import com.berryglow.BerryGlow;
import net.fabricmc.api.ClientModInitializer;
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
