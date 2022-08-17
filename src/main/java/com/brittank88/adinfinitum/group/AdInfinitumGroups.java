package com.brittank88.adinfinitum.group;

import com.brittank88.adinfinitum.api.AdInfinitumAPI;
import com.brittank88.adinfinitum.api.registry.singularity.SingularityItem;
import com.brittank88.adinfinitum.util.AdInfinitumUtil;
import com.brittank88.adinfinitum.util.NumeralUtil;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

import java.util.stream.IntStream;

public final class AdInfinitumGroups {

    public static final OwoItemGroup GROUP_MAIN = new OwoItemGroup(AdInfinitumUtil.id("group.main")) {
        @Override public ItemStack createIcon() { return new ItemStack(Items.IRON_INGOT); }

        @Override protected void setup() {
            keepStaticTitle();  // The title will not change depending on the selected tab.
            displaySingleTab(); // Display item tabs even if only one exists.

            // Add all tabs.
            addTab(Icon.of(Items.DEBUG_STICK), "machines", TagKey.of(Registry.ITEM_KEY, AdInfinitumUtil.id("machines")));
            addTab(Icon.of(Items.DEBUG_STICK), "blocks"  , TagKey.of(Registry.ITEM_KEY, AdInfinitumUtil.id("blocks"  )));
            addTab(Icon.of(Items.DEBUG_STICK), "tools"   , TagKey.of(Registry.ITEM_KEY, AdInfinitumUtil.id("tools"   )));
            addTab(Icon.of(Items.DEBUG_STICK), "armour"  , TagKey.of(Registry.ITEM_KEY, AdInfinitumUtil.id("armour"  )));
        }
    };

    public static final OwoItemGroup GROUP_SINGULARITIES = new OwoItemGroup(AdInfinitumUtil.id("group.singularities")) {
        @Override public ItemStack createIcon() { return new ItemStack(Registry.ITEM.get(AdInfinitumUtil.id("cobblestone_singularity_mk-i"))); }

        @Override protected void setup() {
            keepStaticTitle();  // The title will not change depending on the selected tab.
            displaySingleTab(); // Display item tabs even if only one exists.

            // Add all tabs.
            IntStream.range(1, SingularityItem.DEFAULT_MAXIMUM_TIER + 1)
                    .mapToObj(NumeralUtil.RomanNumeral::toRoman)
                    .forEach(n -> addTab(Icon.of(Items.DEBUG_STICK), "MK-" + n, TagKey.of(Registry.ITEM_KEY, AdInfinitumUtil.id("singularity_mk-" + n.toLowerCase()))));
        }
    };

    public static void register() {

        // Initialise the groups.
        GROUP_MAIN.initialize();
        GROUP_SINGULARITIES.initialize();

        // Register lang for all custom-defined groups.
        // TODO: Dear god what have I done?
        // AdInfinitumRRP.handleItemGroup(GROUP_MAIN);
        // AdInfinitumRRP.handleItemGroup(GROUP_SINGULARITIES);

        AdInfinitumAPI.LOGGER.info("Item groups registered!");
    }
}
