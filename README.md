# fern-configmanager
This is a config manager that allows you to use various data managers such as GSON, fast json and hopefully more to come.'

This is the GSON usage but the FastJSON is the same except the different name of course.
```java
GsonConfig config = new GsonConfig<>(new ConfigClass(), new File("file.anyextension"); // Calls load() on constructor. If file doesn't exist on load(), it calls save();
config.load(); // To load
config.save(); // To save
```
You can also make your own config manager by extending the class Config<T> // Use the GsonConfig<T> as an example
