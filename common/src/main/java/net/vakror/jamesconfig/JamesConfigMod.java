package net.vakror.jamesconfig;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.Config;
import net.vakror.jamesconfig.config.config.commands.JamesConfigCommands;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import net.vakror.jamesconfig.config.event.ConfigEvents;
import net.vakror.jamesconfig.config.event.ConfigObjectRegisterEvent;
import net.vakror.jamesconfig.config.event.ConfigRegisterEvent;
import net.vakror.jamesconfig.config.example.ExampleConfigs;
import net.vakror.jamesconfig.config.manager.CommandManager;
import net.vakror.jamesconfig.config.manager.ManagerRegister;
import net.vakror.jamesconfig.config.manager.MasterManager;
import net.vakror.jamesconfig.config.manager.config.SimpleConfigManager;
import net.vakror.jamesconfig.config.manager.object.SimpleConfigObjectManager;
import net.vakror.jamesconfig.config.packet.ArchModPackets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

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

		PlayerEvent.PLAYER_JOIN.register(ArchModPackets::onLogIn);
		PlayerEvent.CHANGE_DIMENSION.register(((player, oldLevel, newLevel) -> {

		}));

		//Call this in mod constructor or anywhere before common setup fires
		ExampleConfigs.addExampleConfig();

		ManagerRegister.INSTANCE.register(CommandManager.INSTANCE);
		ManagerRegister.INSTANCE.register(SimpleConfigManager.INSTANCE);
		ManagerRegister.INSTANCE.register(SimpleConfigObjectManager.INSTANCE);

		MasterManager.register();

		JamesConfigCommands.registerCommands();

		LifecycleEvent.SETUP.register(this::commonSetup);
		ClientLifecycleEvent.CLIENT_SETUP.register((minecraft)-> ArchModPackets.register());
	}

	public static void readAllConfigs() {
		LOGGER.info("Reading All Configs");
		CONFIGS.forEach((name, register) -> register.readConfig(false));
		LOGGER.info("Finished reading all configs");
	}

	public static void addConfig(ResourceLocation name, Config register) {
		CONFIGS.put(name, register);
	}

	public static void analyzePerformance() {
		CONFIGS.values().forEach(Config::analyzeConfigPerformance);
	}

	public static SuggestionProvider<CommandSourceStack> buildConfigSuggestions() {
		return (context, suggestionsBuilder) -> {
            StringReader stringReader = new StringReader(suggestionsBuilder.getInput());
            stringReader.setCursor(suggestionsBuilder.getStart());
            Function<SuggestionsBuilder, CompletableFuture<Suggestions>> function = JamesConfigMod::suggestConfig;
            return function.apply(suggestionsBuilder.createOffset(stringReader.getCursor()));
        };
	}

	private static CompletableFuture<Suggestions> suggestConfig(SuggestionsBuilder suggestionsBuilder) {
		return SharedSuggestionProvider.suggestResource(JamesConfigMod.CONFIGS.keySet(), suggestionsBuilder);
	}

	private void commonSetup() {
		ConfigRegisterEvent configRegisterEvent = new ConfigRegisterEvent();
		ConfigEvents.CONFIG_REGISTER_EVENT.invoker().post(configRegisterEvent);
		ConfigObjectRegisterEvent event = new ConfigObjectRegisterEvent();
		ConfigEvents.OBJECT_REGISTER_EVENT.invoker().post(event);
		for (ConfigObject configObject : event.getAll()) {
			KNOWN_OBJECT_TYPES.put(configObject.getType(), configObject);
		}
		configRegisterEvent.getAll().forEach((config -> JamesConfigMod.addConfig(config.getName(), config)));
		readAllConfigs();
	}
}
