package com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.pathjobs;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.*;
import com.github.alexthe666.citadel.server.message.MessageSyncPath;
import com.github.alexthe666.citadel.server.message.MessageSyncPathReached;
import com.mojang.datafixers.util.Pair;
import dev.arktechmc.iafextra.network.IafServerNetworkHandler;
import dev.arktechmc.iafextra.util.BlockUtil;
import dev.arktechmc.iafextra.util.PathUtil;
import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.Callable;

import static com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.PathfindingConstants.*;
import static com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.SurfaceType.getSurfaceType;
import static com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.SurfaceType.isWater;

/**
 * Abstract class for Jobs that run in the multithreaded pathfinder.
 */
public abstract class AbstractPathJob implements Callable<Path> {
    /**
     * Which citizens are being tracked by which players.
     */
    public static final Map<PlayerEntity, UUID> trackingMap = new HashMap<>();
    /**
     * Start position to path from.
     */
    protected final BlockPos start;
    /**
     * The pathing cache.
     */
    protected final WorldView world;
    /**
     * The result of the path calculation.
     */
    protected final PathResult result;
    /**
     * Queue of all open nodes.
     */
    private final Queue<MNode> nodesOpen = new PriorityQueue<>(500);
    /**
     * Queue of all the visited nodes.
     */
    private final Map<Integer, MNode> nodesVisited = new HashMap<>();
    /**
     * Type of restriction.
     */
    private final AbstractAdvancedPathNavigate.RestrictionType restrictionType;
    /**
     * Are xz restrictions hard or soft.
     */
    private final boolean hardXzRestriction;
    /**
     * Are there hard xz restrictions.
     */
    private final boolean xzRestricted = false;
    /**
     * Max range used to calculate the number of nodes we visit (square of maxrange).
     */
    protected int maxRange;
    /**
     * End position trying to reach.
     */
    protected BlockPos end = null;
    //  Debug Rendering
    protected boolean debugDrawEnabled = false;
    protected Set<MNode> debugNodesVisited = new HashSet<>();
    protected Set<MNode> debugNodesNotVisited = new HashSet<>();
    protected Set<MNode> debugNodesPath = new HashSet<>();
    /**
     * The entity this job belongs to.
     */
    protected final WeakReference<LivingEntity> entity;
    IPassabilityNavigator passabilityNavigator;
    //  May be faster, but can produce strange results
    private boolean allowJumpPointSearchTypeWalk;
    //  May be faster, but can produce strange results
    private float entitySizeXZ = 1;
    private int entitySizeY = 1;
    /**
     * The cost values for certain nodes.
     */
    private boolean circumventSizeCheck = false;
    private int totalNodesAdded = 0;
    private int totalNodesVisited = 0;
    /**
     * The cost values for certain nodes.
     */
    private PathingOptions pathingOptions = new PathingOptions();
    /**
     * The restriction parameters
     */
    private int maxX;
    private int minX;
    private int maxZ;
    private int minZ;
    private int maxY;
    private int minY;
    private double maxJumpHeight = MAX_JUMP_HEIGHT;

    private int maxNavigableGroundDist = 1;

    /**
     * AbstractPathJob constructor.
     *
     * @param world  the world within which to path.
     * @param start  the start position from which to path from.
     * @param end    the end position to path to.
     * @param range  maximum path range.
     * @param entity the entity.
     */
    public AbstractPathJob(final World world, final BlockPos start, final BlockPos end, final int range, final LivingEntity entity) {
        this(world, start, end, range, new PathResult<AbstractPathJob>(), entity);
    }

    /**
     * AbstractPathJob constructor.
     *
     * @param world  the world within which to path.
     * @param start  the start position from which to path from.
     * @param end    the end position to path to
     * @param range  maximum path range.
     * @param result path result.
     * @param entity the entity.
     * @see AbstractPathJob#AbstractPathJob(World, BlockPos, BlockPos, int, LivingEntity)
     */
    public AbstractPathJob(final World world, final BlockPos start, final BlockPos end, final int range, final PathResult result, final LivingEntity entity) {
        final int minX = Math.min(start.getX(), end.getX()) - (range / 2);
        final int minZ = Math.min(start.getZ(), end.getZ()) - (range / 2);
        final int maxX = Math.max(start.getX(), end.getX()) + (range / 2);
        final int maxZ = Math.max(start.getZ(), end.getZ()) + (range / 2);

        this.restrictionType = AbstractAdvancedPathNavigate.RestrictionType.NONE;
        this.hardXzRestriction = false;

        this.world = new ChunkCache(world, new BlockPos(minX, world.getBottomY(), minZ), new BlockPos(maxX, world.getTopY(), maxZ), range, world.getDimension());

        this.start = new BlockPos(start);
        this.end = end;

        this.maxRange = range;

        this.result = result;
        result.setJob(this);
        this.allowJumpPointSearchTypeWalk = false;

        if (entity != null && trackingMap.containsValue(entity.getUuid())) {
            this.debugDrawEnabled = true;
            this.debugNodesVisited = new HashSet<>();
            this.debugNodesNotVisited = new HashSet<>();
            this.debugNodesPath = new HashSet<>();
        }
        this.setEntitySizes(entity);
        if (entity instanceof IPassabilityNavigator) {
            this.passabilityNavigator = (IPassabilityNavigator) entity;
            this.maxRange = this.passabilityNavigator.maxSearchNodes();
        }
        if (entity instanceof ITallWalker tallWalker) {
            this.maxNavigableGroundDist = tallWalker.getMaxNavigableDistanceToGround();
        }
        this.maxJumpHeight = (float) Math.floor(entity.getStepHeight() - 0.2F) + 1.3F;
        this.entity = new WeakReference<>(entity);
    }

    /**
     * AbstractPathJob constructor.
     *
     * @param world            the world within which to path.
     * @param start            the start position from which to path from.
     * @param startRestriction start of restricted area.
     * @param endRestriction   end of restricted area.
     * @param range            range^2 is used as cap for visited MNode count
     * @param hardRestriction  if <code>true</code> start has to be inside the restricted area (otherwise the search immidiately finishes) -
     *                         MNode visits outside the area are not allowed, isAtDestination is called on every node, if <code>false</code>
     *                         restricted area only applies to calling isAtDestination thus searching outside area is allowed
     * @param result           path result.
     * @param entity           the entity.
     */
    public AbstractPathJob(final World world,
                           final BlockPos start,
                           final BlockPos startRestriction,
                           final BlockPos endRestriction,
                           final int range,
                           final boolean hardRestriction,
                           final PathResult result,
                           final LivingEntity entity,
                           final AbstractAdvancedPathNavigate.RestrictionType restrictionType) {
        this(world, start, startRestriction, endRestriction, range, Vec3i.ZERO, hardRestriction, result, entity, restrictionType);
        this.setEntitySizes(entity);
        if (entity instanceof IPassabilityNavigator) {
            this.passabilityNavigator = (IPassabilityNavigator) entity;
            this.maxRange = this.passabilityNavigator.maxSearchNodes();
        }
        this.maxJumpHeight = (float) Math.floor(entity.getStepHeight() - 0.2F) + 1.3F;
    }

