package net.vakror.jamesconfig.config.packet;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.Config;

import java.util.*;
import java.util.function.Supplier;

public class SyncAllConfigsS2CPacket {
    private final Map<ResourceLocation, Config<?>> configs;

    public SyncAllConfigsS2CPacket(List<Config<?>> configs) {
        this.configs = new HashMap<>();
        for (Config<?> config : configs) {
            this.configs.put(config.getName(), config);
        }
    }

    public SyncAllConfigsS2CPacket(FriendlyByteBuf buf) {
        this.configs = new HashMap<>();
        try {
            CompoundTag nbt = buf.readNbt();
            for (String key : nbt.getAllKeys()) {
                ResourceLocation location = new ResourceLocation(nbt.getCompound(key).getString("name"));
                if (JamesConfigMod.CODECS.get(location) == null) {
                    JamesConfigMod.LOGGER.error("Server Contains Codec {} but client does not", location);
                } else {
                    Codec<? extends Config<?>> codec = JamesConfigMod.CODECS.get(location);
                    Optional<? extends Pair<? extends Config<?>, Tag>> optional = codec.decode(NbtOps.INSTANCE, nbt.getCompound(key).get("config")).result();
                    optional.ifPresent(tagPair -> this.configs.put(tagPair.getFirst().getName(), tagPair.getFirst()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void encode(FriendlyByteBuf buf) {
        CompoundTag rootNbt = new CompoundTag();
        int i = 0;
        for (Config<?> config : configs.values()) {
            CompoundTag tag = new CompoundTag();
            tag.putString("name", config.getName().toString());
            Tag configNbt = null;
            Codec<Config<?>> codec = (Codec<Config<?>>) config.getCodec();
            Optional<Tag> a = codec.encodeStart(NbtOps.INSTANCE, config).result();
            if (a.isPresent()) {
                configNbt = a.get();
            }
            tag.put("config", configNbt == null ? new CompoundTag(): configNbt);
            rootNbt.put("" + i, tag);
            i++;
        }
        buf.writeNbt(rootNbt);
    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> {
            assert Minecraft.getInstance().player != null;

            JamesConfigMod.CONFIGS.forEach(((resourceLocation, config) -> {
                if (config.shouldSync()) {
                    if (config.shouldClearBeforeSync()) {
                        config.discardAllValues();
                    }
                    if (configs.containsKey(resourceLocation)) {
                        JamesConfigMod.CONFIGS.put(resourceLocation, configs.get(config.getName()));
                        configs.remove(resourceLocation);
                    }
                }
            }));
            for (Config<?> config : configs.values()) {
                if (config.shouldSync()) {
                    JamesConfigMod.LOGGER.warn("Client did not contain config {}, synced anyway", config.getName());
                    JamesConfigMod.CONFIGS.put(config.getName(), config);
                }
            }
        });
        return true;
    }
}
