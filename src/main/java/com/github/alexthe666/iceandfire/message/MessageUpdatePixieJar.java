package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import dev.arktechmc.iafextra.network.S2CMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class MessageUpdatePixieJar implements S2CMessage {

    public long blockPos;
    public boolean isProducing;

    public MessageUpdatePixieJar(long blockPos, boolean isProducing) {
        this.blockPos = blockPos;
        this.isProducing = isProducing;

    }

    public MessageUpdatePixieJar() {
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "update_pixie_jar");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeLong(this.blockPos);
        buf.writeBoolean(this.isProducing);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.blockPos = buf.readLong();
        this.isProducing = buf.readBoolean();
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        PlayerEntity player = client.player;
        if (player != null) {
            BlockPos pos = BlockPos.fromLong(this.blockPos);
            if (player.getWorld().getBlockEntity(pos) instanceof TileEntityJar jar)
                jar.hasProduced = this.isProducing;
        }
    }
}