package com.github.fernthedev.gson;

import com.github.fernthedev.common.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.NonNull;

import java.io.File;
import java.util.List;

/**
 *
 * @param <T> The data type
 */
public class GsonConfig<T> extends Config<T> {
    private static final Gson gson = new Gson();

    public GsonConfig(@NonNull T gsonConfigData, @NonNull File file) {
        super(gsonConfigData, file);
    }

    /**
     * Should return a String representation of the file {@link #configData}. This string representation should be the way that it is read in {@link #parseConfigFromData(String)}
     * @return String representation of {@link #configData} that is read by {@link #parseConfigFromData(List)}
     */
    @Override
    protected String configToFileString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(configData);
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
        return gson.fromJson(jsonString.toString(), tClass);
    }

}
