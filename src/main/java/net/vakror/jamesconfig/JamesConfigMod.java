package net.vakror.jamesconfig;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vakror.jamesconfig.config.Config;
import net.vakror.jamesconfig.config.event.ConfigRegisterEvent;
import net.vakror.jamesconfig.config.example.Events;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(JamesConfigMod.MOD_ID)
public class JamesConfigMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "jamesconfig";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Map<ResourceLocation, Config<?>> CONFIGS = new HashMap<>();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public JamesConfigMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, Events.ModEvents::onConfigsRegister);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, Events.ModEvents::onGetConfigTypeAdapters);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void registerConfig(ResourceLocation config, boolean replace) {
        CONFIGS.get(config).readConfig(replace);
    }

    public static void registerAllConfigs(boolean replace) {
        CONFIGS.forEach((name, register) -> register.readConfig(replace));
    }

    public static void addConfig(ResourceLocation name, Config<?> register) {
        CONFIGS.put(name, register);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ConfigRegisterEvent event1 = new ConfigRegisterEvent();
            FMLJavaModLoadingContext.get().getModEventBus().post(event1);
            event1.getConfigs().forEach((JamesConfigMod::addConfig));
            registerAllConfigs(false);
        });
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
