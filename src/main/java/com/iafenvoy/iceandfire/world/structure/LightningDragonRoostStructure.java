package com.iafenvoy.iceandfire.world.structure;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.registry.IafStructurePieces;
import com.iafenvoy.iceandfire.registry.IafStructureTypes;
import com.iafenvoy.iceandfire.registry.tag.CommonTags;
import com.iafenvoy.iceandfire.world.GenerationConstant;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.structure.StructureType;

import java.util.stream.Collectors;

public class LightningDragonRoostStructure extends DragonRoostStructure {
    public static final Codec<LightningDragonRoostStructure> CODEC = RecordCodecBuilder.<LightningDragonRoostStructure>mapCodec(instance ->
            instance.group(configCodecBuilder(instance)).apply(instance, LightningDragonRoostStructure::new)).codec();

    protected LightningDragonRoostStructure(Config config) {
        super(config);
    }

    @Override
    protected DragonRoostPiece createPiece(BlockBox boundingBox, boolean isMale) {
        return new LightningDragonRoostPiece(0, boundingBox, IafBlocks.COPPER_PILE, isMale);
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
            if (distance > 0.05D && random.nextInt(800) == 0)
                this.generateSpire(world, random, this.getSurfacePosition(world, position));
            if (distance > 0.05D && random.nextInt(1000) == 0)
                this.generateSpike(world, random, this.getSurfacePosition(world, position), GenerationConstant.HORIZONTALS[random.nextInt(3)]);
        }

        private void generateSpike(WorldAccess worldIn, Random rand, BlockPos position, Direction direction) {
            int radius = 5;
            for (int i = 0; i < 5; i++) {
                int j = Math.max(0, radius - (int) (i * 1.75F));
                int l = radius - i;
                int k = Math.max(0, radius - (int) (i * 1.5F));
                float f = (float) (j + l) * 0.333F + 0.5F;
                BlockPos up = position.up().offset(direction, i);
                int xOrZero = direction.getAxis() == Direction.Axis.Z ? j : 0;
                int zOrZero = direction.getAxis() == Direction.Axis.Z ? 0 : k;
                for (BlockPos blockpos : BlockPos.stream(up.add(-xOrZero, -l, -zOrZero), up.add(xOrZero, l, zOrZero)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
                    if (blockpos.getSquaredDistance(position) <= (double) (f * f)) {
                        int height = Math.max(blockpos.getY() - up.getY(), 0);
                        if (i == 0) {
                            if (rand.nextFloat() < height * 0.3F)
                                worldIn.setBlockState(blockpos, IafBlocks.CRACKLED_STONE.getDefaultState(), 2);
                        } else worldIn.setBlockState(blockpos, IafBlocks.CRACKLED_STONE.getDefaultState(), 2);
                    }
                }
            }
        }

        private void generateSpire(WorldAccess worldIn, Random rand, BlockPos position) {
            int height = 5 + rand.nextInt(5);
            Direction bumpDirection = Direction.NORTH;
            for (int i = 0; i < height; i++) {
                worldIn.setBlockState(position.up(i), IafBlocks.CRACKLED_STONE.getDefaultState(), 2);
                if (rand.nextBoolean()) {
                    bumpDirection = bumpDirection.rotateYClockwise();
                }
                int offset = 1;
                if (i < 4) {
                    worldIn.setBlockState(position.up(i).north(), IafBlocks.CRACKLED_GRAVEL.getDefaultState(), 2);
                    worldIn.setBlockState(position.up(i).south(), IafBlocks.CRACKLED_GRAVEL.getDefaultState(), 2);
                    worldIn.setBlockState(position.up(i).east(), IafBlocks.CRACKLED_GRAVEL.getDefaultState(), 2);
                    worldIn.setBlockState(position.up(i).west(), IafBlocks.CRACKLED_GRAVEL.getDefaultState(), 2);
                    offset = 2;
                }
                if (i < height - 2)
                    worldIn.setBlockState(position.up(i).offset(bumpDirection, offset), IafBlocks.CRACKLED_COBBLESTONE.getDefaultState(), 2);
            }
        }
    }
}
