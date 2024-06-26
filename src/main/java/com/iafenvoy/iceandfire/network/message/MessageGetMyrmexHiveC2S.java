package com.iafenvoy.iceandfire.network.message;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.util.MyrmexHive;
import com.iafenvoy.iceandfire.network.C2SMessage;
import com.iafenvoy.iceandfire.world.MyrmexWorldData;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MessageGetMyrmexHiveC2S implements C2SMessage {
    public NbtCompound hive;

    public MessageGetMyrmexHiveC2S(NbtCompound hive) {
        this.hive = hive;
    }

    public MessageGetMyrmexHiveC2S() {
    }

    public static MessageGetMyrmexHiveC2S read(PacketByteBuf buf) {
        return new MessageGetMyrmexHiveC2S(buf.readNbt());
    }

    public static void write(MessageGetMyrmexHiveC2S message, PacketByteBuf buf) {
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
}