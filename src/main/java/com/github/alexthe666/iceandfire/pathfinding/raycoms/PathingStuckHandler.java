package com.github.alexthe666.iceandfire.pathfinding.raycoms;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiPredicate;

/**
 * Stuck handler for pathing
 */
public class PathingStuckHandler implements IStuckHandler
{
    /**
     * The distance at which we consider a target to arrive
     */
    private static final double MIN_TARGET_DIST = 3;

    /**
     * All directions.
     */
    private final List<Direction> directions = Arrays.asList(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);

    /**
     * Constants related to tp.
     */
    private static final int MIN_TP_DELAY    = 120 * 20;
    private static final int MIN_DIST_FOR_TP = 10;

    /**
     * Amount of path steps allowed to teleport on stuck, 0 = disabled
     */
    private int teleportRange = 0;

    /**
     * Max timeout per block to go, default = 5sec per block
     */
    private int timePerBlockDistance = 100;

    /**
     * The current stucklevel, determines actions taken
     */
    private int stuckLevel = 0;

    /**
     * Global timeout counter, used to determine when we're completly stuck
     */
    private int globalTimeout = 0;

    /**
     * The previously desired go to position of the entity
     */
    private BlockPos prevDestination = BlockPos.ORIGIN;

    /**
     * Whether breaking blocks is enabled
     */
    private boolean canBreakBlocks = false;

    /**
     * Whether placing ladders is enabled
     */
    private boolean canPlaceLadders = false;

    /**
     * Whether leaf bridges are enabled
     */
    private boolean canBuildLeafBridges = false;

    /**
     * Whether teleport to goal at full stuck is enabled
     */
    private boolean canTeleportGoal = false;

    /**
     * Whether take damage on stuck is enabled
     */
    private boolean takeDamageOnCompleteStuck = false;
    private float   damagePct                 = 0.2f;

    /**
     * BLock break range on complete stuck
     */
    private int completeStuckBlockBreakRange = 0;

    /**
     * Temporary comparison variables to compare with last update
     */
    private boolean hadPath         = false;
    private int     lastPathIndex   = -1;
    private int     progressedNodes = 0;

    /**
     * Delay before taking unstuck actions in ticks, default 60 seconds
     */
    private int delayBeforeActions       = 60 * 20;
    private int delayToNextUnstuckAction = this.delayBeforeActions;

    /**
     * The start position of moving away unstuck
     */
    private BlockPos moveAwayStartPos = BlockPos.ORIGIN;

    private final Random rand = new Random();

    private PathingStuckHandler()
    {
    }

    /**
     * Creates a new stuck handler
     *
     * @return new stuck handler
     */
    public static PathingStuckHandler createStuckHandler()
    {
        return new PathingStuckHandler();
    }

    /**
     * Checks the entity for stuck
     *
     * @param navigator navigator to check
     */
    @Override
    public void checkStuck(final AbstractAdvancedPathNavigate navigator)
    {
        if (navigator.getDesiredPos() == null || navigator.getDesiredPos().equals(BlockPos.ORIGIN))
        {
            return;
        }

        final double distanceToGoal =
            navigator.getOurEntity().getPos().distanceTo(new Vec3d(navigator.getDesiredPos().getX(), navigator.getDesiredPos().getY(), navigator.getDesiredPos().getZ()));

        // Close enough to be considered at the goal
        if (distanceToGoal < MIN_TARGET_DIST)
        {
            this.resetGlobalStuckTimers();
            return;
        }

        // Global timeout check
        if (this.prevDestination.equals(navigator.getDesiredPos()))
        {
            this.globalTimeout++;

            // Try path first, if path fits target pos
            if (this.globalTimeout > Math.max(MIN_TP_DELAY, this.timePerBlockDistance * Math.max(MIN_DIST_FOR_TP, distanceToGoal)))
            {
                this.completeStuckAction(navigator);
            }
        }
        else
        {
            this.resetGlobalStuckTimers();
        }

        this.prevDestination = navigator.getDesiredPos();

        if (navigator.getCurrentPath() == null || navigator.getCurrentPath().isFinished()) {
            // With no path reset the last path index point to -1
            this.lastPathIndex = -1;
            this.progressedNodes = 0;

            // Stuck when we have no path and had no path last update before
            if (!this.hadPath) {
                this.tryUnstuck(navigator);
            }
        }
        else
        {
            if (navigator.getCurrentPath().getCurrentNodeIndex() == this.lastPathIndex) {
                // Stuck when we have a path, but are not progressing on it
                this.tryUnstuck(navigator);
            } else {
                if (this.lastPathIndex != -1 && navigator.getCurrentPath().getTarget().getSquaredDistance(this.prevDestination) < 25) {
                    this.progressedNodes = navigator.getCurrentPath().getCurrentNodeIndex() > this.lastPathIndex ? this.progressedNodes + 1 : this.progressedNodes - 1;

                    if (this.progressedNodes > 5 && (navigator.getCurrentPath().getEnd() == null || !this.moveAwayStartPos.equals(navigator.getCurrentPath().getEnd().getBlockPos()))) {
                        // Not stuck when progressing
                        this.resetStuckTimers();
                    }
                }
            }
        }

        this.lastPathIndex = navigator.getCurrentPath() != null ? navigator.getCurrentPath().getCurrentNodeIndex() : -1;

        this.hadPath = navigator.getCurrentPath() != null && !navigator.getCurrentPath().isFinished();
    }

