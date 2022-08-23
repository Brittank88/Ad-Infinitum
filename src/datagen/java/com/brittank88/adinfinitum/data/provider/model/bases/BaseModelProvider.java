package com.brittank88.adinfinitum.data.provider.model.bases;

import net.minecraft.data.DataCache;
import net.minecraft.data.DataProvider;

import java.io.IOException;

public abstract class BaseModelProvider implements DataProvider {
    @Override public void run(DataCache dataCache) throws IOException {

    }

    @Override public String getName() {
        return null;
    }
}
