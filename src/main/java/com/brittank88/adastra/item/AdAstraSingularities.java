package com.brittank88.adastra.item;

import com.brittank88.adastra.AdAstra;
import com.brittank88.adastra.api.registry.SingularityRegistryContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@SuppressWarnings("unused") public class AdAstraSingularities extends SingularityRegistryContainer {

    // Basic Resources
    public static final SingularityItem COBBLESTONE_SINGULARITY  = new SingularityItem(new ItemStack(Items.COBBLESTONE    ));
    public static final SingularityItem COAL_SINGULARITY         = new SingularityItem(new ItemStack(Items.COAL_BLOCK     ));
    public static final SingularityItem OBSIDIAN_SINGULARITY     = new SingularityItem(new ItemStack(Items.OBSIDIAN       ));

    // Metals
    public static final SingularityItem IRON_SINGULARITY         = new SingularityItem(new ItemStack(Items.IRON_BLOCK     ));
    public static final SingularityItem GOLD_SINGULARITY         = new SingularityItem(new ItemStack(Items.GOLD_BLOCK     ));
    public static final SingularityItem NETHERITE_SINGULARITY    = new SingularityItem(new ItemStack(Items.NETHERITE_BLOCK));

    // Gems / Similar
    public static final SingularityItem DIAMOND_SINGULARITY      = new SingularityItem(new ItemStack(Items.DIAMOND_BLOCK  ));
    public static final SingularityItem EMERALD_SINGULARITY      = new SingularityItem(new ItemStack(Items.EMERALD_BLOCK  ));
    public static final SingularityItem LAPIS_LAZULI_SINGULARITY = new SingularityItem(new ItemStack(Items.LAPIS_BLOCK    ));
    public static final SingularityItem REDSTONE_SINGULARITY     = new SingularityItem(new ItemStack(Items.REDSTONE_BLOCK ));
    public static final SingularityItem QUARTZ_SINGULARITY       = new SingularityItem(new ItemStack(Items.QUARTZ_BLOCK   ));

    // Special
    public static final SingularityItem NETHER_STAR_SINGULARITY  = new SingularityItem(new ItemStack(Items.NETHER_STAR    ));

    public AdAstraSingularities() { super(AdAstra.MOD_ID); }
}
