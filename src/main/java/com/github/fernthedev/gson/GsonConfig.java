package com.github.fernthedev.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.github.fernthedev.common.Config;
import lombok.NonNull;

import java.io.File;

/**
 *
 * @param <T>
 */
public class GsonConfig<T> extends Config {
    public GsonConfig(@NonNull T gsonConfigData, @NonNull File file) {
        super(gsonConfigData, file);
    }

    @Override
    protected String configToFileString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(configData);
    }

    @Override
    protected T parseConfigFromData(String json) {
        return (T) new Gson().fromJson(json, configData.getClass());
    }

}
