package com.brittank88.adinfinitum.api.client.registry.singularity

import com.brittank88.adinfinitum.api.client.render.item.SingularityItemData
import com.brittank88.adinfinitum.api.registry.singularity.SingularityItem
import com.google.common.collect.ImmutableMap

private val SINGULARITIES: MutableMap<SingularityItem, SingularityItemData> = HashMap()
val singularities: Map<SingularityItem, SingularityItemData> get() = ImmutableMap.copyOf(SINGULARITIES)

fun SingularityItem.register(singularityItemData: SingularityItemData) {
    if (SINGULARITIES.containsKey(this)) throw DuplicateSingularityRegistrationException(this)
    SINGULARITIES[this] = singularityItemData
}

class DuplicateSingularityRegistrationException(item: SingularityItem) : IllegalArgumentException("Duplicate singularity item registered: $item")
