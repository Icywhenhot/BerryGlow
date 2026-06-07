package com.berryglow.client;

import com.berryglow.BerryGlow;
import com.berryglow.mixin.client.SelectItemModelPropertiesAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.resources.ResourceLocation;

public class BerryGlowClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Register our backported "berryglow:glowing" select property so the potion
		// item definitions can swap in the custom glowing models. The models carry no
		// tint, so the textures render at full brightness with no color provider.
		SelectItemModelPropertiesAccessor.berryglow$getIdMapper().put(
			ResourceLocation.fromNamespaceAndPath(BerryGlow.MOD_ID, "glowing"),
			GlowingPotionSelectProperty.TYPE
		);
	}
}
