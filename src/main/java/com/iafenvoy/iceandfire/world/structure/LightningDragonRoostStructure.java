package com.iafenvoy.iceandfire.world.structure;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.registry.IafStructurePieces;
import com.iafenvoy.iceandfire.registry.IafStructureTypes;
import com.iafenvoy.iceandfire.registry.tag.CommonTags;
import com.iafenvoy.iceandfire.world.gen.WorldGenRoostSpike;
import com.iafenvoy.iceandfire.world.gen.WorldGenRoostSpire;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.StructureContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.structure.StructureType;

public class LightningDragonRoostStructure extends DragonRoostStructure {
    public static final Codec<LightningDragonRoostStructure> CODEC = RecordCodecBuilder.<LightningDragonRoostStructure>mapCodec(instance ->
            instance.group(configCodecBuilder(instance)).apply(instance, LightningDragonRoostStructure::new)).codec();

    protected LightningDragonRoostStructure(Config config) {
        super(config);
    }

    @Override
    protected DragonRoostPiece createPiece(int length, BlockBox boundingBox, boolean isMale) {
        return new LightningDragonRoostPiece(length, boundingBox, IafBlocks.COPPER_PILE, isMale);
    }

    @Override
    public StructureType<?> getType() {
        return IafStructureTypes.LIGHTNING_DRAGON_ROOST;
    }

    public static class LightningDragonRoostPiece extends DragonRoostPiece {
        private static final Identifier DRAGON_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/lightning_dragon_roost");

        protected LightningDragonRoostPiece(int length, BlockBox boundingBox, Block treasureBlock, boolean isMale) {
            super(IafStructurePieces.LIGHTNING_DRAGON_ROOST, length, boundingBox, treasureBlock, isMale);
        }

        public LightningDragonRoostPiece(StructureContext context, NbtCompound nbt) {
            super(IafStructurePieces.LIGHTNING_DRAGON_ROOST, nbt);
        }

        @Override
        protected EntityType<? extends EntityDragonBase> getDragonType() {
            return IafEntities.LIGHTNING_DRAGON;
        }

        @Override
        protected Identifier getRoostLootTable() {
            return DRAGON_CHEST;
        }

        @Override
        protected BlockState transform(final BlockState state) {
            Block block = null;
            if (state.isOf(Blocks.GRASS_BLOCK))
                block = IafBlocks.CRACKLED_GRASS;
            else if (state.isOf(Blocks.DIRT_PATH))
                block = IafBlocks.CRACKLED_DIRT_PATH;
            else if (state.isIn(CommonTags.Blocks.GRAVEL))
                block = IafBlocks.CRACKLED_GRAVEL;
            else if (state.isIn(BlockTags.DIRT))
                block = IafBlocks.CRACKLED_DIRT;
            else if (state.isIn(CommonTags.Blocks.STONE))
                block = IafBlocks.CRACKLED_STONE;
            else if (state.isIn(CommonTags.Blocks.COBBLESTONE))
                block = IafBlocks.CRACKLED_COBBLESTONE;
            else if (state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.PLANKS))
                block = IafBlocks.ASH;
            else if (state.isOf(Blocks.GRASS) || state.isIn(BlockTags.LEAVES) || state.isIn(BlockTags.FLOWERS) || state.isIn(BlockTags.CROPS))
                block = Blocks.AIR;
            if (block != null) return block.getDefaultState();
            return state;
        }

        @Override
        protected void handleCustomGeneration(StructureWorldAccess world, BlockPos origin, Random random, BlockPos position, double distance) {
            if (distance > 0.05D && random.nextInt(800) == 0)// FIXME
                new WorldGenRoostSpire().generate(world, random, this.getSurfacePosition(world, position));
            if (distance > 0.05D && random.nextInt(1000) == 0)// FIXME
                new WorldGenRoostSpike(HORIZONTALS[random.nextInt(3)]).generate(world, random, this.getSurfacePosition(world, position));
        }
    }
}
