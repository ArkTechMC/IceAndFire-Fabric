package com.github.alexthe666.citadel.server.message;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PropertiesMessage {
    private final String propertyID;
    private final NbtCompound compound;
    private final int entityID;

    public PropertiesMessage(String propertyID, NbtCompound compound, int entityID) {
        this.propertyID = propertyID;
        this.compound = compound;
        this.entityID = entityID;
    }

    public static void write(PropertiesMessage message, PacketByteBuf packetBuffer) {
        PacketBufferUtils.writeUTF8String(packetBuffer, message.propertyID);
        PacketBufferUtils.writeTag(packetBuffer, message.compound);
        packetBuffer.writeInt(message.entityID);
    }

    public static PropertiesMessage read(PacketByteBuf packetBuffer) {
        return new PropertiesMessage(PacketBufferUtils.readUTF8String(packetBuffer), PacketBufferUtils.readTag(packetBuffer), packetBuffer.readInt());
    }

    public static class Handler {

        public static void handle(final PropertiesMessage message, Supplier<NetworkEvent.Context> context) {
            context.get().setPacketHandled(true);
            context.get().enqueueWork(() -> {
                if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    Citadel.PROXY.handlePropertiesPacket(message.propertyID, message.compound, message.entityID);
                } else {
                    Entity e = context.get().getSender().level().getEntity(message.entityID);
                    if (e instanceof LivingEntity && (message.propertyID.equals("CitadelPatreonConfig") || message.propertyID.equals("CitadelTagUpdate"))) {
                        CitadelEntityData.setCitadelTag((LivingEntity) e, message.compound);

                    }
                }
            });
        }
    }
}