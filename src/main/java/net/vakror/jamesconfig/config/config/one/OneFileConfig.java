package net.vakror.jamesconfig.config.config.one;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.adapter.CompoundTagAdapter;
import net.vakror.jamesconfig.config.config.Config;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public abstract class OneFileConfig<P> extends Config<P> {
    private Gson GSON;
    public final Codec<P> codec;

    public OneFileConfig(Codec<P> codec) {
        this.codec = codec;
    }

    public void setGSON() {
        GsonBuilder builder = new GsonBuilder().enableComplexMapKeySerialization()
                .registerTypeAdapter(CompoundTag.class, CompoundTagAdapter.INSTANCE);

        getTypeAdapters().forEach(builder::registerTypeAdapter);

        GSON = builder.setPrettyPrinting().create();
    }

    @Override
    public void generateConfig() {
        this.resetToDefault();
        this.writeConfig();
    }

    @Override
    @NotNull
    public File getConfigDir() {
        File configDir;
        configDir = FMLPaths.CONFIGDIR.get().resolve(getSubPath()).toFile();
        return configDir;
    }

    @Override
    @NotNull
    public File getConfigFile(String fileName) {
        File configDir;
        configDir = FMLPaths.CONFIGDIR.get().resolve(getSubPath() + "/" + fileName + ".json").toFile();
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
                    File file = getConfigFile(getFileName().replaceAll(" ", "_").replaceAll("[^A-Za-z0-9_]", "").toLowerCase());
                    try (FileReader reader = new FileReader(file)) {
                        JsonElement element = JsonParser.parseReader(reader);
                        List<P> list = codec.listOf().decode(JsonOps.INSTANCE, element).resultOrPartial((s) -> {
                            throw new IllegalStateException(s);
                        }).get().getFirst();
                        for (P object : list) {
                            if (shouldAddObject(object)) {
                                if (isValueAcceptable(object)) {
                                    this.add(object);
                                    this.onAddObject(object);
                                } else {
                                    if (shouldDiscardConfigOnUnacceptableValue()) {
                                        JamesConfigMod.LOGGER.error("Discarding config because value {} is unacceptable", getName(object));
                                        this.invalidate();
                                    } else {
                                        JamesConfigMod.LOGGER.error("Discarding unacceptable value {} in config {}", getName(object), getName());
                                        this.discardValue(object);
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
    public abstract void add(P object);

    @Override
    public abstract Map<Type, Object> getTypeAdapters();

    @Override
    public abstract List<P> getObjects();

    public abstract String getFileName();

    @Override
    protected abstract void resetToDefault();

    @Override
    public void writeConfig() {
        File cfgDir = this.getConfigDir();
        if (!cfgDir.exists() && !cfgDir.mkdirs()) {
            return;
        }
        try {
            FileWriter writer = new FileWriter(getConfigFile(getFileName().replaceAll(" ", "_").replaceAll("[^A-Za-z0-9_]", "").toLowerCase()));
            GSON.toJson(codec.listOf().encodeStart(JsonOps.INSTANCE, getObjects()).resultOrPartial(s -> {throw new IllegalStateException(s);}).get(), writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