    /**
     * AbstractPathJob constructor.
     *
     * @param world            the world within which to path.
     * @param start            the start position from which to path from.
     * @param startRestriction start of restricted area.
     * @param endRestriction   end of restricted area.
     * @param range            range^2 is used as cap for visited MNode count
     * @param grow             adjustment for restricted area, can be either shrink or grow, is applied in both of xz directions after
     *                         getting min/max box values
     * @param hardRestriction  if <code>true</code> start has to be inside the restricted area (otherwise the search immidiately finishes) -
     *                         MNode visits outside the area are not allowed, isAtDestination is called on every node, if <code>false</code>
     *                         restricted area only applies to calling isAtDestination thus searching outside area is allowed
     * @param result           path result.
     * @param entity           the entity.
     */
    public AbstractPathJob(final World world,
                           final BlockPos start,
                           final BlockPos startRestriction,
                           final BlockPos endRestriction,
                           final int range,
                           final Vec3i grow,
                           final boolean hardRestriction,
                           final PathResult result,
                           final LivingEntity entity,
                           final AbstractAdvancedPathNavigate.RestrictionType restrictionType) {
        this.minX = Math.min(startRestriction.getX(), endRestriction.getX()) - grow.getX();
        this.minZ = Math.min(startRestriction.getZ(), endRestriction.getZ()) - grow.getZ();
        this.maxX = Math.max(startRestriction.getX(), endRestriction.getX()) + grow.getX();
        this.maxZ = Math.max(startRestriction.getZ(), endRestriction.getZ()) + grow.getZ();
        this.minY = Math.min(startRestriction.getY(), endRestriction.getY()) - grow.getY();
        this.maxY = Math.max(startRestriction.getY(), endRestriction.getY()) + grow.getY();

        this.restrictionType = restrictionType;
        this.hardXzRestriction = hardRestriction;

        this.world = new ChunkCache(world, new BlockPos(this.minX, world.getBottomY(), this.minZ), new BlockPos(this.maxX, world.getTopY(), this.maxZ), range, world.getDimension());

        this.start = start;

        this.maxRange = range;

        this.result = result;
        result.setJob(this);

        this.allowJumpPointSearchTypeWalk = false;

        if (entity != null && trackingMap.containsValue(entity.getUuid())) {
            this.debugDrawEnabled = true;
            this.debugNodesVisited = new HashSet<>();
            this.debugNodesNotVisited = new HashSet<>();
            this.debugNodesPath = new HashSet<>();
        }
        this.entity = new WeakReference<>(entity);
    }

    /**
     * Set the set of reached blocks to the client.
     *
     * @param reached the reached blocks.
     * @param mob     the tracked mob.
     */
    public static void synchToClient(final HashSet<BlockPos> reached, final MobEntity mob) {

        if (!Pathfinding.isDebug() || reached.isEmpty()) {
            return;
        }

        for (final Map.Entry<PlayerEntity, UUID> entry : trackingMap.entrySet()) {
            if (entry.getValue().equals(mob.getUuid())) {
                IafServerNetworkHandler.send(new MessageSyncPathReached(reached), (ServerPlayerEntity) entry.getKey());
            }
        }
    }

    /**
     * Generates a good path starting location for the entity to path from, correcting for the following conditions. - Being in water: pathfinding in water occurs along the
     * surface; adjusts position to surface. - Being in a fence space: finds correct adjacent position which is not a fence space, to prevent starting path. from within the fence
     * block.
     *
     * @param entity Entity for the pathfinding operation.
     * @return ChunkCoordinates for starting location.
     */
    public static BlockPos prepareStart(final LivingEntity entity) {
        BlockPos.Mutable pos = new BlockPos.Mutable(entity.getBlockX(),
                entity.getBlockY(),
                entity.getBlockZ());
        final World world = entity.getWorld();

        BlockState bs = world.getBlockState(pos);
        // 1 Up when we're standing within this collision shape
        final VoxelShape collisionShape = bs.getSidesShape(world, pos);
        if (bs.blocksMovement() && collisionShape.getMax(Direction.Axis.X) > 0) {
            final double relPosX = Math.abs(entity.getX() % 1);
            final double relPosZ = Math.abs(entity.getZ() % 1);

            for (final Box box : collisionShape.getBoundingBoxes()) {
                if (relPosX >= box.minX && relPosX <= box.maxX
                        && relPosZ >= box.minZ && relPosZ <= box.maxZ
                        && box.maxY > 0) {
                    pos.set(pos.getX(), pos.getY() + 1, pos.getZ());
                    bs = world.getBlockState(pos);
                    break;
                }
            }
        }

        BlockState down = world.getBlockState(pos.down());
        while (!bs.blocksMovement() && !down.blocksMovement() && !BlockUtil.isLadder(down.getBlock())) {
            pos.move(Direction.DOWN, 1);
            bs = down;
            down = world.getBlockState(pos.down());

            if (pos.getY() < world.getBottomY()) {
                return entity.getBlockPos();
            }
        }

        final Block b = bs.getBlock();

        if (entity.isTouchingWater()) {
            while (!bs.getFluidState().isEmpty()) {
                pos.set(pos.getX(), pos.getY() + 1, pos.getZ());
                bs = world.getBlockState(pos);
            }
        } else if (b instanceof FenceBlock || b instanceof WallBlock || bs.isSolid()) {
            //Push away from fence
            final double dX = entity.getX() - Math.floor(entity.getX());
            final double dZ = entity.getZ() - Math.floor(entity.getZ());

            if (dX < ONE_SIDE) {
                pos.set(pos.getX() - 1, pos.getY(), pos.getZ());
            } else if (dX > OTHER_SIDE) {
                pos.set(pos.getX() + 1, pos.getY(), pos.getZ());
            }

            if (dZ < ONE_SIDE) {
                pos.set(pos.getX(), pos.getY(), pos.getZ() - 1);
            } else if (dZ > OTHER_SIDE) {
                pos.set(pos.getX(), pos.getY(), pos.getZ() + 1);
            }
        }

        return pos.toImmutable();
    }

    /**
     * Sets the direction where the ladder is facing.
     *
     * @param world the world in.
     * @param pos   the position.
     * @param p     the path.
     */
    private static void setLadderFacing(final WorldView world, final BlockPos pos, final PathPointExtended p) {
        final BlockState state = world.getBlockState(pos);
        final Block block = state.getBlock();
        if (block instanceof VineBlock) {
            if (state.get(VineBlock.SOUTH)) {
                p.setLadderFacing(Direction.NORTH);
            } else if (state.get(VineBlock.WEST)) {
                p.setLadderFacing(Direction.EAST);
            } else if (state.get(VineBlock.NORTH)) {
                p.setLadderFacing(Direction.SOUTH);
            } else if (state.get(VineBlock.EAST)) {
                p.setLadderFacing(Direction.WEST);
            }
        } else if (block instanceof LadderBlock) {
            p.setLadderFacing(state.get(LadderBlock.FACING));
        } else {
            p.setLadderFacing(Direction.UP);
        }
    }

