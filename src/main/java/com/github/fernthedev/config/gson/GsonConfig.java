package com.github.fernthedev.config.gson;

import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.config.common.exceptions.ConfigLoadException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.NonNull;
import lombok.Setter;

import java.io.File;
import java.util.List;

/**
 *
 * @param <T> The data type
 */
public class GsonConfig<T> extends Config<T> {
    private static final Gson defaultPrettyGson;

    static {
        defaultPrettyGson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Setter
    @NonNull
    private Gson gson;

    public GsonConfig(@NonNull T gsonConfigData, @NonNull File file) throws ConfigLoadException {
        super(gsonConfigData, file);
        if (configData == null) throw new NullPointerException("Config data is null");
        gson = defaultPrettyGson;

        if (gson == null) throw new NullPointerException("Gson is null");
    }

    /**
     * Should return a String representation of the file {@link #configData}. This string representation should be the way that it is read in {@link #parseConfigFromData(List)}
     * @return String representation of {@link #configData} that is read by {@link #parseConfigFromData(List)}
     */
    @Override
    protected String configToFileString() {
        if (configData == null) throw new NullPointerException("Config data is null");
        if (gson == null) gson = defaultPrettyGson;

        return gson.toJson(configData);
    }

    /**
     * Returns the object instance of {@link #configData} parsed from the file which is saved by {@link #configToFileString()}
     * @param json The String data from the file.
     * @return The object instance.
     */
    @Override
    protected T parseConfigFromData(@NonNull List<String> json) {
        StringBuilder jsonString = new StringBuilder();

        for (String s : json) jsonString.append(s);
        return defaultPrettyGson.fromJson(jsonString.toString(), tClass);
    }

}
