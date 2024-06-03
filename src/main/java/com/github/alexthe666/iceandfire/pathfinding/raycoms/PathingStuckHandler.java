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
    private int delayToNextUnstuckAction = delayBeforeActions;

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
            resetGlobalStuckTimers();
            return;
        }

        // Global timeout check
        if (prevDestination.equals(navigator.getDesiredPos()))
        {
            globalTimeout++;

            // Try path first, if path fits target pos
            if (globalTimeout > Math.max(MIN_TP_DELAY, timePerBlockDistance * Math.max(MIN_DIST_FOR_TP, distanceToGoal)))
            {
                completeStuckAction(navigator);
            }
        }
        else
        {
            resetGlobalStuckTimers();
        }

        prevDestination = navigator.getDesiredPos();

        if (navigator.getCurrentPath() == null || navigator.getCurrentPath().isFinished()) {
            // With no path reset the last path index point to -1
            lastPathIndex = -1;
            progressedNodes = 0;

            // Stuck when we have no path and had no path last update before
            if (!hadPath) {
                tryUnstuck(navigator);
            }
        }
        else
        {
            if (navigator.getCurrentPath().getCurrentNodeIndex() == lastPathIndex) {
                // Stuck when we have a path, but are not progressing on it
                tryUnstuck(navigator);
            } else {
                if (lastPathIndex != -1 && navigator.getCurrentPath().getTarget().getSquaredDistance(prevDestination) < 25) {
                    progressedNodes = navigator.getCurrentPath().getCurrentNodeIndex() > lastPathIndex ? progressedNodes + 1 : progressedNodes - 1;

                    if (progressedNodes > 5 && (navigator.getCurrentPath().getEnd() == null || !moveAwayStartPos.equals(navigator.getCurrentPath().getEnd().getBlockPos()))) {
                        // Not stuck when progressing
                        resetStuckTimers();
                    }
                }
            }
        }

        lastPathIndex = navigator.getCurrentPath() != null ? navigator.getCurrentPath().getCurrentNodeIndex() : -1;

        hadPath = navigator.getCurrentPath() != null && !navigator.getCurrentPath().isFinished();
    }

    /**
     * Resets global stuck timers
     */
    private void resetGlobalStuckTimers()
    {
        globalTimeout = 0;
        prevDestination = BlockPos.ORIGIN;
        resetStuckTimers();
    }

    /**
     * Final action when completly stuck before resetting stuck handler and path
     */
    private void completeStuckAction(final AbstractAdvancedPathNavigate navigator) {
        final BlockPos desired = navigator.getDesiredPos();
        final World world = navigator.getOurEntity().getWorld();
        final MobEntity entity = navigator.getOurEntity();

        if (canTeleportGoal) {
            final BlockPos tpPos = findAround(world, desired, 10, 10,
                (posworld, pos) -> SurfaceType.getSurfaceType(posworld, posworld.getBlockState(pos.down()), pos.down()) == SurfaceType.WALKABLE
                    && SurfaceType.getSurfaceType(posworld, posworld.getBlockState(pos), pos) == SurfaceType.DROPABLE
                    && SurfaceType.getSurfaceType(posworld, posworld.getBlockState(pos.up()), pos.up()) == SurfaceType.DROPABLE);
            if (tpPos != null) {
                entity.requestTeleport(tpPos.getX() + 0.5, tpPos.getY(), tpPos.getZ() + 0.5);
            }
        }
        if (takeDamageOnCompleteStuck) {
            entity.damage(new DamageSource(entity.getWorld().getDamageSources().inWall().getTypeRegistryEntry(), entity), entity.getMaxHealth() * damagePct);
        }

        if (completeStuckBlockBreakRange > 0)
        {
            final Direction facing = getFacing(entity.getBlockPos(), navigator.getDesiredPos());

            for (int i = 1; i <= completeStuckBlockBreakRange; i++)
            {
                if (!world.isAir(new BlockPos(entity.getBlockPos()).offset(facing, i)) || !world.isAir(new BlockPos(entity.getBlockPos()).offset(facing, i).up())) {
                    breakBlocksAhead(world, new BlockPos(entity.getBlockPos()).offset(facing, i - 1), facing);
                    break;
                }
            }
        }

        navigator.stop();
        resetGlobalStuckTimers();
    }

    /**
     * Tries unstuck options depending on the level
     */
    private void tryUnstuck(final AbstractAdvancedPathNavigate navigator)
    {
        if (delayToNextUnstuckAction-- > 0)
        {
            return;
        }
        delayToNextUnstuckAction = 50;

        // Clear path
        if (stuckLevel == 0)
        {
            stuckLevel++;
            delayToNextUnstuckAction = 100;
            navigator.stop();
            return;
        }

        // Move away
        if (stuckLevel == 1) {
            stuckLevel++;
            delayToNextUnstuckAction = 200;
            navigator.stop();
            navigator.moveAwayFromXYZ(new BlockPos(navigator.getOurEntity().getBlockPos()), 10, 1.0f, false);
            navigator.getPathingOptions().setCanClimb(false);
            moveAwayStartPos = navigator.getOurEntity().getBlockPos();
            return;
        }

        // Skip ahead
        if (stuckLevel == 2 && teleportRange > 0 && hadPath) {
            int index = Math.min(navigator.getCurrentPath().getCurrentNodeIndex() + teleportRange, navigator.getCurrentPath().getLength() - 1);
            final PathNode togo = navigator.getCurrentPath().getNode(index);
            navigator.getOurEntity().requestTeleport(togo.x + 0.5d, togo.y, togo.z + 0.5d);
            delayToNextUnstuckAction = 300;
        }

        // Place ladders & leaves
        if (stuckLevel >= 3 && stuckLevel <= 5)
        {
            if (canPlaceLadders && rand.nextBoolean())
            {
                delayToNextUnstuckAction = 200;
                placeLadders(navigator);
            }
            else if (canBuildLeafBridges && rand.nextBoolean())
            {
                delayToNextUnstuckAction = 100;
                placeLeaves(navigator);
            }
        }

        // break blocks
        if (stuckLevel >= 6 && stuckLevel <= 8 && canBreakBlocks)
        {
            delayToNextUnstuckAction = 200;
            breakBlocks(navigator);
        }

        chanceStuckLevel();

        if (stuckLevel == 9)
        {
            completeStuckAction(navigator);
            resetStuckTimers();
        }
    }

    /**
     * Random chance to decrease to a previous level of stuck
     */
    private void chanceStuckLevel()
    {
        stuckLevel++;
        // 20 % to decrease to the previous level again
        if (stuckLevel > 1 && rand.nextInt(6) == 0)
        {
            stuckLevel -= 2;
        }
    }

    /**
     * Resets timers
     */
    private void resetStuckTimers()
    {
        delayToNextUnstuckAction = delayBeforeActions;
        lastPathIndex = -1;
        progressedNodes = 0;
        stuckLevel = 0;
        moveAwayStartPos = BlockPos.ORIGIN;
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
            setAirIfPossible(world, start.up(3));
            return;
        }

        // Goal direction up
        if (!world.isAir(start.up().offset(facing))) {
            setAirIfPossible(world, start.up().offset(facing));
            return;
        }

        // In goal direction
        if (!world.isAir(start.offset(facing))) {
            setAirIfPossible(world, start.offset(facing));
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

        tryPlaceLadderAt(world, entityPos);
        tryPlaceLadderAt(world, entityPos.up());
        tryPlaceLadderAt(world, entityPos.up(2));
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

        for (final Direction dir : directions) {
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

        breakBlocksAhead(world, entity.getBlockPos(), facing);
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
            for (final Direction dir : directions) {
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
        canBreakBlocks = true;
        return this;
    }

    public PathingStuckHandler withPlaceLadders()
    {
        canPlaceLadders = true;
        return this;
    }

    public PathingStuckHandler withBuildLeafBridges()
    {
        canBuildLeafBridges = true;
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
        teleportRange = steps;
        return this;
    }

    public PathingStuckHandler withTeleportOnFullStuck()
    {
        canTeleportGoal = true;
        return this;
    }

    public PathingStuckHandler withTakeDamageOnStuck(float damagePct)
    {
        this.damagePct = damagePct;
        takeDamageOnCompleteStuck = true;
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
        timePerBlockDistance = time;
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
        delayBeforeActions = delay;
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
        completeStuckBlockBreakRange = range;
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
