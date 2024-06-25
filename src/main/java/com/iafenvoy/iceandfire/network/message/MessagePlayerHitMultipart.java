package com.iafenvoy.iceandfire.network.message;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityHydra;
import com.iafenvoy.iceandfire.network.C2SMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MessagePlayerHitMultipart implements C2SMessage {
    public int creatureID;
    public int extraData;

    public MessagePlayerHitMultipart(int creatureID, int extraData) {
        this.creatureID = creatureID;
        this.extraData = extraData;
    }

    public MessagePlayerHitMultipart() {
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender) {
        if (player != null) {
            Entity entity = player.getWorld().getEntityById(this.creatureID);
            if (entity instanceof LivingEntity livingEntity) {
                double dist = player.distanceTo(livingEntity);
                if (dist < 100) {
                    player.attack(livingEntity);
                    if (livingEntity instanceof EntityHydra hydra)
                        hydra.triggerHeadFlags(this.extraData);
                }
            }
        }
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "player_hit_multipart");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeInt(this.creatureID);
        buf.writeInt(this.extraData);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.creatureID = buf.readInt();
        this.extraData = buf.readInt();
    }
}
