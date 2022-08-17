package com.brittank88.adinfinitum.data.provider.lang;

import com.brittank88.adinfinitum.data.provider.lang.modules.BlockLanguageModule;
import com.brittank88.adinfinitum.data.provider.lang.modules.ConfigLanguageModule;
import com.brittank88.adinfinitum.data.provider.lang.modules.GroupLanguageModule;
import com.brittank88.adinfinitum.data.provider.lang.modules.ItemLanguageModule;
import net.minecraft.data.DataGenerator;

public class AdInfinitumLanguageProvider extends LanguageProvider {

    public AdInfinitumLanguageProvider(DataGenerator dataGenerator) {
        super(dataGenerator);

        addAllModules(
                BlockLanguageModule ::new,
                ItemLanguageModule  ::new,
                GroupLanguageModule ::new,
                ConfigLanguageModule::new
        );
    }
}
