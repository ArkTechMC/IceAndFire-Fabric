package com.iafenvoy.iceandfire.message;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.block.BlockEntityJar;
import com.iafenvoy.iceandfire.entity.block.BlockEntityPixieHouse;
import com.iafenvoy.iceandfire.network.S2CMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class MessageUpdatePixieHouseModel implements S2CMessage {

    public long blockPos;
    public int houseType;

    public MessageUpdatePixieHouseModel(long blockPos, int houseType) {
        this.blockPos = blockPos;
        this.houseType = houseType;

    }

    public MessageUpdatePixieHouseModel() {
    }

    public static void write(MessageUpdatePixieHouseModel message, PacketByteBuf buf) {
        buf.writeLong(message.blockPos);
        buf.writeInt(message.houseType);
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "update_pixie_house");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeLong(this.blockPos);
        buf.writeInt(this.houseType);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.blockPos = buf.readLong();
        this.houseType = buf.readInt();
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        PlayerEntity player = client.player;
        if (player != null) {
            BlockPos pos = BlockPos.fromLong(this.blockPos);
            BlockEntity blockEntity = player.getWorld().getBlockEntity(pos);
            if (blockEntity instanceof BlockEntityPixieHouse house)
                house.houseType = this.houseType;
            else if (blockEntity instanceof BlockEntityJar jar)
                jar.pixieType = this.houseType;
        }
    }
}