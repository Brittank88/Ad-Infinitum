package com.brittank88.adinfinitum.data.provider.lang;

import com.brittank88.adinfinitum.util.AdInfinitumUtil;
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
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.StatType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class LanguageProvider implements DataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().setLenient().create();

    private final Map<String, String> data = new TreeMap<>();
    private final DataGenerator       gen                   ;
    private final String              modid                 ;
    private final String              locale                ;

    public LanguageProvider(DataGenerator gen, String modid, String locale) {
        this.gen    = gen   ;
        this.modid  = modid ;
        this.locale = locale;
    }

    protected void addTranslations() { languageModules.forEach(lm -> lm.addTranslations(this)); }

    @Override public void run(DataCache cache) throws IOException {
        addTranslations();
        if (!data.isEmpty()) save(cache, data, this.gen.getOutput().resolve("assets/" + modid + "/lang/" + locale + ".json"));
    }

    @Override public String getName() { return "Languages: " + locale; }

    private void save(DataCache cache, Object object, Path target) throws IOException {
        JsonObject json = new JsonObject();
        data.forEach(json::addProperty);
        DataProvider.writeToPath(GSON, cache, json, target);
    }

    public void addLanguage(Block         key, String name) { addLanguage(key.getTranslationKey(), name);                                 }
    public void addLanguage(Item          key, String name) { addLanguage(key.getTranslationKey(), name);                                 }
    public void addLanguage(ItemStack     key, String name) { addLanguage(key.getTranslationKey(), name);                                 }
    public void addLanguage(Enchantment   key, String name) { addLanguage(key.getTranslationKey(), name);                                 }
    public void addLanguage(StatusEffect  key, String name) { addLanguage(key.getTranslationKey(), name);                                 }
    public void addLanguage(EntityType<?> key, String name) { addLanguage(key.getTranslationKey(), name);                                 }
    public void addLanguage(StatType<?>   key, String name) { addLanguage(key.getTranslationKey(), name);                                 }
    public void addLanguage(ItemGroup     key, String name) { addLanguage(key.getName(), name);                                           }
    //public void addLanguage(ItemGroupTab  key, String name) { addLanguage(key.getTranslationKey(), name);                                 }
    public void addLanguage(Text          key, String name) { addLanguage(key instanceof TranslatableText tt ? tt.getKey() : null, name); }
    public void addLanguage(Identifier    key, String name) { addLanguage(key.getNamespace() + '.' + key.getPath(), name);           }
    public void addLanguage(String        key, String name) {
        if (key == null) throw new IllegalArgumentException("Key was null for name: " + name);
        if (data.put(key, name) != null) throw new IllegalStateException("Duplicate translation key: " + key);
    }

    private void addLanguage(LanguageConsumer languageConsumer, OwoItemGroup group, String value, List<String> tabNames) {

        String groupLanguageKey = "itemGroup." + AdInfinitumUtil.MOD_ID + '.' + group.getName();
        languageConsumer.addLanguage(groupLanguageKey, value);

        if (group.tabs.size() != tabNames.size()) throw new IllegalArgumentException("The number of tabs in the group does not match the number of tab names provided.");

        for (int i = 0; i < group.tabs.size(); i++) addLanguage(languageConsumer, group.tabs.get(i), groupLanguageKey, tabNames.get(i));
    }

    private void addLanguage(LanguageConsumer languageConsumer, ItemGroupTab tab, String groupKey, String value) { languageConsumer.addLanguage(tab.getTranslationKey(groupKey), value); }

    private final List<LanguageModule> languageModules = new ArrayList<>();
    public void addLanguageModule(LanguageModule languageModule) { languageModules.add(languageModule); }
    public interface LanguageModule { void addTranslations(LanguageProvider provider); }
}
