package com.iafenvoy.iceandfire.message;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.util.MyrmexHive;
import com.iafenvoy.iceandfire.network.C2SMessage;
import com.iafenvoy.iceandfire.network.S2CMessage;
import com.iafenvoy.iceandfire.world.MyrmexWorldData;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MessageGetMyrmexHive implements C2SMessage, S2CMessage {

    public NbtCompound hive;

    public MessageGetMyrmexHive(NbtCompound hive) {
        this.hive = hive;
    }

    public MessageGetMyrmexHive() {
    }

    public static MessageGetMyrmexHive read(PacketByteBuf buf) {
        return new MessageGetMyrmexHive(buf.readNbt());
    }

    public static void write(MessageGetMyrmexHive message, PacketByteBuf buf) {
        buf.writeNbt(message.hive);
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender) {
        MyrmexHive serverHive = MyrmexHive.fromNBT(this.hive);
        NbtCompound tag = new NbtCompound();
        serverHive.writeVillageDataToNBT(tag);
        serverHive.readVillageDataFromNBT(tag);
        IceAndFire.PROXY.setReferencedHive(serverHive);
        if (player != null) {
            MyrmexHive realHive = MyrmexWorldData.get(player.getWorld()).getHiveFromUUID(serverHive.hiveUUID);
            realHive.readVillageDataFromNBT(serverHive.toNBT());
        }
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "get_myrmex_hive");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeNbt(this.hive);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.hive = buf.readNbt();
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        PlayerEntity player = client.player;
        MyrmexHive serverHive = MyrmexHive.fromNBT(this.hive);
        NbtCompound tag = new NbtCompound();
        serverHive.writeVillageDataToNBT(tag);
        serverHive.readVillageDataFromNBT(tag);
        IceAndFire.PROXY.setReferencedHive(serverHive);
        if (player != null) {
            MyrmexHive realHive = MyrmexWorldData.get(player.getWorld()).getHiveFromUUID(serverHive.hiveUUID);
            realHive.readVillageDataFromNBT(serverHive.toNBT());
        }
    }
}