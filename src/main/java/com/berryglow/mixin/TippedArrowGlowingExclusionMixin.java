package com.berryglow.mixin;

import com.berryglow.BerryGlow;
import com.berryglow.recipe.GlowingSpectralArrowRecipe;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.TippedArrowRecipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// The vanilla tipped-arrow recipe turns any lingering potion + 8 arrows into tipped
// arrows. Since our glowing potion is a real potion, that would produce "Arrow of
// Glowing" and shadow our spectral-arrow recipe. When our spectral-arrow feature is
// active, refuse the vanilla recipe for glowing lingering potions so only
// GlowingSpectralArrowRecipe matches. Every other potion still tips arrows normally.
@Mixin(TippedArrowRecipe.class)
public class TippedArrowGlowingExclusionMixin {
	@Inject(
		method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
		at = @At("HEAD"),
		cancellable = true
	)
	private void berryglow$rejectGlowingPotion(CraftingInput input, Level level, CallbackInfoReturnable<Boolean> cir) {
		if (!BerryGlow.CONFIG.enableGlowingPotions() || !BerryGlow.CONFIG.enableGlowingSpectralArrows()) {
			return;
		}

		if (input.width() != 3 || input.height() != 3) {
			return;
		}

		if (GlowingSpectralArrowRecipe.isGlowingLingeringPotion(input.getItem(1, 1))) {
			cir.setReturnValue(false);
		}
	}
}
