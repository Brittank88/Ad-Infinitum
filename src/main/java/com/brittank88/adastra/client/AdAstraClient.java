package com.brittank88.adastra.client;

import com.brittank88.adastra.AdAstra;
import com.brittank88.adastra.client.resource.AdAstraRRP;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.screen.PlayerScreenHandler;

@Environment(EnvType.CLIENT)
public class AdAstraClient implements ClientModInitializer {

    public static float TICK_COUNT = 0;

    @Override public void onInitializeClient() {

        ClientTickEvents.START_CLIENT_TICK.register(mc -> TICK_COUNT++);

        // Register custom sprites.
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(this::registerSprites);

        // Register the RRP.
        AdAstraRRP.registerPack();
    }

    private void registerSprites(SpriteAtlasTexture atlasTexture, ClientSpriteRegistryCallback.Registry registry) {

        // Halo
        registry.register(AdAstra.id("item/render/halo"));
        registry.register(AdAstra.id("item/render/halo_noise"));

        // Singularity
        registry.register(AdAstra.id("item/singularity/base"));
        registry.register(AdAstra.id("item/singularity/core"));
    }
}
