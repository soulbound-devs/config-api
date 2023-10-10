package net.vakror.jamesconfig.config.packet;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.vakror.jamesconfig.JamesConfigMod;

import java.util.ArrayList;

public class ModPackets {
    public static SimpleChannel INSTANCE;

    private static int packetID = 0;
    private static int id() {
        return packetID++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(JamesConfigMod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(SyncAllConfigsS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncAllConfigsS2CPacket::new)
                .encoder(SyncAllConfigsS2CPacket::encode)
                .consumerNetworkThread(SyncAllConfigsS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG packet) {
        INSTANCE.sendToServer(packet);
    }

    public static <MSG> void sendToClient(MSG packet, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

    public static void onLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().isLocalPlayer()) {
            sendToClient(new SyncAllConfigsS2CPacket(new ArrayList<>(JamesConfigMod.CONFIGS.values())), (ServerPlayer) event.getEntity());
        }
    }
}