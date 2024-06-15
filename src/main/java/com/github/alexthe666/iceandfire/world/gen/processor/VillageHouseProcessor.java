package com.github.alexthe666.iceandfire.world.gen.processor;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.IafProcessors;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;

public class VillageHouseProcessor extends StructureProcessor {
    public static final Identifier LOOT = new Identifier(IceAndFire.MOD_ID, "chest/village_scribe");
    public static final VillageHouseProcessor INSTANCE = new VillageHouseProcessor();
    public static final Codec<VillageHouseProcessor> CODEC = Codec.unit(() -> INSTANCE);

    public VillageHouseProcessor() {
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(WorldView worldReader, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo infoIn2, StructurePlacementData settings) {
        Random random = settings.getRandom(infoIn2.pos());
        if (infoIn2.state().getBlock() == Blocks.CHEST) {
            NbtCompound tag = new NbtCompound();
            tag.putString("LootTable", LOOT.toString());
            tag.putLong("LootTableSeed", random.nextLong());
            return new StructureTemplate.StructureBlockInfo(infoIn2.pos(), infoIn2.state(), tag);
        }
        return infoIn2;
    }

    @Override
    protected StructureProcessorType getType() {
        return IafProcessors.VILLAGEHOUSEPROCESSOR.get();
    }

}
