package com.brittank88.adinfinitum.data.provider.advancement;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;

import java.util.function.Consumer;

public class AdInfinitumAdvancementProvider extends FabricAdvancementProvider {
    public AdInfinitumAdvancementProvider(FabricDataGenerator dataGenerator) { super(dataGenerator); }

    @Override public void generateAdvancement(Consumer<Advancement> consumer) {
        
    }
}
