package com.github.alexthe666.citadel.server.message;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.client.tick.ClientTickRateTracker;
import dev.arktechmc.iafextra.network.S2CMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SyncClientTickRateMessage implements S2CMessage {
    private NbtCompound compound;

    public SyncClientTickRateMessage(NbtCompound compound) {
        this.compound = compound;
    }

    public SyncClientTickRateMessage() {
    }

    @Override
    public Identifier getId() {
        return new Identifier(Citadel.MOD_ID, "sync_client_tick_rate");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        PacketBufferUtils.writeTag(buf, this.compound);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.compound = PacketBufferUtils.readTag(buf);
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        ClientTickRateTracker.getForClient(MinecraftClient.getInstance()).syncFromServer(this.compound);
    }
}