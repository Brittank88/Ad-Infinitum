package com.brittank88.adinfinitum.api.registry.singularity;

import com.brittank88.adinfinitum.util.NumeralUtils;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.Function;

/**
 * Abstract implementation of a singularity item.
 * <br /><br />
 * Allows for the creation of singularity items with custom properties.
 *
 * @author brittank88
 */

public abstract class SingularityItem extends Item {

    /** The {@link SingularityItem}'s default tier. */
    public static final int DEFAULT_TIER         = 1;
    /** The default maximum tier that any {@link SingularityItem} can have. */
    public static final int DEFAULT_MAXIMUM_TIER = 5;

    private final int             tier    ;
    private final ItemStack       material;
    private final OwoItemSettings settings;

    /**
     * Constructs a {@link SingularityItem}.
     *
     * @param settings The {@link Settings} to use.
     * @param material The {@link ItemStack} to use as the material.
     * @param tier The tier of the singularity.
     */
    public SingularityItem(ItemStack material, OwoItemSettings settings, int tier) {
        super(settings);
        this.settings = settings;
        this.material = material;
        this.tier     = tier    ;
    }

    /**
     * Constructs a {@link SingularityItem} with the default tier.
     *
     * @param material The {@link ItemStack} to use as the material.
     * @param settings The {@link OwoItemSettings} to use.
     */
    public SingularityItem(ItemStack material, OwoItemSettings settings) { this(material, settings, DEFAULT_TIER); }

    /**
     * Returns the tier of the singularity - by default, {@link #DEFAULT_TIER}.
     *
     * @return The tier of the singularity.
     */
    public int getTier() { return tier; }

    /**
     * Returns the tier of the singularity, expressed as capitalised Roman numerals.
     *
     * @return The tier of the singularity, expressed as capitalised Roman numerals.
     * @see NumeralUtils#toRomanNumeral(int, boolean)
     */
    public String getTierNumeral() { return NumeralUtils.toRomanNumeral(this.tier, true); }

    /**
     * A version of {@link SingularityItem#getTierNumeral()} that takes a {@link Function postProcessor} to transform the resulting Roman numerals.
     *
     * @param postProcessor The {@link Function} to transform the resulting Roman numerals.
     * @return The tier of the singularity, expressed as capitalised Roman numerals.
     * @see SingularityItem#getTierNumeral()
     */
    public String getTierNumeral(Function<String, String> postProcessor) { return postProcessor.apply(this.getTierNumeral()); }

    /**
     * Returns the material of the singularity.
     *
     * @return The material of the singularity.
     */
    public ItemStack getMaterial() { return material.copy(); }

    /**
     * Returns the {@link net.minecraft.item.Item.Settings Settings} of the singularity's {@link Item}.
     *
     * @return The {@link net.minecraft.item.Item.Settings Settings} of the singularity's {@link Item}.
     */
    public OwoItemSettings getSettings() { return this.settings; }

    /**
     * Generates the remaining tiers, up to the default {@link #DEFAULT_MAXIMUM_TIER}. <u>Excludes tier 1.</u>
     *
     * @return The remaining tiers of singularities, up to the default {@link #DEFAULT_MAXIMUM_TIER}.
     * @see #generateUpToTier(int)
     */
    public List<? extends SingularityItem> generateRemainingTiers() { return this.generateUpToTier(DEFAULT_MAXIMUM_TIER); }

    /**
     * Generates the remaining tiers, up to the specified tier. <u>Excludes tier 1.</u>
     *
     * @param tier The maximum tier to generate.
     * @return The remaining tiers of singularities, up to the specified tier.
     */
    public abstract List<? extends SingularityItem> generateUpToTier(int tier);
}
