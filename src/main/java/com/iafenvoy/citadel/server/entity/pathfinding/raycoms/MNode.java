package com.iafenvoy.citadel.server.entity.pathfinding.raycoms;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

/**
 * Nodes used in pathfinding.
 */
public class MNode implements Comparable<MNode> {
    /**
     * Values used in the generation of the hash of the node.
     */
    private static final int HASH_A = 12;
    private static final int HASH_B = 20;
    private static final int HASH_C = 24;

    /**
     * The position of the node.
     */

    public final BlockPos pos;

    /**
     * The hash of the node.
     */
    private final int hash;

    /**
     * The parent of the node (Node preceding this node).
     */
    public MNode parent;

    /**
     * Added counter.
     */
    private int counterAdded;

    /**
     * Visited counter.
     */
    private int counterVisited;

    /**
     * Number of steps.
     */
    private int steps;

    /**
     * The cost of the node. A* g value.
     */
    private double cost;

    /**
     * The heuristic of the node. A* h value.
     */
    private double heuristic;

    /**
     * The score of the node. A* f value (g + h).
     */
    private double score;

    /**
     * Checks if the node has been closed already.
     */
    private boolean closed = false;

    /**
     * Checks if the node is on a ladder.
     */
    private boolean ladder = false;

    /**
     * Checks if the node is in water.
     */
    private boolean swimming = false;

    /**
     * If is on rails.
     */
    private boolean isOnRails = false;

    /**
     * Whether this is an air node
     */
    private boolean isCornerNode = false;

    /**
     * Wether this node got reached by an entity, for debug drawing
     */
    private boolean isReachedByWorker = false;

    /**
     * Create initial Node.
     *
     * @param pos       coordinates of node.
     * @param heuristic heuristic estimate.
     */
    public MNode(final BlockPos pos, final double heuristic) {
        this(null, pos, 0, heuristic, heuristic);
    }

    /**
     * Create a Node that inherits from a parent, and has a Cost and Heuristic estimate.
     *
     * @param parent    parent node arrives from.
     * @param pos       coordinate of node.
     * @param cost      node cost.
     * @param heuristic heuristic estimate.
     * @param score     node total score.
     */
    public MNode(final MNode parent, final BlockPos pos, final double cost, final double heuristic, final double score) {
        this.parent = parent;
        this.pos = pos;
        this.steps = parent == null ? 0 : (parent.steps + 1);
        this.cost = cost;
        this.heuristic = heuristic;
        this.score = score;
        this.hash = pos.getX() ^ ((pos.getZ() << HASH_A) | (pos.getZ() >> HASH_B)) ^ (pos.getY() << HASH_C);
    }

    /**
     * Create an MNode from a bytebuf.
     *
     * @param byteBuf the buffer to load it from.
     */
    public MNode(final PacketByteBuf byteBuf) {
        if (byteBuf.readBoolean())
            this.parent = new MNode(byteBuf.readBlockPos(), 0);
        this.pos = byteBuf.readBlockPos();
        this.cost = byteBuf.readDouble();
        this.heuristic = byteBuf.readDouble();
        this.score = byteBuf.readDouble();
        this.hash = this.pos.getX() ^ ((this.pos.getZ() << HASH_A) | (this.pos.getZ() >> HASH_B)) ^ (this.pos.getY() << HASH_C);
        this.isReachedByWorker = byteBuf.readBoolean();
    }

    /**
     * Serialize the Node to buf.
     */
    public void serializeToBuf(final PacketByteBuf byteBuf) {
        byteBuf.writeBoolean(this.parent != null);
        if (this.parent != null)
            byteBuf.writeBlockPos(this.parent.pos);
        byteBuf.writeBlockPos(this.pos);
        byteBuf.writeDouble(this.cost);
        byteBuf.writeDouble(this.heuristic);
        byteBuf.writeDouble(this.score);
        byteBuf.writeBoolean(this.isReachedByWorker);
    }

