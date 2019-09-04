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

    @Override
    protected String configToFileString() {
        return JSON.toJSONString(configData);
    }

    @Override
    protected T parseConfigFromData(String json) {
        return (T) JSON.parseObject(json, configData.getClass());
    }

}
