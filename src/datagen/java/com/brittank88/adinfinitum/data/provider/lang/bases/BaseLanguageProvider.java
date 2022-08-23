package com.brittank88.adinfinitum.data.provider.lang.bases;

import com.brittank88.adinfinitum.AdInfinitum;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.itemgroup.gui.ItemGroupTab;
import net.minecraft.block.Block;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.StatType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 * Language provider for Ad Infinitum.
 * <br /><br />
 * Based on the code used in Forge to generate translation data.
 * <br /><br />
 * Special thanks to <a href="https://github.com/ThatGravyBoat">ThatGravyBoat</a> for their help in understanding this class!
 *
 * @author Brittank88
 */
public abstract class BaseLanguageProvider implements DataProvider {

    /** {@link Gson} instance used to write the {@link BaseLanguageProvider#data} entries to a physical JSON file. */
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().setLenient().create();

    /** A mapping from translation keys to display strings. */
    private final Map<String, String> data = new TreeMap<>();
    private final DataGenerator       dataGenerator         ;
    private final String              modId                 ;
    private final String              locale                ;

    public BaseLanguageProvider(DataGenerator dataGenerator) { this(dataGenerator, AdInfinitum.MOD_ID, "en_us"); }
    public BaseLanguageProvider(DataGenerator dataGenerator, String modId, String locale) {
        this.dataGenerator = dataGenerator;
        this.modId         = modId        ;
        this.locale        = locale       ;
    }

    @Override public void run(DataCache cache) throws IOException {
        if (!data.isEmpty()) save(cache, this.dataGenerator.getOutput().resolve("assets/" + modId + "/lang/" + locale + ".json"));
    }

    @Override public String getName() { return "Languages: " + locale; }

    /**
     * Saves the data in the {@link DataCache} to the specified {@link Path} as JSON.
     *
     * @param cache The {@link DataCache} providing the data to save.
     * @param target The {@link Path} to save the data to.
     * @throws IOException If an I/O error occurs whilst writing the data to the JSON file.
     */
    private void save(DataCache cache, Path target) throws IOException {
        JsonObject json = new JsonObject();
        data.forEach(json::addProperty);
        DataProvider.writeToPath(GSON, cache, json, target);
    }

    //#===#// VIGILANCE //#===#//

    // TODO: Implement

    //#===#// OWO-LIB //#===#//

    /**
     * Supports adding a translation for a {@link OwoItemGroup}.
     * <br /><br />
     * Will automatically call {@link BaseLanguageProvider#addLanguage(ItemGroupTab, OwoItemGroup, String)} for each tab within the {@link OwoItemGroup}.
     */
    public void addLanguage(OwoItemGroup key, String name) {
        addLanguage(key.getDisplayName(), name);
        key.tabs.forEach(tab -> addLanguage(tab, key, tab.name()));
    }

    /** Supports adding a translation for an {@link ItemGroupTab}, using a reference to the instance of the {@link OwoItemGroup} that contains it. */
    public void addLanguage(ItemGroupTab key, @NotNull OwoItemGroup group, String name) { addLanguage(key, group.getName(), name); }

    /** Supports adding a translation for an {@link ItemGroupTab}, using the ID of the {@link OwoItemGroup} that contains it. */
    public void addLanguage(@NotNull ItemGroupTab key, String groupKey   , String name) { addLanguage(key.getTranslationKey(groupKey)       , name); }

    //#===#// VANILLA //#===#//

    /** Supports adding a translation for a {@link Block}. */
    public void addLanguage(@NotNull Block           key, String name) { addLanguage(key.getTranslationKey(), name); }

    /** Supports adding a translation for an {@link Item}. */
    public void addLanguage(@NotNull Item            key, String name) { addLanguage(key.getTranslationKey(), name); }

    /** Supports adding a translation for an {@link ItemStack}. */
    public void addLanguage(@NotNull ItemStack       key, String name) { addLanguage(key.getTranslationKey(), name); }

    /** Supports adding a translation for an {@link Enchantment}. */
    public void addLanguage(@NotNull Enchantment     key, String name) { addLanguage(key.getTranslationKey(), name); }

    /** Supports adding a translation for an {@link StatusEffect}. */
    public void addLanguage(@NotNull StatusEffect    key, String name) { addLanguage(key.getTranslationKey(), name); }

    /** Supports adding a translation for an {@link EntityType}. */
    public void addLanguage(@NotNull EntityType<?>   key, String name) { addLanguage(key.getTranslationKey(), name); }

    /** Supports adding a translation for an {@link EntityAttribute}. */
    public void addLanguage(@NotNull EntityAttribute key, String name) { addLanguage(key.getTranslationKey(), name); }

    /** Supports adding a translation for a {@link StatType}. */
    public void addLanguage(@NotNull StatType<?>     key, String name) { addLanguage(key.getTranslationKey(), name); }

    /** Supports adding a translation for a {@link ItemGroup}. */
    public void addLanguage(@NotNull ItemGroup       key, String name) { addLanguage(key.getName(), name); }

    /** Supports adding a translation for a {@link Text}. */
    public void addLanguage(@NotNull Text            key, String name) { addLanguage(key instanceof TranslatableText tt ? tt.getKey() : null, name); }

    /** Supports adding a translation for a {@link Identifier}. */
    public void addLanguage(@NotNull Identifier      key, String name) { addLanguage(key.getNamespace() + '.' + key.getPath(), name);           }

    /**
     * Supports adding a translation for a {@link String} acting as a translation key.
     * <br /><br />
     * This is the most base-level method for adding a translation.
     */
    @Contract("null, _ -> fail") public void addLanguage(String key, String name) {
        if (key == null) throw new IllegalArgumentException("Key was null for name: " + name);
        if (data.put(key, name) != null) throw new IllegalStateException("Duplicate translation key: " + key);
    }

    @SafeVarargs protected final void addAll(Consumer<BaseLanguageProvider>... baseLanguageProviderConsumer) {
        for (Consumer<BaseLanguageProvider> languageProviderConsumer : baseLanguageProviderConsumer) {
            languageProviderConsumer.accept(this);
        }
    }
}
