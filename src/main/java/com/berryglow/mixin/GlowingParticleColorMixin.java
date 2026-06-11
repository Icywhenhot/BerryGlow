package com.berryglow.mixin;

import com.berryglow.BerryGlow;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.ARGB;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Vanilla's GLOWING effect spawns dull olive-yellow swirl particles on the affected entity.
// GlowingPotionColorMixin already brightens the thrown/lingering potion particles via
// PotionContents#getColor, but the swirl attached to the player comes straight from the mob
// effect. Override the GLOWING effect's particle options so the on-entity swirl matches the
// bright yellow of our potions. This applies to every source of Glowing (potion, spectral
// arrow), keeping the effect visually consistent.
@Mixin(MobEffect.class)
public class GlowingParticleColorMixin {
	@Inject(method = "createParticleOptions", at = @At("HEAD"), cancellable = true)
	private void berryglow$brightenGlowingParticles(MobEffectInstance instance, CallbackInfoReturnable<ParticleOptions> cir) {
		if ((Object) this == MobEffects.GLOWING.value()) {
			cir.setReturnValue(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, ARGB.opaque(BerryGlow.GLOWING_PARTICLE_COLOR)));
		}
	}
}
