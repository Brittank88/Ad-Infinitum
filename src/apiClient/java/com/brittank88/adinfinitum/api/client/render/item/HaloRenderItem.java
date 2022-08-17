package com.brittank88.adinfinitum.api.client.render.item;

import com.brittank88.adinfinitum.util.AdInfinitumUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;

import java.util.Objects;
import java.util.Random;

/**
 * Ported directly from <a href="https://github.com/Morpheus1101/Avaritia/blob/master/src/main/java/morph/avaritia/api/IHaloRenderItem.java">IHaloRenderItem.java</a>
 */
public interface HaloRenderItem {

    Random RANDOM = new Random();

    /**
     * Determines if the item stack should have a halo rendered underneath - is true by default.
     *
     * @param stack The item stack to get the halo render status for.
     * @return True if the item stack should have a halo rendered underneath.
     */
    default boolean shouldRenderHalo(ItemStack stack) { return true; }

    /**
     * The halo sprite.
     * <br /><br />
     * By default, this uses the sprite provided by Ad Infinitum.
     *
     * @param stack The item stack to get the halo sprite for.
     * @return The halo sprite identifier.
     */
    default SpriteIdentifier getHaloSprite(ItemStack stack) {
        return new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, AdInfinitumUtil.id("item/render/halo"));
    };

    /**
     * The halo noise sprite.
     * <br /><br />
     * By default, this uses the sprite provided by Ad Infinitum.
     *
     * @param stack The item stack to get the halo noise sprite for.
     * @return The halo noise sprite identifier.
     */
    default SpriteIdentifier getHaloNoiseSprite(ItemStack stack) { return new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, AdInfinitumUtil.id("item/render/halo_noise")); }

    /**
     * The halo colour.
     * <br /><br />
     * By default, this is 0xFF000000 (solid black).
     *
     * @param stack The item stack to get the halo colour for.
     * @return The halo colour as an ARGB int.
     */
    default int getHaloColour(ItemStack stack) { return 0xFFFFFFFF; };

    /**
     * The halo size - will be clamped between 0 and 1.
     *
     * @param stack The item stack to get the halo size for.
     * @return The halo size.
     */
    default float getHaloSize(ItemStack stack) { return 0.25F; }

    /**
     * Determines if the item stack should pulse - is true by default.
     *
     * @param stack The item stack to get the pulse state for.
     * @return True if the item stack should pulse, false otherwise.
     */
    default boolean shouldPulse(ItemStack stack) { return true; }

    /**
     * Pulse (scaling) factor for the item stack - will be clamped between 0.8F and 1.2F.
     *
     * @param stack The item stack to get the scaling factor for.
     * @return The scaling factor.
     * @see #getPulseScale(ItemStack, Random)
     */
    default float getPulseScale(ItemStack stack) { return getPulseScale(stack, Objects.requireNonNull(MinecraftClient.getInstance().world).getRandom()); }

    /**
     * Pulse (scaling) factor for the item stack - will be clamped between 0.8F and 1.2F.
     *
     * @param stack The item stack to get the scaling factor for.
     * @param random The random to use for randomness.
     * @return The scaling factor.
     * @see #getPulseScale(ItemStack)
     */
    default float getPulseScale(ItemStack stack, Random random) { return random.nextFloat() * 0.6F + 0.6F; }
}
