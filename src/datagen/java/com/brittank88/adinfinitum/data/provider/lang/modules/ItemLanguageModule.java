package com.brittank88.adinfinitum.data.provider.lang.modules;

import com.brittank88.adinfinitum.api.client.registry.singularity.SingularityItemDataRegistryKt;
import com.brittank88.adinfinitum.data.provider.lang.bases.BaseLanguageProvider;

public class ItemLanguageModule {
    public static void addTranslations(BaseLanguageProvider provider) {


        SingularityItemDataRegistryKt.getSingularities().forEach((si, sid) -> {
            // provider.addLanguage(si.getTranslationKey(), "");  // FIXME
        });
    }
}
