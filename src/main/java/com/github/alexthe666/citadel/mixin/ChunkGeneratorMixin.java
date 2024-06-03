package com.github.alexthe666.citadel.mixin;

import com.github.alexthe666.citadel.CitadelConstants;
import com.github.alexthe666.citadel.server.event.EventMergeStructureSpawns;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin {

    @Inject(at = @At("RETURN"), remap = CitadelConstants.REMAPREFS, cancellable = true,
            method = "Lnet/minecraft/world/level/chunk/ChunkGenerator;getMobsAt(Lnet/minecraft/core/Holder;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/util/random/WeightedRandomList;")
    private void citadel_getMobsAt(RegistryEntry<Biome> biome, StructureAccessor structureManager, SpawnGroup mobCategory, BlockPos pos, CallbackInfoReturnable<Pool<SpawnSettings.SpawnEntry>> cir) {
        Pool<SpawnSettings.SpawnEntry> biomeSpawns = biome.value().getSpawnSettings().getSpawnEntries(mobCategory);
        if(biomeSpawns != cir.getReturnValue()){
            EventMergeStructureSpawns event = new EventMergeStructureSpawns(structureManager, pos, mobCategory, cir.getReturnValue(), biomeSpawns);
            MinecraftForge.EVENT_BUS.post(event);
            if(event.getResult() == Event.Result.ALLOW){
                cir.setReturnValue(event.getStructureSpawns());
            }
        }
    }

}
