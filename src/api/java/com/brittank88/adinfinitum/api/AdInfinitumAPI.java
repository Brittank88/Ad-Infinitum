package com.brittank88.adinfinitum.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The mod's main API class. Not intended to be used by external developers.
 *
 * @author Brittank88
 */
public final class AdInfinitumAPI {

    /**
     * {@link AdInfinitumAPI} instances should NOT be instantiated in standard programming.
     *
     * @throws IllegalAccessException If this constructor is invoked.
     */
    private AdInfinitumAPI() throws IllegalAccessException { throw new IllegalAccessException("Class should not be instantiated!"); }

    /** {@link Logger} specific to the api source set. */
    public static final Logger LOGGER = LogManager.getLogger();
}
