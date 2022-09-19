package com.brittank88.adinfinitum.api.client.render.item

import com.brittank88.adinfinitum.AdInfinitum
import com.brittank88.adinfinitum.util.client.colour.Colour
import com.brittank88.adinfinitum.util.client.colour.ItemStackDominantColourExtractor.extractColourPalette
import com.brittank88.adinfinitum.util.client.colour.ItemStackDominantColourExtractor.extractDominantColour
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.util.function.ToFloatFunction
import java.util.function.ToIntFunction

/**
 * Data class recording data used for rendering a singularity item.
 * @author Brittank88
 */
data class SingularityItemRenderData(

    /** The [SpriteIdentifier] for the base texture. */
    val baseSpriteIdentifier: (ItemStack) -> SpriteIdentifier = {
        SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, AdInfinitum.id("textures/item/singularity/base.png"))
    },

    /** The [SpriteIdentifier] for the core texture. */
    val coreSpriteIdentifier: (ItemStack) -> SpriteIdentifier = {
        SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, AdInfinitum.id("textures/item/singularity/core.png"))
    },

    /** The rotation speed of the base in `deg/tick`. */
    val baseRotationSpeed: ToFloatFunction<ItemStack> = ToFloatFunction { 1f   },

    /** The rotation speed of the core in `deg/tick`. */
    val coreRotationSpeed: ToFloatFunction<ItemStack> = ToFloatFunction { 0.5f },

    /** The colour of the core as an ARGB integer. */
    val colourFunction: ToIntFunction<ItemStack> = ToIntFunction {
        val extractedDominant = Colour(it.extractDominantColour()).integerARGB
        val extractedPalette = it.extractColourPalette().map { c -> Colour(c).integerARGB }

        AdInfinitum.LOGGER.info("Extracted dominant: ${"%08X".format(extractedDominant)}")
        AdInfinitum.LOGGER.info("Extracted palette: ${extractedPalette.joinToString { c -> "%08X".format(c) }}")

        extractedDominant
    }
)