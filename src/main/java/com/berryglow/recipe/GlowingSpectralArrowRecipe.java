package com.berryglow.recipe;

import com.berryglow.BerryGlow;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class GlowingSpectralArrowRecipe extends CustomRecipe {
	public GlowingSpectralArrowRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		if (!BerryGlow.CONFIG.enableGlowingPotions() || !BerryGlow.CONFIG.enableGlowingSpectralArrows()) {
			return false;
		}

		if (input.width() != 3 || input.height() != 3 || input.ingredientCount() != 9) {
			return false;
		}

		for (int y = 0; y < input.height(); y++) {
			for (int x = 0; x < input.width(); x++) {
				ItemStack stack = input.getItem(x, y);

				if (stack.isEmpty()) {
					return false;
				}

				if (x == 1 && y == 1) {
					if (!isGlowingLingeringPotion(stack)) {
						return false;
					}
				} else if (!stack.is(Items.ARROW)) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		ItemStack centerStack = input.getItem(1, 1);

		if (!isGlowingLingeringPotion(centerStack)) {
			return ItemStack.EMPTY;
		}

		return new ItemStack(Items.SPECTRAL_ARROW, 8);
	}

	@Override
	public RecipeSerializer<GlowingSpectralArrowRecipe> getSerializer() {
		return BerryGlow.GLOWING_SPECTRAL_ARROW_RECIPE_SERIALIZER;
	}

	public static boolean isGlowingLingeringPotion(ItemStack stack) {
		if (!stack.is(Items.LINGERING_POTION)) {
			return false;
		}

		PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);
		if (potionContents == null) {
			return false;
		}

		return potionContents.is(BerryGlow.GLOWING_POTION) || potionContents.is(BerryGlow.LONG_GLOWING_POTION);
	}
}
