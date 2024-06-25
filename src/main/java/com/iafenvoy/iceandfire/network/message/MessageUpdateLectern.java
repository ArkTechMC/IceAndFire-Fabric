package com.iafenvoy.iceandfire.network.message;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.block.BlockEntityLectern;
import com.iafenvoy.iceandfire.enums.EnumBestiaryPages;
import com.iafenvoy.iceandfire.network.C2SMessage;
import com.iafenvoy.iceandfire.network.S2CMessage;
import com.iafenvoy.iceandfire.registry.IafItems;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class MessageUpdateLectern implements S2CMessage, C2SMessage {

    public long blockPos;
    public int selectedPages1;
    public int selectedPages2;
    public int selectedPages3;
    public boolean updateStack;
    public int pageOrdinal;

    public MessageUpdateLectern(long blockPos, int selectedPages1, int selectedPages2, int selectedPages3, boolean updateStack, int pageOrdinal) {
        this.blockPos = blockPos;
        this.selectedPages1 = selectedPages1;
        this.selectedPages2 = selectedPages2;
        this.selectedPages3 = selectedPages3;
        this.updateStack = updateStack;
        this.pageOrdinal = pageOrdinal;
    }

    public MessageUpdateLectern() {
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender) {
        server.execute(() -> {
            if (player != null) {
                BlockPos pos = BlockPos.fromLong(this.blockPos);
                if (player.getWorld().isChunkLoaded(pos) && player.getWorld().getBlockEntity(pos) instanceof BlockEntityLectern lectern) {
                    if (this.updateStack) {
                        ItemStack bookStack = lectern.getStack(0);
                        if (bookStack.getItem() == IafItems.BESTIARY)
                            EnumBestiaryPages.addPage(EnumBestiaryPages.fromInt(this.pageOrdinal), bookStack);
                        lectern.randomizePages(bookStack, lectern.getStack(1));
                    } else {
                        lectern.selectedPages[0] = EnumBestiaryPages.fromInt(this.selectedPages1);
                        lectern.selectedPages[1] = EnumBestiaryPages.fromInt(this.selectedPages2);
                        lectern.selectedPages[2] = EnumBestiaryPages.fromInt(this.selectedPages3);
                    }
                }
            }
        });
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "update_lectern");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeLong(this.blockPos);
        buf.writeInt(this.selectedPages1);
        buf.writeInt(this.selectedPages2);
        buf.writeInt(this.selectedPages3);
        buf.writeBoolean(this.updateStack);
        buf.writeInt(this.pageOrdinal);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.blockPos = buf.readLong();
        this.selectedPages1 = buf.readInt();
        this.selectedPages2 = buf.readInt();
        this.selectedPages3 = buf.readInt();
        this.updateStack = buf.readBoolean();
        this.pageOrdinal = buf.readInt();
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        client.execute(() -> {
            PlayerEntity player = client.player;
            if (player != null) {
                BlockPos pos = BlockPos.fromLong(this.blockPos);
                if (player.getWorld().isChunkLoaded(pos) && player.getWorld().getBlockEntity(pos) instanceof BlockEntityLectern lectern) {
                    if (this.updateStack) {
                        ItemStack bookStack = lectern.getStack(0);
                        if (bookStack.getItem() == IafItems.BESTIARY)
                            EnumBestiaryPages.addPage(EnumBestiaryPages.fromInt(this.pageOrdinal), bookStack);
                        lectern.randomizePages(bookStack, lectern.getStack(1));
                    } else {
                        lectern.selectedPages[0] = EnumBestiaryPages.fromInt(this.selectedPages1);
                        lectern.selectedPages[1] = EnumBestiaryPages.fromInt(this.selectedPages2);
                        lectern.selectedPages[2] = EnumBestiaryPages.fromInt(this.selectedPages3);
                    }
                }
            }
        });
    }
}