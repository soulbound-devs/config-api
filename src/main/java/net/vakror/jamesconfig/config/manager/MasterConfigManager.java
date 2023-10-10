package net.vakror.jamesconfig.config.manager;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vakror.jamesconfig.config.event.RegisterConfigManagersEvent;

import java.util.List;

public class MasterConfigManager {
    public static void register() {
        getAllManagers().forEach(ConfigManager::register);
    }

    public static List<ConfigManager> getAllManagers() {
        RegisterConfigManagersEvent event = new RegisterConfigManagersEvent();
        MinecraftForge.EVENT_BUS.post(event);
        return event.getManagers();
    }
}
