package net.vakror.jamesconfig.config.packet;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.vakror.jamesconfig.JamesConfigMod;

import java.util.ArrayList;

public class ArchModPackets {
    public static ResourceLocation packetID = new ResourceLocation(JamesConfigMod.MOD_ID,"config_sync_packet");

    public static void register(){
        NetworkManager.registerReceiver(NetworkManager.Side.S2C,packetID,(buf,context)->{
            SyncAllConfigsS2CPacket syncAllConfigsS2CPacket = new SyncAllConfigsS2CPacket(buf);
            syncAllConfigsS2CPacket.handle();
        });
    }

    public static void onLogIn(ServerPlayer player){
        FriendlyByteBuf buf =new FriendlyByteBuf(Unpooled.buffer());
        SyncAllConfigsS2CPacket syncAllConfigsS2CPacket = new SyncAllConfigsS2CPacket(new ArrayList<>(JamesConfigMod.CONFIGS.values()));
        syncAllConfigsS2CPacket.encode(buf);
        NetworkManager.sendToPlayer(player,packetID,buf);
    }
}