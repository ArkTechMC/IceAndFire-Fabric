package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.render.pathfinding.PathfindingDebugRenderer;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.MNode;
import com.iafenvoy.iafextra.network.S2CMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

/**
 * Message to sync some path over to the client.
 */
public class MessageSyncPath implements S2CMessage {
    /**
     * Set of visited nodes.
     */
    public Set<MNode> lastDebugNodesVisited = new HashSet<>();

    /**
     * Set of not visited nodes.
     */
    public Set<MNode> lastDebugNodesNotVisited = new HashSet<>();

    /**
     * Set of chosen nodes for the path.
     */
    public Set<MNode> lastDebugNodesPath = new HashSet<>();

    /**
     * Create a new path message with the filled pathpoints.
     */
    public MessageSyncPath(final Set<MNode> lastDebugNodesVisited, final Set<MNode> lastDebugNodesNotVisited, final Set<MNode> lastDebugNodesPath) {
        super();
        this.lastDebugNodesVisited = lastDebugNodesVisited;
        this.lastDebugNodesNotVisited = lastDebugNodesNotVisited;
        this.lastDebugNodesPath = lastDebugNodesPath;
    }

    public MessageSyncPath() {
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "sync_path");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeInt(this.lastDebugNodesVisited.size());
        for (final MNode MNode : this.lastDebugNodesVisited)
            MNode.serializeToBuf(buf);
        buf.writeInt(this.lastDebugNodesNotVisited.size());
        for (final MNode MNode : this.lastDebugNodesNotVisited)
            MNode.serializeToBuf(buf);
        buf.writeInt(this.lastDebugNodesPath.size());
        for (final MNode MNode : this.lastDebugNodesPath)
            MNode.serializeToBuf(buf);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        int size = buf.readInt();
        for (int i = 0; i < size; i++)
            this.lastDebugNodesVisited.add(new MNode(buf));
        size = buf.readInt();
        for (int i = 0; i < size; i++)
            this.lastDebugNodesNotVisited.add(new MNode(buf));
        size = buf.readInt();
        for (int i = 0; i < size; i++)
            this.lastDebugNodesPath.add(new MNode(buf));
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        PathfindingDebugRenderer.lastDebugNodesVisited = this.lastDebugNodesVisited;
        PathfindingDebugRenderer.lastDebugNodesNotVisited = this.lastDebugNodesNotVisited;
        PathfindingDebugRenderer.lastDebugNodesPath = this.lastDebugNodesPath;
    }
}
