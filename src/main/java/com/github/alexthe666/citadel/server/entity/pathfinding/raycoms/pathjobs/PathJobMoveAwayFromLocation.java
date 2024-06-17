package com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.pathjobs;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */

import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.MNode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Job that handles moving away from something.
 */
public class PathJobMoveAwayFromLocation extends AbstractPathJob {
    /**
     * Position to run to, in order to avoid something.
     */

    protected final BlockPos avoid;
    /**
     * Required avoidDistance.
     */
    protected final int avoidDistance;

    /**
     * Prepares the PathJob for the path finding system.
     *
     * @param world         world the entity is in.
     * @param start         starting location.
     * @param avoid         location to avoid.
     * @param avoidDistance how far to move away.
     * @param range         max range to search.
     * @param entity        the entity.
     */
    public PathJobMoveAwayFromLocation(final World world, final BlockPos start, final BlockPos avoid, final int avoidDistance, final int range, final LivingEntity entity) {
        super(world, start, avoid, range, entity);
        this.avoid = new BlockPos(avoid);
        this.avoidDistance = avoidDistance;
    }

    /**
     * For MoveAwayFromLocation we want our heuristic to weight.
     *
     * @param pos Position to compute heuristic from.
     * @return heuristic as a double - Manhatten Distance with tie-breaker.
     */
    @Override
    protected double computeHeuristic(final BlockPos pos) {
        return -this.avoid.getSquaredDistance(pos);
    }

    /**
     * Checks if the destination has been reached. Meaning that the avoid distance has been reached.
     *
     * @param n Node to test.
     * @return true if so.
     */
    @Override
    protected boolean isAtDestination(final MNode n) {
        return Math.sqrt(this.avoid.getSquaredDistance(n.pos)) > this.avoidDistance;
    }

    /**
     * Calculate the distance to the target.
     *
     * @param n Node to test.
     * @return double amount.
     */
    @Override
    protected double getNodeResultScore(final MNode n) {
        return -this.avoid.getSquaredDistance(n.pos);
    }
}
