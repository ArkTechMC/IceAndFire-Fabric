package com.iafenvoy.iceandfire.world.gen;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.block.BlockGoldPile;
import com.iafenvoy.iceandfire.entity.util.MyrmexHive;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;

import java.util.stream.Collectors;

public class WorldGenMyrmexDecoration {

    public static final Identifier MYRMEX_GOLD_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/myrmex_loot_chest");
    public static final Identifier DESERT_MYRMEX_FOOD_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/myrmex_desert_food_chest");
    public static final Identifier JUNGLE_MYRMEX_FOOD_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/myrmex_jungle_food_chest");
    public static final Identifier MYRMEX_TRASH_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/myrmex_trash_chest");
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    public static void generateSkeleton(WorldAccess worldIn, BlockPos blockpos, BlockPos origin, int radius, Random rand) {
        if (worldIn.getBlockState(blockpos.down()).isSideSolidFullSquare(worldIn, blockpos.down(), Direction.UP)) {
            Direction direction = Direction.fromHorizontal(rand.nextInt(3));
            Direction.Axis oppositeAxis = direction.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
            int maxRibHeight = rand.nextInt(2);
            for (int spine = 0; spine < 5 + rand.nextInt(2) * 2; spine++) {
                BlockPos segment = blockpos.offset(direction, spine);
                if (origin.getSquaredDistance(segment) <= (double) (radius * radius)) {
                    worldIn.setBlockState(segment, Blocks.BONE_BLOCK.getDefaultState().with(PillarBlock.AXIS, direction.getAxis()), 2);
                }
                if (spine % 2 != 0) {
                    BlockPos rightRib = segment.offset(direction.rotateYCounterclockwise());
                    BlockPos leftRib = segment.offset(direction.rotateYClockwise());
                    if (origin.getSquaredDistance(rightRib) <= (double) (radius * radius)) {
                        worldIn.setBlockState(rightRib, Blocks.BONE_BLOCK.getDefaultState().with(PillarBlock.AXIS, oppositeAxis), 2);
                    }
                    if (origin.getSquaredDistance(leftRib) <= (double) (radius * radius)) {
                        worldIn.setBlockState(leftRib, Blocks.BONE_BLOCK.getDefaultState().with(PillarBlock.AXIS, oppositeAxis), 2);
                    }
                    for (int ribHeight = 1; ribHeight < maxRibHeight + 2; ribHeight++) {
                        if (origin.getSquaredDistance(rightRib.up(ribHeight).offset(direction.rotateYCounterclockwise())) <= (double) (radius * radius)) {
                            worldIn.setBlockState(rightRib.up(ribHeight).offset(direction.rotateYCounterclockwise()), Blocks.BONE_BLOCK.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y), 2);
                        }
                        if (origin.getSquaredDistance(leftRib.up(ribHeight).offset(direction.rotateYClockwise())) <= (double) (radius * radius)) {
                            worldIn.setBlockState(leftRib.up(ribHeight).offset(direction.rotateYClockwise()), Blocks.BONE_BLOCK.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y), 2);
                        }
                    }
                    if (origin.getSquaredDistance(rightRib.up(maxRibHeight + 2)) <= (double) (radius * radius)) {
                        worldIn.setBlockState(rightRib.up(maxRibHeight + 2), Blocks.BONE_BLOCK.getDefaultState().with(PillarBlock.AXIS, oppositeAxis), 2);
                    }
                    if (origin.getSquaredDistance(leftRib.up(maxRibHeight + 2)) <= (double) (radius * radius)) {
                        worldIn.setBlockState(leftRib.up(maxRibHeight + 2), Blocks.BONE_BLOCK.getDefaultState().with(PillarBlock.AXIS, oppositeAxis), 2);
                    }
                }
            }
        }
    }

    public static void generateLeaves(WorldAccess worldIn, BlockPos blockpos, BlockPos origin, int radius, Random rand, boolean jungle) {
        if (worldIn.getBlockState(blockpos.down()).isSideSolidFullSquare(worldIn, blockpos.down(), Direction.UP)) {
            BlockState leaf = Blocks.OAK_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, Boolean.TRUE);
            if (jungle) {
                leaf = Blocks.JUNGLE_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, Boolean.TRUE);
            }
            int i1 = 0;
            for (int i = 0; i < 3; ++i) {
                int j = i1 + rand.nextInt(2);
                int k = i1 + rand.nextInt(2);
                int l = i1 + rand.nextInt(2);
                float f = (float) (j + k + l) * 0.333F + 0.5F;
                for (BlockPos pos : BlockPos.stream(blockpos.add(-j, -k, -l), blockpos.add(j, k, l)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
                    if (pos.getSquaredDistance(blockpos) <= (double) (f * f) && worldIn.isAir(pos)) {
                        worldIn.setBlockState(pos, leaf, 4);
                    }
                }
                blockpos = blockpos.add(-(i1 + 1) + rand.nextInt(2 + 0), -rand.nextInt(2), -(i1 + 1) + rand.nextInt(2 + 0));
            }
        }
    }

    public static void generatePumpkins(WorldAccess worldIn, BlockPos blockpos, BlockPos origin, int radius, Random rand, boolean jungle) {
        if (worldIn.getBlockState(blockpos.down()).isSideSolidFullSquare(worldIn, blockpos.down(), Direction.UP)) {
            worldIn.setBlockState(blockpos, jungle ? Blocks.MELON.getDefaultState() : Blocks.PUMPKIN.getDefaultState(), 2);
        }
    }

    public static void generateCocoon(WorldAccess worldIn, BlockPos blockpos, Random rand, boolean jungle, Identifier lootTable) {
        if (worldIn.getBlockState(blockpos.down()).isSideSolidFullSquare(worldIn, blockpos.down(), Direction.UP)) {
            worldIn.setBlockState(blockpos, jungle ? IafBlocks.JUNGLE_MYRMEX_COCOON.getDefaultState() : IafBlocks.DESERT_MYRMEX_COCOON.getDefaultState(), 3);

            if (worldIn.getBlockEntity(blockpos) != null && worldIn.getBlockEntity(blockpos) instanceof LootableContainerBlockEntity) {
                BlockEntity tileentity1 = worldIn.getBlockEntity(blockpos);
                assert tileentity1 != null;
                ((LootableContainerBlockEntity) tileentity1).setLootTable(lootTable, rand.nextLong());

            }
        }
    }

    public static void generateMushrooms(WorldAccess worldIn, BlockPos blockpos, BlockPos origin, int radius, Random rand) {
        if (worldIn.getBlockState(blockpos.down()).isSideSolidFullSquare(worldIn, blockpos.down(), Direction.UP)) {
            worldIn.setBlockState(blockpos, rand.nextBoolean() ? Blocks.BROWN_MUSHROOM.getDefaultState() : Blocks.RED_MUSHROOM.getDefaultState(), 2);
        }
    }

    public static void generateGold(WorldAccess worldIn, BlockPos blockpos, BlockPos origin, int radius, Random rand) {
        BlockState gold = IafBlocks.GOLD_PILE.getDefaultState();
        int choice = rand.nextInt(2);
        if (choice == 1) {
            gold = IafBlocks.SILVER_PILE.getDefaultState();
        } else if (choice == 2) {
            gold = IafBlocks.COPPER_PILE.getDefaultState();
        }
        if (worldIn.getBlockState(blockpos.down()).isSideSolidFullSquare(worldIn, blockpos.down(), Direction.UP)) {
            worldIn.setBlockState(blockpos, gold.with(BlockGoldPile.LAYERS, 8), 3);
            worldIn.setBlockState(MyrmexHive.getGroundedPos(worldIn, blockpos.north()), gold.with(BlockGoldPile.LAYERS, 1 + Random.create().nextInt(7)), 3);
            worldIn.setBlockState(MyrmexHive.getGroundedPos(worldIn, blockpos.south()), gold.with(BlockGoldPile.LAYERS, 1 + Random.create().nextInt(7)), 3);
            worldIn.setBlockState(MyrmexHive.getGroundedPos(worldIn, blockpos.west()), gold.with(BlockGoldPile.LAYERS, 1 + Random.create().nextInt(7)), 3);
            worldIn.setBlockState(MyrmexHive.getGroundedPos(worldIn, blockpos.east()), gold.with(BlockGoldPile.LAYERS, 1 + Random.create().nextInt(7)), 3);
            if (rand.nextInt(3) == 0) {
                worldIn.setBlockState(blockpos.up(), Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, HORIZONTALS[Random.create().nextInt(3)]), 2);
                if (worldIn.getBlockState(blockpos.up()).getBlock() instanceof ChestBlock) {
                    BlockEntity tileentity1 = worldIn.getBlockEntity(blockpos.up());
                    if (tileentity1 instanceof ChestBlockEntity) {
                        ((ChestBlockEntity) tileentity1).setLootTable(MYRMEX_GOLD_CHEST, rand.nextLong());
                    }
                }
            }
        }
    }

    public static void generateTrashHeap(WorldAccess worldIn, BlockPos blockpos, BlockPos origin, int radius, Random rand) {
        if (worldIn.getBlockState(blockpos.down()).isSideSolidFullSquare(worldIn, blockpos.down(), Direction.UP)) {
            Block blob = switch (rand.nextInt(3)) {
                case 0 -> Blocks.DIRT;
                case 1 -> Blocks.SAND;
                case 2 -> Blocks.COBBLESTONE;
                case 3 -> Blocks.GRAVEL;
                default -> Blocks.AIR;
            };
            int i1 = 0;
            for (int i = 0; i < 3; ++i) {
                int j = i1 + rand.nextInt(2);
                int k = i1 + rand.nextInt(2);
                int l = i1 + rand.nextInt(2);
                float f = (float) (j + k + l) * 0.333F + 0.5F;
                for (BlockPos pos : BlockPos.stream(blockpos.add(-j, -k, -l), blockpos.add(j, k, l)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
                    if (pos.getSquaredDistance(blockpos) <= (double) (f * f)) {
                        worldIn.setBlockState(pos, blob.getDefaultState(), 4);
                    }
                }
                blockpos = blockpos.add(-(i1 + 1) + rand.nextInt(2 + 0), -rand.nextInt(2), -(i1 + 1) + rand.nextInt(2 + 0));
            }

        }
    }

    public static void generateTrashOre(WorldAccess worldIn, BlockPos blockpos, BlockPos origin, int radius, Random rand) {
        Block current = worldIn.getBlockState(blockpos).getBlock();
        if (origin.getSquaredDistance(blockpos) <= (double) (radius * radius)) {
            if (current == Blocks.DIRT || current == Blocks.SAND || current == Blocks.COBBLESTONE || current == Blocks.GRAVEL) {
                Block ore = Blocks.REDSTONE_ORE;
                if (rand.nextInt(3) == 0) {
                    ore = rand.nextBoolean() ? Blocks.GOLD_ORE : IafBlocks.SILVER_ORE;
                    if (rand.nextInt(2) == 0) {
                        ore = Blocks.COPPER_ORE;
                    }
                } else if (rand.nextInt(3) == 0) {
                    ore = Blocks.DIAMOND_ORE;
                } else if (rand.nextInt(2) == 0) {
                    ore = rand.nextBoolean() ? Blocks.EMERALD_ORE : IafBlocks.SAPPHIRE_ORE;
                    if (rand.nextInt(2) == 0) {
                        ore = Blocks.AMETHYST_CLUSTER;
                    }
                }
                worldIn.setBlockState(blockpos, ore.getDefaultState(), 2);
            }
        }
    }
}