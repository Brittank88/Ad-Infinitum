package com.brittank88.adinfinitum.client

import com.brittank88.adinfinitum.AdInfinitum
import com.brittank88.adinfinitum.client.config.AdInfinitumCommonConfig
import com.brittank88.adinfinitum.util.client.colour.ItemStackDominantColourExtractor.extractDominantColour
import gg.essential.elementa.effects.StencilEffect
import gg.essential.vigilance.Vigilance.initialize
import gg.essential.vigilance.Vigilant
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
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

        val testItemStack = ItemStack(Items.MAGMA_BLOCK)

        // TODO: Find some callback that occurs at the end of ALL possible rendering.
        WorldRenderEvents.END.register(WorldRenderEvents.End { testItemStack.extractDominantColour() })

        // Register custom sprites.
        registerSprites()

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