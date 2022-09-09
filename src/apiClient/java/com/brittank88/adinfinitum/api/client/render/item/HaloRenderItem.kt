package com.brittank88.adinfinitum.api.client.render.item

import com.brittank88.adinfinitum.AdInfinitum
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import java.util.*

/**
 * Ported from [IHaloRenderItem.java](https://github.com/Morpheus1101/Avaritia/blob/master/src/main/java/morph/avaritia/api/IHaloRenderItem.java)
 */
interface HaloRenderItem {
    /**
     * Determines if the item stack should have a halo rendered underneath - is true by default.
     *
     * @param stack The item stack to get the halo render status for.
     * @return True if the item stack should have a halo rendered underneath.
     */
    fun shouldRenderHalo(stack: ItemStack?) = true

    /**
     * The halo sprite.
     * <br></br><br></br>
     * By default, this uses the sprite provided by Ad Infinitum.
     *
     * @param stack The item stack to get the halo sprite for.
     * @return The halo sprite identifier.
     */
    fun getHaloSprite(stack: ItemStack?) = SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, AdInfinitum.id("item/render/halo"))

    /**
     * The halo noise sprite.
     * <br></br><br></br>
     * By default, this uses the sprite provided by Ad Infinitum.
     *
     * @param stack The item stack to get the halo noise sprite for.
     * @return The halo noise sprite identifier.
     */
    fun getHaloNoiseSprite(stack: ItemStack?) = SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, AdInfinitum.id("item/render/halo_noise"))

    /**
     * The halo colour.
     * <br></br><br></br>
     * By default, this is 0xFF000000 (solid black).
     *
     * @param stack The item stack to get the halo colour for.
     * @return The halo colour as an ARGB int.
     */
    fun getHaloColour(stack: ItemStack?) = -0x1

    /**
     * The halo size - will be clamped between 0 and 1.
     *
     * @param stack The item stack to get the halo size for.
     * @return The halo size.
     */
    fun getHaloSize(stack: ItemStack?) = 0.25f

    /**
     * Determines if the item stack should pulse - is true by default.
     *
     * @param stack The item stack to get the pulse state for.
     * @return True if the item stack should pulse, false otherwise.
     */
    fun shouldPulse(stack: ItemStack?) = true

    /**
     * Pulse (scaling) factor for the item stack - will be clamped between 0.8F and 1.2F.
     *
     * @param stack The item stack to get the scaling factor for.
     * @return The scaling factor.
     * @see .getPulseScale
     */
    fun getPulseScale(stack: ItemStack?) = MinecraftClient.getInstance().world?.let { getPulseScale(stack, it.getRandom()) }

    /**
     * Pulse (scaling) factor for the item stack - will be clamped between 0.8F and 1.2F.
     *
     * @param stack The item stack to get the scaling factor for.
     * @param random The random to use for randomness.
     * @return The scaling factor.
     * @see .getPulseScale
     */
    fun getPulseScale(stack: ItemStack?, random: Random) = random.nextFloat() * 0.6f + 0.6f
}