    /**
     * Resets global stuck timers
     */
    private void resetGlobalStuckTimers()
    {
        this.globalTimeout = 0;
        this.prevDestination = BlockPos.ORIGIN;
        this.resetStuckTimers();
    }

    /**
     * Final action when completly stuck before resetting stuck handler and path
     */
    private void completeStuckAction(final AbstractAdvancedPathNavigate navigator) {
        final BlockPos desired = navigator.getDesiredPos();
        final World world = navigator.getOurEntity().getWorld();
        final MobEntity entity = navigator.getOurEntity();

        if (this.canTeleportGoal) {
            final BlockPos tpPos = findAround(world, desired, 10, 10,
                (posworld, pos) -> SurfaceType.getSurfaceType(posworld, posworld.getBlockState(pos.down()), pos.down()) == SurfaceType.WALKABLE
                    && SurfaceType.getSurfaceType(posworld, posworld.getBlockState(pos), pos) == SurfaceType.DROPABLE
                    && SurfaceType.getSurfaceType(posworld, posworld.getBlockState(pos.up()), pos.up()) == SurfaceType.DROPABLE);
            if (tpPos != null) {
                entity.requestTeleport(tpPos.getX() + 0.5, tpPos.getY(), tpPos.getZ() + 0.5);
            }
        }
        if (this.takeDamageOnCompleteStuck) {
            entity.damage(new DamageSource(entity.getWorld().getDamageSources().inWall().getTypeRegistryEntry(), entity), entity.getMaxHealth() * this.damagePct);
        }

        if (this.completeStuckBlockBreakRange > 0)
        {
            final Direction facing = getFacing(entity.getBlockPos(), navigator.getDesiredPos());

            for (int i = 1; i <= this.completeStuckBlockBreakRange; i++)
            {
                if (!world.isAir(new BlockPos(entity.getBlockPos()).offset(facing, i)) || !world.isAir(new BlockPos(entity.getBlockPos()).offset(facing, i).up())) {
                    this.breakBlocksAhead(world, new BlockPos(entity.getBlockPos()).offset(facing, i - 1), facing);
                    break;
                }
            }
        }

        navigator.stop();
        this.resetGlobalStuckTimers();
    }

    /**
     * Tries unstuck options depending on the level
     */
    private void tryUnstuck(final AbstractAdvancedPathNavigate navigator)
    {
        if (this.delayToNextUnstuckAction-- > 0)
        {
            return;
        }
        this.delayToNextUnstuckAction = 50;

        // Clear path
        if (this.stuckLevel == 0)
        {
            this.stuckLevel++;
            this.delayToNextUnstuckAction = 100;
            navigator.stop();
            return;
        }

        // Move away
        if (this.stuckLevel == 1) {
            this.stuckLevel++;
            this.delayToNextUnstuckAction = 200;
            navigator.stop();
            navigator.moveAwayFromXYZ(new BlockPos(navigator.getOurEntity().getBlockPos()), 10, 1.0f, false);
            navigator.getPathingOptions().setCanClimb(false);
            this.moveAwayStartPos = navigator.getOurEntity().getBlockPos();
            return;
        }

        // Skip ahead
        if (this.stuckLevel == 2 && this.teleportRange > 0 && this.hadPath) {
            int index = Math.min(navigator.getCurrentPath().getCurrentNodeIndex() + this.teleportRange, navigator.getCurrentPath().getLength() - 1);
            final PathNode togo = navigator.getCurrentPath().getNode(index);
            navigator.getOurEntity().requestTeleport(togo.x + 0.5d, togo.y, togo.z + 0.5d);
            this.delayToNextUnstuckAction = 300;
        }

        // Place ladders & leaves
        if (this.stuckLevel >= 3 && this.stuckLevel <= 5)
        {
            if (this.canPlaceLadders && this.rand.nextBoolean())
            {
                this.delayToNextUnstuckAction = 200;
                this.placeLadders(navigator);
            }
            else if (this.canBuildLeafBridges && this.rand.nextBoolean())
            {
                this.delayToNextUnstuckAction = 100;
                this.placeLeaves(navigator);
            }
        }

        // break blocks
        if (this.stuckLevel >= 6 && this.stuckLevel <= 8 && this.canBreakBlocks)
        {
            this.delayToNextUnstuckAction = 200;
            this.breakBlocks(navigator);
        }

        this.chanceStuckLevel();

        if (this.stuckLevel == 9)
        {
            this.completeStuckAction(navigator);
            this.resetStuckTimers();
        }
    }

