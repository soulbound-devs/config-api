package net.vakror.jamesconfig.config.config.setting;

import com.google.common.base.Stopwatch;
import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import dev.architectury.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.Config;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import net.vakror.jamesconfig.config.config.object.default_objects.setting.SettingConfigObject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class SettingConfigImpl extends Config {

    @Override
    public void generateConfig() {
        this.clear();
        this.getDefaultValues().forEach(this::setValue);
        this.writeConfig();
    }

    @Override
    @NotNull
    public File getConfigDir() {
        File configDir;
        configDir = Platform.getConfigFolder().resolve(getSubPath()).toFile();
        return configDir;
    }

    @NotNull
    public File getConfigFile() {
        File configDir;
        configDir = Platform.getConfigFolder().resolve(getSubPath() + "/" + getFileName() + ".json").toFile();
        return configDir;
    }

    @Override
    public abstract String getSubPath();

    @Override
    public abstract ResourceLocation getName();

    @Override
    public String toString() {
        return this.getName().toString();
    }

    /**
     * contents of config object do not matter
     */
    public abstract Map<String, ConfigObject> getRequiredSettings();

    public abstract Map<String, ConfigObject> getDefaultValues();

    public abstract String getFileName();

    @Override
    public void readConfig(boolean overrideCurrent) {
        if (shouldReadConfig()) {
            if (!overrideCurrent) {
                Stopwatch stopwatch = Stopwatch.createStarted();
                JamesConfigMod.LOGGER.info("Reading config: " + this.getName());
                try {
                    File file = getConfigFile();
                    if (file.exists()) {
                        try (FileReader reader = new FileReader(file)) {
                            JsonObject jsonObject = (JsonObject) JsonParser.parseReader(reader);
                            List<ConfigObject> configObjects = parse(jsonObject);
                            for (ConfigObject object : configObjects) {
                                setValue(object.getName(), object);
                            }
                        } catch (IOException e) {
                            System.out.println(e.getClass());
                            e.printStackTrace();
                            JamesConfigMod.LOGGER.warn("Error with config {}, generating new", this);
                            this.generateConfig();
                        }
                    } else {
                        this.generateConfig();
                        JamesConfigMod.LOGGER.warn("Config " + this.getName() + "not found, generating new");
                    }
                } catch (JsonIOException | JsonSyntaxException e) {
                    throw new IllegalStateException(e);
                }
                JamesConfigMod.LOGGER.info("Finished reading config, \033[0;31mTook {}\033[0;0m", stopwatch);
            } else {
                this.generateConfig();
                JamesConfigMod.LOGGER.info("Successfully Overwrote Config: " + this.getName());
            }
        }
    }

    @Override
    public List<ConfigObject> parse(JsonObject jsonObject) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        JamesConfigMod.LOGGER.info("Parsing config: " + this.getName());
        List<ConfigObject> list = new ArrayList<>();
        for (String key : jsonObject.keySet()) {
            if (!getRequiredSettings().containsKey(key)) {
                JamesConfigMod.LOGGER.error("Key {} present in config {}, even though it was not requested", key, getFileName());
            } else {
                ConfigObject object = getRequiredSettings().get(key);
                if (object instanceof SettingConfigObject settingConfigObject) {
                    if (!jsonObject.get(key).isJsonObject()) {
                        JamesConfigMod.LOGGER.error("Config setting definition {} in config {} is not json object", key, getName());
                    }
                    ConfigObject object1 = settingConfigObject.deserializeSettingValues(key, (JsonObject) jsonObject.get(key), getName().toString());
                    object1.setName(key);
                    list.add(object1);
                } else if (object != null) {
                    ConfigObject object1 = object.deserialize(key, jsonObject.get(key));
                    object1.setName(key);
                    list.add(object1);
                }
            }
        }
        JamesConfigMod.LOGGER.info("Finished parsing config, \033[0;31mTook {}\033[0;0m", stopwatch);
        return list;
    }

    public abstract void setValue(String name, ConfigObject value);

    @Override
    public void writeConfig() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        JamesConfigMod.LOGGER.info("Writing config: " + this.getName());
        File cfgDir = this.getConfigDir();
        if (!cfgDir.exists() && !cfgDir.mkdirs()) {
            return;
        }
        try (
                FileWriter writer = new FileWriter(getConfigFile());
                JsonWriter jsonWriter = new JsonWriter(writer)) {


            JsonObject object = new JsonObject();
            for (ConfigObject configObject : getAll()) {
                object.add(configObject.getName(), configObject.serialize());
            }

            jsonWriter.setIndent("    ");
            jsonWriter.setSerializeNulls(true);
            jsonWriter.setLenient(true);
            Streams.write(object, jsonWriter);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JamesConfigMod.LOGGER.info("Finished writing config, \033[0;31mTook {}\033[0;0m", stopwatch);
    }

    public List<JsonObject> serialize() {
        JsonObject object = new JsonObject();
        for (ConfigObject configObject : getAll()) {
            object.add(configObject.getName(), configObject.serialize());
        }
        return List.of(object);
    }
}
