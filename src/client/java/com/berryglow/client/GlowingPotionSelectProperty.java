package com.berryglow.client;

import com.berryglow.BerryGlow;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;

// 1.21.4 has the new item-model definition system but not the vanilla
// "minecraft:component" select property (added in 1.21.5). We backport a tiny
// boolean select property that reports whether a stack is one of our glowing
// potions, registered as "berryglow:glowing" and referenced from the potion item
// definitions to swap in the custom (untinted) glowing models.
public record GlowingPotionSelectProperty() implements SelectItemModelProperty<Boolean> {
	public static final SelectItemModelProperty.Type<GlowingPotionSelectProperty, Boolean> TYPE =
		SelectItemModelProperty.Type.create(MapCodec.unit(new GlowingPotionSelectProperty()), Codec.BOOL);

	@Override
	public Boolean get(ItemStack stack, ClientLevel level, LivingEntity entity, int seed, ItemDisplayContext context) {
		PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);
		if (potionContents == null) {
			return false;
		}

		return potionContents.is(BerryGlow.GLOWING_POTION) || potionContents.is(BerryGlow.LONG_GLOWING_POTION);
	}

	@Override
	public SelectItemModelProperty.Type<GlowingPotionSelectProperty, Boolean> type() {
		return TYPE;
	}
}
