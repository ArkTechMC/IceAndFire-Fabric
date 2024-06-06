package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iafextra.network.S2CMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class MessageDragonSetBurnBlock implements S2CMessage {
    public int dragonId;
    public boolean breathingFire;
    public int posX;
    public int posY;
    public int posZ;

    public MessageDragonSetBurnBlock(int dragonId, boolean breathingFire, BlockPos pos) {
        this.dragonId = dragonId;
        this.breathingFire = breathingFire;
        this.posX = pos.getX();
        this.posY = pos.getY();
        this.posZ = pos.getZ();
    }

    public MessageDragonSetBurnBlock() {
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "dragon_set_burn_block");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeInt(this.dragonId);
        buf.writeBoolean(this.breathingFire);
        buf.writeInt(this.posX);
        buf.writeInt(this.posY);
        buf.writeInt(this.posZ);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.dragonId = buf.readInt();
        this.breathingFire = buf.readBoolean();
        this.posX = buf.readInt();
        this.posY = buf.readInt();
        this.posZ = buf.readInt();
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        PlayerEntity player = client.player;
        if (player != null) {
            Entity entity = player.getWorld().getEntityById(this.dragonId);
            if (entity instanceof EntityDragonBase dragon) {
                dragon.setBreathingFire(this.breathingFire);
                dragon.burningTarget = new BlockPos(this.posX, this.posY, this.posZ);
            }
        }
    }
}