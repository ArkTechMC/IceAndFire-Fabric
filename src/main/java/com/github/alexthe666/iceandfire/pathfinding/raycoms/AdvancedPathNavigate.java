package com.github.alexthe666.iceandfire.pathfinding.raycoms;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.pathfinding.NodeProcessorFly;
import com.github.alexthe666.iceandfire.pathfinding.NodeProcessorWalk;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.AbstractPathJob;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.PathJobMoveAwayFromLocation;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.PathJobMoveToLocation;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.PathJobRandomPos;
import com.github.alexthe666.iceandfire.util.WorldUtil;
import net.minecraft.block.LadderBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.ai.pathing.PathNodeNavigator;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

/**
 * Minecolonies async PathNavigate.
 */
public class AdvancedPathNavigate extends AbstractAdvancedPathNavigate {
    public static final double MIN_Y_DISTANCE = 0.001;
    public static final int MAX_SPEED_ALLOWED = 2;
    public static final double MIN_SPEED_ALLOWED = 0.1;

    private PathResult<AbstractPathJob> pathResult;

    /**
     * The world time when a path was added.
     */
    private long pathStartTime = 0;

    /**
     * Spawn pos of minecart.
     */
    private final BlockPos spawnedPos = BlockPos.ORIGIN;

    /**
     * Desired position to reach
     */
    private BlockPos desiredPos;

    /**
     * Timeout for the desired pos, resets when its no longer wanted
     */
    private int desiredPosTimeout = 0;

    /**
     * The stuck handler to use
     */
    private IStuckHandler stuckHandler;

    /**
     * Whether we did set sneaking
     */
    private boolean isSneaking = true;

    private double swimSpeedFactor = 1.0;

    private float width = 1;

    private float height = 1;

    public enum MovementType {
        WALKING,
        FLYING,
        CLIMBING
    }

    /**
     * Instantiates the navigation of an ourEntity.
     *
     * @param entity the ourEntity.
     * @param world  the world it is in.
     */
    public AdvancedPathNavigate(final MobEntity entity, final World world) {
        this(entity, world, MovementType.WALKING);
    }

    public AdvancedPathNavigate(final MobEntity entity, final World world, MovementType type) {
        this(entity, world, type, 1, 1);
    }

    public AdvancedPathNavigate(final MobEntity entity, final World world, MovementType type, float width, float height) {
        this(entity, world, type, width, height, PathingStuckHandler.createStuckHandler().withTeleportSteps(6).withTeleportOnFullStuck());
    }

    public AdvancedPathNavigate(final MobEntity entity, final World world, MovementType type, float width, float height, PathingStuckHandler stuckHandler) {
        super(entity, world);
        switch (type) {
            case FLYING:
                this.nodeMaker = new NodeProcessorFly();
                this.getPathingOptions().setIsFlying(true);
                break;
            case WALKING:
                this.nodeMaker = new NodeProcessorWalk();
                break;
            case CLIMBING:
                this.nodeMaker = new NodeProcessorWalk();
                this.getPathingOptions().setCanClimb(true);
                break;
        }
        this.nodeMaker.setCanEnterOpenDoors(true);
        this.getPathingOptions().setEnterDoors(true);
        this.nodeMaker.setCanOpenDoors(true);
        this.getPathingOptions().setCanOpenDoors(true);
        this.nodeMaker.setCanSwim(true);
        this.getPathingOptions().setCanSwim(true);
        this.width = width;
        this.height = height;
        this.stuckHandler = stuckHandler;
    }

    @Override
    public BlockPos getDestination() {
        return this.destination;
    }


