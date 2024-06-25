package com.iafenvoy.iceandfire.network.message;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.util.ISyncMount;
import com.iafenvoy.iceandfire.network.C2SMessage;
import com.iafenvoy.iceandfire.network.S2CMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MessageStartRidingMob implements S2CMessage, C2SMessage {

    public int dragonId;
    public boolean ride;
    public boolean baby;

    public MessageStartRidingMob(int dragonId, boolean ride, boolean baby) {
        this.dragonId = dragonId;
        this.ride = ride;
        this.baby = baby;
    }

    public MessageStartRidingMob() {
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender) {
        if (player != null) {
            Entity entity = player.getWorld().getEntityById(this.dragonId);
            if (entity instanceof ISyncMount && entity instanceof TameableEntity tamable) {
                if (tamable.isOwner(player) && tamable.distanceTo(player) < 14) {
                    if (this.ride) {
                        if (this.baby) tamable.startRiding(player, true);
                        else player.startRiding(tamable, true);

                    } else {
                        if (this.baby) tamable.stopRiding();
                        else player.stopRiding();
                    }
                }
            }
        }
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "start_riding_mob");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeInt(this.dragonId);
        buf.writeBoolean(this.ride);
        buf.writeBoolean(this.baby);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.dragonId = buf.readInt();
        this.ride = buf.readBoolean();
        this.baby = buf.readBoolean();
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        PlayerEntity player = client.player;
        if (player != null) {
            Entity entity = player.getWorld().getEntityById(this.dragonId);
            if (entity instanceof ISyncMount && entity instanceof TameableEntity tamable) {
                if (tamable.isOwner(player) && tamable.distanceTo(player) < 14) {
                    if (this.ride) {
                        if (this.baby) tamable.startRiding(player, true);
                        else player.startRiding(tamable, true);

                    } else {
                        if (this.baby) tamable.stopRiding();
                        else player.stopRiding();
                    }
                }
            }
        }
    }
}