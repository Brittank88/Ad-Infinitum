package com.brittank88.adinfinitum.client.integration;

import com.brittank88.adinfinitum.client.AdInfinitumClient;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * Integration to allow navigation to the mod's config screen through ModMenu's config button.
 *
 * @author Brittank88
 * @see <a href="https://shedaniel.gitbook.io/cloth-config/advanced/modmenu-integration">ModMenu Integration Wiki Page</a>
 */
public class ModMenuIntegration implements ModMenuApi {
    @Override public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> AdInfinitumClient.COMMON_CONFIG.gui();
    }
}
