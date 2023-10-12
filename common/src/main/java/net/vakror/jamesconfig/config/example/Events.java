package net.vakror.jamesconfig.config.example;

public class Events {

    /**
     * @James is this required?
    @Mod.EventBusSubscriber(modid = JamesConfigMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onGetConfigTypeAdapters(EntityJoinLevelEvent event) {
            if (event.getEntity() instanceof Player player) {
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
    */
}