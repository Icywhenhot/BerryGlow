package com.berryglow.mixin;

import com.berryglow.BerryGlow;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// 1.21.2+ reworked eating into the Consumable component system, so the old
// Item.finishUsingItem hook is gone. We instead hook the entity that finishes
// using an item, which is stable across the 1.21.2+ line, and read the item it
// was using.
@Mixin(LivingEntity.class)
public class GlowBerryEatMixin {
	@Inject(method = "completeUsingItem", at = @At("HEAD"))
	private void berryglow$applyGlowOnEat(CallbackInfoReturnable<ItemStack> cir) {
		LivingEntity self = (LivingEntity) (Object) this;

		if (self.level().isClientSide()) {
			return;
		}

		if (self.getUseItem().is(Items.GLOW_BERRIES)) {
			self.addEffect(new MobEffectInstance(MobEffects.GLOWING, BerryGlow.GLOW_DURATION_TICKS));
		}
	}
}
