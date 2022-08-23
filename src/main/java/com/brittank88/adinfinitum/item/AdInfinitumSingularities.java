package com.brittank88.adinfinitum.item;

import com.brittank88.adinfinitum.AdInfinitum;
import com.brittank88.adinfinitum.api.registry.singularity.SingularityItem;
import com.brittank88.adinfinitum.api.registry.singularity.SingularityItemProcessingSubject;
import net.minecraft.item.Items;

/**
 * This class is used to register all the {@link com.brittank88.adinfinitum.api.registry.singularity.SingularityItem singularities} in the mod.
 *
 * @author Brittank88
 */
public final class AdInfinitumSingularities extends SingularityItemProcessingSubject {

    // Basic Resources
    public static final SingularityItem COBBLESTONE_SINGULARITY  = new SingularityItem(Items.COBBLESTONE    .getDefaultStack());
    public static final SingularityItem COAL_SINGULARITY         = new SingularityItem(Items.COAL_BLOCK     .getDefaultStack());
    public static final SingularityItem OBSIDIAN_SINGULARITY     = new SingularityItem(Items.OBSIDIAN       .getDefaultStack());

    // Metals
    public static final SingularityItem IRON_SINGULARITY         = new SingularityItem(Items.IRON_BLOCK     .getDefaultStack());
    public static final SingularityItem GOLD_SINGULARITY         = new SingularityItem(Items.GOLD_BLOCK     .getDefaultStack());
    public static final SingularityItem NETHERITE_SINGULARITY    = new SingularityItem(Items.NETHERITE_BLOCK.getDefaultStack());

    // Gems / Similar
    public static final SingularityItem DIAMOND_SINGULARITY      = new SingularityItem(Items.DIAMOND_BLOCK  .getDefaultStack());
    public static final SingularityItem EMERALD_SINGULARITY      = new SingularityItem(Items.EMERALD_BLOCK  .getDefaultStack());
    public static final SingularityItem LAPIS_LAZULI_SINGULARITY = new SingularityItem(Items.LAPIS_BLOCK    .getDefaultStack());
    public static final SingularityItem REDSTONE_SINGULARITY     = new SingularityItem(Items.REDSTONE_BLOCK .getDefaultStack());
    public static final SingularityItem QUARTZ_SINGULARITY       = new SingularityItem(Items.QUARTZ_BLOCK   .getDefaultStack());

    // Special
    public static final SingularityItem NETHER_STAR_SINGULARITY  = new SingularityItem(Items.NETHER_STAR    .getDefaultStack());

    /** Constructs a new {@link AdInfinitumSingularities} instance, which is simply a {@link SingularityItemProcessingSubject} with {@link AdInfinitum#MOD_ID} as the namespace. */
    public AdInfinitumSingularities() { super(AdInfinitum.MOD_ID); }
}
