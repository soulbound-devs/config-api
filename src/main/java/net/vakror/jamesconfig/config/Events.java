package net.vakror.jamesconfig.config;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.event.RegisterConfigManagersEvent;
import net.vakror.jamesconfig.config.manager.NoAdapterConfigManager;
import net.vakror.jamesconfig.config.manager.SimpleConfigManager;

public class Events {
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = JamesConfigMod.MOD_ID)
    public static class ModEvents {
        @SubscribeEvent
        public static void registerSimpleManager(RegisterConfigManagersEvent event) {
            event.addManager(SimpleConfigManager.INSTANCE);
            event.addManager(NoAdapterConfigManager.INSTANCE);
        }
    }
}