    /**
     * Checks if entity is on a ladder.
     *
     * @param node       the path node.
     * @param nextInPath the next path point.
     * @param pos        the position.
     * @return true if on a ladder.
     */
    private static boolean onALadder(final MNode node, final MNode nextInPath, final BlockPos pos) {
        return nextInPath != null && node.isLadder()
                &&
                (nextInPath.pos.getX() == pos.getX() && nextInPath.pos.getZ() == pos.getZ());
    }

    /**
     * Generate a pseudo-unique key for identifying a given MNode by it's coordinates Encodes the lowest 12 bits of x,z and all useful bits of y. This creates unique keys for all
     * blocks within a 4096x256x4096 cube, which is FAR bigger volume than one should attempt to pathfind within This version takes a BlockPos
     *
     * @param pos BlockPos to generate key from
     * @return key for MNode in map
     */
    private static int computeNodeKey(final BlockPos pos) {
        return ((pos.getX() & 0xFFF) << SHIFT_X_BY)
                | ((pos.getY() & 0xFF) << SHIFT_Y_BY)
                | (pos.getZ() & 0xFFF);
    }

    private static boolean nodeClosed(final MNode node) {
        return node != null && node.isClosed();
    }

    private static boolean calculateSwimming(final WorldView world, final BlockPos pos, final MNode node) {
        return (node == null) ? isWater(world, pos.down()) : node.isSwimming();
    }

    public static Direction getXZFacing(final BlockPos pos, final BlockPos neighbor) {
        final BlockPos vector = neighbor.subtract(pos);
        return Direction.getFacing(vector.getX(), 0, vector.getZ());
    }

    /**
     * Sync the path of a given mob to the client.
     *
     * @param mob the tracked mob.
     */
    public void synchToClient(final LivingEntity mob) {
        for (final Iterator<Map.Entry<PlayerEntity, UUID>> iter = trackingMap.entrySet().iterator(); iter.hasNext(); ) {
            final Map.Entry<PlayerEntity, UUID> entry = iter.next();
            if (entry.getKey().isRemoved()) {
                iter.remove();
            } else if (entry.getValue().equals(mob.getUuid())) {
                IafServerNetworkHandler.send(new MessageSyncPath(this.debugNodesVisited, this.debugNodesNotVisited, this.debugNodesPath), (ServerPlayerEntity) entry.getKey());
            }
        }
    }

    protected boolean onLadderGoingUp(final MNode currentNode, final BlockPos dPos) {
        return currentNode.isLadder() && (dPos.getY() >= 0 || dPos.getX() != 0 || dPos.getZ() != 0);
    }

    public void setEntitySizes(LivingEntity entity) {
        if (entity instanceof ICustomSizeNavigator) {
            this.entitySizeXZ = ((ICustomSizeNavigator) entity).getXZNavSize();
            this.entitySizeY = ((ICustomSizeNavigator) entity).getYNavSize();
            this.circumventSizeCheck = ((ICustomSizeNavigator) entity).isSmallerThanBlock();
        } else {
            this.entitySizeXZ = entity.getWidth() / 2.0F;
            this.entitySizeY = MathHelper.ceil(entity.getHeight());
        }
        //TODO:
        this.allowJumpPointSearchTypeWalk = false;
    }

    /**
     * Compute the cost (immediate 'g' value) of moving from the parent space to the new space.
     *
     * @param dPos       The delta from the parent to the new space; assumes dx,dy,dz in range of [-1..1].
     * @param isSwimming true is the current MNode would require the citizen to swim.
     * @param onRails    checks if the MNode is a rail block.
     * @param railsExit  the exit of the rails.
     * @param swimStart  if its the swim start.
     * @param blockPos   the position.
     * @return cost to move from the parent to the new position.
     */
    protected double computeCost(
            final BlockPos dPos,
            final boolean isSwimming,
            final boolean onRails,
            final boolean railsExit,
            final boolean swimStart,
            final boolean corner,
            final BlockState state,
            final BlockPos blockPos) {
        double cost = Math.sqrt(dPos.getX() * dPos.getX() + dPos.getY() * dPos.getY() + dPos.getZ() * dPos.getZ());

        if (dPos.getY() != 0 && !(Math.abs(dPos.getY()) <= 1 && this.world.getBlockState(blockPos).getBlock() instanceof StairsBlock)) {
            if (dPos.getY() > 0) {
                cost *= this.pathingOptions.jumpCost * Math.abs(dPos.getY());
            } else {
                cost *= this.pathingOptions.dropCost * Math.abs(dPos.getY());
            }
        }

        if (this.world.getBlockState(blockPos).contains(Properties.OPEN)) {
            cost *= this.pathingOptions.traverseToggleAbleCost;
        }

        if (false) {
            cost *= this.pathingOptions.onPathCost;
        }

        if (onRails) {
            cost *= this.pathingOptions.onRailCost;
        }

        if (railsExit) {
            cost *= this.pathingOptions.railsExitCost;
        }

        if (state.getBlock() instanceof VineBlock) {
            cost *= this.pathingOptions.vineCost;
        }

        if (isSwimming) {
            if (swimStart) {
                cost *= this.pathingOptions.swimCostEnter;
            } else {
                cost *= this.pathingOptions.swimCost;
            }
        }

        return cost;
    }

    public PathResult getResult() {
        return this.result;
    }

    /**
     * Callable method for initiating asynchronous task.
     *
     * @return path to follow or null.
     */
    @Override
    public final Path call() {
        try {
            return this.search();
        } catch (final Exception e) {
            // Log everything, so exceptions of the pathfinding-thread show in Log
            Citadel.LOGGER.warn("Pathfinding Exception", e);
        }

        return null;
    }

    /**
     * Perform the search.
     *
     * @return Path of a path to the given location, a best-effort, or null.
     */
    protected Path search() {
        MNode bestNode = this.getAndSetupStartNode();

        double bestNodeResultScore = Double.MAX_VALUE;

        while (!this.nodesOpen.isEmpty()) {
            if (Thread.currentThread().isInterrupted()) {
                return null;
            }

            final MNode currentNode = this.nodesOpen.poll();

            this.totalNodesVisited++;

            // Limiting max amount of nodes mapped
            if (this.totalNodesVisited > PathfindingConstants.maxPathingNodes || this.totalNodesVisited > this.maxRange * this.maxRange) {
                break;
            }
            currentNode.setCounterVisited(this.totalNodesVisited);

            this.handleDebugOptions(currentNode);
            currentNode.setClosed();

            final boolean isViablePosition =
                    this.isInRestrictedArea(currentNode.pos) && getSurfaceType(this.world, this.world.getBlockState(currentNode.pos.down()), currentNode.pos.down()) == SurfaceType.WALKABLE;
            if (isViablePosition && this.isAtDestination(currentNode)) {
                bestNode = currentNode;
                this.result.setPathReachesDestination(true);
                break;
            }

            //  If this is the closest MNode to our destination, treat it as our best node
            final double nodeResultScore =
                    this.getNodeResultScore(currentNode);
            if (isViablePosition && nodeResultScore < bestNodeResultScore && !currentNode.isCornerNode()) {
                bestNode = currentNode;
                bestNodeResultScore = nodeResultScore;
            }

            // if xz soft-restricted we can walk outside the restricted area to be able to find ways around back to the area
            if (!this.hardXzRestriction || isViablePosition) {
                this.walkCurrentNode(currentNode);
            }
        }

        return this.finalizePath(bestNode);
    }

