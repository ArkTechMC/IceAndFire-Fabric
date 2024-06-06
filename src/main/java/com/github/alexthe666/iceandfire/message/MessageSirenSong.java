package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.iafenvoy.iafextra.network.S2CMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class MessageSirenSong implements S2CMessage {

    public int sirenId;
    public boolean isSinging;

    public MessageSirenSong(int sirenId, boolean isSinging) {
        this.sirenId = sirenId;
        this.isSinging = isSinging;
    }

    public MessageSirenSong() {
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "siren_song");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeInt(this.sirenId);
        buf.writeBoolean(this.isSinging);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.sirenId = buf.readInt();
        this.isSinging = buf.readBoolean();
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        PlayerEntity player = client.player;
        if (player != null) {
            Entity entity = player.getWorld().getEntityById(this.sirenId);
            if (entity instanceof EntitySiren siren)
                siren.setSinging(this.isSinging);
        }
    }
}