package com.brittank88.adinfinitum.util;

import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AdInfinitumUtil {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "ad-infinitum";

    public static Identifier id(String id) { return new Identifier(MOD_ID, id); }
}
