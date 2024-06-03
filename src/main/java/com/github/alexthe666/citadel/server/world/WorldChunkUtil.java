package com.github.alexthe666.citadel.server.world;


import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.ChunkStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Class which has world related util functions like chunk load checks
 */
public class WorldChunkUtil {

    /**
     * Checks if the block is loaded for block access
     *
     * @param world world to use
     * @param pos   position to check
     * @return true if block is accessible/loaded
     */
    public static boolean isBlockLoaded(final WorldAccess world, final BlockPos pos) {
        return isChunkLoaded(world, pos.getX() >> 4, pos.getZ() >> 4);
    }

    /**
     * Returns whether a chunk is fully loaded
     *
     * @param world world to check on
     * @param x     chunk position
     * @param z     chunk position
     * @return true if loaded
     */
    public static boolean isChunkLoaded(final WorldAccess world, final int x, final int z) {
        if (world.getChunkManager() instanceof ServerChunkManager) {
            final ChunkHolder holder = ((ServerChunkManager) world.getChunkManager()).threadedAnvilChunkStorage.getChunkHolder(ChunkPos.toLong(x, z));
            if (holder != null) {
                return holder.getAccessibleFuture().getNow(ChunkHolder.UNLOADED_WORLD_CHUNK).left().isPresent();
            }

            return false;
        }
        return world.getChunk(x, z, ChunkStatus.FULL, false) != null;
    }


    /**
     * Returns whether a chunk is fully loaded
     *
     * @param world world to check on
     * @param pos   chunk position
     * @return true if loaded
     */
    public static boolean isChunkLoaded(final WorldAccess world, final ChunkPos pos) {
        return isChunkLoaded(world, pos.x, pos.z);
    }

    /**
     * Checks if the block is loaded for ticking entities(not all chunks tick entities)
     *
     * @param world world to use
     * @param pos   position to check
     * @return true if block is accessible/loaded
     */
    public static boolean isEntityBlockLoaded(final WorldAccess world, final BlockPos pos) {
        return isEntityChunkLoaded(world, pos.getX() >> 4, pos.getZ() >> 4);
    }

    /**
     * Returns whether an entity ticking chunk is loaded at the position
     *
     * @param world world to check on
     * @param x     chunk position
     * @param z     chunk position
     * @return true if loaded
     */
    public static boolean isEntityChunkLoaded(final WorldAccess world, final int x, final int z) {
        return isEntityChunkLoaded(world, new ChunkPos(x, z));
    }

    /**
     * Returns whether an entity ticking chunk is loaded at the position
     *
     * @param world world to check on
     * @param pos   chunk position
     * @return true if loaded
     */
    public static boolean isEntityChunkLoaded(final WorldAccess world, final ChunkPos pos) {
        if (world instanceof ServerWorld) {
            return isChunkLoaded(world, pos) && ((ServerWorld) world).shouldTickEntity(pos.getStartPos());
        }
        return isChunkLoaded(world, pos);
    }
}
