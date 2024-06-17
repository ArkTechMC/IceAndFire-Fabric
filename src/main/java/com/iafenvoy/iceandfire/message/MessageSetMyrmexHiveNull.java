package com.iafenvoy.iceandfire.message;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.network.S2CMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class MessageSetMyrmexHiveNull implements S2CMessage {

    public MessageSetMyrmexHiveNull() {
    }


    public static void write(MessageSetMyrmexHiveNull message, PacketByteBuf buf) {
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "set_myrmex_hive_null");
    }

    @Override
    public void encode(PacketByteBuf buf) {
    }

    @Override
    public void decode(PacketByteBuf buf) {
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        PlayerEntity player = client.player;
        if (player != null) {
            IceAndFire.PROXY.setReferencedHive(null);
        }
    }
}