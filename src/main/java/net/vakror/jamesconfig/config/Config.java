package net.vakror.jamesconfig.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.adapter.CompoundTagAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public abstract class Config<P> {
    private Gson GSON;


    public void setGSON() {
        GsonBuilder builder = new GsonBuilder().enableComplexMapKeySerialization()
                .registerTypeAdapter(CompoundTag.class, CompoundTagAdapter.INSTANCE);

        getTypeAdapters().forEach(builder::registerTypeAdapter);

        GSON = builder.setPrettyPrinting().create();
    }
    public void generateConfig() {
        this.resetToDefault();
        try {
            this.writeConfig();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    private File getConfigDir() {
        File configDir;
        configDir = FMLPaths.CONFIGDIR.get().resolve(getSubPath()).toFile();
        return configDir;
    }

    @NotNull
    private File getConfigFile(String fileName) {
        File configDir;
        configDir = FMLPaths.CONFIGDIR.get().resolve(getSubPath() + "/" + fileName + ".json").toFile();
        return configDir;
    }

    public abstract String getSubPath();

    public abstract ResourceLocation getName();

    public String toString() {
        return this.getName().toString();
    }

    public void readConfig(boolean overrideCurrent) {
        if (!overrideCurrent) {
            JamesConfigMod.LOGGER.info("Reading configs: " + this.getName());
            File[] configFiles = this.getConfigDir().listFiles(File::isFile);
            if (configFiles != null && configFiles.length != 0) {
                for (File file : configFiles) {
                    try (FileReader reader = new FileReader(file)) {
                        P object = GSON.fromJson(reader, getConfigClass());
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

    public abstract void add(P object);

    protected boolean isValid() {
        return true;
    }

    public static boolean checkAllFieldsAreNotNull(Object o) throws IllegalAccessException {
        for (Field v : o.getClass().getDeclaredFields()) {
            boolean b;
            if (!v.canAccess(o)) continue;
            Object field = v.get(o);
            if (field == null) {
                return false;
            }
            if (field.getClass().isPrimitive() || (b = Config.checkAllFieldsAreNotNull(field))) continue;
            return false;
        }
        return true;
    }

    public abstract Map<Type, Object> getTypeAdapters();

    public abstract List<P> getObjects();

    public abstract String getFileName(P object);

    protected abstract void resetToDefault();

    public abstract Class<P> getConfigClass();

    public void writeConfig() throws IOException {
        File cfgDIr = this.getConfigDir();
        if (!cfgDIr.exists() && !cfgDIr.mkdirs()) {
            return;
        }
        for (P object : getObjects()) {
            FileWriter writer = new FileWriter(getConfigFile(getFileName(object).replaceAll(" ", "_").replaceAll("[^A-Za-z0-9_]", "").toLowerCase()));
            GSON.toJson(object, writer);
            writer.flush();
            writer.close();
        }
    }
}

