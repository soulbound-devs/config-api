package net.vakror.jamesconfig.config.packet;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.Config;
import net.vakror.jamesconfig.config.config.object.ConfigObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ArchModPackets {
    public static final ResourceLocation syncPacketId = new ResourceLocation(JamesConfigMod.MOD_ID,"config_sync_packet");

    public static void register(){
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, syncPacketId,(buf, context)->{
            SyncAllConfigsS2CPacket syncAllConfigsS2CPacket = new SyncAllConfigsS2CPacket(buf);
            syncAllConfigsS2CPacket.handle();
        });
    }

    public static void onLogIn(ServerPlayer player){
        FriendlyByteBuf buf =new FriendlyByteBuf(Unpooled.buffer());
        Multimap<ResourceLocation, ConfigObject> map = Multimaps.newMultimap(new HashMap<>(), ArrayList::new);
        for (Config config : JamesConfigMod.CONFIGS.values()) {
            if (config.shouldSync()) {
                map.putAll(config.getName(), config.getAll());
            }
        }
        SyncAllConfigsS2CPacket syncAllConfigsS2CPacket = new SyncAllConfigsS2CPacket(map);
        syncAllConfigsS2CPacket.encode(buf);
        NetworkManager.sendToPlayer(player, syncPacketId,buf);
    }
}
