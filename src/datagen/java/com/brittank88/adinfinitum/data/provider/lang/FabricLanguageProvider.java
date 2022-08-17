package com.brittank88.adinfinitum.data.provider.lang;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataProvider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.TreeMap;

public abstract class FabricLanguageProvider implements DataProvider {

    protected final FabricDataGenerator dataGenerator;
    private final String languageCode;

    protected FabricLanguageProvider(FabricDataGenerator dataGenerator) { this(dataGenerator, "en_us"); }

    protected FabricLanguageProvider(FabricDataGenerator dataGenerator, String languageCode) {
        this.dataGenerator = dataGenerator;
        this.languageCode  = languageCode ;
    }

    /**
     * Implement this method to register languages.
     * <br /><br />
     * Call {@link LanguageConsumer#addLanguage(String, String)} to add a language entry.
     */
    public abstract void generateLanguages(LanguageConsumer languageConsumer);

    @Override public void run(DataCache dataCache) throws IOException {
        TreeMap<String, String> languageEntries = new TreeMap<>();

        generateLanguages(languageEntries::put);

        JsonObject langEntryJson = new JsonObject();
        languageEntries.forEach(langEntryJson::addProperty);

        DataProvider.writeToPath(new Gson(), dataCache, langEntryJson, getLangFilePath(this.languageCode));
    }

    private Path getLangFilePath(String code) { return dataGenerator.getOutput().resolve("assets/%s/lang/%s.json".formatted(dataGenerator.getModId(), code)); }

    @Override public String getName() { return "Languages"; }
}
