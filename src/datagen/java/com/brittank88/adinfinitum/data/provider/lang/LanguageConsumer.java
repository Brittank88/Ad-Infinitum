package com.brittank88.adinfinitum.data.provider.lang;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SuppressWarnings("unused")
public interface LanguageConsumer {

    /**
     * Adds a language entry.
     *
     * @param languageKey  The key of the language entry.
     * @param value        The value of the entry.
     */
    void addLanguage(String languageKey, String value);

    /**
     * Adds a language entry for an {@link Item}.
     *
     * @param item The {@link Item} to get the language entry key from.
     * @param value The value of the entry.
     */
    default void addLanguage(Item item, String value) { addLanguage(item.getTranslationKey(), value); }

    /**
     * Adds a language entry for a {@link Block}.
     *
     * @param block The {@link Block} to get the language entry key from.
     * @param value The value of the entry.
     */
    default void addLanguage(Block block, String value) { addLanguage(block.getTranslationKey(), value); }

    /**
     * Adds a language entry for an {@link ItemGroup}.
     *
     * @param group The {@link ItemGroup} to get the language entry key from.
     * @param value The value of the entry.
     */
    default void addLanguage(ItemGroup group, String value) { addLanguage("itemGroup." + group.getName(), value); }

    /**
     * Adds a language entry for an {@link EntityType}.
     *
     * @param entityType The {@link EntityType} to get the language entry key from.
     * @param value The value of the entry.
     */
    default void addLanguage(EntityType<?> entityType, String value) { addLanguage(entityType.getTranslationKey(), value); }

    /**
     * Adds a language entry for an {@link Enchantment}.
     *
     * @param enchantment The {@link Enchantment} to get the language entry key from.
     * @param value The value of the entry.
     */
    default void addLanguage(Enchantment enchantment, String value) { addLanguage(enchantment.getTranslationKey(), value); }

    /**
     * Adds a language entry for an {@link EntityAttribute}.
     *
     * @param entityAttribute The {@link EntityAttribute} to get the language entry key from.
     * @param value The value of the entry.
     */
    default void addLanguage(EntityAttribute entityAttribute, String value) { addLanguage(entityAttribute.getTranslationKey(), value); }

    /**
     * Adds a language entry for a {@link StatType}.
     *
     * @param statType The {@link StatType} to get the language entry key from.
     * @param value The value of the entry.
     */
    default void addLanguage(StatType<?> statType, String value) { addLanguage(statType.getTranslationKey(), value); }

    /**
     * Adds a language entry for a {@link StatusEffect}.
     *
     * @param statusEffect The {@link StatusEffect} to get the language entry key from.
     * @param value The value of the entry.
     */
    default void addLanguage(StatusEffect statusEffect, String value) { addLanguage(statusEffect.getTranslationKey(), value); }

    /**
     * Adds a language entry for an {@link Identifier}.
     *
     * @param identifier The {@link Identifier} to get the language entry key from.
     * @param value The value of the entry.
     */
    default void addLanguage(Identifier identifier, String value) { addLanguage(identifier.getNamespace() + '.' + identifier.getPath(), value); }

    /**
     * Merges an existing language file into the data generated language file.
     *
     * @param existingLanguageFile The path to the existing language file.
     * @throws IOException If the path is invalid, an IOException is thrown.
     */
    default void addLanguage(Path existingLanguageFile) throws IOException {
        new Gson()
                .fromJson(Files.readString(existingLanguageFile), JsonObject.class)
                .entrySet()
                .forEach(stringJsonElementEntry -> addLanguage(stringJsonElementEntry.getKey(), stringJsonElementEntry.getValue().getAsString()));
    }
}
