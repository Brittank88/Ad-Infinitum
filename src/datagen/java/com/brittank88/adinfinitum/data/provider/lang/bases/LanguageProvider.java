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
import java.util.*;
import java.util.function.Supplier;

/**
 * Language provider for Ad Infinitum.
 * <br /><br />
 * Based on the code used in Forge to generate translation data.
 * <br /><br />
 * Special thanks to <a href="https://github.com/ThatGravyBoat">ThatGravyBoat</a> for their help in understanding this class!
 *
 * @author Brittank88
 */
public abstract class LanguageProvider implements DataProvider {

    /** {@link Gson} instance used to write the {@link LanguageProvider#data} entries to a physical JSON file. */
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().setLenient().create();

    /** A mapping from translation keys to display strings. */
    private final Map<String, String> data = new TreeMap<>();
    private final DataGenerator       dataGenerator         ;
    private final String              modId                 ;
    private final String              locale                ;

    public LanguageProvider(DataGenerator dataGenerator) { this(dataGenerator, AdInfinitum.MOD_ID, "en_us"); }
    public LanguageProvider(DataGenerator dataGenerator, String modId, String locale) {
        this.dataGenerator = dataGenerator;
        this.modId         = modId        ;
        this.locale        = locale       ;
    }

    /** Adds the translations registered by all added modules to the {@link LanguageProvider}'s {@link LanguageProvider#data} map. */
    protected void addTranslations() { languageModules.forEach(lm -> lm.addTranslations(this)); }

    @Override public void run(DataCache cache) throws IOException {
        addTranslations();
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
     * Will automatically call {@link LanguageProvider#addLanguage(ItemGroupTab, OwoItemGroup, String)} for each tab within the {@link OwoItemGroup}.
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

    private final List<LanguageModule> languageModules = new ArrayList<>();

    /** Adds a {@link LanguageModule} instance to the {@link LanguageProvider#languageModules list of modules} to be run when {@link #run(DataCache)} is called. */
    public final void addModule(LanguageModule module) { languageModules.add(module); }
    /**
     * A version of {@link LanguageProvider#addModule(LanguageModule)} supporting a supplier.
     * <br /><br />
     * Allows for passing in language modules via <strong><code>addModule(MyLanguageModule::new);</code></strong>.
     *
     * @param module A {@link LanguageModule} supplier.
     * @see LanguageProvider#addModule(LanguageModule)
     */
    public final void addModule(Supplier<? extends LanguageModule> module) { languageModules.add(module.get()); }

    /** Adds multiple {@link LanguageModule} instances to the {@link LanguageProvider#languageModules list of modules} to be run when {@link #run(DataCache)} is called. */
    public final void addAllModules(LanguageModule... moduleArray) { languageModules.addAll(Arrays.asList(moduleArray)); }

    /**
     * A version of {@link LanguageProvider#addAllModules(LanguageModule...)} supporting suppliers.
     * <br /><br />
     * Allows for passing in language modules via <strong><code>addAllModules(MyLanguageModule1::new, MyLanguageModule2::new, ...);</code></strong>.
     *
     * @param moduleArray an array of {@link LanguageModule} suppliers.
     * @see LanguageProvider#addAllModules(LanguageModule...)
     */
    @SafeVarargs public final void addAllModules(Supplier<? extends LanguageModule>... moduleArray) { languageModules.addAll(Arrays.stream(moduleArray).map(Supplier::get).toList()); }

    /**
     * An interface to be implemented by classes that provide their own translations.
     * <br /><br />
     * Provides a single method that will be called when {@link LanguageProvider#run(DataCache)} is called,
     * in order to add translations to the {@link LanguageProvider#data map} that will then be saved as JSON in the language files.
     *
     * @author Brittank88
     * @see LanguageProvider#save(DataCache, Path)
     * @see LanguageProvider#addModule(LanguageModule)
     * @see LanguageProvider#addModule(Supplier)
     * @see LanguageProvider#addAllModules(LanguageModule...)
     * @see LanguageProvider#addAllModules(Supplier...)
     */
    public interface LanguageModule { void addTranslations(LanguageProvider provider); }
}
