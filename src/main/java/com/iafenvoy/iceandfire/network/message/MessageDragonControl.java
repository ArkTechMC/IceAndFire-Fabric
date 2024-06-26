package com.iafenvoy.iceandfire.network.message;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.*;
import com.iafenvoy.iceandfire.event.ServerEvents;
import com.iafenvoy.iceandfire.network.C2SMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MessageDragonControl implements C2SMessage {
    public int dragonId;
    public byte controlState;
    private double posX;
    private double posY;
    private double posZ;

    public MessageDragonControl(int dragonId, byte controlState, double posX, double posY, double posZ) {
        this.dragonId = dragonId;
        this.controlState = controlState;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public MessageDragonControl() {
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender) {
        if (player != null) {
            Entity entity = player.getWorld().getEntityById(this.dragonId);
            if (ServerEvents.isRidingOrBeingRiddenBy(entity, player)) {
                        /*
                            For some of these entities the `setPos` is handled in `Entity#move`
                            Doing it here would cause server-side movement checks to fail (resulting in "moved wrongly" messages)
                        */
                if (entity instanceof EntityDragonBase dragon) {
                    if (dragon.isOwner(player)) {
                        dragon.setControlState(this.controlState);
                    }
                } else if (entity instanceof EntityHippogryph hippogryph) {
                    if (hippogryph.isOwner(player)) {
                        hippogryph.setControlState(this.controlState);
                    }
                } else if (entity instanceof EntityHippocampus hippo) {
                    if (hippo.isOwner(player)) {
                        hippo.setControlState(this.controlState);
                    }

                    hippo.setPos(this.posX, this.posY, this.posZ);
                } else if (entity instanceof EntityDeathWorm deathWorm) {
                    deathWorm.setControlState(this.controlState);
                    deathWorm.setPos(this.posX, this.posY, this.posZ);
                } else if (entity instanceof EntityAmphithere amphithere) {
                    if (amphithere.isOwner(player)) {
                        amphithere.setControlState(this.controlState);
                    }

                    // TODO :: Is this handled by Entity#move due to recent changes?
                    amphithere.setPos(this.posX, this.posY, this.posZ);
                }
            }
        }
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "dragon_control");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeInt(this.dragonId);
        buf.writeByte(this.controlState);
        buf.writeDouble(this.posX);
        buf.writeDouble(this.posY);
        buf.writeDouble(this.posZ);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.dragonId = buf.readInt();
        this.controlState = buf.readByte();
        this.posX = buf.readDouble();
        this.posY = buf.readDouble();
        this.posZ = buf.readDouble();
    }
}