package com.brittank88.adinfinitum.util;

import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdInfinitumUtil {

    private AdInfinitumUtil() throws IllegalAccessException { throw new IllegalAccessException("Class should not be instantiated!"); }

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "ad-infinitum";

    public static Identifier id(String id) { return new Identifier(MOD_ID, id); }
}
