package com.brittank88.adinfinitum.api.rarity;

import net.minecraft.util.Formatting;

/**
 * An enum providing further rarities than vanilla Minecraft's {@link net.minecraft.util.Rarity Rarity} enum.
 *
 * @author Brittank88
 */
public enum ExtendedRarity {
    /** Common rarity. Present in vanilla. */
    COMMON   (Formatting.WHITE       ),
    /** Uncommon rarity. Present in vanilla. */
    UNCOMMON (Formatting.YELLOW      ),
    /** Rare rarity. Present in vanilla. */
    RARE     (Formatting.AQUA        ),
    /** Epic rarity. Present in vanilla. */
    EPIC     (Formatting.LIGHT_PURPLE),
    /** Legendary rarity. NOT present in vanilla. */
    LEGENDARY(Formatting.GOLD        );

    /** Each {@link ExtendedRarity} has a corresponding formatting that is applied to the {@link net.minecraft.item.Item Item}'s name. */
    public final Formatting formatting;

    // TODO: Implement
    ExtendedRarity(Formatting formatting) { this.formatting = formatting; }
}
