package com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.pathjobs;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */

import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.MNode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

/**
 * Job that handles moving to a location.
 */
public class PathJobMoveToLocation extends AbstractPathJob {
    private static final float DESTINATION_SLACK_NONE = 0.1F;
    // 1^2 + 1^2 + 1^2 + (epsilon of 0.1F)
    private static final float DESTINATION_SLACK_ADJACENT = (float) Math.sqrt(2f);

    private final BlockPos destination;
    // 0 = exact match
    private float destinationSlack = DESTINATION_SLACK_NONE;

    /**
     * Prepares the PathJob for the path finding system.
     *
     * @param world  world the entity is in.
     * @param start  starting location.
     * @param end    target location.
     * @param range  max search range.
     * @param entity the entity.
     */
    public PathJobMoveToLocation(final World world, final BlockPos start, final BlockPos end, final int range, final LivingEntity entity) {
        super(world, start, end, range, entity);
        this.destination = new BlockPos(end);
    }

    /**
     * Perform the search.
     *
     * @return Path of a path to the given location, a best-effort, or null.
     */
    @Override
    protected Path search() {
        //  Compute destination slack - if the destination point cannot be stood in
        if (this.getGroundHeight(null, this.destination) != this.destination.getY())
            this.destinationSlack = DESTINATION_SLACK_ADJACENT;
        return super.search();
    }

    @Override
    protected BlockPos getPathTargetPos(final MNode finalMNode) {
        return this.destination;
    }

    @Override
    protected double computeHeuristic(final BlockPos pos) {
        return Math.sqrt(this.destination.getSquaredDistance(pos));
    }

    /**
     * Checks if the target has been reached.
     *
     * @param n Node to test.
     * @return true if has been reached.
     */
    @Override
    protected boolean isAtDestination(final MNode n) {
        if (this.destinationSlack <= DESTINATION_SLACK_NONE)
            return n.pos.getX() == this.destination.getX() && n.pos.getY() == this.destination.getY() && n.pos.getZ() == this.destination.getZ();
        if (n.pos.getY() == this.destination.getY() - 1)
            return this.destination.isWithinDistance(new Vec3i(n.pos.getX(), this.destination.getY(), n.pos.getZ()), DESTINATION_SLACK_ADJACENT);
        return this.destination.isWithinDistance(n.pos, DESTINATION_SLACK_ADJACENT);
    }

    /**
     * Calculate the distance to the target.
     *
     * @param n Node to test.
     * @return double of the distance.
     */
    @Override
    protected double getNodeResultScore(final MNode n) {
        //  For Result Score lower is better
        return this.destination.getSquaredDistance(n.pos);
    }
}
