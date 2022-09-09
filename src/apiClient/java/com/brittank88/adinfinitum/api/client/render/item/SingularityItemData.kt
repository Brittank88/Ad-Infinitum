package com.brittank88.adinfinitum.api.client.render.item

import com.brittank88.adinfinitum.AdInfinitum
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.util.function.ToFloatFunction
import java.util.function.ToIntFunction

data class SingularityItemData(
    val baseSpriteIdentifier: (ItemStack) -> SpriteIdentifier = {
        SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, AdInfinitum.id("textures/item/singularity/base.png"))
    },
    val coreSpriteIdentifier: (ItemStack) -> SpriteIdentifier = {
        SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, AdInfinitum.id("textures/item/singularity/core.png"))
    },
    val baseRotationSpeed: ToFloatFunction<ItemStack> = ToFloatFunction { 1f   },
    val coreRotationSpeed: ToFloatFunction<ItemStack> = ToFloatFunction { 0.5f },
    val colourFunction   : ToIntFunction  <ItemStack> = ToIntFunction   { 0 } // FIXME: Colour function
)