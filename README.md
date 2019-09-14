# Fern ConfigManager
This is a config manager that allows you to use various data managers such as GSON, fast json and hopefully more to come.'

## Download
[![](https://jitpack.io/v/Fernthedev/fern-configmanager.svg)](https://jitpack.io/#Fernthedev/fern-configmanager)
### Gradle
```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
```gradle
dependencies {
        implementation 'com.github.Fernthedev:fern-configmanager:1.0.2' // Check github releases tag for the version.
}
```

### Maven
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
  ```
  ```xml
  <dependency>
    <groupId>com.github.Fernthedev</groupId>
    <artifactId>fern-configmanager</artifactId>
    <version>1.0.2</version> <!-- Check github releases tag for the version. -->
</dependency>
  ```
## Usage
This is the GSON usage but the FastJSON is the same except the different name of course.
```java
GsonConfig config = new GsonConfig<>(new ConfigClass(), new File("file.anyextension"); // Calls load() on constructor. If file doesn't exist on load(), it calls save();
config.load(); // To load
config.save(); // To save
```
You can also make your own config manager by extending the class Config<T> // Use the GsonConfig<T> as an example