    /**
     * Random chance to decrease to a previous level of stuck
     */
    private void chanceStuckLevel()
    {
        this.stuckLevel++;
        // 20 % to decrease to the previous level again
        if (this.stuckLevel > 1 && this.rand.nextInt(6) == 0)
        {
            this.stuckLevel -= 2;
        }
    }

    /**
     * Resets timers
     */
    private void resetStuckTimers()
    {
        this.delayToNextUnstuckAction = this.delayBeforeActions;
        this.lastPathIndex = -1;
        this.progressedNodes = 0;
        this.stuckLevel = 0;
        this.moveAwayStartPos = BlockPos.ORIGIN;
    }

    /**
     * Attempt to break blocks that are blocking the entity to reach its destination.
     *
     * @param world  the world it is in.
     * @param start  the position the entity is at.
     * @param facing the direction the goal is in.
     */
    private void breakBlocksAhead(final World world, final BlockPos start, final Direction facing) {
        // Above entity
        if (!world.isAir(start.up(3))) {
            this.setAirIfPossible(world, start.up(3));
            return;
        }

        // Goal direction up
        if (!world.isAir(start.up().offset(facing))) {
            this.setAirIfPossible(world, start.up().offset(facing));
            return;
        }

        // In goal direction
        if (!world.isAir(start.offset(facing))) {
            this.setAirIfPossible(world, start.offset(facing));
        }
    }

    /**
     * Check if the block at the position is indestructible, if not, attempt to break it.
     *
     * @param world the world the block is in.
     * @param pos   the pos the block is at.
     */
    private void setAirIfPossible(final World world, final BlockPos pos) {
        final Block blockAtPos = world.getBlockState(pos).getBlock();
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
    }

    /**
     * Places ladders
     *
     * @param navigator navigator to use
     */
    private void placeLadders(final AbstractAdvancedPathNavigate navigator) {
        final World world = navigator.getOurEntity().getWorld();
        final MobEntity entity = navigator.getOurEntity();

        BlockPos entityPos = entity.getBlockPos();

        while (world.getBlockState(entityPos).getBlock() == Blocks.LADDER) {
            entityPos = entityPos.up();
        }

        this.tryPlaceLadderAt(world, entityPos);
        this.tryPlaceLadderAt(world, entityPos.up());
        this.tryPlaceLadderAt(world, entityPos.up(2));
    }

    /**
     * Tries to place leaves
     *
     * @param navigator navigator to use
     */
    private void placeLeaves(final AbstractAdvancedPathNavigate navigator) {
        final World world = navigator.getOurEntity().getWorld();
        final MobEntity entity = navigator.getOurEntity();

        final Direction badFacing = getFacing(entity.getBlockPos(), navigator.getDesiredPos()).getOpposite();

        for (final Direction dir : this.directions) {
            if (dir == badFacing) {
                continue;
            }

            if (world.isAir(entity.getBlockPos().down().offset(dir))) {
                world.setBlockState(entity.getBlockPos().down().offset(dir), Blocks.ACACIA_LEAVES.getDefaultState());
            }
        }
    }

    public static Direction getFacing(final BlockPos pos, final BlockPos neighbor)
    {
        final BlockPos vector = neighbor.subtract(pos);
        return Direction.getFacing(vector.getX(), vector.getY(), -vector.getZ());
    }

    /**
     * Tries to randomly break blocks
     *
     * @param navigator navigator to use
     */
    private void breakBlocks(final AbstractAdvancedPathNavigate navigator) {
        final World world = navigator.getOurEntity().getWorld();
        final MobEntity entity = navigator.getOurEntity();

        final Direction facing = getFacing(entity.getBlockPos(), navigator.getDesiredPos());

        this.breakBlocksAhead(world, entity.getBlockPos(), facing);
    }

