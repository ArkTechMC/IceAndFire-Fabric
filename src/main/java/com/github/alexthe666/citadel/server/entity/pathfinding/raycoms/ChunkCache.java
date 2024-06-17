package com.github.alexthe666.citadel.server.entity.pathfinding.raycoms;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */

import com.github.alexthe666.citadel.server.world.WorldChunkUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChunkCache implements WorldView {
    protected final int chunkX;
    protected final int chunkZ;
    protected final WorldChunk[][] chunkArray;
    /**
     * set by !chunk.getAreLevelsEmpty
     */
    protected final boolean empty;
    /**
     * Reference to the World object.
     */
    protected final World world;
    /**
     * Dimensiontype.
     */
    private final DimensionType dimType;
    private final int minBuildHeight;
    private final int maxBuildHeight;

    public ChunkCache(World worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn, final DimensionType type) {
        this.world = worldIn;
        this.chunkX = posFromIn.getX() - subIn >> 4;
        this.chunkZ = posFromIn.getZ() - subIn >> 4;
        int i = posToIn.getX() + subIn >> 4;
        int j = posToIn.getZ() + subIn >> 4;
        this.chunkArray = new WorldChunk[i - this.chunkX + 1][j - this.chunkZ + 1];
        this.empty = true;

        for (int k = this.chunkX; k <= i; ++k)
            for (int l = this.chunkZ; l <= j; ++l)
                if (WorldChunkUtil.isEntityChunkLoaded(this.world, new ChunkPos(k, l)) && worldIn.getChunkManager() instanceof ServerChunkManager serverChunkCache) {
                    final ChunkHolder holder = serverChunkCache.threadedAnvilChunkStorage.getChunkHolder(ChunkPos.toLong(k, l));
                    if (holder != null)
                        this.chunkArray[k - this.chunkX][l - this.chunkZ] = holder.getAccessibleFuture().getNow(ChunkHolder.UNLOADED_WORLD_CHUNK).left().orElse(null);
                }
        this.dimType = type;
        this.minBuildHeight = worldIn.getBottomY();
        this.maxBuildHeight = worldIn.getTopY();
    }

    /**
     * set by !chunk.getAreLevelsEmpty
     *
     * @return if so.
     */
    public boolean isEmpty() {
        return this.empty;
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        return this.getTileEntity(pos, WorldChunk.CreationType.CHECK); // Forge: don't modify world from other threads
    }

    public BlockEntity getTileEntity(BlockPos pos, WorldChunk.CreationType createType) {
        int i = (pos.getX() >> 4) - this.chunkX;
        int j = (pos.getZ() >> 4) - this.chunkZ;
        if (!this.withinBounds(i, j)) return null;
        return this.chunkArray[i][j].getBlockEntity(pos, createType);
    }

    @Override
    public int getBottomY() {
        return this.minBuildHeight;
    }

    @Override
    public int getTopY() {
        return this.maxBuildHeight;
    }

    @NotNull
    @Override
    public BlockState getBlockState(BlockPos pos) {
        if (pos.getY() >= this.getBottomY() && pos.getY() < this.getTopY()) {
            int i = (pos.getX() >> 4) - this.chunkX;
            int j = (pos.getZ() >> 4) - this.chunkZ;
            if (i >= 0 && i < this.chunkArray.length && j >= 0 && j < this.chunkArray[i].length) {
                WorldChunk chunk = this.chunkArray[i][j];
                if (chunk != null) return chunk.getBlockState(pos);
            }
        }
        return Blocks.AIR.getDefaultState();
    }

    @Override
    public FluidState getFluidState(final BlockPos pos) {
        if (pos.getY() >= this.getBottomY() && pos.getY() < this.getTopY()) {
            int i = (pos.getX() >> 4) - this.chunkX;
            int j = (pos.getZ() >> 4) - this.chunkZ;
            if (i >= 0 && i < this.chunkArray.length && j >= 0 && j < this.chunkArray[i].length) {
                WorldChunk chunk = this.chunkArray[i][j];
                if (chunk != null) return chunk.getFluidState(pos);
            }
        }
        return Fluids.EMPTY.getDefaultState();
    }

    @Override
    public RegistryEntry<Biome> getGeneratorStoredBiome(final int x, final int y, final int z) {
        return null;
    }

    /**
     * Checks to see if an air block exists at the provided location. Note that this only checks to see if the blocks material is set to air, meaning it is possible for non-vanilla
     * blocks to still pass this check.
     */
    @Override
    public boolean isAir(BlockPos pos) {
        BlockState state = this.getBlockState(pos);
        return state.isAir();
    }

    @Override
    public Chunk getChunk(final int x, final int z, final ChunkStatus requiredStatus, final boolean nonnull) {
        int i = x - this.chunkX;
        int j = z - this.chunkZ;
        if (i >= 0 && i < this.chunkArray.length && j >= 0 && j < this.chunkArray[i].length)
            return this.chunkArray[i][j];
        return null;
    }

    @Override
    public boolean isChunkLoaded(final int chunkX, final int chunkZ) {
        return false;
    }

    @Override
    public BlockPos getTopPosition(final Heightmap.Type heightmapType, final BlockPos pos) {
        return null;
    }

    @Override
    public int getTopY(final Heightmap.Type heightmapType, final int x, final int z) {
        return 0;
    }

    @Override
    public int getAmbientDarkness() {
        return 0;
    }

    @Override
    public BiomeAccess getBiomeAccess() {
        return null;
    }

    @Override
    public WorldBorder getWorldBorder() {
        return null;
    }

    @Override
    public boolean doesNotIntersectEntities(final Entity entityIn, final VoxelShape shape) {
        return false;
    }

    @Override
    public List<VoxelShape> getEntityCollisions(@org.jetbrains.annotations.Nullable final Entity p_186427_, final Box p_186428_) {
        return null;
    }

    @Override
    public int getStrongRedstonePower(BlockPos pos, Direction direction) {
        return this.getBlockState(pos).getStrongRedstonePower(this, pos, direction);
    }

    @Override
    public DynamicRegistryManager getRegistryManager() {
        return DynamicRegistryManager.EMPTY;
    }

    @Override
    public FeatureSet getEnabledFeatures() {
        return FeatureSet.empty();
    }

    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public DimensionType getDimension() {
        return this.dimType;
    }

    private boolean withinBounds(int x, int z) {
        return x >= 0 && x < this.chunkArray.length && z >= 0 && z < this.chunkArray[x].length && this.chunkArray[x][z] != null;
    }

    @Override
    public float getBrightness(final Direction direction, final boolean b) {
        return 0;
    }

    @Override
    public LightingProvider getLightingProvider() {
        return null;
    }
}