    private void handleDebugOptions(final MNode currentNode) {
        if (this.debugDrawEnabled) {
            this.addNodeToDebug(currentNode);
        }

        if (Pathfinding.isDebug()) {
            Citadel.LOGGER.info(String.format("Examining MNode [%d,%d,%d] ; g=%f ; f=%f",
                    currentNode.pos.getX(), currentNode.pos.getY(), currentNode.pos.getZ(), currentNode.getCost(), currentNode.getScore()));
        }
    }

    private void addNodeToDebug(final MNode currentNode) {
        this.debugNodesNotVisited.remove(currentNode);
        this.debugNodesVisited.add(currentNode);
    }

    private void addPathNodeToDebug(final MNode node) {
        this.debugNodesVisited.remove(node);
        this.debugNodesPath.add(node);
    }

    private void walkCurrentNode(final MNode currentNode) {
        BlockPos dPos = BLOCKPOS_IDENTITY;
        if (currentNode.parent != null) {
            dPos = currentNode.pos.subtract(currentNode.parent.pos);
        }

        //  On a ladder, we can go 1 straight-up
        if (this.onLadderGoingUp(currentNode, dPos)) {
            this.walk(currentNode, BLOCKPOS_UP);
        }

        //  We can also go down 1, if the lower block is a ladder
        if (this.onLadderGoingDown(currentNode, dPos)) {
            this.walk(currentNode, BLOCKPOS_DOWN);
        }
        if (this.pathingOptions.canClimb()) {
            //If the entity can climb and it needs to climb a block higher than 1 block
            //TODO: Add code for climbing downwards
            if (this.getHighest(currentNode).getFirst() > 1) {
                this.walk(currentNode, BLOCKPOS_IDENTITY.up(this.getHighest(currentNode).getFirst()));
            }
            //After entity has climbed something step forward
            if (currentNode.parent != null && dPos.getX() == 0 && dPos.getZ() == 0 && dPos.getY() > 1) {
                //Step forwards into the direction we climbed from
                if (this.getHighest(currentNode.parent).getSecond() != null)
                    this.walk(currentNode, this.getHighest(currentNode.parent).getSecond());
            }
        }

        // Only explore downwards when dropping
        if ((currentNode.parent == null || !currentNode.parent.pos.equals(currentNode.pos.down())) && currentNode.isCornerNode()) {
            this.walk(currentNode, BLOCKPOS_DOWN);
            return;
        }

        // Walk downwards MNode if passable
        if ((this.circumventSizeCheck && this.isPassable(currentNode.pos.down(), false, currentNode.parent) && (!currentNode.isSwimming() && this.isLiquid(this.world.getBlockState(currentNode.pos.down()))))
                || currentNode.parent != null && this.isPassableBBDown(currentNode.parent.pos, currentNode.pos.down(), currentNode.parent)) {
            this.walk(currentNode, BLOCKPOS_DOWN);
        }

        // N
        if (dPos.getZ() <= 0) {
            this.walk(currentNode, BLOCKPOS_NORTH);
        }

        // E
        if (dPos.getX() >= 0) {
            this.walk(currentNode, BLOCKPOS_EAST);
        }

        // S
        if (dPos.getZ() >= 0) {
            this.walk(currentNode, BLOCKPOS_SOUTH);
        }

        // W
        if (dPos.getX() <= 0) {
            this.walk(currentNode, BLOCKPOS_WEST);
        }


    }

    private boolean onLadderGoingDown(final MNode currentNode, final BlockPos dPos) {
        return (dPos.getY() <= 0 || dPos.getX() != 0 || dPos.getZ() != 0) && this.isLadder(currentNode.pos.down());
    }


    private MNode getAndSetupStartNode() {
        MNode startNode = new MNode(this.start, this.computeHeuristic(this.start));
        // If the entity is Flying set the start MNode to the end node
        // Basically letting its pathfinder do the pathfinding
        if (this.pathingOptions.isFlying() && this.start.isWithinDistance(this.end, this.maxRange)) {
            startNode = new MNode(this.end, this.computeHeuristic(this.end));
        }
        if (this.isLadder(this.start)) {
            startNode.setLadder();
        } else if (this.isLiquid(this.world.getBlockState(this.start.down()))) {
            startNode.setSwimming();
        }

        startNode.setOnRails(this.pathingOptions.canUseRails() && this.world.getBlockState(this.start).getBlock() instanceof AbstractRailBlock);

        this.nodesOpen.offer(startNode);
        this.nodesVisited.put(computeNodeKey(this.start), startNode);

        ++this.totalNodesAdded;

        return startNode;
    }

    /**
     * Check if this is a liquid state for swimming.
     *
     * @param state the state to check.
     * @return true if so.
     */
    public boolean isLiquid(final BlockState state) {
        return state.isLiquid() || (!state.blocksMovement() && !state.getFluidState().isEmpty());
    }

    /**
     * Generate the path to the target node.
     *
     * @param targetNode the MNode to path to.
     * @return the path.
     */
    private Path finalizePath(final MNode targetNode) {
        //  Compute length of path, since we need to allocate an array.  This is cheaper/faster than building a List
        //  and converting it.  Yes, we have targetNode.steps, but I do not want to rely on that being accurate (I might
        //  fudge that value later on for cutoff purposes
        int pathLength = 1;
        int railsLength = 0;
        MNode node = targetNode;
        while (node.parent != null) {
            ++pathLength;
            if (node.isOnRails()) {
                ++railsLength;
            }
            node = node.parent;
        }

        final PathNode[] points = new PathNode[pathLength];
        points[0] = new PathPointExtended(node.pos);
        if (this.debugDrawEnabled) {
            this.addPathNodeToDebug(node);
        }

        MNode nextInPath = null;
        PathNode next = null;
        node = targetNode;
        while (node.parent != null) {
            if (this.debugDrawEnabled) {
                this.addPathNodeToDebug(node);
            }

            --pathLength;

            final BlockPos pos = node.pos;

            if (node.isSwimming()) {
                //  Not truly necessary but helps prevent them spinning in place at swimming nodes
                pos.add(BLOCKPOS_DOWN);
            }

            final PathPointExtended p = new PathPointExtended(pos);
            if (railsLength >= 8) {
                p.setOnRails(node.isOnRails());
                if (p.isOnRails() && (!node.parent.isOnRails() || node.parent.parent == null)) {
                    p.setRailsEntry();
                } else if (p.isOnRails() && points.length > pathLength + 1) {
                    final PathPointExtended point = ((PathPointExtended) points[pathLength + 1]);
                    if (!point.isOnRails()) {
                        point.setRailsExit();
                    }
                }
            }

            //  Climbing on a ladder?
            if (onALadder(node, nextInPath, pos)) {
                p.setOnLadder(true);
                if (nextInPath.pos.getY() > pos.getY()) {
                    //  We only care about facing if going up
                    //In the case of BlockVines (Which does not have Direction) we have to check the metadata of the vines... bitwise...
                    setLadderFacing(this.world, pos, p);
                }
            } else if (onALadder(node.parent, node.parent, pos)) {
                p.setOnLadder(true);
            }

            if (next != null) {
                next.previous = p;
            }
            next = p;
            points[pathLength] = p;

            nextInPath = node;
            node = node.parent;
        }

        this.doDebugPrinting(points);

        return new Path(Arrays.asList(points), this.getPathTargetPos(targetNode), this.isAtDestination(targetNode));
    }

