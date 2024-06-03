package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.event.ServerEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSwingArm {

    public MessageSwingArm() {

    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageSwingArm message, Supplier<NetworkEvent.Context> context) {
            context.get().setPacketHandled(true);
            PlayerEntity player = context.get().getSender();
            if (player != null) {
                ServerEvents.onLeftClick(player, player.getStackInHand(Hand.MAIN_HAND));
            }
        }
    }


    public static MessageSwingArm read(PacketByteBuf buf) {
        return new MessageSwingArm();
    }

    public static void write(MessageSwingArm message, PacketByteBuf buf) {
    }

}
