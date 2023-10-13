package net.vakror.jamesconfig.config.config.registry;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import dev.architectury.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.Config;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class RegistryConfigImpl extends Config {

    public List<ConfigObject> objects = new ArrayList<>();

    @Override
    public void generateConfig() {
        this.resetToDefault();
        this.writeConfig();
    }

    @Override
    @NotNull
    public File getConfigDir() {
        File configDir;
        configDir = Platform.getConfigFolder().resolve(getSubPath()).toFile();
        return configDir;
    }

    @Override
    @NotNull
    public File getConfigFile(String fileName) {
        File configDir;
        configDir = Platform.getConfigFolder().resolve(getSubPath() + "/" + fileName + ".json").toFile();
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

    @Override
    public void readConfig(boolean overrideCurrent) {
        if (shouldReadConfig()) {
            if (!overrideCurrent) {
                JamesConfigMod.LOGGER.info("Reading configs: " + this.getName());
                File[] configFiles = this.getConfigDir().listFiles(File::isFile);
                if (configFiles != null && configFiles.length != 0) {
                    for (File file : configFiles) {
                        try (FileReader reader = new FileReader(file)) {
                            JsonObject jsonObject = (JsonObject) JsonParser.parseReader(reader);
                            if (!jsonObject.has("type") || !jsonObject.get("type").isJsonPrimitive() || !jsonObject.getAsJsonPrimitive("type").isString()) {
                                JamesConfigMod.LOGGER.error("Config object {} either does not contain a type field, or the type field is not a string", file.getName());
                            } else {
                                ConfigObject object = ConfigObject.deserializeUnknown(jsonObject);
                                if (object != null) {
                                    if (shouldAddObject(object)) {
                                        if (this.isValueAcceptable(object)) {
                                            this.add(object);
                                            this.onAddObject(object);
                                        } else {
                                            if (shouldDiscardConfigOnUnacceptableValue()) {
                                                JamesConfigMod.LOGGER.error("Discarding config because value {} is unacceptable", object.getName());
                                                this.invalidate();
                                            } else {
                                                JamesConfigMod.LOGGER.error("Discarding unacceptable value {} in config {}", object.getName(), getName());
                                                this.discardValue(object);
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (IOException e) {
                            System.out.println(e.getClass());
                            e.printStackTrace();
                            JamesConfigMod.LOGGER.warn("Error with config {}, generating new", this);
                            this.generateConfig();
                        }
                    }
                } else {
                    this.generateConfig();
                    JamesConfigMod.LOGGER.warn("Config " + this.getName() + "not found, generating new");
                }
            } else {
                this.generateConfig();
                JamesConfigMod.LOGGER.info("Successfully Overwrote Config: " + this.getName());
            }
            if (!this.isValid()) {
                JamesConfigMod.LOGGER.error("Config {} was found to be invalid, discarding all values", this.getName());
                this.discardAllValues();
            }
        }
    }

    @Override
    public Multimap<ResourceLocation, ConfigObject> getObjects() {
        Multimap<ResourceLocation, ConfigObject> map = Multimaps.newMultimap(new HashMap<>(), ArrayList::new);
        for (ConfigObject object : objects) {
            map.put(getName(), object);
        }
        return map;
    }

    @Override
    protected abstract void resetToDefault();

    @Override
    public void writeConfig() {
        File cfgDir = this.getConfigDir();
        if (!cfgDir.exists() && !cfgDir.mkdirs()) {
            return;
        }
        for (ConfigObject object : getObjects().values()) {
            try(
                    FileWriter writer = new FileWriter(getConfigFile(object.getName().replaceAll(" ", "_").replaceAll("[^A-Za-z0-9_]", "").toLowerCase()));
                    JsonWriter jsonWriter = new JsonWriter(writer)) {
                jsonWriter.setIndent("    ");
                jsonWriter.setSerializeNulls(true);
                jsonWriter.setLenient(true);
                Streams.write(object.serialize(), jsonWriter);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
