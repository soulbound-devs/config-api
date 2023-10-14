package net.vakror.jamesconfig;

import com.google.common.base.Stopwatch;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.Config;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import net.vakror.jamesconfig.config.event.ConfigEvents;
import net.vakror.jamesconfig.config.event.ConfigObjectRegisterEvent;
import net.vakror.jamesconfig.config.event.ConfigRegisterEvent;
import net.vakror.jamesconfig.config.example.ExampleConfigs;
import net.vakror.jamesconfig.config.manager.MasterManager;
import net.vakror.jamesconfig.config.manager.config.SimpleConfigManager;
import net.vakror.jamesconfig.config.manager.object.SimpleConfigObjectManager;
import net.vakror.jamesconfig.config.packet.ArchModPackets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Map<ResourceLocation, Config> CONFIGS = new HashMap<>();
	public static final Map<ResourceLocation, ConfigObject> KNOWN_OBJECT_TYPES = new HashMap<>();

	public JamesConfigMod()
	{
		ConfigEvents.REGISTER_MANAGER.register(event -> {
			event.addManager(SimpleConfigManager.INSTANCE);
			event.addManager(SimpleConfigObjectManager.INSTANCE);
			return EventResult.pass();
		});

		PlayerEvent.PLAYER_JOIN.register(ArchModPackets::onLogIn);
		PlayerEvent.CHANGE_DIMENSION.register(((player, oldLevel, newLevel) -> {

		}));
		//MinecraftForge.EVENT_BUS.addListener(net.vakror.jamesconfig.config.example.Events.ForgeEvents::onGetConfigTypeAdapters);

		//Call this in mod constructor or anywhere before commonsetup fires
		ExampleConfigs.addExampleConfig();

		MasterManager.register();

		LifecycleEvent.SETUP.register(this::commonSetup);
		ClientLifecycleEvent.CLIENT_SETUP.register((minecraft)-> ArchModPackets.register());
	}

	public static void registerConfig(ResourceLocation config, boolean replace) {
		CONFIGS.get(config).readConfig(replace);
	}

	public static void registerAllConfigs(boolean replace) {
		Stopwatch stopwatch = Stopwatch.createStarted();
		LOGGER.info("Reading All Configs");
		CONFIGS.forEach((name, register) -> register.readConfig(replace));
		LOGGER.info("Finished reading all configs, \033[0;31mTook {}\033[0;0m", stopwatch);
	}

	public static void addConfig(ResourceLocation name, Config register) {
		CONFIGS.put(name, register);
	}

	private void commonSetup() {
		ConfigRegisterEvent configRegisterEvent = new ConfigRegisterEvent();
		ConfigEvents.CONFIG_REGISTER_EVENT.invoker().post(configRegisterEvent);
		ConfigObjectRegisterEvent event = new ConfigObjectRegisterEvent();
		ConfigEvents.OBJECT_REGISTER_EVENT.invoker().post(event);
		KNOWN_OBJECT_TYPES.putAll(event.getKnownTypes());
		configRegisterEvent.getConfigs().forEach((JamesConfigMod::addConfig));
		registerAllConfigs(false);
	}
}
