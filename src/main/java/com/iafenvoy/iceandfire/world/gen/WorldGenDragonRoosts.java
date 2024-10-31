package com.iafenvoy.iceandfire.world.gen;

import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.data.DragonColor;
import com.iafenvoy.iceandfire.data.DragonType;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.entity.util.HomePosition;
import com.iafenvoy.iceandfire.item.block.BlockGoldPile;
import com.iafenvoy.iceandfire.util.WorldUtil;
import com.iafenvoy.iceandfire.world.IafWorldData;
import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.List;
import java.util.stream.Collectors;

public abstract class WorldGenDragonRoosts extends Feature<DefaultFeatureConfig> implements TypedFeature {
    protected static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    protected final Block treasureBlock;

    public WorldGenDragonRoosts(final Codec<DefaultFeatureConfig> configuration, final Block treasureBlock) {
        super(configuration);
        this.treasureBlock = treasureBlock;
    }

    @Override
    public String getId() {
        return "dragon_roost";
    }

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.SURFACE;
    }

    @Override
    public boolean generate(final FeatureContext<DefaultFeatureConfig> context) {
        Random random = context.getRandom();
        if (!WorldUtil.canGenerate(IafCommonConfig.INSTANCE.dragon.generateRoostChance.getValue(), context.getWorld(), random, context.getOrigin(), this.getId(), true)) {
            return false;
        }

        boolean isMale = random.nextBoolean();
        int radius = 12 + context.getRandom().nextInt(8);

        this.spawnDragon(context, random, radius, isMale);
        this.generateSurface(context, radius);
        this.generateShell(context, radius);
        radius -= 2;
        this.hollowOut(context, radius);
        radius += 15;
        this.generateDecoration(context, random, radius, isMale);

        return true;
    }

    protected void generateRoostPile(final StructureWorldAccess level, final net.minecraft.util.math.random.Random random, final BlockPos position, final Block block) {
        int radius = random.nextInt(4);

        for (int i = 0; i < radius; i++) {
            int layeredRadius = radius - i;
            double circularArea = this.getCircularArea(radius);
            BlockPos up = position.up(i);

            for (BlockPos blockpos : BlockPos.stream(up.add(-layeredRadius, 0, -layeredRadius), up.add(layeredRadius, 0, layeredRadius)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
                if (blockpos.getSquaredDistance(position) <= circularArea) {
                    level.setBlockState(blockpos, block.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
            }
        }
    }

    protected double getCircularArea(int radius, int height) {
        double area = (radius + height + radius) * 0.333F + 0.5F;
        return MathHelper.floor(area * area);
    }

    protected double getCircularArea(int radius) {
        double area = (radius + radius) * 0.333F + 0.5F;
        return MathHelper.floor(area * area);
    }

    protected BlockPos getSurfacePosition(final StructureWorldAccess level, final BlockPos position) {
        return level.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, position);
    }

    protected BlockState transform(final Block block) {
        return this.transform(block.getDefaultState());
    }

    private void generateDecoration(final FeatureContext<DefaultFeatureConfig> context, final Random random, int radius, boolean isMale) {
        int height = (radius / 5);
        double circularArea = this.getCircularArea(radius, height);

        BlockPos.stream(context.getOrigin().add(-radius, -height, -radius), context.getOrigin().add(radius, height, radius)).map(BlockPos::toImmutable).forEach(position -> {
            if (position.getSquaredDistance(context.getOrigin()) <= circularArea) {
                double distance = position.getSquaredDistance(context.getOrigin()) / circularArea;

                if (!context.getWorld().isAir(context.getOrigin()) && context.getRandom().nextDouble() > distance * 0.5) {
                    BlockState state = context.getWorld().getBlockState(position);

                    if (!(state.getBlock() instanceof BlockWithEntity) && state.getHardness(context.getWorld(), position) >= 0) {
                        BlockState transformed = this.transform(state);

                        if (transformed != state) {
                            context.getWorld().setBlockState(position, transformed, Block.NOTIFY_LISTENERS);
                        }
                    }
                }

                this.handleCustomGeneration(context, position, distance);

                if (distance > 0.5 && context.getRandom().nextInt(1000) == 0) {
                    // FIXME
                    new WorldGenRoostBoulder(this.transform(Blocks.COBBLESTONE).getBlock(), context.getRandom().nextInt(3), true).generate(context.getWorld(), context.getRandom(), this.getSurfacePosition(context.getWorld(), position));
                }

                if (distance < 0.3 && context.getRandom().nextInt(isMale ? 200 : 300) == 0) {
                    this.generateTreasurePile(context.getWorld(), context.getRandom(), position);
                }

                if (distance < 0.3D && context.getRandom().nextInt(isMale ? 500 : 700) == 0) {
                    // TODO :: Using non-world-generation since that one does not seem to keep track of blcks we remove / place ourselves (maybe due to Block.UPDATE_CLIENTS usage?)
                    BlockPos surfacePosition = context.getWorld().getTopPosition(Heightmap.Type.WORLD_SURFACE, position);
                    boolean wasPlaced = context.getWorld().setBlockState(surfacePosition, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, HORIZONTALS[random.nextInt(3)]), Block.NOTIFY_LISTENERS);

                    if (wasPlaced) {
                        BlockEntity blockEntity = context.getWorld().getBlockEntity(surfacePosition);

                        if (blockEntity instanceof ChestBlockEntity chest) {
                            chest.setLootTable(this.getRoostLootTable(), context.getRandom().nextLong());
                        }
                    }
                }

                if (context.getRandom().nextInt(5000) == 0) {
                    // FIXME
                    new WorldGenRoostArch(this.transform(Blocks.COBBLESTONE).getBlock()).generate(context.getWorld(), context.getRandom(), this.getSurfacePosition(context.getWorld(), position));
                }
            }
        });
    }

    private void hollowOut(final FeatureContext<DefaultFeatureConfig> context, int radius) {
        int height = 2;
        double circularArea = this.getCircularArea(radius, height);
        BlockPos up = context.getOrigin().up(height - 1);

        BlockPos.stream(up.add(-radius, 0, -radius), up.add(radius, height, radius)).map(BlockPos::toImmutable).forEach(position -> {
            if (position.getSquaredDistance(context.getOrigin()) <= circularArea) {
                context.getWorld().setBlockState(position, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
        });
    }

    private void generateShell(final FeatureContext<DefaultFeatureConfig> context, int radius) {
        int height = (radius / 5);
        double circularArea = this.getCircularArea(radius, height);

        BlockPos.stream(context.getOrigin().add(-radius, -height, -radius), context.getOrigin().add(radius, 1, radius)).map(BlockPos::toImmutable).forEach(position -> {
            if (position.getSquaredDistance(context.getOrigin()) < circularArea) {
                context.getWorld().setBlockState(position, context.getRandom().nextBoolean() ? this.transform(Blocks.GRAVEL) : this.transform(Blocks.DIRT), Block.NOTIFY_LISTENERS);
            } else if (position.getSquaredDistance(context.getOrigin()) == circularArea) {
                context.getWorld().setBlockState(position, this.transform(Blocks.COBBLESTONE), Block.NOTIFY_LISTENERS);
            }
        });
    }

    private void generateSurface(final FeatureContext<DefaultFeatureConfig> context, int radius) {
        int height = 2;
        double circularArea = this.getCircularArea(radius, height);

        BlockPos.stream(context.getOrigin().add(-radius, height, -radius), context.getOrigin().add(radius, 0, radius)).map(BlockPos::toImmutable).forEach(position -> {
            int heightDifference = position.getY() - context.getOrigin().getY();

            if (position.getSquaredDistance(context.getOrigin()) <= circularArea && heightDifference < 2 + context.getRandom().nextInt(height) && !context.getWorld().isAir(position.down())) {
                if (context.getWorld().isAir(position.up())) {
                    context.getWorld().setBlockState(position, this.transform(Blocks.GRASS), Block.NOTIFY_LISTENERS);
                } else {
                    // TODO :: Usually not much / anything of this survives the next generation steps
                    context.getWorld().setBlockState(position, this.transform(Blocks.DIRT), Block.NOTIFY_LISTENERS);
                }
            }
        });
    }

    private void generateTreasurePile(final StructureWorldAccess level, final Random random, final BlockPos origin) {
        int layers = random.nextInt(3);

        for (int i = 0; i < layers; i++) {
            int radius = layers - i;
            double circularArea = this.getCircularArea(radius);

            for (BlockPos position : BlockPos.stream(origin.add(-radius, i, -radius), origin.add(radius, i, radius)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
                if (position.getSquaredDistance(origin) <= circularArea) {
                    position = level.getTopPosition(Heightmap.Type.WORLD_SURFACE, position);

                    if (this.treasureBlock instanceof BlockGoldPile) {
                        BlockState state = level.getBlockState(position);
                        boolean placed = false;

                        if (state.isAir()) {
                            level.setBlockState(position, this.treasureBlock.getDefaultState().with(BlockGoldPile.LAYERS, 1 + random.nextInt(7)), Block.NOTIFY_LISTENERS);
                            placed = true;
                        } else if (state.getBlock() instanceof SnowBlock) {
                            level.setBlockState(position.down(), this.treasureBlock.getDefaultState().with(BlockGoldPile.LAYERS, state.get(SnowBlock.LAYERS)), Block.NOTIFY_LISTENERS);
                            placed = true;
                        }

                        if (placed && level.getBlockState(position.down()).getBlock() instanceof BlockGoldPile) {
                            level.setBlockState(position.down(), this.treasureBlock.getDefaultState().with(BlockGoldPile.LAYERS, 8), Block.NOTIFY_LISTENERS);
                        }
                    }
                }
            }
        }
    }

    private void spawnDragon(final FeatureContext<DefaultFeatureConfig> context, final Random random, int ageOffset, boolean isMale) {
        EntityDragonBase dragon = this.getDragonType().create(context.getWorld().toServerWorld());
        assert dragon != null;
        dragon.setGender(isMale);
        dragon.growDragon(40 + ageOffset);
        dragon.setAgingDisabled(true);
        dragon.setHealth(dragon.getMaxHealth());
        List<DragonColor> colors = DragonColor.getColorsByType(DragonType.getTypeByEntityType(this.getDragonType()));
        dragon.setVariant(colors.get(random.nextInt(colors.size())).name());
        dragon.updatePositionAndAngles(context.getOrigin().getX() + 0.5, context.getWorld().getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, context.getOrigin()).getY() + 1.5, context.getOrigin().getZ() + 0.5, context.getRandom().nextFloat() * 360, 0);
        dragon.homePos = new HomePosition(context.getOrigin(), context.getWorld().toServerWorld());
        dragon.hasHomePosition = true;
        dragon.setHunger(50);
        context.getWorld().spawnEntity(dragon);
    }

    protected abstract EntityType<? extends EntityDragonBase> getDragonType();

    protected abstract Identifier getRoostLootTable();

    protected abstract BlockState transform(final BlockState block);

    protected abstract void handleCustomGeneration(final FeatureContext<DefaultFeatureConfig> context, final BlockPos position, double distance);
}
