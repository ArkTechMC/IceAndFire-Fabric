package com.iafenvoy.iceandfire.network;

import com.iafenvoy.citadel.server.message.AnimationMessage;
import com.iafenvoy.citadel.server.message.SyncClientTickRateMessage;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.message.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.function.Supplier;

public class IafClientNetworkHandler implements ClientPlayNetworking.PlayChannelHandler {
    public static final Identifier CHANNEL_NAME = new Identifier(IceAndFire.MOD_ID, "client_handler");
    public static final IafClientNetworkHandler INSTANCE = new IafClientNetworkHandler();
    private final HashMap<Identifier, Supplier<S2CMessage>> types = new HashMap<>();

    public static void send(C2SMessage message) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(message.getId().toString());
        message.encode(buf);
        ClientPlayNetworking.send(IafServerNetworkHandler.CHANNEL_NAME, buf);
    }

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_NAME, INSTANCE);

        INSTANCE.registerMessage(AnimationMessage::new);
        INSTANCE.registerMessage(SyncClientTickRateMessage::new);

        INSTANCE.registerMessage(MessageDeathWormHitbox::new);
        INSTANCE.registerMessage(MessageDragonSetBurnBlock::new);
//        INSTANCE.registerMessage(MessageGetMyrmexHive::new);
//        INSTANCE.registerMessage(MessageSetMyrmexHiveNull::new);
        INSTANCE.registerMessage(MessageSirenSong::new);
        INSTANCE.registerMessage(MessageStartRidingMob::new);
        INSTANCE.registerMessage(MessageUpdateDragonforge::new);
        INSTANCE.registerMessage(MessageUpdateLectern::new);
        INSTANCE.registerMessage(MessageUpdatePixieHouse::new);
        INSTANCE.registerMessage(MessageUpdatePixieHouseModel::new);
        INSTANCE.registerMessage(MessageUpdatePixieJar::new);
        INSTANCE.registerMessage(MessageUpdatePodium::new);

        INSTANCE.registerMessage(ParticleSpawnMessage::new);
    }

    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Identifier id = new Identifier(buf.readString());
        if (!this.types.containsKey(id)) return;
        S2CMessage message = this.types.get(id).get();
        message.decode(buf);
        message.handle(client, handler, responseSender);
    }

    public void registerMessage(Supplier<S2CMessage> messageSupplier) {
        S2CMessage message = messageSupplier.get();
        this.types.put(message.getId(), messageSupplier);
    }
}
