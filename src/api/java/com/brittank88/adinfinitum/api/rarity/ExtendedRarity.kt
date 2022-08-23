package com.brittank88.adinfinitum.api.rarity

import net.minecraft.util.Formatting

// TODO: Implement

/**
 * An enum providing further rarities than vanilla Minecraft's [Rarity][net.minecraft.util.Rarity] enum.
 *
 * @param value The corresponding formatting that is applied to the [Item][net.minecraft.item.Item]'s name.
 * @author Brittank88
 */
enum class ExtendedRarity(private val value: Formatting) {

    /** Common rarity. Present in vanilla. */
    COMMON(Formatting.WHITE),

    /** Uncommon rarity. Present in vanilla. */
    UNCOMMON(Formatting.YELLOW),

    /** Rare rarity. Present in vanilla. */
    RARE(Formatting.AQUA),

    /** Epic rarity. Present in vanilla. */
    EPIC(Formatting.LIGHT_PURPLE),

    /** Legendary rarity. NOT present in vanilla. */
    LEGENDARY(Formatting.GOLD);
}