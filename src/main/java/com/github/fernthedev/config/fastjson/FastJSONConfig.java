package com.github.fernthedev.config.fastjson;

import com.alibaba.fastjson.JSON;
import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.config.common.exceptions.ConfigLoadException;
import lombok.NonNull;

import java.io.File;
import java.util.List;

/**
 *
 * @param <T>
 */
public class FastJSONConfig<T> extends Config<T> {
    public FastJSONConfig(@NonNull T gsonConfigData, @NonNull File file) throws ConfigLoadException {
        super(gsonConfigData, file);
    }

    /**
     * Should return a String representation of the file {@link #configData}. This string representation should be the way that it is read in {@link #parseConfigFromData(List)}
     * @return String representation of {@link #configData} that is read by {@link #parseConfigFromData(List)}
     */
    @Override
    public String configToFileString() {
        return JSON.toJSONString(configData);
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

        return JSON.parseObject(jsonString.toString(), tClass);
    }

}
