package net.vakror.jamesconfig.config.example;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.adapter.SimpleCodecAdapter;
import net.vakror.jamesconfig.config.example.configs.ExampleConfig;
import net.vakror.jamesconfig.config.example.configs.StringWithContents;
import net.vakror.jamesconfig.config.event.ConfigRegisterEvent;
import net.vakror.jamesconfig.config.event.GetConfigTypeAdaptersEvent;

public class Events {

    @Mod.EventBusSubscriber(modid = JamesConfigMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {
        @SubscribeEvent
        public static void onGetConfigTypeAdapters(GetConfigTypeAdaptersEvent event) {
            if (event.getConfigName().equals(ExampleConfig.NAME)) {
                event.addAdapter(StringWithContents.class, new SimpleCodecAdapter<>(StringWithContents.CODEC));
            }
        }

        @SubscribeEvent
        public static void onConfigsRegister(ConfigRegisterEvent event) {
            event.register(new ExampleConfig());
        }
    }

    @Mod.EventBusSubscriber(modid = JamesConfigMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onGetConfigTypeAdapters(EntityJoinLevelEvent event) {
            if (!event.getLevel().isClientSide && event.getEntity() instanceof Player player && !FMLEnvironment.production) {
                ExampleConfig.STRINGS.forEach((stringWithContents -> {
                        player.sendSystemMessage(Component.literal(stringWithContents.getName() + ":"));
                        player.sendSystemMessage(Component.literal("    " + stringWithContents.getContent()));
                }));
            }
        }
    }
}