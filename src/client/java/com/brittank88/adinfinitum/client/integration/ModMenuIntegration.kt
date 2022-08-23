package com.brittank88.adinfinitum.client.integration

import com.brittank88.adinfinitum.client.AdInfinitumClient
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import net.minecraft.client.gui.screen.Screen

/**
 * Integration to allow navigation to the mod's config screen through ModMenu's config button.
 *
 * @author Brittank88
 * @see <a href="https://shedaniel.gitbook.io/cloth-config/advanced/modmenu-integration">ModMenu Integration Wiki Page</a>
 */
class ModMenuIntegration : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory<Screen?> { AdInfinitumClient.COMMON_CONFIG.gui() }
    }
}