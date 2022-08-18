package com.brittank88.adinfinitum.client.config;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.io.File;

public class AdInfinitumConfig extends Vigilant {

    public AdInfinitumConfig() { this(new File("./config/ad-infinitum-config.toml")); }
    public AdInfinitumConfig(File configFile) { super(configFile); }

    @Property(
            type        = PropertyType.CHECKBOX,
            name        = "Checkbox",
            description = "This is a checkbox property. It stores a boolean value.",
            category    = "Property Overview"
    )
    boolean demoCheckbox = true;

    // TODO: Implement config and lang datagen for config entries (will have to use reflection + annotations).
}