    /**
     * Creates the path for the given points
     *
     * @param finalNode
     * @return
     */
    protected BlockPos getPathTargetPos(final MNode finalNode) {
        return finalNode.pos;
    }

    /**
     * Turns on debug printing.
     *
     * @param points the points to print.
     */
    private void doDebugPrinting(final PathNode[] points) {
        if (Pathfinding.isDebug()) {
            Citadel.LOGGER.info("Path found:");

            for (final PathNode p : points) {
                Citadel.LOGGER.info(String.format("Step: [%d,%d,%d]", p.x, p.y, p.z));
            }

            Citadel.LOGGER.info(String.format("Total Nodes Visited %d / %d", this.totalNodesVisited, this.totalNodesAdded));
        }
    }

    /**
     * Compute the heuristic cost ('h' value) of a given position x,y,z.
     * <p>
     * Returning a value of 0 performs a breadth-first search. Returning a value less than actual possible cost to goal guarantees shortest path, but at computational expense.
     * Returning a value exactly equal to the cost to the goal guarantees shortest path and least expense (but generally. only works when path is straight and unblocked). Returning
     * a value greater than the actual cost to goal produces good, but not perfect paths, and is fast. Returning a very high value (such that 'h' is very high relative to 'g') then
     * only 'h' (the heuristic) matters as the search will be a very fast greedy best-first-search, ignoring cost weighting and distance.
     *
     * @param pos Position to compute heuristic from.
     * @return the heuristic.
     */
    protected abstract double computeHeuristic(BlockPos pos);

    /**
     * Return true if the given MNode is a viable final destination, and the path should generate to here.
     *
     * @param n MNode to test.
     * @return true if the MNode is a viable destination.
     */
    protected abstract boolean isAtDestination(MNode n);

    /**
     * Compute a 'result score' for the Node; if no destination is determined, the MNode that had the highest 'result' score is used.
     *
     * @param n MNode to test.
     * @return score for the node.
     */
    protected abstract double getNodeResultScore(MNode n);

    /**
     * "Walk" from the parent in the direction specified by the delta, determining the new x,y,z position for such a move and adding or updating a node, as appropriate.
     *
     * @param parent MNode being walked from.
     * @param dPos   Delta from parent, expected in range of [-1..1].
     */
    protected final void walk(final MNode parent, BlockPos dPos) {
        BlockPos pos = parent.pos.add(dPos);

        //  Can we traverse into this node?  Fix the y up
        final int newY = this.getGroundHeight(parent, pos);

        if (newY < this.world.getBottomY()) {
            return;
        }

        boolean corner = false;
        if (pos.getY() != newY) {
            // if the new position is above the current node, we're taking the MNode directly above
            if (!parent.isCornerNode() && newY - pos.getY() > 0 && (parent.parent == null || !parent.parent.pos.equals(parent.pos.add(new BlockPos(0, newY - pos.getY(), 0))))) {
                dPos = new BlockPos(0, newY - pos.getY(), 0);
                pos = parent.pos.add(dPos);
                corner = true;
            }
            // If we're going down, take the air-corner before going to the lower node
            else if (!parent.isCornerNode() && newY - pos.getY() < 0 && (dPos.getX() != 0 || dPos.getZ() != 0) && (parent.parent == null || !parent.pos.down()
                    .equals(parent.parent.pos))) {
                dPos = new BlockPos(dPos.getX(), 0, dPos.getZ());
                pos = parent.pos.add(dPos);
                corner = true;
            }
            // Fix up normal y but only if we aren't climbing
            else if (!(this.pathingOptions.canClimb() && dPos.getY() > 1)) {
                dPos = dPos.add(0, newY - pos.getY(), 0);
                pos = new BlockPos(pos.getX(), newY, pos.getZ());
            }
        }

        int nodeKey = computeNodeKey(pos);
        MNode node = this.nodesVisited.get(nodeKey);
        if (nodeClosed(node)) {
            //  Early out on closed nodes (closed = expanded from)
            return;
        }

        final boolean isSwimming = calculateSwimming(this.world, pos, node);

        if (isSwimming && !this.pathingOptions.canSwim()) {
            return;
        }

        final boolean swimStart = isSwimming && !parent.isSwimming();
        final BlockState state = this.world.getBlockState(pos);
        final boolean onRoad = false;
        final boolean onRails = this.pathingOptions.canUseRails() && this.world.getBlockState(corner ? pos.down() : pos).getBlock() instanceof AbstractRailBlock;
        final boolean railsExit = !onRails && parent != null && parent.isOnRails();
        //  Cost may have changed due to a jump up or drop
        final double stepCost = this.computeCost(dPos, isSwimming, onRails, railsExit, swimStart, corner, state, pos);
        final double heuristic = this.computeHeuristic(pos);
        final double cost = parent.getCost() + stepCost;
        final double score = cost + heuristic;

        if (node == null) {
            node = this.createNode(parent, pos, nodeKey, isSwimming, heuristic, cost, score);
            node.setOnRails(onRails);
            node.setCornerNode(corner);
        } else if (this.updateCurrentNode(parent, node, heuristic, cost, score)) {
            return;
        }

        this.nodesOpen.offer(node);

        //If we climbed something skip jumpPointSearch
        //This is a workaround so that the path generated doesn't go through blocks
        if (this.pathingOptions.canClimb() && dPos.getY() > 1)
            return;

        //  Jump Point Search-ish optimization:
        // If this MNode was a (heuristic-based) improvement on our parent,
        // lets go another step in the same direction...
        this.performJumpPointSearch(parent, dPos, node);

    }

    private void performJumpPointSearch(final MNode parent, final BlockPos dPos, final MNode node) {
        if (this.allowJumpPointSearchTypeWalk && node.getHeuristic() <= parent.getHeuristic()) {
            this.walk(node, dPos);
        }
    }

