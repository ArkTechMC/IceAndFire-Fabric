package com.iafenvoy.iceandfire.util;

import com.iafenvoy.iceandfire.registry.IafFeatures;
import com.iafenvoy.iceandfire.world.IafWorldData;
import net.minecraft.block.BlockState;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

/**
 * Class which has world related util functions like chunk load checks
 */
public class WorldUtil {
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
     * Mark a chunk at a position dirty if loaded.
     *
     * @param world the world to mark it dirty in.
     * @param pos   the position within the chunk.
     */
    public static void markChunkDirty(final World world, final BlockPos pos) {
        if (WorldUtil.isBlockLoaded(world, pos)) {
            world.getChunk(pos.getX() >> 4, pos.getZ() >> 4).setNeedsSaving(true);
            final BlockState state = world.getBlockState(pos);
            world.updateListeners(pos, state, state, 3);
        }
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

    /**
     * Returns whether an axis aligned bb is entirely loaded.
     *
     * @param world world to check on.
     * @param box   the box.
     * @return true if loaded.
     */
    public static boolean isAABBLoaded(final World world, final Box box) {
        return isChunkLoaded(world, ((int) box.minX) >> 4, ((int) box.minZ) >> 4) && isChunkLoaded(world, ((int) box.maxX) >> 4, ((int) box.maxZ) >> 4);
    }


    /**
     * Check if it's currently day inn the world.
     *
     * @param world the world to check.
     * @return true if so.
     */
    public static boolean isPastTime(final World world, final int pastTime) {
        return world.getTimeOfDay() % 24000 <= pastTime;
    }


    /**
     * Check if a world is of the overworld type.
     *
     * @param world the world to check.
     * @return true if so.
     */
    public static boolean isOverworldType(final World world) {
        return isOfWorldType(world, DimensionTypes.OVERWORLD);
    }

    /**
     * Check if a world is of the nether type.
     *
     * @param world the world to check.
     * @return true if so.
     */
    public static boolean isNetherType(final World world) {
        return isOfWorldType(world, DimensionTypes.THE_NETHER);
    }

    /**
     * Check if a world has a specific dimension type.
     *
     * @param world the world to check.
     * @param type  the type to compare.
     * @return true if it matches.
     */
    public static boolean isOfWorldType(final World world, final RegistryKey<DimensionType> type) {
        DynamicRegistryManager dynRegistries = world.getRegistryManager();
        Identifier loc = dynRegistries.getOptional(RegistryKeys.DIMENSION_TYPE).get().getId(world.getDimension());
        if (loc == null) {
            if (world.isClient) {
                return world.getDimension().effects().equals(type.getValue());
            }
            return false;
        }
        RegistryKey<DimensionType> regKey = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, loc);
        return regKey == type;
    }

    /**
     * Check to see if the world is peaceful.
     * <p>
     * There are several checks performed here, currently both gamerule and difficulty.
     *
     * @param world world to check
     * @return true if peaceful
     */
    public static boolean isPeaceful(final World world) {
        return !world.getLevelProperties().getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING) || world.getDifficulty().equals(Difficulty.PEACEFUL);
    }

    /**
     * Returns a dimensions max height
     *
     * @param dimensionType
     * @return
     */
    public static int getDimensionMaxHeight(final DimensionType dimensionType) {
        return dimensionType.logicalHeight() + dimensionType.minY();
    }

    /**
     * Returns a dimension min height
     *
     * @param dimensionType
     * @return
     */
    public static int getDimensionMinHeight(final DimensionType dimensionType) {
        return dimensionType.minY();
    }

    /**
     * Check if a given block y is within world bounds
     *
     * @param yBlock
     * @param world
     * @return
     */
    public static boolean isInWorldHeight(final int yBlock, final World world) {
        final DimensionType dimensionType = world.getDimension();
        return yBlock > getDimensionMinHeight(dimensionType) && yBlock < getDimensionMaxHeight(dimensionType);
    }

    public static boolean canGenerate(double configChance, final StructureWorldAccess level, final Random random, final BlockPos origin, final String id, boolean checkFluid) {
        return canGenerate(configChance, level, random, origin, id, IafWorldData.FeatureType.SURFACE, checkFluid);
    }

    public static boolean canGenerate(double configChance, final StructureWorldAccess level, final Random random, final BlockPos origin, final String id, final IafWorldData.FeatureType type, boolean checkFluid) {
        boolean canGenerate = random.nextDouble() < configChance && IafFeatures.isFarEnoughFromSpawn(level, origin) && IafFeatures.isFarEnoughFromDangerousGen(level, origin, id, type);
        if (canGenerate && checkFluid)
            if (!level.getFluidState(origin.down()).isEmpty())
                return false;
        return canGenerate;
    }
}
