package net.vakror.jamesconfig;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.Config;
import net.vakror.jamesconfig.config.event.ConfigEvents;
import net.vakror.jamesconfig.config.event.ConfigRegisterEvent;
import net.vakror.jamesconfig.config.example.ExampleConfigs;
import net.vakror.jamesconfig.config.manager.MasterConfigManager;
import net.vakror.jamesconfig.config.manager.NoAdapterConfigManager;
import net.vakror.jamesconfig.config.manager.SimpleConfigManager;
import net.vakror.jamesconfig.config.packet.ArchModPackets;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class JamesConfigMod
{
	// Define mod id in a common place for everything to reference
	public static final String MOD_ID = "jamesconfig";

	public static void init() {
		new JamesConfigMod();
	}
	// Directly reference a slf4j logger
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final Map<ResourceLocation, Config<?>> CONFIGS = new HashMap<>();
	public static final Map<ResourceLocation, Codec<? extends Config<?>>> CODECS = new HashMap<ResourceLocation, com.mojang.serialization.Codec<? extends Config<?>>>();

	public JamesConfigMod()
	{
		ConfigEvents.REGISTER_CONFIG_MANAGERS.register(event -> {
			event.addManager(SimpleConfigManager.INSTANCE);
			event.addManager(NoAdapterConfigManager.INSTANCE);
			return EventResult.pass();
		});

		PlayerEvent.PLAYER_JOIN.register(ArchModPackets::onLogIn);
		PlayerEvent.CHANGE_DIMENSION.register(((player, oldLevel, newLevel) -> {

		}));
		//MinecraftForge.EVENT_BUS.addListener(net.vakror.jamesconfig.config.example.Events.ForgeEvents::onGetConfigTypeAdapters);

		//Call this in mod constructor or anywhere before commonsetup fires
		ExampleConfigs.addExampleConfig();

		MasterConfigManager.register();

		LifecycleEvent.SETUP.register(this::commonSetup);
		ClientLifecycleEvent.CLIENT_SETUP.register((minecraft)-> ArchModPackets.register());
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

	public static void addCodec(ResourceLocation name, Config<?> register) {
		CODECS.put(name, register.getCodec());
	}

	private void commonSetup() {
		ConfigRegisterEvent configRegisterEvent = new ConfigRegisterEvent();
		ConfigEvents.CONFIG_REGISTER_EVENT.invoker().post(configRegisterEvent);
		configRegisterEvent.getConfigs().forEach((JamesConfigMod::addConfig));
		configRegisterEvent.getConfigs().forEach((JamesConfigMod::addCodec));
		registerAllConfigs(false);
	}
}
