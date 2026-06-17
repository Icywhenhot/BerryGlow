package com.berryglow.mixin;

import com.berryglow.BerryGlow;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Not registering our brewing recipes only stops new Glowing Potions from being brewed.
// Vanilla's generic container mixes still let players modify EXISTING glowing potions in
// a brewing stand (gunpowder -> splash, dragon's breath -> lingering), and redstone would
// extend them. hasMix(input, ingredient) is the single gate the brewing stand consults,
// so when the feature is off we reject any mix whose input bottle is a glowing potion.
// Existing potions still work when drunk or thrown; they just can't be re-brewed.
@Mixin(PotionBrewing.class)
public class GlowingPotionBrewingMixin {
	@Inject(
		method = "hasMix(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z",
		at = @At("HEAD"),
		cancellable = true
	)
	private void berryglow$blockGlowingBrewing(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
		if (BerryGlow.CONFIG.enableGlowingPotions()) {
			return;
		}

		PotionContents potionContents = input.get(DataComponents.POTION_CONTENTS);
		if (potionContents != null && BerryGlow.isGlowingPotion(potionContents)) {
			cir.setReturnValue(false);
		}
	}
}
