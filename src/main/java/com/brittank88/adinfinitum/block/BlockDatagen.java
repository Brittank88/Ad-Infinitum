package com.brittank88.adinfinitum.block;

import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

public interface BlockDatagen {

    void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator);
    void generateItemModels(ItemModelGenerator itemModelGenerator);
}
