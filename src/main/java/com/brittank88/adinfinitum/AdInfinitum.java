package com.brittank88.adinfinitum;

import com.brittank88.adinfinitum.group.AdInfinitumGroups;
import com.brittank88.adinfinitum.block.AdInfinitumBlocks;
import com.brittank88.adinfinitum.item.AdInfinitumItems;
import com.brittank88.adinfinitum.item.AdInfinitumSingularities;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class AdInfinitum implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "ad-infinitum";

    public static Identifier id(String id) { return new Identifier(MOD_ID, id); }

    @Override public void onInitialize() {

        // Register item groups.
        AdInfinitumGroups.register();

        // Register blocks.
        FieldRegistrationHandler.register(AdInfinitumBlocks.class, MOD_ID, false);

        // Register items.
        FieldRegistrationHandler.register(AdInfinitumItems.class, MOD_ID, false);       // Normal items.
        FieldRegistrationHandler.processSimple(AdInfinitumSingularities.class, false);  // Singularities.
    }
}
