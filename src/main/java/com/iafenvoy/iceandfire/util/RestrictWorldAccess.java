package com.iafenvoy.iceandfire.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.tick.QueryableTickScheduler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class RestrictWorldAccess implements ServerWorldAccess {
    private final ServerWorldAccess origin;
    private final Predicate<BlockPos> checker;

    public RestrictWorldAccess(ServerWorldAccess origin, Predicate<BlockPos> checker) {
        this.origin = origin;
        this.checker = checker;
    }

    @Override
    public ServerWorld toServerWorld() {
        return this.origin.toServerWorld();
    }

    @Override
    public long getTickOrder() {
        return this.origin.getTickOrder();
    }

    @Override
    public QueryableTickScheduler<Block> getBlockTickScheduler() {
        return this.origin.getBlockTickScheduler();
    }

    @Override
    public QueryableTickScheduler<Fluid> getFluidTickScheduler() {
        return this.origin.getFluidTickScheduler();
    }

    @Override
    public WorldProperties getLevelProperties() {
        return this.origin.getLevelProperties();
    }

    @Override
    public LocalDifficulty getLocalDifficulty(BlockPos pos) {
        return this.origin.getLocalDifficulty(pos);
    }

    @Override
    public @Nullable MinecraftServer getServer() {
        return this.origin.getServer();
    }

    @Override
    public ChunkManager getChunkManager() {
        return this.origin.getChunkManager();
    }

    @Override
    public Random getRandom() {
        return this.origin.getRandom();
    }

    @Override
    public void playSound(@Nullable PlayerEntity except, BlockPos pos, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        this.origin.playSound(except, pos, sound, category, volume, pitch);
    }

    @Override
    public void addParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        this.origin.addParticle(parameters, x, y, z, velocityX, velocityY, velocityZ);
    }

    @Override
    public void syncWorldEvent(@Nullable PlayerEntity player, int eventId, BlockPos pos, int data) {
        this.origin.syncWorldEvent(player, eventId, pos, data);
    }

    @Override
    public void emitGameEvent(GameEvent event, Vec3d emitterPos, GameEvent.Emitter emitter) {
        this.origin.emitGameEvent(event, emitterPos, emitter);
    }

    @Override
    public float getBrightness(Direction direction, boolean shaded) {
        return this.origin.getBrightness(direction, shaded);
    }

    @Override
    public LightingProvider getLightingProvider() {
        return this.origin.getLightingProvider();
    }

    @Override
    public WorldBorder getWorldBorder() {
        return this.origin.getWorldBorder();
    }

    @Override
    public @Nullable BlockEntity getBlockEntity(BlockPos pos) {
        if (!this.checker.test(pos)) return null;
        return this.origin.getBlockEntity(pos);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        if (!this.checker.test(pos)) return Blocks.AIR.getDefaultState();
        return this.origin.getBlockState(pos);
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        if (!this.checker.test(pos)) return Fluids.EMPTY.getDefaultState();
        return this.origin.getFluidState(pos);
    }

    @Override
    public List<Entity> getOtherEntities(@Nullable Entity except, Box box, Predicate<? super Entity> predicate) {
        return this.origin.getOtherEntities(except, box, predicate);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesByType(TypeFilter<Entity, T> filter, Box box, Predicate<? super T> predicate) {
        return this.origin.getEntitiesByType(filter, box, predicate);
    }

    @Override
    public List<? extends PlayerEntity> getPlayers() {
        return this.origin.getPlayers();
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState state, int flags, int maxUpdateDepth) {
        if (!this.checker.test(pos)) return false;
        return this.origin.setBlockState(pos, state, flags, maxUpdateDepth);
    }

    @Override
    public boolean removeBlock(BlockPos pos, boolean move) {
        return this.origin.removeBlock(pos, move);
    }

    @Override
    public boolean breakBlock(BlockPos pos, boolean drop, @Nullable Entity breakingEntity, int maxUpdateDepth) {
        return this.origin.breakBlock(pos, drop, breakingEntity, maxUpdateDepth);
    }

    @Override
    public boolean testBlockState(BlockPos pos, Predicate<BlockState> state) {
        return this.origin.testBlockState(pos, state);
    }

    @Override
    public boolean testFluidState(BlockPos pos, Predicate<FluidState> state) {
        return this.origin.testFluidState(pos, state);
    }

    @Override
    public @Nullable Chunk getChunk(int chunkX, int chunkZ, ChunkStatus leastStatus, boolean create) {
        return this.origin.getChunk(chunkX, chunkZ, leastStatus, create);
    }

    @Override
    public int getTopY(Heightmap.Type heightmap, int x, int z) {
        if(!this.checker.test(new BlockPos(x,0,z))) return 64;
        return this.origin.getTopY(heightmap, x, z);
    }

    @Override
    public int getAmbientDarkness() {
        return this.origin.getAmbientDarkness();
    }

    @Override
    public BiomeAccess getBiomeAccess() {
        return this.origin.getBiomeAccess();
    }

    @Override
    public RegistryEntry<Biome> getGeneratorStoredBiome(int biomeX, int biomeY, int biomeZ) {
        return this.origin.getGeneratorStoredBiome(biomeX, biomeY, biomeZ);
    }

    @Override
    public boolean isClient() {
        return this.origin.isClient();
    }

    @Deprecated
    @Override
    public int getSeaLevel() {
        return this.origin.getSeaLevel();
    }

    @Override
    public DimensionType getDimension() {
        return this.origin.getDimension();
    }

    @Override
    public DynamicRegistryManager getRegistryManager() {
        return this.origin.getRegistryManager();
    }

    @Override
    public FeatureSet getEnabledFeatures() {
        return this.origin.getEnabledFeatures();
    }

    public boolean spawnEntity(Entity entity) {
        if (!this.checker.test(entity.getBlockPos())) return false;
        return this.origin.spawnEntity(entity);
    }
}
