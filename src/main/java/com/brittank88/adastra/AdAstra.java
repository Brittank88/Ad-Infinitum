package com.brittank88.adastra;

import com.brittank88.adastra.block.AdAstraBlocks;
import com.brittank88.adastra.client.resource.AdAstraRRP;
import com.brittank88.adastra.group.AdAstraGroups;
import com.brittank88.adastra.item.AdAstraItems;
import com.brittank88.adastra.item.AdAstraSingularities;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdAstra implements ModInitializer {

    public static final String MOD_ID = "ad-astra";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {

        // Register item groups.
        AdAstraGroups.register();

        // Register blocks.
        FieldRegistrationHandler.register(AdAstraBlocks.class, MOD_ID, false);

        // Register items.
        FieldRegistrationHandler.register(AdAstraItems.class, MOD_ID, false);       // Normal items.
        FieldRegistrationHandler.processSimple(AdAstraSingularities.class, false);  // Singularities.

        AdAstraRRP.RESOURCE_PACK.dump();
    }

    public static Identifier id(String id) {
        return new Identifier(MOD_ID, id);
    }
}
