package com.iafenvoy.iceandfire.network.message;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityDeathWorm;
import com.iafenvoy.iceandfire.network.S2CMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;


public class MessageDeathWormHitbox implements S2CMessage {
    public int deathWormId;
    public float scale;

    public MessageDeathWormHitbox(int deathWormId, float scale) {
        this.deathWormId = deathWormId;
        this.scale = scale;
    }

    public MessageDeathWormHitbox() {
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler listener, PacketSender responseSender) {
        PlayerEntity player = client.player;
        if (player != null) {
            Entity entity = player.getWorld().getEntityById(this.deathWormId);
            if (entity instanceof EntityDeathWorm deathWorm)
                deathWorm.initSegments(this.scale);
        }
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "death_worm_hit_box");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeInt(this.deathWormId);
        buf.writeFloat(this.scale);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.deathWormId = buf.readInt();
        this.scale = buf.readFloat();
    }
}