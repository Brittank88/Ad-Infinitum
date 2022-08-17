package com.brittank88.adinfinitum.item;

import com.brittank88.adinfinitum.AdInfinitum;
import com.brittank88.adinfinitum.util.RegistryUtil;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import net.minecraft.item.Item;

import java.lang.reflect.Field;

public final class AdInfinitumItems implements ItemRegistryContainer {

    @Override public void afterFieldProcessing() { AdInfinitum.LOGGER.info("Registered " + RegistryUtil.getRegisteredAmount(net.minecraft.util.registry.Registry.ITEM) + " items!"); }

    @Override public void postProcessField(String namespace, Item value, String identifier, Field field) {


    }
}
