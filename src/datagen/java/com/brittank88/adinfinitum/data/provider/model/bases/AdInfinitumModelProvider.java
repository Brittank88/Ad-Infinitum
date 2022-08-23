package com.brittank88.adinfinitum.data.provider.model.bases;

import com.brittank88.adinfinitum.api.client.registry.singularity.SingularityItemDataRegistry;
import com.brittank88.adinfinitum.block.AdInfinitumBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class AdInfinitumModelProvider extends FabricModelProvider {
    public AdInfinitumModelProvider(FabricDataGenerator dataGenerator) { super(dataGenerator); }

    @Override public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

        AdInfinitumBlocks.getRegisteredBlocks().forEach(block -> {

        });
    }

    @Override public void generateItemModels(ItemModelGenerator itemModelGenerator) {

        // Singularity item models - parent minecraft:builtin/entity to support DIRs.
        SingularityItemDataRegistry.getSingularities().forEach((si, sid) -> {
            itemModelGenerator.register(si, new Model(
                    Optional.of(new Identifier("minecraft", ModelLoader.BUILTIN_ENTITY)),
                    Optional.empty()
            ));
        });
    }
}
