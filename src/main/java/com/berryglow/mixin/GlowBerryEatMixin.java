package com.berryglow.mixin;

import com.berryglow.BerryGlow;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class GlowBerryEatMixin {
	@Inject(method = "finishUsingItem", at = @At("HEAD"))
	private void berryglow$applyGlowOnEat(ItemStack stack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
		// Only run on the server; effects are synced to clients automatically.
		if (level.isClientSide()) {
			return;
		}

		if (!BerryGlow.CONFIG.enableGlowBerryEffect()) {
			return;
		}

		if (stack.is(Items.GLOW_BERRIES)) {
			entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, BerryGlow.getGlowBerryDurationTicks()));
		}
	}
}
