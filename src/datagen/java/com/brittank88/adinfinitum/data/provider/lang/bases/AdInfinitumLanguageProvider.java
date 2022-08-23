package com.brittank88.adinfinitum.data.provider.lang.bases;

import com.brittank88.adinfinitum.data.provider.lang.modules.BlockLanguageModule;
import com.brittank88.adinfinitum.data.provider.lang.modules.ConfigLanguageModule;
import com.brittank88.adinfinitum.data.provider.lang.modules.GroupLanguageModule;
import com.brittank88.adinfinitum.data.provider.lang.modules.ItemLanguageModule;
import net.minecraft.data.DataGenerator;

public class AdInfinitumLanguageProvider extends BaseLanguageProvider {

    public AdInfinitumLanguageProvider(DataGenerator dataGenerator) {
        super(dataGenerator);

        BlockLanguageModule .addTranslations(this);
        ItemLanguageModule  .addTranslations(this);
        GroupLanguageModule .addTranslations(this);
        ConfigLanguageModule.addTranslations(this);
    }
}
