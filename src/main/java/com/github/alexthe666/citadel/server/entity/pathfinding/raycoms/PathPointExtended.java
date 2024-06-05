package com.github.alexthe666.citadel.server.entity.pathfinding.raycoms;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */

import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * Class extending pathPoint for our usage with ladders.
 */
public class PathPointExtended extends PathNode {
    /**
     * Is the point on a ladder.
     */
    private boolean onLadder = false;
    /**
     * What direction does the ladder face. Should be instantiated to something he doesn't recognize as climbable.
     */
    private Direction ladderFacing = Direction.DOWN;

    /**
     * Rails params.
     */
    private boolean onRails;
    private boolean railsEntry;
    private boolean railsExit;

    /**
     * Instantiates the pathPoint with a position.
     *
     * @param pos the position.
     */
    public PathPointExtended(final BlockPos pos)
    {
        super(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Checks if the point is on a ladder.
     *
     * @return true if so.
     */
    public boolean isOnLadder()
    {
        return this.onLadder;
    }

    /**
     * Sets if the point is on a ladder.
     *
     * @param onLadder value to set.
     */
    public void setOnLadder(final boolean onLadder)
    {
        this.onLadder = onLadder;
    }

    /**
     * Get the facing of the ladder.
     *
     * @return Direction.
     */
    public Direction getLadderFacing()
    {
        return this.ladderFacing;
    }

    /**
     * Sets the facing of the ladder.
     *
     * @param ladderFacing facing to set.
     */
    public void setLadderFacing(final Direction ladderFacing)
    {
        this.ladderFacing = ladderFacing;
    }

    /**
     * Set if it is on rails.
     *
     * @param isOnRails if on rails.
     */
    public void setOnRails(final boolean isOnRails)
    {
        this.onRails = isOnRails;
    }

    /**
     * Set the rails entry.
     */
    public void setRailsEntry()
    {
        this.railsEntry = true;
    }

    /**
     * Set the rails exit.
     */
    public void setRailsExit()
    {
        this.railsExit = true;
    }

    /**
     * Whether this is on rails.
     *
     * @return true if so.
     */
    public boolean isOnRails()
    {
        return this.onRails;
    }

    /**
     * Whether this is the rails entry.
     *
     * @return true if so.
     */
    public boolean isRailsEntry()
    {
        return this.railsEntry;
    }

    /**
     * Whether this is the rails exit.
     *
     * @return true if so.
     */
    public boolean isRailsExit()
    {
        return this.railsExit;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || this.getClass() != o.getClass())
        {
            return false;
        }
        if (!super.equals(o))
        {
            return false;
        }

        final PathPointExtended that = (PathPointExtended) o;

        if (this.onLadder != that.onLadder)
        {
            return false;
        }
        return this.ladderFacing == that.ladderFacing;
    }

    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (this.onLadder ? 1 : 0);
        result = 31 * result + this.ladderFacing.hashCode();
        return result;
    }
}
