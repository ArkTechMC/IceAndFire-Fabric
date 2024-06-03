package com.github.alexthe666.citadel.server.message;

import com.github.alexthe666.citadel.Citadel;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncClientTickRateMessage {
    private final NbtCompound compound;

    public SyncClientTickRateMessage(NbtCompound compound) {
        this.compound = compound;
    }

    public static void write(SyncClientTickRateMessage message, PacketByteBuf packetBuffer) {
        PacketBufferUtils.writeTag(packetBuffer, message.compound);
    }

    public static SyncClientTickRateMessage read(PacketByteBuf packetBuffer) {
        return new SyncClientTickRateMessage(PacketBufferUtils.readTag(packetBuffer));
    }

    public static class Handler {

        public static void handle(final SyncClientTickRateMessage message, Supplier<NetworkEvent.Context> context) {
            context.get().setPacketHandled(true);
            context.get().enqueueWork(() -> {
                if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    Citadel.PROXY.handleClientTickRatePacket(message.compound);

                }
            });
        }
    }
}