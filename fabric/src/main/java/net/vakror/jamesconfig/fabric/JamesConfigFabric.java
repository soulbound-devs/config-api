package net.vakror.jamesconfig.fabric;

import net.vakror.jamesconfig.JamesConfigMod;
import net.fabricmc.api.ModInitializer;

public class JamesConfigFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        JamesConfigMod.init();
    }
}