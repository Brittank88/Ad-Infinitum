package com.brittank88.adinfinitum.item;

import com.brittank88.adinfinitum.AdInfinitum;
import com.brittank88.adinfinitum.util.RegistryUtils;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import net.minecraft.item.Item;

import java.lang.reflect.Field;

/**
 * This class is used to register all the {@link Item items} in the mod.
 *
 * @author Brittank88
 */
public final class AdInfinitumItems implements ItemRegistryContainer {

    @Override public void afterFieldProcessing() { AdInfinitum.LOGGER.info("Registered " + RegistryUtils.getRegisteredAmount(net.minecraft.util.registry.Registry.ITEM, AdInfinitum.MOD_ID) + " items!"); }

    @Override public void postProcessField(String namespace, Item value, String identifier, Field field) {


    }
}
