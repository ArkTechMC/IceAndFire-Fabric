package com.iafenvoy.iceandfire.network.message;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.block.BlockEntityDragonForge;
import com.iafenvoy.iceandfire.network.S2CMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class MessageUpdateDragonforge implements S2CMessage {

    public long blockPos;
    public int cookTime;

    public MessageUpdateDragonforge(long blockPos, int houseType) {
        this.blockPos = blockPos;
        this.cookTime = houseType;

    }

    public MessageUpdateDragonforge() {
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "update_dragon_forge");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeLong(this.blockPos);
        buf.writeInt(this.cookTime);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.blockPos = buf.readLong();
        this.cookTime = buf.readInt();
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        PlayerEntity player = client.player;
        if (player != null) {
            BlockPos pos = BlockPos.fromLong(this.blockPos);
            if (player.getWorld().getBlockEntity(pos) instanceof BlockEntityDragonForge forge) {
                forge.getPropertyDelegate().cookTime = this.cookTime;
                if (this.cookTime > 0)
                    forge.lastDragonFlameTimer = 40;
            }
        }
    }
}