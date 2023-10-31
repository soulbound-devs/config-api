package net.vakror.jamesconfig.config.config.setting;

import com.google.common.base.Stopwatch;
import dev.architectury.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.ConfigObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.vakror.jamesconfig.config.config.object.default_objects.setting.SimpleSettingConfigObject.getRequiredSettingsAsString;

public abstract class SimpleSettingConfigImpl extends SettingConfigImpl {

    private final String subPath;
    private final ResourceLocation name;
    private final String fileName;

    public final Map<String, ConfigObject> values = new HashMap<>(getRequiredSettings().size());
    private final String[] requiredMods;

    public SimpleSettingConfigImpl(String subPath, ResourceLocation name) {
        this.subPath = subPath;
        this.name = name;
        this.fileName = name.getPath();
        requiredMods = new String[0];
    }

    public SimpleSettingConfigImpl(String subPath, ResourceLocation name, String... requiredMods) {
        this.subPath = subPath;
        this.name = name;
        this.fileName = name.getPath();
        this.requiredMods = requiredMods;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setValue(String name, ConfigObject value) {
        if (name == null) name = value.getName();
        if (!requiredSettingsMap.containsKey(name)) {
            JamesConfigMod.LOGGER.error("Attempted to add setting \"{}\" which is not found in required values, not adding value. Required values are: {}", name, allRequiredKeys());
        } else {
            values.put(name, value);
        }
    }

    private String allRequiredKeys() {
        return getRequiredSettingsAsString(requiredSettingsMap);
    }

    @Override
    public void clear() {
        JamesConfigMod.LOGGER.info("Clearing config");
        values.clear();
        JamesConfigMod.LOGGER.info("Finished Clearing config");
    }

    @Override
    public List<ConfigObject> getAll() {
        return values.values().stream().toList();
    }

    @Override
    public void add(ConfigObject object) {
        this.setValue(object.getName(), object);
    }

    @Override
    public boolean shouldClearBeforeSync() {
        return true;
    }
    
    @Override
    public String getSubPath() {
        return subPath;
    }

    @Override
    public ResourceLocation getName() {
        return name;
    }

    @Override
    public final boolean shouldReadConfig() {
        for (String requiredMod : requiredMods) {
            if (!Platform.isModLoaded(requiredMod)) {
                JamesConfigMod.LOGGER.error("Mod \"" + requiredMod + "\" is not present but required by config \"" + this + "\", skipping read");
                return false;
            }
        }
        return shouldLoad();
    }

    public boolean shouldLoad() {
        return true;
    }
}

