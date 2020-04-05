package com.github.fernthedev.config.common;

import com.github.fernthedev.config.common.exceptions.ConfigLoadException;
import com.github.fernthedev.config.common.exceptions.ConfigNullException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Synchronized;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    @Setter
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

    /**
     * Creates the config instance and saves the data. If the file does not exist,
     * it saves using the constructed parameter.
     *
     * @param configData The default data saved if the file is nonexistent
     * @param file The file to save
     * @throws ConfigLoadException
     */
    @SuppressWarnings("unchecked")
    public Config(@NonNull T configData, @NonNull File file) throws ConfigLoadException {
        this(configData, (Class<T>) configData.getClass(), file);
    }

    /**
     * Creates the config instance and saves the data. If the file does not exist,
     * it saves using the constructed parameter.
     *
     * @param configData The default data saved if the file is nonexistent
     * @param file The file to save
     * @throws ConfigLoadException
     */
    @SuppressWarnings("unchecked")
    public Config(T configData, Class<T> tClass, @NonNull File file) throws ConfigLoadException {
        this.configData = configData;
        this.file = file;
        this.tClass = tClass;

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

            String data = configToFileString();

            if ((data == null || data.isEmpty()) && !canBeNull()) throw new ConfigNullException("The data being saved is null or empty and it should not be allowed.");

            if (data == null) data = "null";

            try (BufferedSink sink = Okio.buffer(Okio.sink(file))) {
                sink.writeUtf8(data).writeUtf8(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Saves the file, then verifies if the contents were saved successfully
     * @throws ConfigNullException Thrown when the config information is null or malformed.
     */
    public boolean save() throws ConfigLoadException {
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
    public T load() throws ConfigLoadException {
        try {
            if (!file.exists()) {
                quickSave();
            }



            List<String> data = new ArrayList<>();
            try (Source fileSource = Okio.source(file);
                 BufferedSource bufferedSource = Okio.buffer(fileSource)) {

                while (true) {
                    String line = bufferedSource.readUtf8Line();
                    if (line == null) break;

                    data.add(line);
                }
            }

            configData = parseConfigFromData(data);

            if (configData == null && !canBeNull()) throw new ConfigNullException("ConfigData is null. \nData: " + data.toString()
                    + "\nConfigManager: " + getClass().getName());

            return configData;

        } catch (IOException e) {
            throw new ConfigLoadException("Unable to load config", e);
        }
    }

    /**
     * Locks the method to the thread to stop issues
     * while saving or loading files
     */
    @Synchronized
    public T syncLoad() throws ConfigLoadException {
        return load();
    }

    /**
     * Locks the method to the thread to stop issues
     * while saving or loading files
     */
    @Synchronized
    public boolean syncSave() throws ConfigLoadException {
        return save();
    }

    /**
     * Should return a String representation of the file {@link #configData}. This string representation should be the way that it is read in {@link #parseConfigFromData(List)}
     * @return String representation of {@link #configData} that is read by {@link #parseConfigFromData(List)}
     */
    protected abstract String configToFileString();

    /**
     * Returns the object instance of {@link #configData} parsed from the file which is saved by {@link #configToFileString()}
     * @param data The String data from the file.
     * @return The object instance.
     */
    protected abstract T parseConfigFromData(@NonNull List<String> data);

    /**
     * Override if data being saved is nullable
     * @return
     */
    protected boolean canBeNull() {
        return false;
    }
}
