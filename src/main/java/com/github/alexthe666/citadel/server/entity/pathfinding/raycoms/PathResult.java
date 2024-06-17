package com.github.alexthe666.citadel.server.entity.pathfinding.raycoms;

import com.github.alexthe666.citadel.Citadel;
import net.minecraft.entity.ai.pathing.Path;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Creates a pathResult of a certain path.
 */
public class PathResult<T extends Callable<Path>> {
    private static boolean threadException = false;
    /**
     * The pathfinding status
     */
    protected PathFindingStatus status = PathFindingStatus.IN_PROGRESS_COMPUTING;
    /**
     * Whether the pathfinding job reached its destination
     */
    private volatile boolean pathReachesDestination = false;

    /**
     * Finished path reference
     */
    private Path path = null;

    /**
     * The calculation future for this result and job
     */
    private Future<Path> pathCalculation = null;

    /**
     * The job to execute for this result
     */
    private T job = null;

    /**
     * Whether the pathing calc is done and processed
     */
    private boolean pathingDoneAndProcessed = false;

    /**
     * Get Status of the Path.
     *
     * @return status.
     */
    public PathFindingStatus getStatus() {
        return this.status;
    }

    /**
     * For PathNavigate and AbstractPathJob use only.
     *
     * @param s status to set.
     */
    public void setStatus(final PathFindingStatus s) {
        this.status = s;
    }

    /**
     * @return true if the path is still computing or being followed.
     */
    public boolean isInProgress() {
        return this.isComputing() || this.status == PathFindingStatus.IN_PROGRESS_FOLLOWING;
    }

    public boolean isComputing() {
        return this.status == PathFindingStatus.IN_PROGRESS_COMPUTING;
    }

    /**
     * @return true if the no path can be found.
     */
    public boolean failedToReachDestination() {
        return this.isFinished() && !this.pathReachesDestination;
    }

    /**
     * @return true if the path is computed, and it reaches a desired destination.
     */
    public boolean isPathReachingDestination() {
        return this.isFinished() && this.path != null && this.pathReachesDestination;
    }

    /**
     * For PathNavigate and AbstractPathJob use only.
     *
     * @param value new value for pathReachesDestination.
     */
    public void setPathReachesDestination(final boolean value) {
        this.pathReachesDestination = value;
    }

    /**
     * @return true if the path was cancelled before being computed or before the entity reached it's destination.
     */
    public boolean isCancelled() {
        return this.status == PathFindingStatus.CANCELLED;
    }

    /**
     * @return length of the compute path, in nodes.
     */
    public int getPathLength() {
        return this.path.getLength();
    }

    /**
     * @return true if the path moves from the current location, useful for checking if a path actually generated.
     */
    public boolean hasPath() {
        return this.path != null;
    }

    /**
     * Get the generated path or null
     *
     * @return path
     */
    public Path getPath() {
        return this.path;
    }

    /**
     * Get the queried job for the pathresult
     */
    public T getJob() {
        return this.job;
    }

    /**
     * Set the job for this result
     */
    public void setJob(final T job) {
        this.job = job;
    }

    /**
     * Starts the job by queing it to an executor
     *
     * @param executorService executor
     */
    public void startJob(final ExecutorService executorService) {
        if (this.job != null)
            try {
                if (!threadException)
                    this.pathCalculation = executorService.submit(this.job);
            } catch (NullPointerException e) {
                Citadel.LOGGER.error("Mod tried to move an entity from non server thread", e);
            } catch (RuntimeException e) {
                threadException = true;
                Citadel.LOGGER.catching(e);
            } catch (Exception e) {
                Citadel.LOGGER.catching(e);
            }
    }

    /**
     * Processes the completed calculation results
     */
    public void processCalculationResults() {
        if (this.pathingDoneAndProcessed) return;

        try {
            this.path = this.pathCalculation.get();
            this.pathCalculation = null;
            this.setStatus(PathFindingStatus.CALCULATION_COMPLETE);
        } catch (InterruptedException | ExecutionException e) {
            Citadel.LOGGER.catching(e);
        }
    }

    /**
     * Check if we're calculating the pathfinding currently
     *
     * @return true
     */
    public boolean isCalculatingPath() {
        return this.pathCalculation != null && !this.pathCalculation.isDone();
    }

    /**
     * Whether the path calculation finished and was processed
     *
     * @return true if calculation is done and processed
     */
    public boolean isFinished() {
        if (!this.pathingDoneAndProcessed)
            if (this.pathCalculation != null && this.pathCalculation.isDone()) {
                this.processCalculationResults();
                this.pathingDoneAndProcessed = true;
            }
        return this.pathingDoneAndProcessed;
    }

    /**
     * Cancels the path calculation
     */
    public void cancel() {
        if (this.pathCalculation != null) {
            this.pathCalculation.cancel(false);
            this.pathCalculation = null;
        }
        this.pathingDoneAndProcessed = true;
    }
}