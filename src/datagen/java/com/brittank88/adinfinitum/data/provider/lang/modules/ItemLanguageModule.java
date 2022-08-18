package com.brittank88.adinfinitum.data.provider.lang.modules;

import com.brittank88.adinfinitum.api.client.registry.singularity.SingularityItemDataRegistry;
import com.brittank88.adinfinitum.data.provider.lang.bases.LanguageProvider;

public class ItemLanguageModule implements LanguageProvider.LanguageModule {
    @Override public void addTranslations(LanguageProvider provider) {


        SingularityItemDataRegistry.getSingularities().forEach((si, sid) -> {
            // provider.addLanguage(si.getTranslationKey(), "");  // FIXME
        });
    }
}
