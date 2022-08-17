package com.brittank88.adinfinitum.data.provider.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;

import java.util.function.Consumer;

public class AdInfinitumRecipeProvider extends FabricRecipeProvider {
    public AdInfinitumRecipeProvider(FabricDataGenerator dataGenerator) { super(dataGenerator); }

    @Override protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {

    }
}
