package net.vakror.jamesconfig.config.manager;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.Config;
import net.vakror.jamesconfig.config.event.ConfigRegisterEvent;
import net.vakror.jamesconfig.config.event.GetConfigTypeAdaptersEvent;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public abstract class ConfigManager {

    public abstract void addConfig(Config<?> config, Pair<Type, Object>... adapters);
    public abstract void addConfig(Config<?> config, Type type, Object adaptersArray);
    public abstract void addCodecConfig(Config<?> config, Pair<Type, Codec<?>>... codecArray);
    public abstract void addCodecConfig(Config<?> config, Type type, Codec<?> adaptersArray);

    public void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(new ModEvents(this));
    }

    public abstract Map<Config<?>, List<Pair<Type, Object>>> getAllConfigs();

    @Mod.EventBusSubscriber(modid = JamesConfigMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
            manager.getAllConfigs().values().forEach((adapterArray) -> {
                for (Pair<Type, Object> adapter : adapterArray) {
                    event.addAdapter(adapter.getFirst(), adapter.getSecond());
                }
            });
        }
    }
}
