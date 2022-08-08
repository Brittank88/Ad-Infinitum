package com.brittank88.adastra.group;

import com.brittank88.adastra.AdAstra;
import com.brittank88.adastra.api.item.AbstractSingularityItem;
import com.brittank88.adastra.client.resource.AdAstraRRP;
import com.brittank88.adastra.util.NumeralUtil;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

import java.util.stream.IntStream;

@Environment(EnvType.CLIENT)
public abstract class AdAstraGroups {

    public static final OwoItemGroup GROUP_MAIN = new OwoItemGroup(AdAstra.id("group.main")) {
        @Override public ItemStack createIcon() { return new ItemStack(Items.IRON_INGOT); }

        @Override protected void setup() {
            keepStaticTitle();  // The title will not change depending on the selected tab.
            displaySingleTab(); // Display item tabs even if only one exists.

            // Add all tabs.
            addTab(Icon.of(Items.DEBUG_STICK), "machines", TagKey.of(Registry.ITEM_KEY, AdAstra.id("machines")));
            addTab(Icon.of(Items.DEBUG_STICK), "blocks"  , TagKey.of(Registry.ITEM_KEY, AdAstra.id("blocks"  )));
            addTab(Icon.of(Items.DEBUG_STICK), "tools"   , TagKey.of(Registry.ITEM_KEY, AdAstra.id("tools"   )));
            addTab(Icon.of(Items.DEBUG_STICK), "armour"  , TagKey.of(Registry.ITEM_KEY, AdAstra.id("armour"  )));
        }
    };

    public static final OwoItemGroup GROUP_SINGULARITIES = new OwoItemGroup(AdAstra.id("group.singularities")) {
        @Override public ItemStack createIcon() { return new ItemStack(Registry.ITEM.get(AdAstra.id("cobblestone_singularity_mk-i"))); }

        @Override protected void setup() {
            keepStaticTitle();  // The title will not change depending on the selected tab.
            displaySingleTab(); // Display item tabs even if only one exists.

            // Add all tabs.
            IntStream.range(1, AbstractSingularityItem.DEFAULT_MAXIMUM_TIER + 1)
                    .mapToObj(NumeralUtil.RomanNumeral::toRoman)
                    .forEach(n -> addTab(Icon.of(Items.DEBUG_STICK), "MK-" + n, TagKey.of(Registry.ITEM_KEY, AdAstra.id("singularity_mk-" + n.toLowerCase()))));
        }
    };

    public static void register() {

        // Initialise the groups.
        GROUP_MAIN.initialize();
        GROUP_SINGULARITIES.initialize();

        // Register lang for all custom-defined groups.
        AdAstraRRP.handleItemGroup(GROUP_MAIN);
        AdAstraRRP.handleItemGroup(GROUP_SINGULARITIES);

        AdAstra.LOGGER.info("Item groups registered!");
    }
}
