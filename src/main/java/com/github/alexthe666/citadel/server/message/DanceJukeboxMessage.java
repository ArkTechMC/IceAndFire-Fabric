package com.github.alexthe666.citadel.server.message;

import com.github.alexthe666.citadel.Citadel;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DanceJukeboxMessage {

    public int entityID;
    public boolean dance;
    public BlockPos jukeBox;

    public DanceJukeboxMessage(int entityID, boolean dance, BlockPos jukeBox) {
        this.entityID = entityID;
        this.dance = dance;
        this.jukeBox = jukeBox;
    }

    public DanceJukeboxMessage() {
    }

    public static DanceJukeboxMessage read(PacketByteBuf buf) {
        return new DanceJukeboxMessage(buf.readInt(), buf.readBoolean(), buf.readBlockPos());
    }

    public static void write(DanceJukeboxMessage message, PacketByteBuf buf) {
        buf.writeInt(message.entityID);
        buf.writeBoolean(message.dance);
        buf.writeBlockPos(message.jukeBox);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(DanceJukeboxMessage message, Supplier<NetworkEvent.Context> context) {
            context.get().setPacketHandled(true);
            context.get().enqueueWork(() -> {
                Player player = context.get().getSender();
                if(context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT){
                    player = Citadel.PROXY.getClientSidePlayer();
                }

                if (player != null) {
                    Citadel.PROXY.handleJukeboxPacket(player.level(), message.entityID, message.jukeBox, message.dance);

                }
            });
        }
    }
}