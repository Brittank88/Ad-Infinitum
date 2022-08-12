package com.brittank88.adinfinitum.block;

import com.brittank88.adinfinitum.AdInfinitum;
import com.brittank88.adinfinitum.annotation.AssignedTab;
import com.brittank88.adinfinitum.api.item.IHaloRenderItem;
import com.brittank88.adinfinitum.block.custom.CrystalMatrixBlock;
import com.brittank88.adinfinitum.block.custom.InfinityBlock;
import com.brittank88.adinfinitum.block.custom.NeutronCollectorBlock;
import com.brittank88.adinfinitum.block.custom.NeutroniumBlock;
import com.brittank88.adinfinitum.client.resource.AdInfinitumRRP;
import com.brittank88.adinfinitum.group.AdInfinitumGroups;
import com.brittank88.adinfinitum.util.RegistryUtil;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;

public class AdInfinitumBlocks implements BlockRegistryContainer {

    @AssignedTab public static final Block NEUTRON_COLLECTOR = new NeutronCollectorBlock(FabricBlockSettings.of(Material.METAL).nonOpaque());

    @AssignedTab(tab = 1) public static final Block NEUTRONIUM_BLOCK = new NeutroniumBlock(FabricBlockSettings.of(Material.METAL).nonOpaque());
    @AssignedTab(tab = 1) public static final Block CRYSTAL_MATRIX_BLOCK = new CrystalMatrixBlock(FabricBlockSettings.of(Material.METAL).nonOpaque());
    @AssignedTab(tab = 1) public static final Block INFINITY_BLOCK = new InfinityBlock(FabricBlockSettings.of(Material.METAL).nonOpaque());

    @Override
    public void postProcessField(String namespace, Block value, String identifier, Field field) {

        // Register RRP files for the block.
        AdInfinitumRRP.handleBlockRegistryContainer(namespace, value, identifier, field);

        // Preserve normal traversal behaviour.
        if (field.isAnnotationPresent(NoBlockItem.class)) return;

        // Create basic item settings.
        OwoItemSettings settings = new OwoItemSettings();

        // Assign group and tab based on annotation.
        AssignedTab tabAnnotation = field.getAnnotation(AssignedTab.class);
        settings.group(AdInfinitumGroups.GROUP_MAIN).tab(tabAnnotation == null ? AssignedTab.DEFAULT_TAB_INDEX : tabAnnotation.tab());

        // Register the block item.
        BlockItem blockItem = value instanceof IHaloRenderItem ? new BlockItemHalo(value, settings) : new BlockItem(value, settings);
        net.minecraft.util.registry.Registry.register(net.minecraft.util.registry.Registry.ITEM, new Identifier(namespace, identifier), blockItem);
        AdInfinitumRRP.handleItemRegistryContainer(namespace, blockItem, identifier, field);
    }

    @Override public void afterFieldProcessing() { AdInfinitum.LOGGER.info("Registered " + RegistryUtil.getRegisteredAmount(net.minecraft.util.registry.Registry.BLOCK) + " blocks!"); }

    private static class BlockItemHalo extends BlockItem implements IHaloRenderItem {
        public BlockItemHalo(Block block, Settings settings) { super(block, settings); }
    }
}
