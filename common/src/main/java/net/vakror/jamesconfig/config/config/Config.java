package net.vakror.jamesconfig.config.config;

import com.google.gson.JsonObject;
import dev.architectury.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import net.vakror.jamesconfig.config.config.performance.DefaultConfigPerformanceAnalyzer;
import net.vakror.jamesconfig.config.config.performance.IConfigPerformanceAnalyzer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.List;

public abstract class Config {
    public abstract void generateDefaultConfig();

    @NotNull
    public abstract File getConfigDir();

    public abstract String getSubPath();

    public abstract ResourceLocation getName();

    @Override
    public String toString() {
        return this.getName().toString();
    }

    public abstract void readConfig(boolean overrideCurrent);

    public IConfigPerformanceAnalyzer getConfigAnalyzer() {
        return DefaultConfigPerformanceAnalyzer.INSTANCE;
    }

    public final void analyzeConfigPerformance() {
        Path path = Platform.getGameFolder().resolve("config performance");
        File directory = path.toFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IllegalStateException("Cannot create config analysis directory!");
        }
        File thisConfigFile = path.resolve(this.getName().toString()).toFile();
        try {
            if (thisConfigFile.exists()) {
                thisConfigFile.delete();
            }
            thisConfigFile.createNewFile();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(thisConfigFile)) {
            writer.append(getConfigAnalyzer().getPerformanceAnalysis(this));
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract boolean shouldReadConfig();

    public abstract void writeConfig();

    public abstract List<JsonObject> serialize();

    /**
     *
     * @return whether to clear this config before it is synced. Recommended for all non-clientside configs
     */
    public abstract boolean shouldClearBeforeSync();

    /**
     * will not do anything if config is clientside
     * @return whether to sync
     */
    public boolean shouldSync() {
        return true;
    }

    public abstract void add(ConfigObject object);

    public abstract List<ConfigObject> getAll();

    public abstract List<ConfigObject> parse(JsonObject jsonObject);

    public abstract void clear();
}
