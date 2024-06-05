package com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.pathjobs;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;


/**
 * Job that handles random pathing.
 */
public class PathJobRandomPos extends AbstractPathJob {
    /**
     * Direction to walk to.
     */
    protected final BlockPos destination;

    /**
     * Required avoidDistance.
     */
    protected final int minDistFromStart;


    /**
     * Random pathing rand.
     */
    private static final Random random = Random.createThreadSafe();


    /**
     * Minimum distance to the goal.
     */
    private final int maxDistToDest;

    /**
     * Prepares the PathJob for the path finding system.
     *
     * @param world            world the entity is in.
     * @param start            starting location.
     * @param minDistFromStart how far to move away.
     * @param range            max range to search.
     * @param entity           the entity.
     */
    public PathJobRandomPos(
            final World world,
            final BlockPos start,
            final int minDistFromStart,
            final int range,
            final LivingEntity entity) {
        super(world, start, start, range, new PathResult<PathJobRandomPos>(), entity);
        this.minDistFromStart = minDistFromStart;
        this.maxDistToDest = range;

        final Pair<Direction, Direction> dir = getRandomDirectionTuple(random);
        this.destination = start.offset(dir.getLeft(), minDistFromStart).offset(dir.getRight(), minDistFromStart);
    }

    /**
     * Prepares the PathJob for the path finding system.
     *
     * @param world            world the entity is in.
     * @param start            starting location.
     * @param minDistFromStart how far to move away.
     * @param searchRange      max range to search.
     * @param entity           the entity.
     */
    public PathJobRandomPos(
            final World world,
            final BlockPos start,
            final int minDistFromStart,
            final int searchRange,
            final int maxDistToDest,
            final LivingEntity entity,
            final BlockPos dest) {
        super(world, start, dest, searchRange, new PathResult<PathJobRandomPos>(), entity);
        this.minDistFromStart = minDistFromStart;
        this.maxDistToDest = maxDistToDest;
        this.destination = dest;
    }

    /**
     * Prepares the PathJob for the path finding system.
     *
     * @param world            world the entity is in.
     * @param start            starting location.
     * @param minDistFromStart how far to move away.
     * @param range            max range to search.
     * @param entity           the entity.
     */
    public PathJobRandomPos(
            final World world,
            final BlockPos start,
            final int minDistFromStart,
            final int range,
            final LivingEntity entity,
            final BlockPos startRestriction,
            final BlockPos endRestriction,
            final AbstractAdvancedPathNavigate.RestrictionType restrictionType) {
        super(world, start, startRestriction, endRestriction, range, false, new PathResult<PathJobRandomPos>(), entity, restrictionType);
        this.minDistFromStart = minDistFromStart;
        this.maxDistToDest = range;

        final Pair<Direction, Direction> dir = getRandomDirectionTuple(random);
        this.destination = start.offset(dir.getLeft(), minDistFromStart).offset(dir.getRight(), minDistFromStart);
    }

    /**
     * Searches a random direction.
     *
     * @param random a random object.
     * @return a tuple of two directions.
     */
    public static Pair<Direction, Direction> getRandomDirectionTuple(final Random random) {
        return new Pair<>(Direction.random(random), Direction.random(random));
    }

    @Override
    protected Path search() {
        if (Pathfinding.isDebug()) {
            Citadel.LOGGER.info(String.format("Pathfinding from [%d,%d,%d] in the direction of [%d,%d,%d]",
                    this.start.getX(), this.start.getY(), this.start.getZ(), this.destination.getX(), this.destination.getY(), this.destination.getZ()));
        }

        return super.search();
    }


    @Override
    public PathResult getResult() {
        return super.getResult();
    }

    @Override
    protected double computeHeuristic(final BlockPos pos) {
        return Math.sqrt(this.destination.getSquaredDistance(new BlockPos(pos.getX(), this.destination.getY(), pos.getZ())));
    }

    @Override
    protected boolean isAtDestination(final MNode n) {
        return random.nextInt(10) == 0 && this.isInRestrictedArea(n.pos) && (this.start.getSquaredDistance(n.pos) > this.minDistFromStart * this.minDistFromStart)
                && SurfaceType.getSurfaceType(this.world, this.world.getBlockState(n.pos.down()), n.pos.down()) == SurfaceType.WALKABLE
                && this.destination.getSquaredDistance(n.pos) < this.maxDistToDest * this.maxDistToDest;
    }

    @Override
    protected double getNodeResultScore(final MNode n) {
        //  For Result Score lower is better
        return this.destination.getSquaredDistance(n.pos);
    }

    /**
     * Checks if position and range match the given parameters
     *
     * @param range max dist to dest range
     * @param pos   dest to look from
     * @return
     */
    public boolean posAndRangeMatch(final int range, final BlockPos pos) {
        return this.destination != null && range == this.maxDistToDest && this.destination.equals(pos);
    }


}
