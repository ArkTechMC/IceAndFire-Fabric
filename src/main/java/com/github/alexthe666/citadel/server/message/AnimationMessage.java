package com.github.alexthe666.citadel.server.message;

import com.github.alexthe666.citadel.Citadel;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AnimationMessage {

    private final int entityID;
    private final int index;

    public AnimationMessage(int entityID, int index) {
        this.entityID = entityID;
        this.index = index;
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(AnimationMessage message, Supplier<NetworkEvent.Context> context) {
            Citadel.PROXY.handleAnimationPacket(message.entityID, message.index);
            context.get().setPacketHandled(true);
        }
    }

    public static AnimationMessage read(PacketByteBuf buf) {
        return new AnimationMessage(buf.readInt(), buf.readInt());
    }

    public static void write(AnimationMessage message, PacketByteBuf buf) {
        buf.writeInt(message.entityID);
        buf.writeInt(message.index);
    }
}
