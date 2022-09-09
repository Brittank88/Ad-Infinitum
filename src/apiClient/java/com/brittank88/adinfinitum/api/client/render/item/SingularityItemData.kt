package com.brittank88.adinfinitum.api.client.render.item

import com.brittank88.adinfinitum.AdInfinitum
import com.brittank88.adinfinitum.api.registry.singularity.SingularityItem
import com.brittank88.adinfinitum.util.capitaliseFully
import com.brittank88.adinfinitum.util.client.ColourExtractionException
import com.brittank88.adinfinitum.util.client.DEFAULT_COLOUR
import com.brittank88.adinfinitum.util.client.extractProminentRGB
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.registry.Registry
import java.util.function.Function
import java.util.function.IntSupplier
import java.util.function.ToIntFunction

data class SingularityItemData(
    val baseSpriteIdentifier: (ItemStack) -> SpriteIdentifier = {
        SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, AdInfinitum.id("textures/item/singularity/base.png"))
    },
    val coreSpriteIdentifier: (ItemStack) -> SpriteIdentifier = {
        SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, AdInfinitum.id("textures/item/singularity/core.png"))
    },
    val baseRotationSpeed: ToFloatFunction<ItemStack> = ToFloatFunction { 1f },
    val coreRotationSpeed: ToFloatFunction<ItemStack> = ToFloatFunction { 0.5f },
    val colourFunction: ToIntFunction<ItemStack> = ToIntFunction<ItemStack> {
        try {
            SpriteIdentifier(
                PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Registry.ITEM.getId((it.item as SingularityItem).material.item)
            ).extractProminentRGB()
        }
        catch (e: ColourExtractionException) { DEFAULT_COLOUR }
    }
)