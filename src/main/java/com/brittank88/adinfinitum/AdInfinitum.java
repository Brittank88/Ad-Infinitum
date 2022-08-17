package com.brittank88.adinfinitum;

import com.brittank88.adinfinitum.group.AdInfinitumGroups;
import com.brittank88.adinfinitum.block.AdInfinitumBlocks;
import com.brittank88.adinfinitum.item.AdInfinitumItems;
import com.brittank88.adinfinitum.item.AdInfinitumSingularities;
import com.brittank88.adinfinitum.util.AdInfinitumUtil;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class AdInfinitum extends AdInfinitumUtil implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {

        // Register item groups.
        AdInfinitumGroups.register();

        // Register blocks.
        FieldRegistrationHandler.register(AdInfinitumBlocks.class, AdInfinitumUtil.MOD_ID, false);

        // Register items.
        FieldRegistrationHandler.register(AdInfinitumItems.class, AdInfinitumUtil.MOD_ID, false);       // Normal items.
        FieldRegistrationHandler.processSimple(AdInfinitumSingularities.class, false);  // Singularities.
    }
}
