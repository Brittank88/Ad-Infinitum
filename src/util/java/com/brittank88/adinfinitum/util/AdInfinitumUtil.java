package com.brittank88.adinfinitum.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The mod's main utility class. Not intended to be used by external developers.
 *
 * @author Brittank88
 */
public class AdInfinitumUtil {

    /**
     * {@link AdInfinitumUtil} instances should NOT be instantiated in standard programming.
     *
     * @throws IllegalAccessException If this constructor is invoked.
     */
    private AdInfinitumUtil() throws IllegalAccessException { throw new IllegalAccessException("Class should not be instantiated!"); }

    /** {@link Logger} specific to the util source set. */
    public static final Logger LOGGER = LogManager.getLogger();
}