    @Override
    public int compareTo(final MNode o) {
        //  Comparing doubles and returning value as int; can't simply cast the result
        if (this.score < o.score) return -1;
        if (this.score > o.score) return 1;
        if (this.heuristic < o.heuristic) return -1;
        if (this.heuristic > o.heuristic) return 1;

        //  In case of score tie, older node has better score
        return this.counterAdded - o.counterAdded;
    }

    @Override
    public int hashCode() {
        return this.hash;
    }

    @Override
    public boolean equals(final Object o) {
        if (o != null && o.getClass() == this.getClass()) {
            final MNode other = (MNode) o;
            return this.pos.getX() == other.pos.getX() && this.pos.getY() == other.pos.getY() && this.pos.getZ() == other.pos.getZ();
        }
        return false;
    }

    /**
     * Checks if node is closed.
     *
     * @return true if so.
     */
    public boolean isClosed() {
        return this.closed;
    }

    /**
     * Checks if node is on a ladder.
     *
     * @return true if so.
     */
    public boolean isLadder() {
        return this.ladder;
    }

    /**
     * Checks if node is in water.
     *
     * @return true if so.
     */
    public boolean isSwimming() {
        return this.swimming;
    }

    /**
     * Sets the node as closed.
     */
    public void setClosed() {
        this.closed = true;
    }

    /**
     * Getter for the visited counter.
     *
     * @return the amount.
     */
    public int getCounterVisited() {
        return this.counterVisited;
    }

    /**
     * Setter for the visited counter.
     *
     * @param counterVisited amount to set.
     */
    public void setCounterVisited(final int counterVisited) {
        this.counterVisited = counterVisited;
    }

    /**
     * Getter of the score of the node.
     *
     * @return the score.
     */
    public double getScore() {
        return this.score;
    }

    /**
     * Sets the node score.
     *
     * @param score the score.
     */
    public void setScore(final double score) {
        this.score = score;
    }

    /**
     * Getter of the cost of the node.
     *
     * @return the cost.
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * Sets the node cost.
     *
     * @param cost the cost.
     */
    public void setCost(final double cost) {
        this.cost = cost;
    }

    /**
     * Getter of the steps.
     *
     * @return the steps.
     */
    public int getSteps() {
        return this.steps;
    }

    /**
     * Sets the amount of steps.
     *
     * @param steps the amount.
     */
    public void setSteps(final int steps) {
        this.steps = steps;
    }

    /**
     * Sets the node as a ladder node.
     */
    public void setLadder() {
        this.ladder = true;
    }

    /**
     * Sets the node as a swimming node.
     */
    public void setSwimming() {
        this.swimming = true;
    }

    /**
     * Getter of the heuristic.
     *
     * @return the heuristic.
     */
    public double getHeuristic() {
        return this.heuristic;
    }

    /**
     * Sets the node heuristic.
     *
     * @param heuristic the heuristic.
     */
    public void setHeuristic(final double heuristic) {
        this.heuristic = heuristic;
    }

    /**
     * Getter of the added counter.
     *
     * @return the amount.
     */
    public int getCounterAdded() {
        return this.counterAdded;
    }

    /**
     * Sets the added counter.
     *
     * @param counterAdded amount to set.
     */
    public void setCounterAdded(final int counterAdded) {
        this.counterAdded = counterAdded;
    }

    /**
     * Check if is on rails.
     *
     * @return true if so.
     */
    public boolean isOnRails() {
        return this.isOnRails;
    }

    /**
     * Setup rails params.
     *
     * @param isOnRails if on rails.
     */
    public void setOnRails(final boolean isOnRails) {
        this.isOnRails = isOnRails;
    }

    /**
     * Whether the node is reached by a worker
     *
     * @return reached
     */
    public boolean isReachedByWorker() {
        return this.isReachedByWorker;
    }

    /**
     * Marks the node as reached by the worker
     */
    public void setReachedByWorker(boolean bool) {
        this.isReachedByWorker = bool;
    }

    /**
     * Whether the node is reached by a worker
     *
     * @return reached
     */
    public boolean isCornerNode() {
        return this.isCornerNode;
    }

    /**
     * Marks the node as reached by the worker
     */
    public void setCornerNode(boolean isCornerNode) {
        this.isCornerNode = isCornerNode;
    }
}
