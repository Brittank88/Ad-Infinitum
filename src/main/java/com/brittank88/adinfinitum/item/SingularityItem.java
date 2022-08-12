package com.brittank88.adinfinitum.item;

import com.brittank88.adinfinitum.api.item.AbstractSingularityItem;
import com.brittank88.adinfinitum.group.AdInfinitumGroups;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import org.apache.commons.lang3.Range;

import java.util.List;
import java.util.stream.IntStream;

public class SingularityItem extends AbstractSingularityItem {

    public SingularityItem(ItemStack material, OwoItemSettings settings, int tier, SpriteIdentifier baseSpriteID, SpriteIdentifier coreSpriteID, float baseSpriteRotationSpeed, float coreSpriteRotationSpeed) {
        super(material, settings, tier, baseSpriteID, coreSpriteID, baseSpriteRotationSpeed, coreSpriteRotationSpeed);
    }
    public SingularityItem(ItemStack material, OwoItemSettings settings, int tier, SpriteIdentifier baseSpriteID, SpriteIdentifier coreSpriteID) {
        super(material, settings, tier, baseSpriteID, coreSpriteID);
    }
    public SingularityItem(ItemStack material, OwoItemSettings settings, int tier) {
        super(material, settings, tier);
    }
    public SingularityItem(ItemStack material, OwoItemSettings settings) {
        super(material, settings);
    }
    public SingularityItem(ItemStack material) {
        super(material);
    }

    @Override public List<? extends AbstractSingularityItem> generateUpToTier(int tier) {
        return IntStream.range(1, tier)
                .mapToObj(i -> new SingularityItem(
                        this.getMaterial(),
                        this.getSettings()
                                .rarity(Rarity.values()[Range.between(0, 3).fit(i)])
                                .group(AdInfinitumGroups.GROUP_SINGULARITIES)
                                .tab(i),
                        i + 1))
                .toList();
    }
}
