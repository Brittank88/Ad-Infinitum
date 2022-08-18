package com.brittank88.adinfinitum.api.registry.singularity;

import com.brittank88.adinfinitum.api.registry.annotation.WithDisplayName;
import io.wispforest.owo.registration.reflect.SimpleFieldProcessingSubject;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

/**
 * A registry container that is capable of registering {@link SingularityItem items} to the {@link Registry#ITEM item registry}.
 *
 * @author Brittank88
 * @see io.wispforest.owo.registration.reflect.SimpleFieldProcessingSubject SimpleFieldProcessingSubject
 * @see SingularityItem AbstractSingularityItem
 */
public abstract class SingularityItemProcessingSubject implements SimpleFieldProcessingSubject<SingularityItem> {

    // TODO: Migrate to usage of the @Identifier annotation.
    public final String namespace;

    public SingularityItemProcessingSubject(@NotNull String namespace) { this.namespace = namespace; }

    @Override public void processField(SingularityItem value, String identifier, Field field) {
        processSingularity(value, identifier, field);                                           // Process MK-I singularity.
        value.generateRemainingTiers().forEach(s -> processSingularity(s, identifier, field));  // Process remaining tiers.
    }

    private void processSingularity(SingularityItem value, String identifier, Field field) {

        Identifier sID = new Identifier(this.namespace, identifier + "_mk-" + value.getTierNumeral(String::toLowerCase));

        // Register singularity item.
        Registry.register(Registry.ITEM, sID, value);

        // Register singularity lang.
        WithDisplayName withDisplayNameAnnotation = field.getAnnotation(WithDisplayName.class);
        // TODO: A
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

    @Override public Class<SingularityItem> getTargetFieldType() { return SingularityItem.class; }
}