    private MNode createNode(
            final MNode parent, final BlockPos pos, final int nodeKey,
            final boolean isSwimming, final double heuristic, final double cost, final double score) {
        final MNode node;
        node = new MNode(parent, pos, cost, heuristic, score);
        this.nodesVisited.put(nodeKey, node);
        if (this.debugDrawEnabled) {
            this.debugNodesNotVisited.add(node);
        }

        if (this.isLadder(pos)) {
            node.setLadder();
        } else if (isSwimming) {
            node.setSwimming();
        }

        this.totalNodesAdded++;
        node.setCounterAdded(this.totalNodesAdded);
        return node;
    }

    private boolean updateCurrentNode(final MNode parent, final MNode node, final double heuristic, final double cost, final double score) {
        //  This MNode already exists
        if (score >= node.getScore()) {
            return true;
        }

        if (!this.nodesOpen.remove(node)) {
            return true;
        }

        node.parent = parent;
        node.setSteps(parent.getSteps() + 1);
        node.setCost(cost);
        node.setHeuristic(heuristic);
        node.setScore(score);
        return false;
    }

    /**
     * Get the height of the ground at the given x,z coordinate, within 1 step of y.
     *
     * @param parent parent node.
     * @param pos    coordinate of block.
     * @return y height of first open, viable block above ground, or -1 if blocked or too far a drop.
     */
    protected int getGroundHeight(final MNode parent, final BlockPos pos) {
        //  Check (y+1) first, as it's always needed, either for the upper body (level),
        //  lower body (headroom drop) or lower body (jump up)
        if (this.checkHeadBlock(parent, pos)) {
            return this.handleTargetNotPassable(parent, pos.up(), this.world.getBlockState(pos.up()));
        }

        //  Now check the block we want to move to
        final BlockState target = this.world.getBlockState(pos);
        if (parent != null && !this.isPassableBB(parent.pos, pos, parent)) {
            return this.handleTargetNotPassable(parent, pos, target);
        }

        //  Do we have something to stand on in the target space?
        int i = 0;
        BlockState below = null;
        SurfaceType lastSurfaceType = null;
        while (i < this.maxNavigableGroundDist) {
            i++;
            below = this.world.getBlockState(pos.down(i));
            if (this.pathingOptions.isFlying()) {
                lastSurfaceType = this.isFlyable(below, pos, parent);
                if (lastSurfaceType == SurfaceType.FLYABLE) {
                    return pos.getY();
                }
            } else {
                lastSurfaceType = this.isWalkableSurface(below, pos);
                if (lastSurfaceType == SurfaceType.WALKABLE) {
                    //  Level path
                    return pos.getY();
                }
            }
        }
        return lastSurfaceType != SurfaceType.NOT_PASSABLE && below != null ? this.handleNotStanding(parent, pos, below) : -1;
    }

    private int handleNotStanding(final MNode parent, final BlockPos pos, final BlockState below) {
        final boolean isSwimming = parent != null && parent.isSwimming();

        if (this.isLiquid(below)) {
            return this.handleInLiquid(pos, below, isSwimming);
        }

        if (this.isLadder(below.getBlock(), pos.down())) {
            return pos.getY();
        }

        return this.checkDrop(parent, pos, isSwimming);
    }

    private int checkDrop(final MNode parent, final BlockPos pos, final boolean isSwimming) {
        final boolean canDrop = parent != null && !parent.isLadder();
        boolean isChonker = true;

        if (this.pathingOptions.canClimb() && parent != null && pos.getY() > parent.pos.getY() + 1) {
            return pos.getY();
        }
        //  Nothing to stand on
        if (!isChonker) {
            if (!canDrop || isSwimming || ((parent.pos.getX() != pos.getX() || parent.pos.getZ() != pos.getZ()) && this.isPassableBBFull(parent.pos.down(), parent)
                    && this.isWalkableSurface(this.world.getBlockState(parent.pos.down()), parent.pos.down()) == SurfaceType.DROPABLE)) {
                return -1;
            }
        }

        for (int i = 2; i <= 10; i++) {
            final BlockState below = this.world.getBlockState(pos.down(i));
            if (this.isWalkableSurface(below, pos) == SurfaceType.WALKABLE && i <= 4 || below.isLiquid()) {
                //  Level path
                return pos.getY() - i + 1;
            } else if (below.isAir()) {
                return -1;
            }
        }

        return -1;
    }

    private int handleInLiquid(final BlockPos pos, final BlockState below, final boolean isSwimming) {
        if (isSwimming) {
            //  Already swimming in something, or allowed to swim and this is water
            return pos.getY();
        }

        if (this.pathingOptions.canSwim() && isWater(this.world, pos.down())) {
            //  This is water, and we are allowed to swim
            return pos.getY();
        }

        //  Not allowed to swim or this isn't water, and we're on dry land
        return -1;
    }

    private int handleTargetNotPassable(final MNode parent, final BlockPos pos, final BlockState target) {
        final boolean canJump = parent != null && !parent.isLadder() && !parent.isSwimming();
        //  Need to try jumping up one, if we can
        if (!canJump || getSurfaceType(this.world, target, pos) != SurfaceType.WALKABLE) {
            return -1;
        }
/*
        //  Check for headroom in the target space
        if (!isPassableBB(parent.pos, pos.up())) {
            return -1;
        }*/
        //  Check for headroom in the target space
        if (!this.isPassable(pos.up(2), false, parent)) {
            final VoxelShape bb1 = this.world.getBlockState(pos).getSidesShape(this.world, pos);
            final VoxelShape bb2 = this.world.getBlockState(pos.up(2)).getSidesShape(this.world, pos.up(2));
            if ((pos.up(2).getY() + this.getStartY(bb2)) - (pos.getY() + this.getEndY(bb1)) < 2) {
                return -1;
            }
        }

        //  Check for jump room from the origin space
        if (!this.isPassable(parent.pos.up(2), false, parent)) {
            final VoxelShape bb1 = this.world.getBlockState(pos).getSidesShape(this.world, pos);
            final VoxelShape bb2 = this.world.getBlockState(parent.pos.up(2)).getSidesShape(this.world, parent.pos.up(2));
            if ((parent.pos.up(2).getY() + this.getStartY(bb2)) - (pos.getY() + this.getEndY(bb1)) < 2) {
                return -1;
            }
        }

        final BlockState parentBelow = this.world.getBlockState(parent.pos.down());
        final VoxelShape parentBB = parentBelow.getSidesShape(this.world, parent.pos.down());

        double parentY = parentBB.getMax(Direction.Axis.Y);
        double parentMaxY = parentY + parent.pos.down().getY();
        final double targetMaxY = target.getSidesShape(this.world, pos).getMax(Direction.Axis.Y) + pos.getY();
        if (targetMaxY - parentMaxY < this.maxJumpHeight) {
            return pos.getY() + 1;
        }
        if (target.getBlock() instanceof StairsBlock
                && parentY - HALF_A_BLOCK < this.maxJumpHeight
                && target.get(StairsBlock.HALF) == BlockHalf.BOTTOM
                && getXZFacing(parent.pos, pos) == target.get(StairsBlock.FACING)) {
            return pos.getY() + 1;
        }
        return -1;
    }

