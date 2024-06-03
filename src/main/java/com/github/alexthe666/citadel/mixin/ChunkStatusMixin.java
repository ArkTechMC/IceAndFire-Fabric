package com.github.alexthe666.citadel.mixin;

import com.github.alexthe666.citadel.CitadelConstants;
import com.github.alexthe666.citadel.server.generation.IMultiNoiseBiomeSourceAccessor;
import com.github.alexthe666.citadel.server.world.ExpandedBiomeSource;
import com.mojang.datafixers.util.Either;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.chunk.ChunkGenerator;

@Mixin(ChunkStatus.class)
public class ChunkStatusMixin {

    @Inject(at = @At("HEAD"), remap = CitadelConstants.REMAPREFS, cancellable = true,
            method = "Lnet/minecraft/world/level/chunk/ChunkStatus;generate(Ljava/util/concurrent/Executor;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;Lnet/minecraft/server/level/ThreadedLevelLightEngine;Ljava/util/function/Function;Ljava/util/List;)Ljava/util/concurrent/CompletableFuture;")
    private void citadel_fillFromNoise(Executor p_283276_, ServerWorld serverLevel, ChunkGenerator chunkGenerator, StructureTemplateManager p_281305_, ServerLightingProvider p_282570_, Function<Chunk, CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> p_283114_, List<Chunk> p_282723_, CallbackInfoReturnable<CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> cir) {
        if(chunkGenerator.getBiomeSource() instanceof IMultiNoiseBiomeSourceAccessor multiNoiseBiomeSourceAccessor){
            multiNoiseBiomeSourceAccessor.setLastSampledSeed(serverLevel.getSeed());
            multiNoiseBiomeSourceAccessor.setLastSampledDimension(serverLevel.getRegistryKey());
        }
    }
}
