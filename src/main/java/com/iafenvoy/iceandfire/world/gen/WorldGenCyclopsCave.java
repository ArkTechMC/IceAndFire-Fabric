package com.iafenvoy.iceandfire.world.gen;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.entity.EntityCyclops;
import com.iafenvoy.iceandfire.item.block.BlockGoldPile;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.util.WorldUtil;
import com.iafenvoy.iceandfire.world.IafWorldData;
import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;
import java.util.stream.Collectors;

public class WorldGenCyclopsCave extends Feature<DefaultFeatureConfig> implements TypedFeature {
    public static final Identifier CYCLOPS_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/cyclops_cave");
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    public WorldGenCyclopsCave(final Codec<DefaultFeatureConfig> configuration) {
        super(configuration);
    }

    private static void generateShell(final FeatureContext<DefaultFeatureConfig> context, int size) {
        int x = size + context.getRandom().nextInt(2);
        int y = 12 + context.getRandom().nextInt(2);
        int z = size + context.getRandom().nextInt(2);
        float radius = (x + y + z) * 0.333F + 0.5F;

        for (BlockPos position : BlockPos.stream(context.getOrigin().add(-x, -y, -z), context.getOrigin().add(x, y, z)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
            boolean doorwayX = position.getX() >= context.getOrigin().getX() - 2 + context.getRandom().nextInt(2) && position.getX() <= context.getOrigin().getX() + 2 + context.getRandom().nextInt(2);
            boolean doorwayZ = position.getZ() >= context.getOrigin().getZ() - 2 + context.getRandom().nextInt(2) && position.getZ() <= context.getOrigin().getZ() + 2 + context.getRandom().nextInt(2);
            boolean isNotInDoorway = !doorwayX && !doorwayZ && position.getY() > context.getOrigin().getY() || position.getY() > context.getOrigin().getY() + y - (3 + context.getRandom().nextInt(2));

            if (position.getSquaredDistance(context.getOrigin()) <= radius * radius) {
                BlockState state = context.getWorld().getBlockState(position);

                if (!(state.getBlock() instanceof AbstractChestBlock) && state.getHardness(context.getWorld(), position) >= 0 && isNotInDoorway) {
                    context.getWorld().setBlockState(position, Blocks.STONE.getDefaultState(), Block.NOTIFY_ALL);
                }
                if (position.getY() == context.getOrigin().getY()) {
                    context.getWorld().setBlockState(position, Blocks.MOSSY_COBBLESTONE.getDefaultState(), Block.NOTIFY_ALL);
                }
                if (position.getY() <= context.getOrigin().getY() - 1 && !state.isOpaque()) {
                    context.getWorld().setBlockState(position, Blocks.COBBLESTONE.getDefaultState(), Block.NOTIFY_ALL);
                }
            }
        }
    }

    @Override
    public boolean generate(final FeatureContext<DefaultFeatureConfig> context) {
        if (!WorldUtil.canGenerate(IafCommonConfig.INSTANCE.cyclops.spawnCaveChance.getDoubleValue(), context.getWorld(), context.getRandom(), context.getOrigin(), this.getId(), true)) {
            return false;
        }

        int size = 16;
        int distance = 6;

        // Unsure :: Checks if the corners of the feature are on solid ground to make sure it doesn't float
        if (context.getWorld().isAir(context.getOrigin().add(size - distance, -3, -size + distance)) || context.getWorld().isAir(context.getOrigin().add(size - distance, -3, size - distance)) || context.getWorld().isAir(context.getOrigin().add(-size + distance, -3, -size + distance)) || context.getWorld().isAir(context.getOrigin().add(-size + distance, -3, size - distance))) {
            return false;
        }

        generateShell(context, size);

        int innerSize = size - 2;
        int x = innerSize + context.getRandom().nextInt(2);
        int y = 10 + context.getRandom().nextInt(2);
        int z = innerSize + context.getRandom().nextInt(2);
        float radius = (x + y + z) * 0.333F + 0.5F;

        int sheepPenCount = 0;

        // Clear out the area
        for (BlockPos position : BlockPos.stream(context.getOrigin().add(-x, -y, -z), context.getOrigin().add(x, y, z)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
            if (position.getSquaredDistance(context.getOrigin()) <= radius * radius && position.getY() > context.getOrigin().getY()) {
                if (!(context.getWorld().getBlockState(context.getOrigin()).getBlock() instanceof AbstractChestBlock)) {
                    context.getWorld().setBlockState(position, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
                }
            }
        }

        // Set up the actual content
        for (BlockPos position : BlockPos.stream(context.getOrigin().add(-x, -y, -z), context.getOrigin().add(x, y, z)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
            if (position.getSquaredDistance(context.getOrigin()) <= radius * radius && position.getY() == context.getOrigin().getY()) {
                if (context.getRandom().nextInt(130) == 0 && this.isTouchingAir(context.getWorld(), position.up())) {
                    this.generateSkeleton(context.getWorld(), position.up(), context.getRandom(), context.getOrigin(), radius);
                }

                if (context.getRandom().nextInt(130) == 0 && position.getSquaredDistance(context.getOrigin()) <= (double) (radius * radius) * 0.8F && sheepPenCount < 2) {
                    this.generateSheepPen(context.getWorld(), position.up(), context.getRandom(), context.getOrigin(), radius);
                    sheepPenCount++;
                }

                if (context.getRandom().nextInt(80) == 0 && this.isTouchingAir(context.getWorld(), position.up())) {
                    context.getWorld().setBlockState(position.up(), IafBlocks.GOLD_PILE.getDefaultState().with(BlockGoldPile.LAYERS, 8), 3);
                    context.getWorld().setBlockState(position.up().north(), IafBlocks.GOLD_PILE.getDefaultState().with(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                    context.getWorld().setBlockState(position.up().south(), IafBlocks.GOLD_PILE.getDefaultState().with(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                    context.getWorld().setBlockState(position.up().west(), IafBlocks.GOLD_PILE.getDefaultState().with(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                    context.getWorld().setBlockState(position.up().east(), IafBlocks.GOLD_PILE.getDefaultState().with(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                    context.getWorld().setBlockState(position.up(2), Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, HORIZONTALS[new Random().nextInt(3)]), 2);

                    if (context.getWorld().getBlockState(position.up(2)).getBlock() instanceof AbstractChestBlock) {
                        BlockEntity blockEntity = context.getWorld().getBlockEntity(position.up(2));

                        if (blockEntity instanceof ChestBlockEntity chestBlockEntity) {
                            chestBlockEntity.setLootTable(CYCLOPS_CHEST, context.getRandom().nextLong());
                        }
                    }
                }

                if (context.getRandom().nextInt(50) == 0 && this.isTouchingAir(context.getWorld(), position.up())) {
                    int torchHeight = context.getRandom().nextInt(2) + 1;

                    for (int fence = 0; fence < torchHeight; fence++) {
                        context.getWorld().setBlockState(position.up(1 + fence), this.getFenceState(context.getWorld(), position.up(1 + fence)), 3);
                    }

                    context.getWorld().setBlockState(position.up(1 + torchHeight), Blocks.TORCH.getDefaultState(), 2);
                }
            }
        }

        EntityCyclops cyclops = IafEntities.CYCLOPS.create(context.getWorld().toServerWorld());
        assert cyclops != null;
        cyclops.updatePositionAndAngles(context.getOrigin().getX() + 0.5, context.getOrigin().getY() + 1.5, context.getOrigin().getZ() + 0.5, context.getRandom().nextFloat() * 360, 0);
        // TODO :: Finalize spawn?
        context.getWorld().spawnEntity(cyclops);

        return true;
    }

    private void generateSheepPen(final ServerWorldAccess level, final BlockPos position, final net.minecraft.util.math.random.Random random, final BlockPos origin, float radius) {
        int width = 5 + random.nextInt(3);
        int sheepAmount = 2 + random.nextInt(3);
        Direction direction = Direction.NORTH;
        BlockPos end = position;

        for (int sideCount = 0; sideCount < 4; sideCount++) {
            for (int side = 0; side < width; side++) {
                BlockPos relativePosition = end.offset(direction, side);

                if (origin.getSquaredDistance(relativePosition) <= radius * radius) {
                    level.setBlockState(relativePosition, this.getFenceState(level, relativePosition), Block.NOTIFY_ALL);

                    if (level.isAir(relativePosition.offset(direction.rotateYClockwise())) && sheepAmount > 0) {
                        BlockPos sheepPos = relativePosition.offset(direction.rotateYClockwise());

                        SheepEntity sheep = new SheepEntity(EntityType.SHEEP, level.toServerWorld());
                        sheep.setPosition(sheepPos.getX() + 0.5F, sheepPos.getY() + 0.5F, sheepPos.getZ() + 0.5F);
                        sheep.setColor(random.nextInt(4) == 0 ? DyeColor.YELLOW : DyeColor.WHITE);
                        level.spawnEntity(sheep);

                        sheepAmount--;
                    }
                }
            }

            end = end.offset(direction, width);
            direction = direction.rotateYClockwise();
        }

        // Go through the fence blocks again and make sure they're properly connected to each other
        for (int sideCount = 0; sideCount < 4; sideCount++) {
            for (int side = 0; side < width; side++) {
                BlockPos relativePosition = end.offset(direction, side);

                if (origin.getSquaredDistance(relativePosition) <= radius * radius) {
                    level.setBlockState(relativePosition, this.getFenceState(level, relativePosition), Block.NOTIFY_ALL);
                }
            }

            end = end.offset(direction, width);
            direction = direction.rotateYClockwise();
        }
    }

    private void generateSkeleton(final WorldAccess level, final BlockPos position, final net.minecraft.util.math.random.Random random, final BlockPos origin, float radius) {
        Direction direction = HORIZONTALS[new Random().nextInt(3)];
        Direction.Axis oppositeAxis = direction.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        int maxRibHeight = random.nextInt(2);

        for (int spine = 0; spine < 5 + random.nextInt(2) * 2; spine++) {
            BlockPos segment = position.offset(direction, spine);

            if (origin.getSquaredDistance(segment) <= radius * radius) {
                level.setBlockState(segment, Blocks.BONE_BLOCK.getDefaultState().with(PillarBlock.AXIS, direction.getAxis()), 2);
            }

            if (spine % 2 != 0) {
                BlockPos rightRib = segment.offset(direction.rotateYCounterclockwise());
                BlockPos leftRib = segment.offset(direction.rotateYClockwise());

                if (origin.getSquaredDistance(rightRib) <= radius * radius) {
                    level.setBlockState(rightRib, Blocks.BONE_BLOCK.getDefaultState().with(PillarBlock.AXIS, oppositeAxis), 2);
                }

                if (origin.getSquaredDistance(leftRib) <= radius * radius) {
                    level.setBlockState(leftRib, Blocks.BONE_BLOCK.getDefaultState().with(PillarBlock.AXIS, oppositeAxis), 2);
                }

                for (int ribHeight = 1; ribHeight < maxRibHeight + 2; ribHeight++) {
                    if (origin.getSquaredDistance(rightRib.up(ribHeight).offset(direction.rotateYCounterclockwise())) <= radius * radius) {
                        level.setBlockState(rightRib.up(ribHeight).offset(direction.rotateYCounterclockwise()), Blocks.BONE_BLOCK.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y), Block.NOTIFY_LISTENERS);
                    }

                    if (origin.getSquaredDistance(leftRib.up(ribHeight).offset(direction.rotateYClockwise())) <= radius * radius) {
                        level.setBlockState(leftRib.up(ribHeight).offset(direction.rotateYClockwise()), Blocks.BONE_BLOCK.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y), Block.NOTIFY_LISTENERS);
                    }
                }

                if (origin.getSquaredDistance(rightRib.up(maxRibHeight + 2)) <= radius * radius) {
                    level.setBlockState(rightRib.up(maxRibHeight + 2), Blocks.BONE_BLOCK.getDefaultState().with(PillarBlock.AXIS, oppositeAxis), Block.NOTIFY_LISTENERS);
                }

                if (origin.getSquaredDistance(leftRib.up(maxRibHeight + 2)) <= radius * radius) {
                    level.setBlockState(leftRib.up(maxRibHeight + 2), Blocks.BONE_BLOCK.getDefaultState().with(PillarBlock.AXIS, oppositeAxis), Block.NOTIFY_LISTENERS);
                }
            }

        }
    }

    private boolean isTouchingAir(final WorldAccess level, final BlockPos position) {
        boolean isTouchingAir = true;

        for (Direction direction : HORIZONTALS) {
            if (!level.isAir(position.offset(direction))) {
                isTouchingAir = false;
            }
        }

        return isTouchingAir;
    }

    private BlockState getFenceState(final WorldAccess level, final BlockPos position) {
        boolean east = level.getBlockState(position.east()).getBlock() == Blocks.OAK_FENCE;
        boolean west = level.getBlockState(position.west()).getBlock() == Blocks.OAK_FENCE;
        boolean north = level.getBlockState(position.north()).getBlock() == Blocks.OAK_FENCE;
        boolean south = level.getBlockState(position.south()).getBlock() == Blocks.OAK_FENCE;

        return Blocks.OAK_FENCE.getDefaultState().with(FenceBlock.EAST, east).with(FenceBlock.WEST, west).with(FenceBlock.NORTH, north).with(FenceBlock.SOUTH, south);
    }

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.SURFACE;
    }

    @Override
    public String getId() {
        return "cyclops_cave";
    }
}
