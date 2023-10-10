package net.vakror.jamesconfig.config.manager;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.Config;
import net.vakror.jamesconfig.config.event.ConfigRegisterEvent;
import net.vakror.jamesconfig.config.event.GetConfigTypeAdaptersEvent;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public abstract class ConfigManager {

    public void register() {
        MinecraftForge.EVENT_BUS.register(new ModEvents(this));
    }

    public abstract Map<Config<?>, List<Pair<Type, Object>>> getAllConfigs();

    @Mod.EventBusSubscriber(modid = JamesConfigMod.MOD_ID)
    public static class ModEvents {
        private final ConfigManager manager;
        private ModEvents(ConfigManager manager) {
            this.manager = manager;
        }
        @SubscribeEvent
        public void registerAllConfigs(ConfigRegisterEvent event) {
            manager.getAllConfigs().keySet().forEach((config) -> event.register(config));
        }

        @SubscribeEvent
        public void registerAllConfigAdapters(GetConfigTypeAdaptersEvent event) {
            manager.getAllConfigs().forEach((config, adapterArray) -> {
                if (event.getConfigName().equals(config.getName())) {
                    for (Pair<Type, Object> adapter : adapterArray) {
                        event.addAdapter(adapter.getFirst(), adapter.getSecond());
                    }
                }
            });
        }
    }
}
