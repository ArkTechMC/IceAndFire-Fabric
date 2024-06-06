package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.iafenvoy.iafextra.network.C2SMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MessageHippogryphArmor implements C2SMessage {

    public int dragonId;
    public int slot_index;
    public int armor_type;

    public MessageHippogryphArmor(int dragonId, int slot_index, int armor_type) {
        this.dragonId = dragonId;
        this.slot_index = slot_index;
        this.armor_type = armor_type;
    }

    public MessageHippogryphArmor() {
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender) {
        if (player != null) {
            Entity entity = player.getWorld().getEntityById(this.dragonId);

            if (entity instanceof EntityHippogryph hippogryph) {
                if (this.slot_index == 0) {
                    hippogryph.setSaddled(this.armor_type == 1);
                } else if (this.slot_index == 1) {
                    hippogryph.setChested(this.armor_type == 1);
                } else if (this.slot_index == 2) {
                    hippogryph.setArmor(this.armor_type);
                }
            } else if (entity instanceof EntityHippocampus hippo) {
                if (this.slot_index == 0) {
                    hippo.setSaddled(this.armor_type == 1);
                } else if (this.slot_index == 1) {
                    hippo.setChested(this.armor_type == 1);
                } else if (this.slot_index == 2) {
                    hippo.setArmor(this.armor_type);
                }
            }
        }
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "hippogryph_armor");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeInt(this.dragonId);
        buf.writeInt(this.slot_index);
        buf.writeInt(this.armor_type);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.dragonId = buf.readInt();
        this.slot_index = buf.readInt();
        this.armor_type = buf.readInt();
    }
}