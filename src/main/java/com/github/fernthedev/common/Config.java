package com.github.fernthedev.common;

import lombok.Getter;
import lombok.NonNull;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

import java.io.File;
import java.io.IOException;

/**
 *
 * @param <T> The data type
 */
public abstract class Config<T> {

    /**
     * The data instance. It is recommended it uses the {@code lombok.Data} annotation
     */
    @NonNull
    @Getter
    protected T configData;

    /**
     * The file that stores the {@link #configData}
     */
    @NonNull
    @Getter
    protected File file;

    public Config(@NonNull T configData, @NonNull File file) {
        this.configData = configData;
        this.file = file;

        load();
    }

    /**
     * Saves the file
     */
    public void save() {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            try (BufferedSink sink = Okio.buffer(Okio.sink(file))) {
                sink.writeUtf8(configToFileString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Loads the file
     */
    public void load() {
        try {
            if (!file.exists()) {
                save();
            }

            StringBuilder json = new StringBuilder();
            try (Source fileSource = Okio.source(file);
                 BufferedSource bufferedSource = Okio.buffer(fileSource)) {

                while (true) {
                    String line = bufferedSource.readUtf8Line();
                    if (line == null) break;

                    json.append(line);
                }
            }

            configData = parseConfigFromData(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Should return a String representation of the file {@link #configData}. This string representation should be the way that it is read in {@link #parseConfigFromData(String)}
     * @return String representation of {@link #configData} that is read by {@link #parseConfigFromData(String)}
     */
    protected abstract String configToFileString();

    /**
     * Returns the object instance of {@link #configData} parsed from the file which is saved by {@link #configToFileString()}
     * @param json The String data from the file.
     * @return The object instance.
     */
    protected abstract T parseConfigFromData(String json);
}
