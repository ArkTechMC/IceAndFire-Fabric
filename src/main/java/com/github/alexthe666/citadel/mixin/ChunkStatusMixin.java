package com.github.alexthe666.citadel.mixin;

import com.github.alexthe666.citadel.server.generation.IMultiNoiseBiomeSourceAccessor;
import com.mojang.datafixers.util.Either;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

@Mixin(ChunkStatus.class)
public class ChunkStatusMixin {
    @Inject(at = @At("HEAD"), method = "runGenerationTask")
    private void citadel_fillFromNoise(Executor p_283276_, ServerWorld serverLevel, ChunkGenerator chunkGenerator, StructureTemplateManager p_281305_, ServerLightingProvider p_282570_, Function<Chunk, CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> p_283114_, List<Chunk> p_282723_, CallbackInfoReturnable<CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> cir) {
        if (chunkGenerator.getBiomeSource() instanceof IMultiNoiseBiomeSourceAccessor multiNoiseBiomeSourceAccessor) {
            multiNoiseBiomeSourceAccessor.setLastSampledSeed(serverLevel.getSeed());
            multiNoiseBiomeSourceAccessor.setLastSampledDimension(serverLevel.getRegistryKey());
        }
    }
}
