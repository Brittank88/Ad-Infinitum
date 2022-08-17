package com.brittank88.adinfinitum.api.client.registry.singularity;

import com.brittank88.adinfinitum.api.AdInfinitumAPI;
import com.brittank88.adinfinitum.api.client.render.item.SingularityItemData;
import com.brittank88.adinfinitum.api.registry.singularity.SingularityItem;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public final class SingularityItemDataRegistry {

    private static final Map<SingularityItem, SingularityItemData> SINGULARITIES = new HashMap<>();

    private SingularityItemDataRegistry() throws IllegalAccessException { throw new IllegalAccessException("Class should not be instantiated!"); }

    public static void register(SingularityItem singularityItem, SingularityItemData singularityItemData) {
        if (SINGULARITIES.containsKey(singularityItem)) AdInfinitumAPI.LOGGER.warn("Duplicate singularity item registered: " + singularityItem);
        SINGULARITIES.put(singularityItem, singularityItemData);
    }

    public static Map<SingularityItem, SingularityItemData> getSingularities() { return ImmutableMap.copyOf(SINGULARITIES); }
}
