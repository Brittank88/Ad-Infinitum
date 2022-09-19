package com.brittank88.adinfinitum.api.client.registry.singularity

import com.brittank88.adinfinitum.api.client.render.item.SingularityItemRenderData
import com.brittank88.adinfinitum.api.registry.singularity.SingularityItem
import com.google.common.collect.ImmutableMap

private val SINGULARITIES: MutableMap<SingularityItem, SingularityItemRenderData> = HashMap()
val singularities: Map<SingularityItem, SingularityItemRenderData> get() = ImmutableMap.copyOf(SINGULARITIES)

fun SingularityItem.register(singularityItemRenderData: SingularityItemRenderData) {
    if (SINGULARITIES.containsKey(this)) throw DuplicateSingularityRegistrationException(this)
    SINGULARITIES[this] = singularityItemRenderData
}

class DuplicateSingularityRegistrationException(item: SingularityItem) : IllegalArgumentException("Duplicate singularity item registered: $item")
