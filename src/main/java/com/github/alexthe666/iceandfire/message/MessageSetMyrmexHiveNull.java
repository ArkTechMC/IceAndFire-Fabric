package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSetMyrmexHiveNull {

    public MessageSetMyrmexHiveNull() {
    }

    public static MessageSetMyrmexHiveNull read(PacketByteBuf buf) {
        return new MessageSetMyrmexHiveNull();
    }

    public static void write(MessageSetMyrmexHiveNull message, PacketByteBuf buf) {
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageSetMyrmexHiveNull message, Supplier<NetworkEvent.Context> context) {
            context.get().setPacketHandled(true);
            PlayerEntity player = context.get().getSender();
            if (player != null) {
                IceAndFire.PROXY.setReferencedHive(null);
            }
        }
    }
}