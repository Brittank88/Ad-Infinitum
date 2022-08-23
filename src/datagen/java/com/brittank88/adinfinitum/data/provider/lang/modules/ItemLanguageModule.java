package com.brittank88.adinfinitum.data.provider.lang.modules;

import com.brittank88.adinfinitum.api.client.registry.singularity.SingularityItemDataRegistry;
import com.brittank88.adinfinitum.data.provider.lang.bases.BaseLanguageProvider;

public class ItemLanguageModule {
    public static void addTranslations(BaseLanguageProvider provider) {


        SingularityItemDataRegistry.getSingularities().forEach((si, sid) -> {
            // provider.addLanguage(si.getTranslationKey(), "");  // FIXME
        });
    }
}
