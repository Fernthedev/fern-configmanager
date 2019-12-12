package com.github.fernthedev.common;

import com.github.fernthedev.common.exceptions.ConfigNullException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Synchronized;
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

    @Getter
    @NonNull
    protected final Class<T> tClass;

    /**
     * The file that stores the {@link #configData}
     */
    @NonNull
    @Getter
    protected File file;

    @SuppressWarnings("unchecked")
    public Config(@NonNull T configData, @NonNull File file) {
        this.configData = configData;
        this.file = file;
        this.tClass = (Class<T>) configData.getClass();

        load();
    }

    /**
     * Saves the file without verifying the contents of the file
     */
    public void quickSave() {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            try (BufferedSink sink = Okio.buffer(Okio.sink(file))) {
                sink.writeUtf8(configToFileString()).writeUtf8(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Saves the file, then verifies if the contents were saved successfully
     * @throws ConfigNullException Thrown when the config information is null or malformed.
     */
    public boolean save() {
        String oldData = configToFileString();
        quickSave();
        load();
        String newData = configToFileString();

        return oldData.equals(newData);
    }

    /**
     *  Loads the file
     * @throws ConfigNullException Thrown when the config information is null or malformed.
     */
    public void load() {
        try {
            if (!file.exists()) {
                quickSave();
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

            if (configData == null) throw new ConfigNullException("ConfigData is null. \nJSON: " + json.toString()
                    + "\nConfigManager: " + getClass().getName());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Locks the method to the thread to stop issues
     * while saving or loading files
     */
    @Synchronized
    public void syncLoad() {
        load();
    }

    /**
     * Locks the method to the thread to stop issues
     * while saving or loading files
     */
    @Synchronized
    public void syncSave() {
        quickSave();
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
    protected abstract T parseConfigFromData(@NonNull String json);
}
