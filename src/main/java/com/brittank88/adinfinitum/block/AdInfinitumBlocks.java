package com.brittank88.adinfinitum.block;

import com.brittank88.adinfinitum.api.registry.annotation.AssignedTab;
import com.brittank88.adinfinitum.block.custom.CrystalMatrixBlock;
import com.brittank88.adinfinitum.block.custom.InfinityBlock;
import com.brittank88.adinfinitum.block.custom.NeutronCollectorBlock;
import com.brittank88.adinfinitum.block.custom.NeutroniumBlock;
import com.brittank88.adinfinitum.group.AdInfinitumGroups;
import com.google.common.collect.ImmutableList;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to register all the {@link Block blocks} in the mod.
 *
 * @author Brittank88
 */
public final class AdInfinitumBlocks implements BlockRegistryContainer {

    private static final List<Block> REGISTERED_BLOCKS = new ArrayList<>();
    private static final List<BlockItem> REGISTERED_BLOCK_ITEMS = new ArrayList<>();

    public static List<Block> getRegisteredBlocks() { return ImmutableList.copyOf(REGISTERED_BLOCKS); }
    public static List<BlockItem> getRegisteredBlockItems() { return ImmutableList.copyOf(REGISTERED_BLOCK_ITEMS); }

    /** Neutron Collector - A machine used to generate Neutronium material when supplied with energy. */
    @AssignedTab public static final Block NEUTRON_COLLECTOR = new NeutronCollectorBlock(FabricBlockSettings.of(Material.METAL).nonOpaque());

    /** Neutronium Block - A block of Neutronium material. */
    @AssignedTab(1) public static final Block NEUTRONIUM_BLOCK = new NeutroniumBlock(FabricBlockSettings.of(Material.METAL).nonOpaque());
    /** Crystal Matrix Block - A block of Crystal Matrix material. */
    @AssignedTab(1) public static final Block CRYSTAL_MATRIX_BLOCK = new CrystalMatrixBlock(FabricBlockSettings.of(Material.METAL).nonOpaque());
    /** Infinity Block - A block of Infinity material. */
    @AssignedTab(1) public static final Block INFINITY_BLOCK = new InfinityBlock(FabricBlockSettings.of(Material.METAL).nonOpaque());

    @Override public void postProcessField(String namespace, Block value, String identifier, Field field) {

        // Preserve normal traversal behaviour.
        if (field.isAnnotationPresent(NoBlockItem.class)) return;

        // Create basic item settings.
        OwoItemSettings settings = new OwoItemSettings();

        // Assign group and tab based on annotation.
        AssignedTab tabAnnotation = field.getAnnotation(AssignedTab.class);
        settings.group(AdInfinitumGroups.GROUP_MAIN).tab(tabAnnotation == null ? 0 : tabAnnotation.value());

        // Register the BlockItem.
        BlockItem blockItem = new BlockItem(value, settings);
        net.minecraft.util.registry.Registry.register(net.minecraft.util.registry.Registry.ITEM, new Identifier(namespace, identifier), blockItem);

        // Store a reference to the Block and BlockItem for later use.
        REGISTERED_BLOCKS.add(value);
        REGISTERED_BLOCK_ITEMS.add(blockItem);
    }

    // TODO: @Override public void afterFieldProcessing() { AdInfinitum.LOGGER.info("Registered " + RegistryUtilsKt.getRegisteredAmount(net.minecraft.util.registry.Registry.BLOCK, AdInfinitum.MOD_ID) + " blocks!"); }
}