    /*
    Get's the maximum height of a climbable column and the direction the column can be climbed from
     */
    private Pair<Integer, BlockPos> getHighest(MNode node) {
        int max = 1;
        BlockPos pos = node.pos;
        BlockPos direction = null;
        if (this.world.getBlockState(pos.north()).isOpaque()) {
            if (this.climbableTop(pos.north(), Direction.SOUTH, node) > max) {
                max = this.climbableTop(pos.north(), Direction.SOUTH, node);
                direction = BLOCKPOS_NORTH;
            }
        }
        if (this.world.getBlockState(pos.east()).isOpaque()) {
            if (this.climbableTop(pos.east(), Direction.WEST, node) > max) {
                max = this.climbableTop(pos.east(), Direction.WEST, node);
                direction = BLOCKPOS_EAST;
            }
        }
        if (this.world.getBlockState(pos.south()).isOpaque()) {
            if (this.climbableTop(pos.south(), Direction.NORTH, node) > max) {
                max = this.climbableTop(pos.south(), Direction.NORTH, node);
                direction = BLOCKPOS_SOUTH;
            }
        }
        if (this.world.getBlockState(pos.west()).isOpaque()) {
            if (this.climbableTop(pos.west(), Direction.EAST, node) > max) {
                max = this.climbableTop(pos.west(), Direction.EAST, node);
                direction = BLOCKPOS_WEST;
            }
        }
        return new Pair<>(max, direction);
    }

    /*
    Keeps going up a column centered at pos checking if in the direction there's free space
     */
    private int climbableTop(BlockPos pos, Direction direction, MNode node) {
        BlockState target = this.world.getBlockState(pos);
        BlockState origin;
        int i = 0;
        //TODO: Use collision shapes of blocks
        while (target.isOpaque()) {
            pos = pos.up();
            target = this.world.getBlockState(pos);
            origin = this.world.getBlockState(pos.offset(direction));
            //If the climbable side can't be passed reset height to 0
            if (!this.isPassable(origin, pos.offset(direction), node)) {
                i = 0;
                break;
            }
            i++;
        }
        return i;
    }

    private boolean checkHeadBlock(final MNode parent, final BlockPos pos) {
        BlockPos localPos = pos;
        final VoxelShape bb = this.world.getBlockState(localPos).getCollisionShape(this.world, localPos);
        if (bb.getMax(Direction.Axis.Y) < 1) {
            localPos = pos.up();
        }

        if (parent == null || !this.isPassableBB(parent.pos, pos.up(), parent)) {
            final VoxelShape bb1 = this.world.getBlockState(pos.down()).getSidesShape(this.world, pos.down());
            final VoxelShape bb2 = this.world.getBlockState(pos.up()).getSidesShape(this.world, pos.up());
            if ((pos.up().getY() + this.getStartY(bb2)) - (pos.down().getY() + this.getEndY(bb1)) < 2) {
                return true;
            }
            if (parent != null) {
                final VoxelShape bb3 = this.world.getBlockState(parent.pos.down()).getSidesShape(this.world, pos.down());
                if ((pos.up().getY() + this.getStartY(bb2)) - (parent.pos.down().getY() + this.getEndY(bb3)) < 1.75) {
                    return true;
                }
            }
        }

        if (parent != null) {
            final BlockState hereState = this.world.getBlockState(localPos.down());
            final VoxelShape bb1 = this.world.getBlockState(pos).getSidesShape(this.world, pos);
            final VoxelShape bb2 = this.world.getBlockState(localPos.up()).getSidesShape(this.world, localPos.up());
            if ((localPos.up().getY() + this.getStartY(bb2)) - (pos.getY() + this.getEndY(bb1)) >= 2) {
                return false;
            }

            return this.isLiquid(hereState) && !this.isPassable(pos, false, parent);
        }
        return false;
    }

    /**
     * Get the start y of a voxelshape.
     *
     * @param bb the voxelshape.
     * @return the start y.
     */
    private double getStartY(final VoxelShape bb) {
        return bb.isEmpty() ? 1 : bb.getMin(Direction.Axis.Y);
    }

    /**
     * Get the end y of a voxelshape.
     *
     * @param bb the voxelshape.
     * @return the end y.
     */
    private double getEndY(final VoxelShape bb) {
        return bb.isEmpty() ? 0 : bb.getMax(Direction.Axis.Y);
    }

    /**
     * Is the space passable.
     *
     * @param block the block we are checking.
     * @return true if the block does not block movement.
     */
    protected boolean isPassable(final BlockState block, final BlockPos pos, final MNode parent) {
        final BlockPos parentPos = parent == null ? this.start : parent.pos;
        final BlockState parentBlock = this.world.getBlockState(parentPos);

        if (parentBlock.getBlock() instanceof TrapdoorBlock) {
            final BlockPos dir = pos.subtract(parentPos);
            if (dir.getX() != 0 || dir.getZ() != 0) {
                // Check if we can leave the current block, there might be a trapdoor or panel blocking us.
                final Direction direction = getXZFacing(parentPos, pos);
                final Direction facing = parentBlock.get(TrapdoorBlock.FACING);
                if (direction == facing.getOpposite()) {
                    return false;
                }
            }
        }

        if (!block.isAir()) {
            final VoxelShape shape = block.getSidesShape(this.world, pos);
            if (block.blocksMovement() && !(shape.isEmpty() || shape.getMax(Direction.Axis.Y) <= 0.1)) {
                if (block.getBlock() instanceof TrapdoorBlock) {
                    final BlockPos dir = pos.subtract(parentPos);
                    if (dir.getY() != 0 && dir.getX() == 0 && dir.getZ() == 0) {
                        return true;
                    }

                    final Direction direction = getXZFacing(parentPos, pos);
                    final Direction facing = block.get(TrapdoorBlock.FACING);

                    // We can enter a space of a trapdoor if it's facing the same direction
                    if (direction == facing.getOpposite()) {
                        return true;
                    }

                    // We cannot enter a space of a trapdoor if its facing the opposite direction.
                    return direction != facing;
                } else {
                    return this.pathingOptions.canEnterDoors() && (block.getBlock() instanceof DoorBlock
                            || block.getBlock() instanceof FenceGateBlock)
                            || block.getBlock() instanceof PressurePlateBlock
                            || block.getBlock() instanceof AbstractSignBlock
                            || block.getBlock() instanceof AbstractBannerBlock;
                }
            } else if (block.getBlock() instanceof FireBlock || block.getBlock() instanceof SweetBerryBushBlock) {
                return false;
            } else {
                if (this.isLadder(block.getBlock(), pos)) {
                    return true;
                }

                // TODO: I'd be cool if dragons could squash multiple snow layers when walking over them
                if (shape.isEmpty() || shape.getMax(Direction.Axis.Y) <= 0.125 && !this.isLiquid((block)) && (block.getBlock() != Blocks.SNOW || block.get(SnowBlock.LAYERS) == 1)) {
                    final PathNodeType pathType = PathUtil.getAiPathNodeType(block, this.world, pos);
                    return pathType == null || PathUtil.getDanger(pathType) == null;
                }
                return false;
            }
        }

        return true;
    }

