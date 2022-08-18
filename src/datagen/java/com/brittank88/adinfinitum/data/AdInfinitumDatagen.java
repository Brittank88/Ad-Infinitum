package com.brittank88.adinfinitum.data;

import com.brittank88.adinfinitum.data.provider.advancement.AdInfinitumAdvancementProvider;
import com.brittank88.adinfinitum.data.provider.lang.bases.BaseLanguageProvider;
import com.brittank88.adinfinitum.data.provider.loottable.AdInfinitumBlockLootTableProvider;
import com.brittank88.adinfinitum.data.provider.model.AdInfinitumModelProvider;
import com.brittank88.adinfinitum.data.provider.recipe.AdInfinitumRecipeProvider;
import com.brittank88.adinfinitum.data.provider.tag.AdInfinitumBlockTagProvider;
import com.brittank88.adinfinitum.data.provider.tag.AdInfinitumItemTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataProvider;

/**
 * Entrypoint for the mod's data generator.
 *
 * @author Brittank88
 * @see DataGeneratorEntrypoint
 */
public class AdInfinitumDatagen implements DataGeneratorEntrypoint {

    /**
     * Main initialisation method of the mod's main datagen class.
     *
     * @param fabricDataGenerator The {@link FabricDataGenerator} instance used to register {@link net.minecraft.data.DataProvider DataProviders}.
     *
     * @see FabricDataGenerator#addProvider(DataProvider)
     */
    @Override public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(AdInfinitumAdvancementProvider   ::new);
        fabricDataGenerator.addProvider(BaseLanguageProvider             ::new);
        fabricDataGenerator.addProvider(AdInfinitumBlockLootTableProvider::new);
        fabricDataGenerator.addProvider(AdInfinitumModelProvider         ::new);
        fabricDataGenerator.addProvider(AdInfinitumRecipeProvider        ::new);
        fabricDataGenerator.addProvider(AdInfinitumBlockTagProvider      ::new);
        fabricDataGenerator.addProvider(AdInfinitumItemTagProvider       ::new);
    }
}
