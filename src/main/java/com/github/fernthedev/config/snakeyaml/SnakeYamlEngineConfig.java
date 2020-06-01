package com.github.fernthedev.config.snakeyaml;

import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.config.common.exceptions.ConfigLoadException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.snakeyaml.engine.v2.api.Dump;
import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

import java.io.File;
import java.util.List;

/**
 *
 * @param <T> The data type
 */
public class SnakeYamlEngineConfig<T> extends Config<T> {

    @Getter
    @Setter
    @NonNull
    private Dump dumpSettings;

    @Getter
    @Setter
    @NonNull
    private Load loadSettings;

    /**
     * Creates the config instance and saves the data. If the file does not exist,
     * it saves using the constructed parameter.
     *
     * @param configData The default data saved if the file is nonexistent
     * @param file       The file to save
     * @throws ConfigLoadException
     */
    public SnakeYamlEngineConfig(T configData, @NonNull File file, Dump dumpSettings, Load loadSettings) throws ConfigLoadException {
        super(configData, file);
        this.dumpSettings = dumpSettings;
        this.loadSettings = loadSettings;
    }

    /**
     * Creates the config instance and saves the data. If the file does not exist,
     * it saves using the constructed parameter.
     *
     * @param configData The default data saved if the file is nonexistent
     * @param tClass
     * @param file       The file to save
     * @throws ConfigLoadException
     */
    public SnakeYamlEngineConfig(T configData, Class<T> tClass, @NonNull File file, Dump dumpSettings, Load loadSettings) throws ConfigLoadException {
        super(configData, tClass, file);
        this.dumpSettings = dumpSettings;
        this.loadSettings = loadSettings;
    }

    public SnakeYamlEngineConfig(@NonNull T gsonConfigData, @NonNull File file) throws ConfigLoadException {
        super(gsonConfigData, file);
        defaultDumpInit();
        defaultLoadInit();
    }

    private void defaultLoadInit() {
        loadSettings = new Load(LoadSettings.builder().build());
    }

    private void defaultDumpInit() {
        dumpSettings = new Dump(DumpSettings.builder().build());
    }



    /**
     * Should return a String representation of the file {@link #configData}. This string representation should be the way that it is read in {@link #parseConfigFromData(List)}
     * @return String representation of {@link #configData} that is read by {@link #parseConfigFromData(List)}
     */
    @Override
    public String configToFileString() {
        if (dumpSettings == null) defaultDumpInit();

        return dumpSettings.dumpToString(configData);
//        Map<String, Object> valueMap = toKeyMap(configData);
    }

//    private Map<String, Object> toKeyMap(Object classObj) {
//        Map<String, Object> objectMap = new HashMap<>();
//        for (Field field : classObj.getClass().getDeclaredFields()) {
//            if (field.isAnnotationPresent(ConfigIgnore.class) || Modifier.isTransient(field.getModifiers())) continue;
//
//            String name = field.getName();
//
//            if (field.isAnnotationPresent(ConfigName.class)) name = field.getAnnotation(ConfigName.class).value();
//
//            field.setAccessible(true);
//            if (field.getType().isPrimitive()) {
//
//                try {
//                    objectMap.put(name, field.get(classObj));
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                try {
//                    objectMap.put(name, toKeyMap(field.get(classObj))); // Recursive class map
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return objectMap;
//    }

    /**
     * Returns the object instance of {@link #configData} parsed from the file which is saved by {@link #configToFileString()}
     * @param json The String data from the file.
     * @return The object instance.
     */
    @Override
    protected T parseConfigFromData(@NonNull List<String> json) {
        StringBuilder jsonString = new StringBuilder();

        for (String s : json) jsonString.append(s);

        if (loadSettings == null) defaultLoadInit();

        return (T) loadSettings.loadFromString(jsonString.toString());
    }

}
