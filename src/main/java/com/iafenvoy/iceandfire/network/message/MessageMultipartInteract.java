package com.iafenvoy.iceandfire.network.message;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.network.C2SMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class MessageMultipartInteract implements C2SMessage {

    public int creatureID;
    public float dmg;

    public MessageMultipartInteract(int creatureID, float dmg) {
        this.creatureID = creatureID;
        this.dmg = dmg;
    }

    public MessageMultipartInteract() {
    }

    public static void write(MessageMultipartInteract message, PacketByteBuf buf) {
        buf.writeInt(message.creatureID);
        buf.writeFloat(message.dmg);
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender) {
        if (player != null) {
            Entity entity = player.getWorld().getEntityById(this.creatureID);

            if (entity instanceof LivingEntity livingEntity) {
                double dist = player.distanceTo(livingEntity);

                if (dist < 100) {
                    if (this.dmg > 0F) {
                        livingEntity.damage(player.getWorld().damageSources.mobAttack(player), this.dmg);
                    } else {
                        livingEntity.interact(player, Hand.MAIN_HAND);
                    }
                }
            }
        }
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "multipart_interact");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeInt(this.creatureID);
        buf.writeFloat(this.dmg);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.creatureID = buf.readInt();
        this.dmg = buf.readFloat();
    }
}