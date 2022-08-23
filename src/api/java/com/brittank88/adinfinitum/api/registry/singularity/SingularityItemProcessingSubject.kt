package com.brittank88.adinfinitum.api.registry.singularity

import com.brittank88.adinfinitum.api.registry.singularity.SingularityItem
import io.wispforest.owo.itemgroup.OwoItemSettings
import io.wispforest.owo.registration.reflect.SimpleFieldProcessingSubject
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.lang.RuntimeException
import java.lang.reflect.Field

/**
 * A registry container that is capable of registering [items][SingularityItem] to the [item registry][Registry.ITEM].
 *
 * @author Brittank88
 * @see io.wispforest.owo.registration.reflect.SimpleFieldProcessingSubject SimpleFieldProcessingSubject
 *
 * @see SingularityItem AbstractSingularityItem
 */
abstract class SingularityItemProcessingSubject(private val namespace: String) : SimpleFieldProcessingSubject<SingularityItem> {

    // TODO: Migrate to usage of the @Identifier annotation.
    override fun processField(value: SingularityItem, identifier: String, field: Field) {
        processSingularity(value, identifier, field) // Process MK-I singularity.
        (2 .. value.tier).forEach { tier ->
            processSingularity(SingularityItem(value.material, tier) { OwoItemSettings().tab(it) }, identifier, field)
        }
    }

    private fun processSingularity(value: SingularityItem, identifier: String, field: Field) {
        val sID = Identifier(namespace, identifier + "_mk-" + (
                value.tierNumeral?.lowercase() ?: throw RuntimeException("Failed to generate ID for singularity '$value' - Numeral was null for tier '${value.tier}'!")
        ))

        // Register singularity item.
        Registry.register(Registry.ITEM, sID, value)

        // Register singularity lang.
        // WithDisplayName withDisplayNameAnnotation = field.getAnnotation(WithDisplayName.class);
        // TODO: Move to datagen
        /*
        AdInfinitumRRP.LANG_EN_US.item(
                sID, (
                        withDisplayNameAnnotation == null
                                ? RegistryUtil.registryToDisplayName(identifier)
                                : withDisplayNameAnnotation.name()
                ) + " [MK-" + value.getTierNumeral() + ']'
        );
        */

        // Register singularity model.
        //AdInfinitumRRP.RESOURCE_PACK.addModel(JModel.model("minecraft:builtin/entity"), new Identifier(this.namespace, "item/" + sID.getPath()));

        // TODO: Finish this.
    }

    override fun getTargetFieldType() = SingularityItem::class.java
}