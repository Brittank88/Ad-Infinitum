package com.brittank88.adastra.item;

import com.brittank88.adastra.AdAstra;
import com.brittank88.adastra.util.RegistryUtil;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import net.minecraft.item.Item;

import java.lang.reflect.Field;

public class AdAstraItems implements ItemRegistryContainer {

    @Override public void afterFieldProcessing() { AdAstra.LOGGER.info("Registered " + RegistryUtil.getRegisteredAmount(net.minecraft.util.registry.Registry.ITEM) + " items!"); }

    @Override public void postProcessField(String namespace, Item value, String identifier, Field field) {


    }
}
