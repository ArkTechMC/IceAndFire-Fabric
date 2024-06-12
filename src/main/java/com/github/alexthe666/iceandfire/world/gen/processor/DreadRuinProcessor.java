package com.github.alexthe666.iceandfire.world.gen.processor;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.world.IafProcessors;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;

public class DreadRuinProcessor extends StructureProcessor {

    public static final DreadRuinProcessor INSTANCE = new DreadRuinProcessor();
    public static final Codec<DreadRuinProcessor> CODEC = Codec.unit(() -> INSTANCE);

    public DreadRuinProcessor() {
    }

    public static BlockState getRandomCrackedBlock(BlockState prev, Random random) {
        float rand = random.nextFloat();
        if (rand < 0.5) {
            return IafBlockRegistry.DREAD_STONE_BRICKS.get().getDefaultState();
        } else if (rand < 0.9) {
            return IafBlockRegistry.DREAD_STONE_BRICKS_CRACKED.get().getDefaultState();
        } else {
            return IafBlockRegistry.DREAD_STONE_BRICKS_MOSSY.get().getDefaultState();
        }
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(@NotNull WorldView worldReader, @NotNull BlockPos pos, @NotNull BlockPos pos2, StructureTemplate.@NotNull StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo infoIn2, StructurePlacementData settings) {
        Random random = settings.getRandom(infoIn2.pos());

        if (infoIn2.state().getBlock() == IafBlockRegistry.DREAD_STONE_BRICKS.get()) {
            BlockState state = getRandomCrackedBlock(null, random);
            return new StructureTemplate.StructureBlockInfo(infoIn2.pos(), state, null);
        }
        if (infoIn2.state().getBlock() == IafBlockRegistry.DREAD_SPAWNER.get()) {
            NbtCompound tag = new NbtCompound();
            NbtCompound spawnData = new NbtCompound();
            Identifier spawnerMobId = Registries.ENTITY_TYPE.getId(this.getRandomMobForMobSpawner(random));
            if (spawnerMobId != null) {
                NbtCompound entity = new NbtCompound();
                entity.putString("id", spawnerMobId.toString());
                spawnData.put("entity", entity);
                tag.remove("SpawnPotentials");
                tag.put("SpawnData", spawnData.copy());
            }
            return new StructureTemplate.StructureBlockInfo(infoIn2.pos(), IafBlockRegistry.DREAD_SPAWNER.get().getDefaultState(), tag);

        }
        return infoIn2;

    }

    @Override
    protected @NotNull StructureProcessorType getType() {
        return IafProcessors.DREADRUINPROCESSOR.get();
    }

    private EntityType getRandomMobForMobSpawner(Random random) {
        float rand = random.nextFloat();
        if (rand < 0.3D) {
            return IafEntityRegistry.DREAD_THRALL.get();
        } else if (rand < 0.5D) {
            return IafEntityRegistry.DREAD_GHOUL.get();
        } else if (rand < 0.7D) {
            return IafEntityRegistry.DREAD_BEAST.get();
        } else if (rand < 0.85D) {
            return IafEntityRegistry.DREAD_SCUTTLER.get();
        }
        return IafEntityRegistry.DREAD_KNIGHT.get();
    }
}
