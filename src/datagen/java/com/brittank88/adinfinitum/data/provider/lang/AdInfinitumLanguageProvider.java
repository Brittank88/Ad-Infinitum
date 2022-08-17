package com.brittank88.adinfinitum.data.provider.lang;

import com.brittank88.adinfinitum.api.client.registry.singularity.SingularityItemDataRegistry;
import com.brittank88.adinfinitum.util.AdInfinitumUtil;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.itemgroup.gui.ItemGroupTab;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.util.List;

public class AdInfinitumLanguageProvider extends FabricLanguageProvider {

    public AdInfinitumLanguageProvider(FabricDataGenerator dataGenerator) { super(dataGenerator); }
    public AdInfinitumLanguageProvider(FabricDataGenerator dataGenerator, String languageCode) { super(dataGenerator, languageCode); }

    @Override public void generateLanguages(LanguageConsumer languageConsumer) {

        SingularityItemDataRegistry.getSingularities().forEach((si, sid) -> {
            //languageConsumer.addLanguage(si.getTranslationKey(), );
        });
    }


}
