package com.iafenvoy.iceandfire.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public interface MessageBase {
    Identifier getId();

    /**
     * Use when send message
     */
    void encode(PacketByteBuf buf);


    /**
     * Use when receive message
     */
    void decode(PacketByteBuf buf);
}
