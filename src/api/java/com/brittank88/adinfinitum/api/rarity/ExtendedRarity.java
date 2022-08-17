package com.brittank88.adinfinitum.api.rarity;

import net.minecraft.util.Formatting;

public enum ExtendedRarity {
    COMMON   (Formatting.WHITE       ),
    UNCOMMON (Formatting.YELLOW      ),
    RARE     (Formatting.AQUA        ),
    EPIC     (Formatting.LIGHT_PURPLE),
    LEGENDARY(Formatting.GOLD        );

    public final Formatting formatting;

    // TODO: Implement
    ExtendedRarity(Formatting formatting) {
        this.formatting = formatting;
    }
}