    /**
     * Tries to place a ladder at the given position
     *
     * @param world world to use
     * @param pos   position to set
     */
    private void tryPlaceLadderAt(final World world, final BlockPos pos) {
        final BlockState state = world.getBlockState(pos);
        if (state.getBlock() != Blocks.LADDER && !state.isOpaque() && world.getFluidState(pos).isEmpty()) {
            for (final Direction dir : this.directions) {
                final BlockState toPlace = Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, dir.getOpposite());
                if (world.getBlockState(pos.offset(dir)).isSolid() && Blocks.LADDER.canPlaceAt(toPlace, world, pos)) {
                    world.setBlockState(pos, toPlace);
                    break;
                }
            }
        }
    }

    public PathingStuckHandler withBlockBreaks()
    {
        this.canBreakBlocks = true;
        return this;
    }

    public PathingStuckHandler withPlaceLadders()
    {
        this.canPlaceLadders = true;
        return this;
    }

    public PathingStuckHandler withBuildLeafBridges()
    {
        this.canBuildLeafBridges = true;
        return this;
    }

    /**
     * Enables teleporting a certain amount of steps along a generated path
     *
     * @param steps steps to teleport
     * @return this
     */
    public PathingStuckHandler withTeleportSteps(int steps)
    {
        this.teleportRange = steps;
        return this;
    }

    public PathingStuckHandler withTeleportOnFullStuck()
    {
        this.canTeleportGoal = true;
        return this;
    }

    public PathingStuckHandler withTakeDamageOnStuck(float damagePct)
    {
        this.damagePct = damagePct;
        this.takeDamageOnCompleteStuck = true;
        return this;
    }

    /**
     * Sets the time per block distance to travel, before timing out
     *
     * @param time in ticks to set
     * @return this
     */
    public PathingStuckHandler withTimePerBlockDistance(int time)
    {
        this.timePerBlockDistance = time;
        return this;
    }

    /**
     * Sets the delay before taking stuck actions
     *
     * @param delay to set
     * @return this
     */
    public PathingStuckHandler withDelayBeforeStuckActions(int delay)
    {
        this.delayBeforeActions = delay;
        return this;
    }

    /**
     * Sets the block break range on complete stuck
     *
     * @param range to set
     * @return this
     */
    public PathingStuckHandler withCompleteStuckBlockBreak(int range)
    {
        this.completeStuckBlockBreakRange = range;
        return this;
    }

    /**
     * Returns the first air position near the given start. Advances vertically first then horizontally
     *
     * @param start     start position
     * @param vRange    vertical search range
     * @param hRange    horizontal search range
     * @param predicate check predicate for the right block
     * @return position or null
     */
    public static BlockPos findAround(final World world, final BlockPos start, final int vRange, final int hRange, final BiPredicate<BlockView, BlockPos> predicate) {
        if (vRange < 1 && hRange < 1) {
            return null;
        }

        if (predicate.test(world, start)) {
            return start;
        }

        BlockPos temp;
        int y = 0;
        int y_offset = 1;

        for (int i = 0; i < hRange + 2; i++)
        {
            for (int steps = 1; steps <= vRange; steps++)
            {
                // Start topleft of middle point
                temp = start.add(-steps, y, -steps);

                // X ->
                for (int x = 0; x <= steps; x++)
                {
                    temp = temp.add(1, 0, 0);
                    if (predicate.test(world, temp))
                    {
                        return temp;
                    }
                }

                // X
                // |
                // v
                for (int z = 0; z <= steps; z++)
                {
                    temp = temp.add(0, 0, 1);
                    if (predicate.test(world, temp))
                    {
                        return temp;
                    }
                }

                // < - X
                for (int x = 0; x <= steps; x++)
                {
                    temp = temp.add(-1, 0, 0);
                    if (predicate.test(world, temp))
                    {
                        return temp;
                    }
                }

                // ^
                // |
                // X
                for (int z = 0; z <= steps; z++)
                {
                    temp = temp.add(0, 0, -1);
                    if (predicate.test(world, temp))
                    {
                        return temp;
                    }
                }
            }

            y += y_offset;
            y_offset = y_offset > 0 ? y_offset + 1 : y_offset - 1;
            y_offset *= -1;

            if (world.getTopY() <= start.getY() + y) {
                return null;
            }
        }

        return null;
    }
}
