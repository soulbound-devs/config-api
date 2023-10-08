package net.vakror.jamesconfig.config.config.individual;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

public abstract class IndividualFileConfig<P> extends Config<P> {
    private Gson GSON;


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
        if (!overrideCurrent) {
            JamesConfigMod.LOGGER.info("Reading configs: " + this.getName());
            File[] configFiles = this.getConfigDir().listFiles(File::isFile);
            if (configFiles != null && configFiles.length != 0) {
                for (File file : configFiles) {
                    try (FileReader reader = new FileReader(file)) {
                        P object = GSON.fromJson(reader, getConfigObjectClass());
                        this.add(object);
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
    }

    @Override
    public abstract void add(P object);

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public abstract Map<Type, Object> getTypeAdapters();

    @Override
    public abstract List<P> getObjects();

    public abstract String getFileName(P object);

    @Override
    protected abstract void resetToDefault();

    public abstract Class<P> getConfigObjectClass();

    @Override
    public void writeConfig() {
        File cfgDir = this.getConfigDir();
        if (!cfgDir.exists() && !cfgDir.mkdirs()) {
            return;
        }
        for (P object : getObjects()) {
            try {
                FileWriter writer = new FileWriter(getConfigFile(getFileName(object).replaceAll(" ", "_").replaceAll("[^A-Za-z0-9_]", "").toLowerCase()));
                GSON.toJson(object, writer);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
