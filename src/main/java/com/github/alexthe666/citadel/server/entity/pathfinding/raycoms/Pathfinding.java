package com.github.alexthe666.citadel.server.entity.pathfinding.raycoms;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */

import com.github.alexthe666.citadel.Citadel;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;


/**
 * Static class the handles all the Pathfinding.
 */
public final class Pathfinding {
    private static final BlockingQueue<Runnable> jobQueue = new LinkedBlockingDeque<>();
    private static ThreadPoolExecutor executor;

    private Pathfinding() {
        //Hides default constructor.
    }

    public static boolean isDebug() {
        return PathfindingConstants.isDebugMode;
    }

    /**
     * Creates a new thread pool for pathfinding jobs
     *
     * @return the threadpool executor.
     */
    public static ThreadPoolExecutor getExecutor() {
        if (executor == null) {
            executor = new ThreadPoolExecutor(1, PathfindingConstants.pathfindingThreads, 10, TimeUnit.SECONDS, jobQueue, new CitadelThreadFactory());
        }
        return executor;
    }

    /**
     * Ice and Fire specific thread factory.
     */
    public static class CitadelThreadFactory implements ThreadFactory {
        /**
         * Ongoing thread IDs.
         */
        public static int id;

        @Override
        public Thread newThread(final Runnable runnable) throws RuntimeException {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            final Thread thread = new Thread(runnable, "Citadel Pathfinding Worker #" + (id++));
            thread.setDaemon(true);
            thread.setPriority(Thread.MAX_PRIORITY);
            if (thread.getContextClassLoader() != classLoader) {
                Citadel.LOGGER.info("Corrected CCL of new Citadel Pathfinding Thread, was: {}", thread.getContextClassLoader().toString());
                thread.setContextClassLoader(classLoader);
            }
            thread.setUncaughtExceptionHandler((thread1, throwable) -> Citadel.LOGGER.error("Citadel Pathfinding Thread errored! ", throwable));
            return thread;
        }
    }
}
