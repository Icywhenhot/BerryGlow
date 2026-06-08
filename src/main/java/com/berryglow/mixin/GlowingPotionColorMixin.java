package com.berryglow.mixin;

import com.berryglow.BerryGlow;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Vanilla's GLOWING effect tints the broken splash/lingering potion particles a dull
// olive-yellow. ThrownPotion and AreaEffectCloud derive that particle color from
// PotionContents#getColor, so override it for our glowing potions to make the dropped
// particles a bright yellow. The bottle items use custom untinted models, so this only
// affects the in-world particle color.
@Mixin(PotionContents.class)
public class GlowingPotionColorMixin {
	@Inject(method = "getColor()I", at = @At("HEAD"), cancellable = true)
	private void berryglow$brightenGlowingColor(CallbackInfoReturnable<Integer> cir) {
		if (BerryGlow.isGlowingPotion((PotionContents) (Object) this)) {
			cir.setReturnValue(BerryGlow.GLOWING_PARTICLE_COLOR);
		}
	}
}
