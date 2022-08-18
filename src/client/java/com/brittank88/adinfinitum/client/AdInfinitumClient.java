package com.brittank88.adinfinitum.client;

import com.brittank88.adinfinitum.AdInfinitum;
import com.brittank88.adinfinitum.client.config.AdInfinitumConfig;
import gg.essential.elementa.effects.StencilEffect;
import gg.essential.vigilance.Vigilance;
import gg.essential.vigilance.Vigilant;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.screen.PlayerScreenHandler;

/**
 * Entrypoint for the mod's client-side components.
 *
 * @author Brittank88
 */
public class AdInfinitumClient implements ClientModInitializer {

    /** The main config of the mod. */
    public static final Vigilant CONFIG = new AdInfinitumConfig();

    /** A variable tracking the count of current ticks. */
    private static float tickCount = 0;

    /** @return The total count of ticks. */
    public static float getTickCount() { return tickCount; }

    /** Main initialisation method of the mod's main client class. */
    @Override public void onInitializeClient() {

        // Register config.
        Vigilance.initialize();
        CONFIG.preload();
        StencilEffect.enableStencil();

        // Start tracking ticks.
        ClientTickEvents.START_CLIENT_TICK.register(mc -> tickCount++);

        // Register custom sprites.
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(this::registerSprites);

        // TODO: Migrate all client-side stuff like lang to the client (client entrypoint runs after common entrypoint anyways).
    }

    /**
     * Registers custom sprites to the provided {@link SpriteAtlasTexture} via the {@link ClientSpriteRegistryCallback.Registry}.
     *
     * @param atlasTexture The {@link SpriteAtlasTexture} to register sprites to.
     * @param registry The {@link ClientSpriteRegistryCallback.Registry} used to register the sprites.
     */
    private void registerSprites(SpriteAtlasTexture atlasTexture, ClientSpriteRegistryCallback.Registry registry) {

        // Halo
        registry.register(AdInfinitum.id("item/render/halo"      ));
        registry.register(AdInfinitum.id("item/render/halo_noise"));

        // Singularity
        registry.register(AdInfinitum.id("item/singularity/base"));
        registry.register(AdInfinitum.id("item/singularity/core"));
    }
}
