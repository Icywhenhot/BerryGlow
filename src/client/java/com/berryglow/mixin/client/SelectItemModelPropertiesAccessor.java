package com.berryglow.mixin.client;

import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// Exposes the private LateBoundIdMapper so we can register our custom
// "berryglow:glowing" select property at client init time.
@Mixin(SelectItemModelProperties.class)
public interface SelectItemModelPropertiesAccessor {
	@Accessor("ID_MAPPER")
	static ExtraCodecs.LateBoundIdMapper<ResourceLocation, SelectItemModelProperty.Type<?, ?>> berryglow$getIdMapper() {
		throw new AssertionError();
	}
}
