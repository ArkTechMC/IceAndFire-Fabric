package com.iafenvoy.iceandfire.network.message;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.network.C2SMessage;
import com.iafenvoy.iceandfire.network.S2CMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

//TODO: Split
public class MessageDragonSyncFire implements C2SMessage, S2CMessage {
    public int dragonId;
    public double posX;
    public double posY;
    public double posZ;
    public int syncType;

    public MessageDragonSyncFire(int dragonId, double posX, double posY, double posZ, int syncType) {
        this.dragonId = dragonId;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.syncType = syncType;
    }

    public MessageDragonSyncFire() {
    }

    public static void write(MessageDragonSyncFire message, PacketByteBuf buf) {
        buf.writeInt(message.dragonId);
        buf.writeDouble(message.posX);
        buf.writeDouble(message.posY);
        buf.writeDouble(message.posZ);
        buf.writeInt(message.syncType);
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender) {
        if (player != null) {
            Entity entity = player.getWorld().getEntityById(this.dragonId);
            if (entity instanceof EntityDragonBase dragon)
                dragon.stimulateFire(this.posX, this.posY, this.posZ, this.syncType);
        }
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "dragon_sync_fire");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeInt(this.dragonId);
        buf.writeDouble(this.posX);
        buf.writeDouble(this.posY);
        buf.writeDouble(this.posZ);
        buf.writeInt(this.syncType);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.dragonId = buf.readInt();
        this.posX = buf.readDouble();
        this.posY = buf.readDouble();
        this.posZ = buf.readDouble();
        this.syncType = buf.readInt();
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        PlayerEntity player = client.player;
        if (player != null) {
            Entity entity = player.getWorld().getEntityById(this.dragonId);
            if (entity instanceof EntityDragonBase dragon)
                dragon.stimulateFire(this.posX, this.posY, this.posZ, this.syncType);
        }
    }
}
