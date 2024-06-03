package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.citadel.server.message.PacketBufferUtils;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPodium;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUpdatePodium {

    public long blockPos;
    public ItemStack heldStack;

    public MessageUpdatePodium(long blockPos, ItemStack heldStack) {
        this.blockPos = blockPos;
        this.heldStack = heldStack;

    }

    public MessageUpdatePodium() {
    }

    public static MessageUpdatePodium read(PacketByteBuf buf) {
        return new MessageUpdatePodium(buf.readLong(), PacketBufferUtils.readItemStack(buf));
    }

    public static void write(MessageUpdatePodium message, PacketByteBuf buf) {
        buf.writeLong(message.blockPos);
        PacketBufferUtils.writeItemStack(buf, message.heldStack);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(final MessageUpdatePodium message, final Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();

            context.enqueueWork(() -> {
                Player player = context.getSender();

                if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = IceAndFire.PROXY.getClientSidePlayer();
                }

                if (player != null) {
                    BlockPos pos = BlockPos.of(message.blockPos);

                    if (player.level().getBlockEntity(pos) instanceof TileEntityPodium podium) {
                        podium.setItem(0, message.heldStack);
                    }
                }
            });

            context.setPacketHandled(true);
        }
    }
}