    // TODO :: Expensive performance-wise
    protected boolean isPassable(final BlockPos pos, final boolean head, final MNode parent) {
        final BlockState state = this.world.getBlockState(pos);
        final VoxelShape shape = state.getSidesShape(this.world, pos);

        if (this.passabilityNavigator != null && this.passabilityNavigator.isBlockExplicitlyNotPassable(state, pos, pos)) {
            return false;
        }

        if ((shape.isEmpty() || shape.getMax(Direction.Axis.Y) <= 0.1)) {
            if (this.passabilityNavigator != null && this.passabilityNavigator.isBlockExplicitlyPassable(state, pos, pos))
                return this.isPassable(state, pos, parent);
            return !head
                    || !(state.getBlock() instanceof DyedCarpetBlock)
                    || this.isLadder(state.getBlock(), pos);
        }
        return this.isPassable(state, pos, parent);
    }

    protected boolean isPassableBBFull(final BlockPos pos, MNode parent) {
        if (this.circumventSizeCheck) {
            return this.isPassable(pos, false, parent) && this.isPassable(pos.up(), true, parent);
        } else {
            for (int i = 0; i <= this.entitySizeXZ; i++) {
                for (int j = 0; j <= this.entitySizeY; j++) {
                    for (int k = 0; k <= this.entitySizeXZ; k++) {
                        if (!this.isPassable(pos.add(i, j, k), false, parent)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    protected boolean isPassableBB(final BlockPos parentPos, final BlockPos pos, MNode parent) {
        if (this.circumventSizeCheck) {
            return this.isPassable(pos, false, parent) && this.isPassable(pos.up(), true, parent);
        } else {

            Direction facingDir = getXZFacing(parentPos, pos);
            if (facingDir == Direction.DOWN || facingDir == Direction.UP)
                return false;
            facingDir = facingDir.rotateYClockwise();
            for (int i = 0; i <= this.entitySizeXZ; i++) {
                for (int j = 0; j <= this.entitySizeY; j++) {
                    if (!this.isPassable(pos.offset(facingDir, i).up(j), false, parent)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /*
        Mobs that break blocks may consider the ground passable.
     */
    protected boolean isPassableBBDown(final BlockPos parentPos, final BlockPos pos, MNode parent) {
        if (this.circumventSizeCheck) {
            return this.isPassable(pos, true, parent);
        } else {
            Direction facingDir = getXZFacing(parentPos, pos);
            if (facingDir == Direction.DOWN || facingDir == Direction.UP)
                return false;
            facingDir = facingDir.rotateYClockwise();
            for (int i = 0; i <= this.entitySizeXZ; i++) {
                for (int j = 0; j <= this.entitySizeY; j++) {
                    if (!this.isPassable(pos.offset(facingDir, i).up(j), false, parent) || pos.getY() <= parentPos.getY()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * Is the block solid and can be stood upon.
     *
     * @param blockState Block to check.
     * @param pos        the position.
     * @return true if the block at that location can be walked on.
     */

    protected SurfaceType isFlyable(final BlockState blockState, final BlockPos pos, MNode parent) {
        final Block block = blockState.getBlock();
        if (block instanceof FenceBlock
                || block instanceof FenceGateBlock
                || block instanceof WallBlock
                || block instanceof FireBlock
                || block instanceof CampfireBlock
                || block instanceof BambooBlock
                || block instanceof BambooSaplingBlock
                || (blockState.getOutlineShape(this.world, pos).getMax(Direction.Axis.Y) > 1.0)) {
            return SurfaceType.NOT_PASSABLE;
        }
        final FluidState fluid = this.world.getFluidState(pos);
        if (fluid != null && !fluid.isEmpty() && (fluid.getFluid() == Fluids.LAVA || fluid.getFluid() == Fluids.FLOWING_LAVA)) {
            return SurfaceType.NOT_PASSABLE;
        }
        if (this.isPassable(blockState, pos, parent)) {
            return SurfaceType.FLYABLE;
        }
        return SurfaceType.DROPABLE;
    }

    protected SurfaceType isWalkableSurface(final BlockState blockState, final BlockPos pos) {
        final Block block = blockState.getBlock();
        if (block instanceof FenceBlock
                || block instanceof FenceGateBlock
                || block instanceof WallBlock
                || block instanceof FireBlock
                || block instanceof CampfireBlock
                || block instanceof BambooBlock
                || block instanceof BambooSaplingBlock
                || (blockState.getOutlineShape(this.world, pos).getMax(Direction.Axis.Y) > 1.0)) {
            return SurfaceType.NOT_PASSABLE;
        }

        final FluidState fluid = this.world.getFluidState(pos);
        if (fluid != null && !fluid.isEmpty() && (fluid.getFluid() == Fluids.LAVA || fluid.getFluid() == Fluids.FLOWING_LAVA)) {
            return SurfaceType.NOT_PASSABLE;
        }

        if (block instanceof AbstractSignBlock) {
            return SurfaceType.DROPABLE;
        }

        if (blockState.isSolid()
                || (blockState.getBlock() == Blocks.SNOW && blockState.get(SnowBlock.LAYERS) > 1)
                || block instanceof DyedCarpetBlock) {
            return SurfaceType.WALKABLE;
        }

        return SurfaceType.DROPABLE;
    }

    /**
     * Is the block a ladder.
     *
     * @param block block to check.
     * @param pos   location of the block.
     * @return true if the block is a ladder.
     */
    protected boolean isLadder(final Block block, final BlockPos pos) {
        return BlockUtil.isLadder(block);
    }

    protected boolean isLadder(final BlockPos pos) {
        return this.isLadder(this.world.getBlockState(pos).getBlock(), pos);
    }

    /**
     * Sets the pathing options
     *
     * @param pathingOptions the pathing options to set.
     */
    public void setPathingOptions(final PathingOptions pathingOptions) {
        this.pathingOptions = pathingOptions;
    }

    /**
     * Check if in restricted area.
     *
     * @param pos the pos to check.
     * @return true if so.
     */
    public boolean isInRestrictedArea(final BlockPos pos) {
        if (this.restrictionType == AbstractAdvancedPathNavigate.RestrictionType.NONE) {
            return true;
        }

        final boolean isInXZ = pos.getX() <= this.maxX && pos.getZ() <= this.maxZ && pos.getZ() >= this.minZ && pos.getX() >= this.minX;
        if (!isInXZ) {
            return false;
        }

        if (this.restrictionType == AbstractAdvancedPathNavigate.RestrictionType.XZ) {
            return true;
        }
        return pos.getY() <= this.maxY && pos.getY() >= this.minY;
    }

}