    @Override
    public PathResult moveAwayFromXYZ(final BlockPos avoid, final double range, final double speedFactor, final boolean safeDestination) {
        final BlockPos start = AbstractPathJob.prepareStart(this.ourEntity);

        return this.setPathJob(new PathJobMoveAwayFromLocation(this.ourEntity.getWorld(),
                start,
                avoid,
                (int) range,
                (int) this.ourEntity.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).getValue(),
                this.ourEntity), null, speedFactor, safeDestination);
    }

    @Override
    public PathResult moveToRandomPos(final double range, final double speedFactor) {
        if (this.pathResult != null && this.pathResult.getJob() instanceof PathJobRandomPos) {
            return this.pathResult;
        }

        this.desiredPos = BlockPos.ORIGIN;
        final int theRange = (int) (this.entity.getRandom().nextInt((int) range) + range / 2);
        final BlockPos start = AbstractPathJob.prepareStart(this.ourEntity);

        return this.setPathJob(new PathJobRandomPos(this.ourEntity.getWorld(),
                start,
                theRange,
                (int) this.ourEntity.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).getValue(),
                this.ourEntity), null, speedFactor, true);
    }

    @Override
    public PathResult moveToRandomPosAroundX(final int range, final double speedFactor, final BlockPos pos) {
        if (this.pathResult != null
                && this.pathResult.getJob() instanceof PathJobRandomPos
                && ((((PathJobRandomPos) this.pathResult.getJob()).posAndRangeMatch(range, pos)))) {
            return this.pathResult;
        }

        this.desiredPos = BlockPos.ORIGIN;
        return this.setPathJob(new PathJobRandomPos(this.ourEntity.getWorld(),
                AbstractPathJob.prepareStart(this.ourEntity),
                3,
                (int) this.ourEntity.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).getValue(),
                range,
                this.ourEntity, pos), pos, speedFactor, true);
    }

    @Override
    public PathResult moveToRandomPos(final int range, final double speedFactor, final net.minecraft.util.Pair<BlockPos, BlockPos> corners, final RestrictionType restrictionType) {
        if (this.pathResult != null && this.pathResult.getJob() instanceof PathJobRandomPos) {
            return this.pathResult;
        }

        this.desiredPos = BlockPos.ORIGIN;
        final int theRange = this.entity.getRandom().nextInt(range) + range / 2;
        final BlockPos start = AbstractPathJob.prepareStart(this.ourEntity);

        return this.setPathJob(new PathJobRandomPos(this.ourEntity.getWorld(),
                start,
                theRange,
                (int) this.ourEntity.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).getValue(),
                this.ourEntity,
                corners.getLeft(),
                corners.getRight(),
                restrictionType), null, speedFactor, true);
    }

    public PathResult setPathJob(
            final AbstractPathJob job,
            final BlockPos dest,
            final double speedFactor, final boolean safeDestination) {
        this.stop();

        this.destination = dest;
        this.originalDestination = dest;
        if (safeDestination) {
            this.desiredPos = dest;
            if (dest != null) {
                this.desiredPosTimeout = 50 * 20;
            }
        }

        this.walkSpeedFactor = speedFactor;

        if (speedFactor > MAX_SPEED_ALLOWED || speedFactor < MIN_SPEED_ALLOWED) {
            IceAndFire.LOGGER.error("Tried to set a bad speed:" + speedFactor + " for entity:" + this.ourEntity, new Exception());
            return null;
        }

        job.setPathingOptions(this.getPathingOptions());
        this.pathResult = job.getResult();
        this.pathResult.startJob(Pathfinding.getExecutor());
        return this.pathResult;
    }

    @Override
    public boolean isIdle() {
        return (this.pathResult == null || this.pathResult.isFinished() && this.pathResult.getStatus() != PathFindingStatus.CALCULATION_COMPLETE) && super.isIdle();
    }

    @Override
    public void tick() {
        if (this.nodeMaker instanceof NodeProcessorWalk) {
            ((NodeProcessorWalk) this.nodeMaker).setEntitySize(this.width, this.height);
        } else {
            ((NodeProcessorFly) this.nodeMaker).setEntitySize(this.width, this.height);
        }
        if (this.desiredPosTimeout > 0) {
            if (this.desiredPosTimeout-- <= 0) {
                this.desiredPos = null;
            }
        }

        if (this.pathResult != null) {
            if (!this.pathResult.isFinished()) {
                return;
            } else if (this.pathResult.getStatus() == PathFindingStatus.CALCULATION_COMPLETE) {
                try {
                    this.processCompletedCalculationResult();
                } catch (InterruptedException | ExecutionException e) {
                    IceAndFire.LOGGER.catching(e);
                }
            }
        }

        int oldIndex = this.isIdle() ? 0 : this.getCurrentPath().getCurrentNodeIndex();

        if (this.isSneaking) {
            this.isSneaking = false;
            this.entity.setSneaking(false);
        }

        this.ourEntity.setUpwardSpeed(0);
        if (this.handleLadders(oldIndex)) {
            this.continueFollowingPath();
            this.stuckHandler.checkStuck(this);
            return;
        }
        if (this.handleRails()) {
            this.stuckHandler.checkStuck(this);
            return;
        }

        // The following block replaces mojangs super.tick(). Why you may ask? Because it's broken, that's why.
        // The moveHelper won't move up if standing in a block with an empty bounding box (put grass, 1 layer snow, mushroom in front of a solid block and have them try jump up).
        ++this.tickCount;
        if (!this.isIdle()) {
            if (this.isAtValidPosition()) {
                this.continueFollowingPath();
            } else if (this.currentPath != null && !this.currentPath.isFinished()) {
                Vec3d vector3d = this.getPos();
                Vec3d vector3d1 = this.currentPath.getNodePosition(this.entity);
                if (vector3d.y > vector3d1.y && !this.entity.isOnGround() && MathHelper.floor(vector3d.x) == MathHelper.floor(vector3d1.x) && MathHelper.floor(vector3d.z) == MathHelper.floor(vector3d1.z)) {
                    this.currentPath.next();
                }
            }

            DebugInfoSender.sendPathfindingData(this.world, this.entity, this.currentPath, this.nodeReachProximity);
            if (!this.isIdle()) {
                Vec3d vector3d2 = this.currentPath.getNodePosition(this.entity);
                BlockPos blockpos = BlockPos.ofFloored(vector3d2);
                if (isEntityBlockLoaded(this.world, blockpos)) {
                    this.entity.getMoveControl()
                            .moveTo(vector3d2.x,
                                    this.world.getBlockState(blockpos.down()).isAir() ? vector3d2.y : getSmartGroundY(this.world, blockpos),
                                    vector3d2.z,
                                    this.speed);
                }
            }
        }
        // End of super.tick.
        if (this.inRecalculationCooldown) {
            this.recalculatePath();
        }
        if (this.pathResult != null && this.isIdle()) {
            this.pathResult.setStatus(PathFindingStatus.COMPLETE);
            this.pathResult = null;
        }
        // TODO: should probably get updated
        // Make sure the entity isn't sleeping, tamed or chained when checking if it's stuck
        if (this.entity instanceof TameableEntity) {
            if (((TameableEntity) this.entity).isTamed())
                return;
            if (this.entity instanceof EntityDragonBase) {
                if (((EntityDragonBase) this.entity).isChained())
                    return;
                if (((EntityDragonBase) this.entity).isInSittingPose())
                    return;
            }

        }

        this.stuckHandler.checkStuck(this);
    }

    /**
     * Similar to WalkNodeProcessor.getGroundY but not broken.
     * This checks if the block below the position we're trying to move to reaches into the block above, if so, it has to aim a little bit higher.
     *
     * @param world the world.
     * @param pos   the position to check.
     * @return the next y level to go to.
     */
    public static double getSmartGroundY(final BlockView world, final BlockPos pos) {
        final BlockPos blockpos = pos.down();
        final VoxelShape voxelshape = world.getBlockState(blockpos).getSidesShape(world, blockpos);
        if (voxelshape.isEmpty() || voxelshape.getMax(Direction.Axis.Y) < 1.0) {
            return pos.getY();
        }
        return blockpos.getY() + voxelshape.getMax(Direction.Axis.Y);
    }

    @Override
    public PathResult moveToXYZ(final double x, final double y, final double z, final double speedFactor) {
        final int newX = MathHelper.floor(x);
        final int newY = (int) y;
        final int newZ = MathHelper.floor(z);

        if (this.pathResult != null && this.pathResult.getJob() instanceof PathJobMoveToLocation &&
                (
                        this.pathResult.isComputing()
                                || (this.destination != null && isEqual(this.destination, newX, newY, newZ))
                                || (this.originalDestination != null && isEqual(this.originalDestination, newX, newY, newZ))
                )
        ) {
            return this.pathResult;
        }

        final BlockPos start = AbstractPathJob.prepareStart(this.ourEntity);
        this.desiredPos = new BlockPos(newX, newY, newZ);

        return this.setPathJob(
                new PathJobMoveToLocation(this.ourEntity.getWorld(),
                        start,
                        this.desiredPos,
                        (int) this.ourEntity.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).getValue(),
                        this.ourEntity),
                this.desiredPos, speedFactor, true);
    }

    @Override
    public boolean tryMoveToBlockPos(final BlockPos pos, final double speedFactor) {
        this.moveToXYZ(pos.getX(), pos.getY(), pos.getZ(), speedFactor);
        return true;
    }

    //Return a new WalkNodeProcessor for safety reasons eg if the entity
    //has a passenger this method get's called and returning null is not a great idea
    @Override
    protected @NotNull PathNodeNavigator createPathNodeNavigator(final int p_179679_1_) {
        return new PathNodeNavigator(new LandPathNodeMaker(), p_179679_1_);
    }

    @Override
    protected boolean isAtValidPosition() {
        // Auto dismount when trying to path.
        if (this.ourEntity.getVehicle() != null) {
            final PathPointExtended pEx = (PathPointExtended) this.getCurrentPath().getNode(this.getCurrentPath().getCurrentNodeIndex());
            if (pEx.isRailsExit()) {
                final Entity entity = this.ourEntity.getVehicle();
                this.ourEntity.stopRiding();
                entity.remove(Entity.RemovalReason.DISCARDED);
            } else if (!pEx.isOnRails()) {
                if (this.destination == null || this.entity.squaredDistanceTo(this.destination.getX(), this.destination.getY(), this.destination.getZ()) > 2) {
                    this.ourEntity.stopRiding();
                }

            } else if ((Math.abs(pEx.x - this.entity.getX()) > 7 || Math.abs(pEx.z - this.entity.getZ()) > 7) && this.ourEntity.getVehicle() != null) {
                final Entity entity = this.ourEntity.getVehicle();
                this.ourEntity.stopRiding();
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
        }
        return true;
    }


    @Override
    protected @NotNull Vec3d getPos() {
        return this.ourEntity.getPos();
    }

    @Override
    public Path findPathTo(final @NotNull BlockPos pos, final int accuracy) {
        return null;
    }

    @Override
    protected boolean canPathDirectlyThrough(final @NotNull Vec3d start, final @NotNull Vec3d end) {
        // TODO improve road walking. This is better in some situations, but still not great.
        return super.canPathDirectlyThrough(start, end);
    }

    public double getSpeedFactor() {

        if (this.ourEntity.isTouchingWater()) {
            this.speed = this.walkSpeedFactor * this.swimSpeedFactor;
            return this.speed;
        }

        this.speed = this.walkSpeedFactor;
        return this.walkSpeedFactor;
    }

    @Override
    public void setSpeed(final double speedFactor) {
        if (speedFactor > MAX_SPEED_ALLOWED || speedFactor < MIN_SPEED_ALLOWED) {
            IceAndFire.LOGGER.debug("Tried to set a bad speed:" + speedFactor + " for entity:" + this.ourEntity);
            return;
        }
        this.walkSpeedFactor = speedFactor;
    }

    /**
     * Deprecated - try to use BlockPos instead
     */
    @Override
    public boolean startMovingTo(final double x, final double y, final double z, final double speedFactor) {
        if (x == 0 && y == 0 && z == 0) {
            return false;
        }

        this.moveToXYZ(x, y, z, speedFactor);
        return true;
    }

    @Override
    public boolean startMovingTo(final Entity entityIn, final double speedFactor) {
        return this.tryMoveToBlockPos(entityIn.getBlockPos(), speedFactor);
    }

    // Removes stupid vanilla stuff, causing our pathpoints to occasionally be replaced by vanilla ones.
    @Override
    protected void adjustPath() {
    }

    @Override
    public boolean startMovingAlong(final Path path, final double speedFactor) {
        if (path == null) {
            this.stop();
            return false;
        }
        this.pathStartTime = this.world.getTime();
        return super.startMovingAlong(this.convertPath(path), speedFactor);
    }

    /**
     * Converts the given path to a minecolonies path if needed.
     *
     * @param path given path
     * @return resulting path
     */
    private Path convertPath(final Path path) {
        final int pathLength = path.getLength();
        Path tempPath = null;
        if (pathLength > 0 && !(path.getNode(0) instanceof PathPointExtended)) {
            //  Fix vanilla PathPoints to be PathPointExtended
            final PathPointExtended[] newPoints = new PathPointExtended[pathLength];

            for (int i = 0; i < pathLength; ++i) {
                final PathNode point = path.getNode(i);
                if (!(point instanceof PathPointExtended)) {
                    newPoints[i] = new PathPointExtended(new BlockPos(point.x, point.y, point.z));
                } else {
                    newPoints[i] = (PathPointExtended) point;
                }
            }

            tempPath = new Path(Arrays.asList(newPoints), path.getTarget(), path.reachesTarget());

            final PathPointExtended finalPoint = newPoints[pathLength - 1];
            this.destination = new BlockPos(finalPoint.x, finalPoint.y, finalPoint.z);
        }

        return tempPath == null ? path : tempPath;
    }

    private boolean processCompletedCalculationResult() throws InterruptedException, ExecutionException {
        this.pathResult.getJob().synchToClient(this.entity);
        this.startMovingAlong(this.pathResult.getPath(), this.getSpeedFactor());

        if (this.pathResult != null)
            this.pathResult.setStatus(PathFindingStatus.IN_PROGRESS_FOLLOWING);
        return false;
    }

    private boolean handleLadders(int oldIndex) {
        //  Ladder Workaround
        if (!this.isIdle()) {
            final PathPointExtended pEx = (PathPointExtended) this.getCurrentPath().getNode(this.getCurrentPath().getCurrentNodeIndex());
            final PathPointExtended pExNext = this.getCurrentPath().getLength() > this.getCurrentPath().getCurrentNodeIndex() + 1
                    ? (PathPointExtended) this.getCurrentPath()
                    .getNode(this.getCurrentPath()
                            .getCurrentNodeIndex() + 1) : null;


            final BlockPos pos = new BlockPos(pEx.x, pEx.y, pEx.z);
            if (pEx.isOnLadder() && pExNext != null && (pEx.y != pExNext.y || this.entity.getY() > pEx.y) && this.world.getBlockState(pos).isLadder(this.world, pos, this.ourEntity)) {
                return this.handlePathPointOnLadder(pEx);
            } else if (this.ourEntity.isTouchingWater()) {
                return this.handleEntityInWater(oldIndex, pEx);
            } else if (this.world.random.nextInt(10) == 0) {
                if (!pEx.isOnLadder() && pExNext != null && pExNext.isOnLadder()) {
                    this.speed = this.getSpeedFactor() / 4.0;
                } else {
                    this.speed = this.getSpeedFactor();
                }
            }
        }
        return false;
    }

    /**
     * Determine what block the entity stands on
     *
     * @param parEntity the entity that stands on the block
     * @return the Blockstate.
     */
    private BlockPos findBlockUnderEntity(final Entity parEntity) {
        int blockX = (int) Math.round(parEntity.getX());
        int blockY = MathHelper.floor(parEntity.getY() - 0.2D);
        int blockZ = (int) Math.round(parEntity.getZ());
        return new BlockPos(blockX, blockY, blockZ);
    }

    /**
     * Handle rails navigation.
     *
     * @return true if block.
     */
    private boolean handleRails() {
        if (!this.isIdle()) {
            final PathPointExtended pEx = (PathPointExtended) this.getCurrentPath().getNode(this.getCurrentPath().getCurrentNodeIndex());
            PathPointExtended pExNext = this.getCurrentPath().getLength() > this.getCurrentPath().getCurrentNodeIndex() + 1
                    ? (PathPointExtended) this.getCurrentPath()
                    .getNode(this.getCurrentPath()
                            .getCurrentNodeIndex() + 1) : null;

            if (pExNext != null && pEx.x == pExNext.x && pEx.z == pExNext.z) {
                pExNext = this.getCurrentPath().getLength() > this.getCurrentPath().getCurrentNodeIndex() + 2
                        ? (PathPointExtended) this.getCurrentPath()
                        .getNode(this.getCurrentPath()
                                .getCurrentNodeIndex() + 2) : null;
            }

            if (pEx.isOnRails() || pEx.isRailsExit()) {
                return this.handlePathOnRails(pEx, pExNext);
            }
        }
        return false;
    }

    /**
     * Handle pathing on rails.
     *
     * @param pEx     the current path point.
     * @param pExNext the next path point.
     * @return if go to next point.
     */
    private boolean handlePathOnRails(final PathPointExtended pEx, final PathPointExtended pExNext) {
        return false;
    }

    private boolean handlePathPointOnLadder(final PathPointExtended pEx) {
        Vec3d vec3 = this.getCurrentPath().getNodePosition(this.ourEntity);
        final BlockPos entityPos = new BlockPos(this.ourEntity.getBlockPos());
        if (vec3.squaredDistanceTo(this.ourEntity.getX(), vec3.y, this.ourEntity.getZ()) < 0.6 && Math.abs(vec3.y - entityPos.getY()) <= 2.0) {
            //This way he is less nervous and gets up the ladder
            double newSpeed = 0.3;
            switch (pEx.getLadderFacing()) {
                //  Any of these values is climbing, so adjust our direction of travel towards the ladder
                case NORTH:
                    vec3 = vec3.add(0, 0, 0.4);
                    break;
                case SOUTH:
                    vec3 = vec3.add(0, 0, -0.4);
                    break;
                case WEST:
                    vec3 = vec3.add(0.4, 0, 0);
                    break;
                case EAST:
                    vec3 = vec3.add(-0.4, 0, 0);
                    break;
                case UP:
                    vec3 = vec3.add(0, 1, 0);
                    break;
                //  Any other value is going down, so lets not move at all
                default:
                    newSpeed = 0;
                    this.entity.setSneaking(true);
                    this.isSneaking = true;
                    this.ourEntity.getMoveControl().moveTo(vec3.x, vec3.y, vec3.z, 0.2);
                    break;
            }

            if (newSpeed > 0) {
                if (!(this.world.getBlockState(this.ourEntity.getBlockPos()).getBlock() instanceof LadderBlock)) {
                    this.ourEntity.setVelocity(this.ourEntity.getVelocity().add(0, 0.1D, 0));
                }
                this.ourEntity.getMoveControl().moveTo(vec3.x, vec3.y, vec3.z, newSpeed);
            } else {
                if (this.world.getBlockState(entityPos.down()).isLadder(this.world, entityPos.down(), this.ourEntity)) {
                    this.ourEntity.setUpwardSpeed(-0.5f);
                } else {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private boolean handleEntityInWater(int oldIndex, final PathPointExtended pEx) {
        //  Prevent shortcuts when swimming
        final int curIndex = this.getCurrentPath().getCurrentNodeIndex();
        if (curIndex > 0
                && (curIndex + 1) < this.getCurrentPath().getLength()
                && this.getCurrentPath().getNode(curIndex - 1).y != pEx.y) {
            //  Work around the initial 'spin back' when dropping into water
            oldIndex = curIndex + 1;
        }

        this.getCurrentPath().setCurrentNodeIndex(oldIndex);

        Vec3d vec3d = this.getCurrentPath().getNodePosition(this.ourEntity);

        if (vec3d.squaredDistanceTo(new Vec3d(this.ourEntity.getX(), vec3d.y, this.ourEntity.getZ())) < 0.1
                && Math.abs(this.ourEntity.getY() - vec3d.y) < 0.5) {
            this.getCurrentPath().next();
            if (this.isIdle()) {
                return true;
            }

            vec3d = this.getCurrentPath().getNodePosition(this.ourEntity);
        }

        this.ourEntity.getMoveControl().moveTo(vec3d.x, vec3d.y, vec3d.z, this.getSpeedFactor());
        return false;
    }

    @Override
    protected void continueFollowingPath() {
        this.getSpeedFactor();
        final int curNode = this.currentPath.getCurrentNodeIndex();
        final int curNodeNext = curNode + 1;
        if (curNodeNext < this.currentPath.getLength()) {
            if (!(this.currentPath.getNode(curNode) instanceof PathPointExtended)) {
                this.currentPath = this.convertPath(this.currentPath);
            }

            final PathPointExtended pEx = (PathPointExtended) this.currentPath.getNode(curNode);
            final PathPointExtended pExNext = (PathPointExtended) this.currentPath.getNode(curNodeNext);

            //  If current node is bottom of a ladder, then stay on this node until
            //  the ourEntity reaches the bottom, otherwise they will try to head out early
            if (pEx.isOnLadder() && pEx.getLadderFacing() == Direction.DOWN
                    && !pExNext.isOnLadder()) {
                final Vec3d vec3 = this.getPos();
                if ((vec3.y - (double) pEx.y) < MIN_Y_DISTANCE) {
                    this.currentPath.setCurrentNodeIndex(curNodeNext);
                }
                return;
            }
        }

        this.nodeReachProximity = Math.max(1.2F, this.entity.getWidth());
        boolean wentAhead = false;
        boolean isTracking = AbstractPathJob.trackingMap.containsValue(this.ourEntity.getUuid());

        // TODO: Figure out a better way to derive this value ideally from the pathfinding code
        int maxDropHeight = 3;

        final HashSet<BlockPos> reached = new HashSet<>();
        // Look at multiple points, incase we're too fast
        for (int i = this.currentPath.getCurrentNodeIndex(); i < Math.min(this.currentPath.getLength(), this.currentPath.getCurrentNodeIndex() + 4); i++) {
            Vec3d next = this.currentPath.getNodePosition(this.entity, i);
            if (Math.abs(this.entity.getX() - next.x) < (double) this.nodeReachProximity - Math.abs(this.entity.getY() - (next.y)) * 0.1
                    && Math.abs(this.entity.getZ() - next.z) < (double) this.nodeReachProximity - Math.abs(this.entity.getY() - (next.y)) * 0.1 &&
                    (Math.abs(this.entity.getY() - next.y) <= Math.min(1.0F, Math.ceil(this.entity.getHeight() / 2.0F)) ||
                            Math.abs(this.entity.getY() - next.y) <= Math.ceil(this.entity.getWidth() / 2) * maxDropHeight)) {
                this.currentPath.next();
                wentAhead = true;

                if (isTracking) {
                    final PathNode point = this.currentPath.getNode(i);
                    reached.add(new BlockPos(point.x, point.y, point.z));
                }
            }
        }

        if (isTracking) {
            AbstractPathJob.synchToClient(reached, this.ourEntity);
            reached.clear();
        }

        if (this.currentPath.isFinished()) {
            this.onPathFinish();
            return;
        }

        if (wentAhead) {
            return;
        }

        if (curNode >= this.currentPath.getLength() || curNode <= 1) {
            return;
        }

        // Check some past nodes case we fell behind.
        final Vec3d curr = this.currentPath.getNodePosition(this.entity, curNode - 1);
        final Vec3d next = this.currentPath.getNodePosition(this.entity, curNode);

        final Vec3i currI = new Vec3i((int) Math.round(curr.x), (int) Math.round(curr.y), (int) Math.round(curr.z));
        final Vec3i nextI = new Vec3i((int) Math.round(next.x), (int) Math.round(next.y), (int) Math.round(next.z));

        if (this.entity.getBlockPos().isWithinDistance(currI, 2.0) && this.entity.getBlockPos().isWithinDistance(nextI, 2.0)) {
            int currentIndex = curNode - 1;
            while (currentIndex > 0) {
                final Vec3d tempoPos = this.currentPath.getNodePosition(this.entity, currentIndex);
                final Vec3i tempoPosI = new Vec3i((int) Math.round(tempoPos.x), (int) Math.round(tempoPos.y), (int) Math.round(tempoPos.z));
                if (this.entity.getBlockPos().isWithinDistance(tempoPosI, 1.0)) {
                    this.currentPath.setCurrentNodeIndex(currentIndex);
                } else if (isTracking) {
                    reached.add(new BlockPos(tempoPosI));
                }
                currentIndex--;
            }
        }

        if (isTracking) {
            AbstractPathJob.synchToClient(reached, this.ourEntity);
            reached.clear();
        }
    }

    /**
     * Called upon reaching the path end, reset values
     */
    private void onPathFinish() {
        this.stop();
    }

    @Override
    public void recalculatePath() {
    }

    /**
     * Don't let vanilla rapidly discard paths, set a timeout before its allowed to use stuck.
     */
    @Override
    protected void checkTimeouts(final @NotNull Vec3d positionVec3) {
        // Do nothing, unstuck is checked on tick, not just when we have a path
    }

    public boolean entityOnAndBelowPath(Entity entity, Vec3d slack) {
        Path path = this.getCurrentPath();
        if (path == null) {
            return false;
        }

        int closest = path.getCurrentNodeIndex();
        //Search through path from the current index outwards to improve performance
        for (int i = 0; i < path.getLength() - 1; i++) {
            if (closest + i < path.getLength()) {
                PathNode currentPoint = path.getNode(closest + i);
                if (this.entityNearAndBelowPoint(currentPoint, entity, slack)) {
                    return true;
                }
            }
            if (closest - i >= 0) {
                PathNode currentPoint = path.getNode(closest - i);
                if (this.entityNearAndBelowPoint(currentPoint, entity, slack)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean entityNearAndBelowPoint(PathNode currentPoint, Entity entity, Vec3d slack) {
        return Math.abs(currentPoint.x - entity.getX()) < slack.getX()
                && currentPoint.y - entity.getY() + slack.getY() > 0
                && Math.abs(currentPoint.z - entity.getZ()) < slack.getZ();
    }


    @Override
    public void stop() {
        if (this.pathResult != null) {
            this.pathResult.cancel();
            this.pathResult.setStatus(PathFindingStatus.CANCELLED);
            this.pathResult = null;
        }

        this.destination = null;
        super.stop();
    }

    @Override
    public PathResult moveToLivingEntity(final Entity e, final double speed) {
        return this.moveToXYZ(e.getX(), e.getY(), e.getZ(), speed);
    }

    @Override
    public PathResult moveAwayFromLivingEntity(final Entity e, final double distance, final double speed) {
        return this.moveAwayFromXYZ(new BlockPos(e.getBlockPos()), distance, speed, true);
    }

    @Override
    public void setCanSwim(boolean canSwim) {
        super.setCanSwim(canSwim);
        this.getPathingOptions().setCanSwim(canSwim);
    }

    @Override
    public BlockPos getDesiredPos() {
        return this.desiredPos;
    }

    /**
     * Sets the stuck handler
     *
     * @param stuckHandler handler to set
     */
    @Override
    public void setStuckHandler(final IStuckHandler stuckHandler) {
        this.stuckHandler = stuckHandler;
    }

    @Override
    public void setSwimSpeedFactor(final double factor) {
        this.swimSpeedFactor = factor;
    }

    public static boolean isEqual(final BlockPos coords, final int x, final int y, final int z) {
        return coords.getX() == x && coords.getY() == y && coords.getZ() == z;
    }

    public static boolean isEntityBlockLoaded(final WorldAccess world, final BlockPos pos) {
        return WorldUtil.isEntityBlockLoaded(world, pos);
    }


}
