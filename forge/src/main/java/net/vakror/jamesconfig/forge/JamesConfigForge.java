package net.vakror.jamesconfig.forge;

import dev.architectury.platform.forge.EventBuses;
import net.vakror.jamesconfig.JamesConfigMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(JamesConfigMod.MOD_ID)
public class JamesConfigForge {
    public JamesConfigForge() {
		// Submit our event bus to let architectury post our content on the right time
        EventBuses.registerModEventBus(JamesConfigMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        JamesConfigMod.init();
    }
}