package com.github.fernthedev.fastjson;

import com.alibaba.fastjson.JSON;
import com.github.fernthedev.common.Config;
import lombok.NonNull;

import java.io.File;

/**
 *
 * @param <T>
 */
public class FastJSONConfig<T> extends Config {
    public FastJSONConfig(@NonNull T gsonConfigData, @NonNull File file) {
        super(gsonConfigData, file);
    }

    /**
     * Should return a String representation of the file {@link #configData}. This string representation should be the way that it is read in {@link #parseConfigFromData(String)}
     * @return String representation of {@link #configData} that is read by {@link #parseConfigFromData(String)}
     */
    @Override
    protected String configToFileString() {
        return JSON.toJSONString(configData);
    }

    /**
     * Returns the object instance of {@link #configData} parsed from the file which is saved by {@link #configToFileString()}
     * @param json The String data from the file.
     * @return The object instance.
     */
    @Override
    protected T parseConfigFromData(String json) {
        return (T) JSON.parseObject(json, configData.getClass());
    }

}
