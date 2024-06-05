package com.github.alexthe666.citadel.mixin;

import com.github.alexthe666.citadel.CitadelConstants;
import com.github.alexthe666.citadel.server.generation.SurfaceRulesManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.HashMap;
import java.util.function.Function;
import net.minecraft.util.Util;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

@Mixin(NoiseChunkGenerator.class)
public class NoiseBasedChunkGeneratorMixin {
    @Unique
    private final Function<MaterialRules.MaterialRule, MaterialRules.MaterialRule> rulesToMerge = Util.memoize(SurfaceRulesManager::mergeOverworldRules);
    @Unique
    private final HashMap<ChunkGeneratorSettings, MaterialRules.MaterialRule> mergedRulesMap = new HashMap<>();

    @Redirect(
            method = "buildSurface(Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/gen/HeightContext;Lnet/minecraft/world/gen/noise/NoiseConfig;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/biome/source/BiomeAccess;Lnet/minecraft/registry/Registry;Lnet/minecraft/world/gen/chunk/Blender;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/chunk/ChunkGeneratorSettings;surfaceRule()Lnet/minecraft/world/gen/surfacebuilder/MaterialRules$MaterialRule;")
    )
    private MaterialRules.MaterialRule citadel_buildSurface_surfaceRuleRedirect(ChunkGeneratorSettings noiseGeneratorSettings) {
        return this.getMergedRulesFor(noiseGeneratorSettings);
    }

    @Redirect(
            method = "carve",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/chunk/ChunkGeneratorSettings;surfaceRule()Lnet/minecraft/world/gen/surfacebuilder/MaterialRules$MaterialRule;")
    )
    private MaterialRules.MaterialRule citadel_applyCarvers_surfaceRuleRedirect(ChunkGeneratorSettings noiseGeneratorSettings) {
        return this.getMergedRulesFor(noiseGeneratorSettings);
    }

    @Unique
    private MaterialRules.MaterialRule getMergedRulesFor(ChunkGeneratorSettings settings){
        MaterialRules.MaterialRule merged = this.mergedRulesMap.get(settings);
        if(merged == null){
            merged = this.rulesToMerge.apply(settings.surfaceRule());
            this.mergedRulesMap.put(settings, merged);
        }
        return merged;
    }
}
