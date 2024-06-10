package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import dev.arktechmc.iafextra.network.S2CMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class MessageUpdatePixieHouse implements S2CMessage {

    public long blockPos;
    public boolean hasPixie;
    public int pixieType;

    public MessageUpdatePixieHouse(long blockPos, boolean hasPixie, int pixieType) {
        this.blockPos = blockPos;
        this.hasPixie = hasPixie;
        this.pixieType = pixieType;

    }

    public MessageUpdatePixieHouse() {
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "update_pixie_house");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeLong(this.blockPos);
        buf.writeBoolean(this.hasPixie);
        buf.writeInt(this.pixieType);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.blockPos = buf.readLong();
        this.hasPixie = buf.readBoolean();
        this.pixieType = buf.readInt();
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        PlayerEntity player = client.player;
        if (player != null) {
            BlockPos pos = BlockPos.fromLong(this.blockPos);
            BlockEntity blockEntity = player.getWorld().getBlockEntity(pos);
            if (blockEntity instanceof TileEntityPixieHouse house) {
                house.hasPixie = this.hasPixie;
                house.pixieType = this.pixieType;
            } else if (blockEntity instanceof TileEntityJar jar) {
                jar.hasPixie = this.hasPixie;
                jar.pixieType = this.pixieType;
            }
        }
    }
}