package com.brittank88.adastra.api.registry;

import com.brittank88.adastra.annotation.DisplayName;
import com.brittank88.adastra.api.item.AbstractSingularityItem;
import com.brittank88.adastra.client.resource.AdAstraRRP;
import com.brittank88.adastra.util.RegistryUtil;
import io.wispforest.owo.registration.reflect.SimpleFieldProcessingSubject;
import net.devtech.arrp.json.models.JModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class SingularityRegistryContainer implements SimpleFieldProcessingSubject<AbstractSingularityItem> {

    public final String namespace;

    public SingularityRegistryContainer(@NotNull String namespace) { this.namespace = namespace; }

    @Override public void processField(AbstractSingularityItem value, String identifier, Field field) {
        processSingularity(value, identifier, field);                                           // Process MK-I singularity.
        value.generateRemainingTiers().forEach(s -> processSingularity(s, identifier, field));  // Process remaining tiers.
    };

    private void processSingularity(AbstractSingularityItem value, String identifier, Field field) {

        Identifier sID = new Identifier(this.namespace, identifier + "_mk-" + value.getTierNumeral(String::toLowerCase));

        // Register singularity item.
        Registry.register(Registry.ITEM, sID, value);

        // Register singularity lang.
        DisplayName displayNameAnnotation = field.getAnnotation(DisplayName.class);
        AdAstraRRP.LANG.item(
                sID, (
                        displayNameAnnotation == null
                                ? RegistryUtil.registryToDisplayName(identifier)
                                : displayNameAnnotation.name()
                ) + " [MK-" + value.getTierNumeral() + ']'
        );

        // Register singularity model.
        AdAstraRRP.RESOURCE_PACK.addModel(JModel.model("minecraft:builtin/entity"), new Identifier(this.namespace, "item/" + sID.getPath()));

        // TODO: Finish this.
    }

    @Override public Class<AbstractSingularityItem> getTargetFieldType() { return AbstractSingularityItem.class; }
}
