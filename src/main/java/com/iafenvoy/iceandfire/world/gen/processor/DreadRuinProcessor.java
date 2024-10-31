package com.iafenvoy.iceandfire.world.gen.processor;

import com.iafenvoy.iceandfire.registry.IafBlocks;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.registry.IafProcessors;
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

public class DreadRuinProcessor extends StructureProcessor {

    public static final DreadRuinProcessor INSTANCE = new DreadRuinProcessor();
    public static final Codec<DreadRuinProcessor> CODEC = Codec.unit(() -> INSTANCE);

    public DreadRuinProcessor() {
    }

    public static BlockState getRandomCrackedBlock(BlockState prev, Random random) {
        float rand = random.nextFloat();
        if (rand < 0.5) {
            return IafBlocks.DREAD_STONE_BRICKS.getDefaultState();
        } else if (rand < 0.9) {
            return IafBlocks.DREAD_STONE_BRICKS_CRACKED.getDefaultState();
        } else {
            return IafBlocks.DREAD_STONE_BRICKS_MOSSY.getDefaultState();
        }
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(WorldView worldReader, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo infoIn2, StructurePlacementData settings) {
        Random random = settings.getRandom(infoIn2.pos());

        if (infoIn2.state().getBlock() == IafBlocks.DREAD_STONE_BRICKS) {
            BlockState state = getRandomCrackedBlock(null, random);
            return new StructureTemplate.StructureBlockInfo(infoIn2.pos(), state, null);
        }
        if (infoIn2.state().getBlock() == IafBlocks.DREAD_SPAWNER) {
            NbtCompound tag = new NbtCompound();
            NbtCompound spawnData = new NbtCompound();
            Identifier spawnerMobId = Registries.ENTITY_TYPE.getId(this.getRandomMobForMobSpawner(random));
            NbtCompound entity = new NbtCompound();
            entity.putString("id", spawnerMobId.toString());
            spawnData.put("entity", entity);
            tag.remove("SpawnPotentials");
            tag.put("SpawnData", spawnData.copy());
            return new StructureTemplate.StructureBlockInfo(infoIn2.pos(), IafBlocks.DREAD_SPAWNER.getDefaultState(), tag);

        }
        return infoIn2;

    }

    @Override
    protected StructureProcessorType<?> getType() {
        return IafProcessors.DREADRUINPROCESSOR;
    }

    private EntityType<?> getRandomMobForMobSpawner(Random random) {
        float rand = random.nextFloat();
        if (rand < 0.3D) {
            return IafEntities.DREAD_THRALL;
        } else if (rand < 0.5D) {
            return IafEntities.DREAD_GHOUL;
        } else if (rand < 0.7D) {
            return IafEntities.DREAD_BEAST;
        } else if (rand < 0.85D) {
            return IafEntities.DREAD_SCUTTLER;
        }
        return IafEntities.DREAD_KNIGHT;
    }
}
