package com.brittank88.adinfinitum.client.config;

import com.brittank88.adinfinitum.AdInfinitum;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = AdInfinitum.MOD_ID)
public class AdInfinitumConfig implements ConfigData {
    // TODO: Implement config and lang datagen for config entries (will have to use reflection + annotations).
    public boolean dummyField = false;
}
