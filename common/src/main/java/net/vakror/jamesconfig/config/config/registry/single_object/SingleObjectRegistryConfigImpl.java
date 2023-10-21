package net.vakror.jamesconfig.config.config.registry.single_object;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
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
import java.util.List;

public abstract class SingleObjectRegistryConfigImpl<P extends ConfigObject> extends Config {

    public final List<P> objects = new ArrayList<>();

    @Override
    public void generateConfig() {
        this.clear();
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
        readConfig(false, true);
    }

    public abstract boolean isValueAcceptable(P value);

    public abstract boolean shouldDiscardConfigOnUnacceptableValue();

    public abstract void invalidate();

    public abstract boolean isValid();

    private File currentFile = null;

    public void readConfig(boolean overrideCurrent, boolean shouldCallAgain) {
        if (shouldReadConfig()) {
            if (!overrideCurrent) {
                JamesConfigMod.LOGGER.info("Reading configs: " + this.getName());
                File[] configFiles = this.getConfigDir().listFiles(File::isFile);
                if (configFiles != null && configFiles.length != 0) {
                    for (File file : configFiles) {
                        try (FileReader reader = new FileReader(file)) {
                            JamesConfigMod.LOGGER.info("Reading config object {} for config {}", this, file.getName());
                            JsonObject jsonObject = (JsonObject) new JsonParser().parse(reader);
                            currentFile = file;
                            List<P> configObjects = parse(jsonObject).stream().map((object -> (P) object)).toList();
                            for (ConfigObject object : configObjects) {
                                if (object != null) {
                                    add(object);
                                }
                            }
                            JamesConfigMod.LOGGER.info("Finished reading config object {}", file.getName());
                        } catch (Exception e) {
                            System.out.println(e.getClass());
                            e.printStackTrace();
                            JamesConfigMod.LOGGER.warn("Error with object {} in config {}, generating new", file.getName(), this);
                            this.generateConfig();
                            if (shouldCallAgain) {
                                this.objects.clear();
                                readConfig(false, false);
                            }
                        }
                    }
                    currentFile = null;
                } else {
                    this.generateConfig();
                    if (shouldCallAgain) {
                        this.objects.clear();
                        readConfig(false, false);
                    }
                    JamesConfigMod.LOGGER.warn("Config " + this.getName() + "not found, generating new");
                }
                JamesConfigMod.LOGGER.info("Finished reading config");
            } else {
                this.generateConfig();
                if (shouldCallAgain) {
                    this.objects.clear();
                    readConfig(false, false);
                }
                JamesConfigMod.LOGGER.info("Successfully Overwrote config {}", this);
            }
            if (!this.isValid()) {
                JamesConfigMod.LOGGER.error("Config {} was found to be invalid, discarding all values", this.getName());
                this.clear();
            }
        }
    }

    public abstract P decode(JsonObject object);

    @Override
    public List<ConfigObject> parse(JsonObject jsonObject) {
        P object = null;
        try {
            object = decode(jsonObject);
        } catch (Exception e) {
            JamesConfigMod.LOGGER.error("Error Parsing Config {}", this.getName().toString());
            e.printStackTrace();
        }
        if (object != null) {
            if (shouldAddObject(object)) {
                if (this.isValueAcceptable(object)) {
                    return List.of(object);
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
        return null;
    }

    @Override
    public List<ConfigObject> getAll() {
        return objects.stream().map((object) -> (ConfigObject) object).toList();
    }

    public abstract void discardValue(P object);

    public abstract boolean shouldAddObject(P object);

    protected abstract void resetToDefault();

    @Override
    public void writeConfig() {
        JamesConfigMod.LOGGER.info("Writing config {}", this);
        File cfgDir = this.getConfigDir();
        if (!cfgDir.exists()) {
            if (!cfgDir.mkdirs()) {
                JamesConfigMod.LOGGER.error("Failed to create config directory {}", cfgDir.getPath());
                return;
            }
            JamesConfigMod.LOGGER.info("Finished creating config directory {}", cfgDir.getPath());
        }
        for (ConfigObject object : getAll()) {
            JamesConfigMod.LOGGER.info("Attempting to write config object {} for config {}", object.getName(), this);
            try (
                    FileWriter writer = new FileWriter(getConfigFile(object.getName().replaceAll(" ", "_").replaceAll("[^A-Za-z0-9_]", "").toLowerCase()));
                    JsonWriter jsonWriter = new JsonWriter(writer)) {
                jsonWriter.setIndent("    ");
                jsonWriter.setSerializeNulls(true);
                jsonWriter.setLenient(true);
                JsonElement element = object.serialize();
                if (!(element instanceof JsonObject)) {
                    JamesConfigMod.LOGGER.error("Config object {} in config {} does not have a json object at root, skipping write", object.getName(), this);
                } else {
                    Streams.write(element, jsonWriter);
                    writer.flush();
                }
                JamesConfigMod.LOGGER.info("Successfully wrote config object {} for config {}", object.getName(), this);
            } catch (IOException e) {
                JamesConfigMod.LOGGER.error("Failed to write config object {} for config {}", object.getName(), this);
                e.printStackTrace();
            }
        }
        JamesConfigMod.LOGGER.info("Finished writing config");
    }

    @Override
    public List<JsonObject> serialize() {
        JamesConfigMod.LOGGER.info("Writing config {} to network", this);
        List<JsonObject> jsonObject = new ArrayList<>();
        for (ConfigObject object : getAll()) {
            try {
                JamesConfigMod.LOGGER.info("Writing config object {} in config {} to network", object.getName(), this);
                JsonElement element = object.serialize();
                if (!(element instanceof JsonObject)) {
                    JamesConfigMod.LOGGER.error("Config object {} in config {} does not have a json object at root, skipping write", object.getName(), this);
                } else {
                    jsonObject.add((JsonObject) element);
                    JamesConfigMod.LOGGER.info("Finished writing config object {} in config {} to network", object.getName(), this);
                }
            } catch (Exception e) {
                JamesConfigMod.LOGGER.error("Failed to write config object {} in config {} to network", object.getName(), this);
                e.printStackTrace();
            }
        }
        JamesConfigMod.LOGGER.info("Finished writing config {} to network", this);
        return jsonObject;
    }
}
