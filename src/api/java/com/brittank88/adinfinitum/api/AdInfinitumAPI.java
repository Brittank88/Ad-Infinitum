package com.brittank88.adinfinitum.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class AdInfinitumAPI {

    private AdInfinitumAPI() throws IllegalAccessException { throw new IllegalAccessException("Class should not be instantiated!"); }

    public static final Logger LOGGER = LogManager.getLogger();
}
