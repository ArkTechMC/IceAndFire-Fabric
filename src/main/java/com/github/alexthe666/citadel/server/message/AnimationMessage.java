package com.github.alexthe666.citadel.server.message;

import com.github.alexthe666.citadel.Citadel;
import com.iafenvoy.iafextra.network.S2CMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class AnimationMessage implements S2CMessage {
    private int entityID;
    private int index;

    public AnimationMessage(int entityID, int index) {
        this.entityID = entityID;
        this.index = index;
    }

    public AnimationMessage() {
    }

    @Override
    public Identifier getId() {
        return new Identifier(Citadel.MOD_ID, "animation");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeInt(this.index);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.entityID = buf.readInt();
        this.index = buf.readInt();
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        Citadel.PROXY.handleAnimationPacket(this.entityID, this.index);
    }
}
