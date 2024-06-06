package com.github.alexthe666.citadel.server.message;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.client.render.pathfinding.PathfindingDebugRenderer;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.MNode;
import com.iafenvoy.iafextra.network.S2CMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

/**
 * Message to sync the reached positions over to the client for rendering.
 */
public class MessageSyncPathReached implements S2CMessage {
    /**
     * Set of reached positions.
     */
    public Set<BlockPos> reached = new HashSet<>();

    /**
     * Create the message to send a set of positions over to the client side.
     */
    public MessageSyncPathReached(final Set<BlockPos> reached) {
        super();
        this.reached = reached;
    }

    public MessageSyncPathReached() {
    }

    @Override
    public Identifier getId() {
        return new Identifier(Citadel.MOD_ID, "sync_path_reached");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeInt(this.reached.size());
        for (final BlockPos node : this.reached)
            buf.writeBlockPos(node);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        int size = buf.readInt();
        for (int i = 0; i < size; i++)
            this.reached.add(buf.readBlockPos());
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        for (final MNode node : PathfindingDebugRenderer.lastDebugNodesPath)
            if (this.reached.contains(node.pos))
                node.setReachedByWorker(true);
    }
}