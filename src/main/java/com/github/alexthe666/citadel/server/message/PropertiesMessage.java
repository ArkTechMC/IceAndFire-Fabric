package com.github.alexthe666.citadel.server.message;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.iafenvoy.iafextra.network.C2SMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PropertiesMessage implements C2SMessage {
    private String propertyID;
    private NbtCompound compound;
    private int entityID;

    public PropertiesMessage(String propertyID, NbtCompound compound, int entityID) {
        this.propertyID = propertyID;
        this.compound = compound;
        this.entityID = entityID;
    }

    public PropertiesMessage() {
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender) {
        Entity e = player.getWorld().getEntityById(this.entityID);
        if (e instanceof LivingEntity && (this.propertyID.equals("CitadelPatreonConfig") || this.propertyID.equals("CitadelTagUpdate")))
            CitadelEntityData.setCitadelTag((LivingEntity) e, this.compound);
    }

    @Override
    public Identifier getId() {
        return new Identifier(Citadel.MOD_ID, "properties");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        PacketBufferUtils.writeUTF8String(buf, this.propertyID);
        PacketBufferUtils.writeTag(buf, this.compound);
        buf.writeInt(this.entityID);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.propertyID = PacketBufferUtils.readUTF8String(buf);
        this.compound = PacketBufferUtils.readTag(buf);
        this.entityID = buf.readInt();
    }
}