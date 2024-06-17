package com.iafenvoy.iceandfire.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public interface S2CMessage extends MessageBase {
    void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender);
}
