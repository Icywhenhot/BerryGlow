package com.berryglow.client;

import net.fabricmc.api.ClientModInitializer;

public class BerryGlowClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// No client-specific logic needed: the glow effect is applied server-side
		// and rendered by vanilla.
	}
}
