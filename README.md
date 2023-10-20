# Depending on using maven

## Adding The Repository

I am using modrinth maven to host these files (too lazy to set up a proper maven repo lol). To add it, add this to your `repositories` block in your `build.gradle`:

```gradle
repositories {
    maven {
        name = "Modrinth Maven"
        url = "https://api.modrinth.com/maven"
    }
}
```

## Adding the dependency

To add the dependency, add this to the `dependencies` block in your `build.gradle`:

```gradle
dependencies {
    implementation(fg.deobf("maven.modrinth:config-lib:VERSION-MODLOADER"))
}
```

Version is the version of the lib (e.g 1.2) and loader is the loader (for fabric and quilt, this is fabric, for forge and neoforge, this is forge)

# Making Your first config

To make your first config, look at the `common/src/main/java/net/vakror/jamesconfig/config/example` directory. this is where examples are placed.
There are examples for both registry config and setting configs.
