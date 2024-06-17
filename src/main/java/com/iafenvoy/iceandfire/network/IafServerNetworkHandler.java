package com.iafenvoy.iceandfire.network;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.StaticVariables;
import com.iafenvoy.iceandfire.message.*;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.function.Supplier;

public class IafServerNetworkHandler implements ServerPlayNetworking.PlayChannelHandler {
    public static final Identifier CHANNEL_NAME = new Identifier(IceAndFire.MOD_ID, "server_handler");
    public static final IafServerNetworkHandler INSTANCE = new IafServerNetworkHandler();
    private final HashMap<Identifier, Supplier<C2SMessage>> types = new HashMap<>();

    public static void send(S2CMessage message, ServerPlayerEntity... target) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(message.getId().toString());
        message.encode(buf);
        for (ServerPlayerEntity entity : target)
            ServerPlayNetworking.send(entity, IafClientNetworkHandler.CHANNEL_NAME, buf);
    }

    public static void sendToAll(S2CMessage message) {
        if (StaticVariables.server != null)
            send(message, StaticVariables.server.getPlayerManager().getPlayerList().toArray(new ServerPlayerEntity[]{}));
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(CHANNEL_NAME, INSTANCE);

        INSTANCE.registerMessage(MessageDragonControl::new);
        INSTANCE.registerMessage(MessageGetMyrmexHive::new);
        INSTANCE.registerMessage(MessageHippogryphArmor::new);
        INSTANCE.registerMessage(MessageMultipartInteract::new);
        INSTANCE.registerMessage(MessagePlayerHitMultipart::new);
        INSTANCE.registerMessage(MessageStartRidingMob::new);
        INSTANCE.registerMessage(MessageUpdateLectern::new);
    }

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Identifier id = new Identifier(buf.readString());
        if (!this.types.containsKey(id)) return;
        C2SMessage message = this.types.get(id).get();
        message.decode(buf);
        message.handle(server, player, handler, responseSender);
    }

    public void registerMessage(Supplier<C2SMessage> messageSupplier) {
        C2SMessage message = messageSupplier.get();
        this.types.put(message.getId(), messageSupplier);
    }
}
