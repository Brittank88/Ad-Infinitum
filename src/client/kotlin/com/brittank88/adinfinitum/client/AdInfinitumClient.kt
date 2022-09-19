package com.brittank88.adinfinitum.client

import com.brittank88.adinfinitum.AdInfinitum
import com.brittank88.adinfinitum.client.config.AdInfinitumCommonConfig
import com.brittank88.adinfinitum.api.client.registry.singularity.singularities
import com.brittank88.adinfinitum.client.item.SingularityItemRenderer
import gg.essential.elementa.effects.StencilEffect
import gg.essential.vigilance.Vigilance.initialize
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.screen.PlayerScreenHandler

/**
 * Entrypoint for the mod's client-side components.
 *
 * @author Brittank88
 */
class AdInfinitumClient : ClientModInitializer {

    /** Main initialisation method of the mod's main client class. */
    override fun onInitializeClient() {

        // Start tracking ticks.
        registerTickTracker()

        // Register config.
        registerConfig()

        // Register custom sprites.
        registerSprites()

        // Register DIR for singularities.
        // TODO: Move to own function.
        // TODO: Need to figure out how to assign minecraft:builtin/entity to these items.
        singularities.forEach { (si, _) -> BuiltinItemRendererRegistry.INSTANCE.register(si, SingularityItemRenderer()) }

        // TODO: Migrate all client-side stuff like lang to the client (client entrypoint runs after common entrypoint anyways).
    }

    companion object {

        private fun registerTickTracker() = ClientTickEvents.START_CLIENT_TICK.register(ClientTickEvents.StartTick { tickCount++ })

        private fun registerConfig() {

            initialize()
            COMMON_CONFIG.preload()
            StencilEffect.enableStencil()
        }

        /** Registers custom sprites to the provided [SpriteAtlasTexture] via the [ClientSpriteRegistryCallback.Registry]. */
        private fun registerSprites() = ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(ClientSpriteRegistryCallback { _, registry ->

            // Halo
            registry.register(AdInfinitum.id("item/render/halo"))
            registry.register(AdInfinitum.id("item/render/halo_noise"))

            // Singularity
            registry.register(AdInfinitum.id("item/singularity/base"))
            registry.register(AdInfinitum.id("item/singularity/core"))
        })

        /** The main config of the mod.  */
        val COMMON_CONFIG = AdInfinitumCommonConfig

        /** @return The total count of ticks. */
        @JvmStatic var tickCount = 0f
            private set
    }
}