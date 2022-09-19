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

/**
 * Entrypoint for the mod's common components.
 *
 * @author Brittank88
 */
public final class AdInfinitum implements ModInitializer {

    /** {@link Logger} specific to the main source set. */
    public static final Logger LOGGER = LogManager.getLogger();

    /** Main {@link String ID} of the mod. */
    public static final String MOD_ID = "ad_infinitum";

    /**
     * Returns an {@link Identifier} with the {@link #MOD_ID} as the namespace, and the supplied {@link String path} as the path.
     *
     * @param path The path of the identifier.
     * @return An {@link Identifier} with the {@link #MOD_ID} as the namespace, and the supplied {@link String path} as the path.
     */
    public static Identifier id(String path) { return new Identifier(MOD_ID, path); }

    /** Main initialisation method of the mod's main class. */
    @Override public void onInitialize() {

        // Register item groups.
        AdInfinitumGroups.register();

        // Register blocks.
        FieldRegistrationHandler.register(AdInfinitumBlocks.class, MOD_ID, true);

        // Register items.
        FieldRegistrationHandler.register     (AdInfinitumItems.class, MOD_ID, true);  // Normal items.
        FieldRegistrationHandler.processSimple(AdInfinitumSingularities.class, true);  // Singularities.
    }
}
