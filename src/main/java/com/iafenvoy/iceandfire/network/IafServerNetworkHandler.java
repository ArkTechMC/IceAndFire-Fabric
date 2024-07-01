package com.iafenvoy.iceandfire.network;

import com.iafenvoy.iceandfire.StaticVariables;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class IafServerNetworkHandler {
    public static void sendToAll(Identifier id, PacketByteBuf buf) {
        if (StaticVariables.server != null)
            for (ServerPlayerEntity player : StaticVariables.server.getPlayerManager().getPlayerList())
                ServerPlayNetworking.send(player, id, buf);
    }
}
