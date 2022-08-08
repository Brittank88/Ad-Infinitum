package com.brittank88.adastra.util;

import com.brittank88.adastra.AdAstra;
import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Helper class for registry-related operations.
 *
 * @author brittank88
 */
public abstract class RegistryUtil {

    /**
     * Returns the amount of entries registered with the given {@link net.minecraft.util.registry.Registry} by this mod.
     *
     * @param registry The registry to check.
     * @return The amount of entries registered with the given {@link net.minecraft.util.registry.Registry} by this mod.
     * 
     * @see #getRegisteredAmount(net.minecraft.util.registry.Registry, String)
     */
    public static long getRegisteredAmount(@NotNull net.minecraft.util.registry.Registry<?> registry) {
        return getRegisteredAmount(registry, AdAstra.MOD_ID);
    }

    /**
     * Returns the amount of entries registered with the given {@link net.minecraft.util.registry.Registry} under the given {@link String namespace}.
     *
     * @param registry The registry to check.
     * @param namespace The namespace to check.
     * @return The amount of entries registered with the given {@link net.minecraft.util.registry.Registry} under the given {@link String namespace}.
     */
    public static long getRegisteredAmount(@NotNull net.minecraft.util.registry.Registry<?> registry, @NotNull String namespace) {
        return registry.getIds().stream().filter(id -> id.getNamespace().equals(namespace)).count();
    }

    /**
     * Attempts to convert a given {@link String registry name} into a display name.
     *
     * @param registryName The identifier to convert.
     * @return The display name of the given {@link String registry name}.
     */
    public static String registryToDisplayName(String registryName) { return WordUtils.capitalizeFully(registryName.replace('_', ' ')); }
}
