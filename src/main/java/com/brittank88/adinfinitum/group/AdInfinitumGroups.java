package com.brittank88.adinfinitum.group;

import com.brittank88.adinfinitum.AdInfinitum;
import com.brittank88.adinfinitum.api.registry.singularity.SingularityItem;
import com.brittank88.adinfinitum.util.NumeralUtilsKt;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

import java.util.Locale;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * This class is used to register all the {@link OwoItemGroup item groups} in the mod.
 *
 * @author Brittank88
 */
public final class AdInfinitumGroups {

    /** The main {@link OwoItemGroup item group} for the mod. */
    public static final OwoItemGroup GROUP_MAIN = new OwoItemGroup(AdInfinitum.id("group.main")) {
        @Override public ItemStack createIcon() { return new ItemStack(Items.IRON_INGOT); }

        @Override protected void setup() {
            keepStaticTitle();  // The title will not change depending on the selected tab.
            displaySingleTab(); // Display item tabs even if only one exists.

            // Add all tabs.
            addTab(Icon.of(Items.DEBUG_STICK), "Machines", TagKey.of(Registry.ITEM_KEY, AdInfinitum.id("machines")));
            addTab(Icon.of(Items.DEBUG_STICK), "Blocks"  , TagKey.of(Registry.ITEM_KEY, AdInfinitum.id("blocks"  )));
            addTab(Icon.of(Items.DEBUG_STICK), "Tools"   , TagKey.of(Registry.ITEM_KEY, AdInfinitum.id("tools"   )));
            addTab(Icon.of(Items.DEBUG_STICK), "Armour"  , TagKey.of(Registry.ITEM_KEY, AdInfinitum.id("armour"  )));
        }
    };

    /** The {@link OwoItemGroup item group} for the mod's singularities, with a {@link io.wispforest.owo.itemgroup.gui.ItemGroupTab tab} for each tier. */
    public static final OwoItemGroup GROUP_SINGULARITIES = new OwoItemGroup(AdInfinitum.id("group.singularities")) {
        @Override public ItemStack createIcon() { return new ItemStack(Registry.ITEM.get(AdInfinitum.id("cobblestone_singularity_mk-i"))); }

        @Override protected void setup() {
            keepStaticTitle();  // The title will not change depending on the selected tab.
            displaySingleTab(); // Display item tabs even if only one exists.

            // Add all tabs.
            IntStream.range(1, SingularityItem.DEFAULT_MAXIMUM_TIER + 1)
                    .forEach(i -> addTab(
                            Icon.of(Items.DEBUG_STICK),
                            "MK-" + NumeralUtilsKt.getAsRomanNumerals(i),
                            TagKey.of(Registry.ITEM_KEY, AdInfinitum.id("singularity_mk-" + Objects.requireNonNull(NumeralUtilsKt.getAsRomanNumerals(i)).toLowerCase(Locale.ROOT)))
                    ));
        }
    };

    /** Initialises the mod's {@link OwoItemGroup item groups}. */
    public static void register() {

        // Initialise the groups.
        GROUP_MAIN.initialize();
        GROUP_SINGULARITIES.initialize();
    }
}
