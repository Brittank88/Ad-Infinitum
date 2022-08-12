package com.brittank88.adinfinitum.client;

import com.brittank88.adinfinitum.AdInfinitum;
import com.brittank88.adinfinitum.client.resource.AdInfinitumRRP;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.screen.PlayerScreenHandler;

@Environment(EnvType.CLIENT)
public class AdInfinitumClient implements ClientModInitializer {

    public static float TICK_COUNT = 0;

    @Override public void onInitializeClient() {

        ClientTickEvents.START_CLIENT_TICK.register(mc -> TICK_COUNT++);

        // Register custom sprites.
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(this::registerSprites);

        // Register the RRP.
        AdInfinitumRRP.registerPack();
    }

    private void registerSprites(SpriteAtlasTexture atlasTexture, ClientSpriteRegistryCallback.Registry registry) {

        // Halo
        registry.register(AdInfinitum.id("item/render/halo"));
        registry.register(AdInfinitum.id("item/render/halo_noise"));

        // Singularity
        registry.register(AdInfinitum.id("item/singularity/base"));
        registry.register(AdInfinitum.id("item/singularity/core"));
    }
}
