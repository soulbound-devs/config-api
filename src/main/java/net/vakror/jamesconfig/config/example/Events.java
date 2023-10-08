package net.vakror.jamesconfig.config.example;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.example.configs.ExampleIndividualFileConfig;
import net.vakror.jamesconfig.config.example.configs.ExampleOneFileConfig;

public class Events {

    @Mod.EventBusSubscriber(modid = JamesConfigMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onGetConfigTypeAdapters(EntityJoinLevelEvent event) {
            if (event.getEntity() instanceof Player player && !FMLEnvironment.production) {
                ExampleIndividualFileConfig.STRINGS.forEach((stringWithContents -> {
                    player.sendSystemMessage(Component.literal(stringWithContents.getName() + ":"));
                    player.sendSystemMessage(Component.literal("    " + stringWithContents.getContent()));
                }));
                ExampleOneFileConfig.INSTANCE.STRINGS.forEach((stringWithContents -> {
                    player.sendSystemMessage(Component.literal(stringWithContents.getName() + ":"));
                    player.sendSystemMessage(Component.literal("    " + stringWithContents.getContent()));
                }));
            }
        }
    }
}