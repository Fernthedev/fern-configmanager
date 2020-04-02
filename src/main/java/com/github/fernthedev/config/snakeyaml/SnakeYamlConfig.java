package com.github.fernthedev.config.snakeyaml;

import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.config.common.exceptions.ConfigLoadException;
import lombok.NonNull;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.List;

/**
 *
 * @param <T> The data type
 */
public class SnakeYamlConfig<T> extends Config<T> {
    private static final Yaml yaml = new Yaml();

    public SnakeYamlConfig(@NonNull T gsonConfigData, @NonNull File file) throws ConfigLoadException {
        super(gsonConfigData, file);
    }

    /**
     * Should return a String representation of the file {@link #configData}. This string representation should be the way that it is read in {@link #parseConfigFromData(List)}
     * @return String representation of {@link #configData} that is read by {@link #parseConfigFromData(List)}
     */
    @Override
    protected String configToFileString() {

//        Map<String, Object> valueMap = toKeyMap(configData);

        return yaml.dump(configData);
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

        return yaml.loadAs(jsonString.toString(), tClass);
    }

}
