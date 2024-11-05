package com.iafenvoy.iceandfire.world.structure;

import com.iafenvoy.iceandfire.data.DragonColor;
import com.iafenvoy.iceandfire.data.DragonType;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.entity.util.HomePosition;
import com.iafenvoy.iceandfire.item.block.BlockGoldPile;
import com.iafenvoy.iceandfire.world.gen.WorldGenRoostArch;
import com.iafenvoy.iceandfire.world.gen.WorldGenRoostBoulder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.structure.Structure;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class DragonRoostStructure extends Structure {
    protected DragonRoostStructure(Config config) {
        super(config);
    }

    @Override
    protected Optional<StructurePosition> getStructurePosition(Context context) {
        BlockRotation blockRotation = BlockRotation.random(context.random());
        BlockPos blockPos = this.getShiftedPos(context, blockRotation);
        return Optional.of(new StructurePosition(blockPos, collector -> collector.addPiece(this.createPiece(0, new BlockBox(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX(), blockPos.getY(), blockPos.getZ()), context.random().nextBoolean()))));
    }

    protected abstract DragonRoostPiece createPiece(int length, BlockBox boundingBox, boolean isMale);

    protected static abstract class DragonRoostPiece extends StructurePiece {
        protected static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        protected final Block treasureBlock;
        private final boolean isMale;

        protected DragonRoostPiece(StructurePieceType type, int length, BlockBox boundingBox, Block treasureBlock, boolean isMale) {
            super(type, length, boundingBox);
            this.treasureBlock = treasureBlock;
            this.isMale = isMale;
        }

        public DragonRoostPiece(StructurePieceType type, NbtCompound nbt) {
            super(type, nbt);
            this.treasureBlock = Registries.BLOCK.get(new Identifier(nbt.getString("treasureBlock")));
            this.isMale = nbt.getBoolean("isMale");
        }

        @Override
        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            nbt.putString("treasureBlock", Registries.BLOCK.getId(this.treasureBlock).toString());
            nbt.putBoolean("isMale", this.isMale);
        }

        @Override
        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            int radius = 12 + random.nextInt(8);
            this.spawnDragon(world, pivot, random, radius, this.isMale);
            this.generateSurface(world, pivot, random, radius);
            this.generateShell(world, pivot, random, radius);
            radius -= 2;
            this.hollowOut(world, pivot, radius);
            radius += 15;
            this.generateDecoration(world, pivot, random, radius, this.isMale);
        }


        protected void generateRoostPile(StructureWorldAccess level, Random random, BlockPos position, Block block) {
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

        protected BlockPos getSurfacePosition(StructureWorldAccess level, BlockPos position) {
            return level.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, position);
        }

        protected BlockState transform(Block block) {
            return this.transform(block.getDefaultState());
        }

        private void generateDecoration(StructureWorldAccess world, BlockPos origin, Random random, int radius, boolean isMale) {
            int height = (radius / 5);
            double circularArea = this.getCircularArea(radius, height);

            BlockPos.stream(origin.add(-radius, -height, -radius), origin.add(radius, height, radius)).map(BlockPos::toImmutable).forEach(position -> {
                if (position.getSquaredDistance(origin) <= circularArea) {
                    double distance = position.getSquaredDistance(origin) / circularArea;

                    if (!world.isAir(origin) && random.nextDouble() > distance * 0.5) {
                        BlockState state = world.getBlockState(position);

                        if (!(state.getBlock() instanceof BlockWithEntity) && state.getHardness(world, position) >= 0) {
                            BlockState transformed = this.transform(state);

                            if (transformed != state) {
                                world.setBlockState(position, transformed, Block.NOTIFY_LISTENERS);
                            }
                        }
                    }

                    this.handleCustomGeneration(world, origin, random, position, distance);

                    if (distance > 0.5 && random.nextInt(1000) == 0) {
                        // FIXME
                        new WorldGenRoostBoulder(this.transform(Blocks.COBBLESTONE).getBlock(), random.nextInt(3), true).generate(world, random, this.getSurfacePosition(world, position));
                    }

                    if (distance < 0.3 && random.nextInt(isMale ? 200 : 300) == 0) {
                        this.generateTreasurePile(world, random, position);
                    }

                    if (distance < 0.3D && random.nextInt(isMale ? 500 : 700) == 0) {
                        // TODO :: Using non-world-generation since that one does not seem to keep track of blcks we remove / place ourselves (maybe due to Block.UPDATE_CLIENTS usage?)
                        BlockPos surfacePosition = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, position);
                        boolean wasPlaced = world.setBlockState(surfacePosition, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, HORIZONTALS[random.nextInt(3)]), Block.NOTIFY_LISTENERS);

                        if (wasPlaced) {
                            BlockEntity blockEntity = world.getBlockEntity(surfacePosition);

                            if (blockEntity instanceof ChestBlockEntity chest) {
                                chest.setLootTable(this.getRoostLootTable(), random.nextLong());
                            }
                        }
                    }

                    if (random.nextInt(5000) == 0) {
                        // FIXME
                        new WorldGenRoostArch(this.transform(Blocks.COBBLESTONE).getBlock()).generate(world, random, this.getSurfacePosition(world, position));
                    }
                }
            });
        }

        private void hollowOut(StructureWorldAccess world, BlockPos origin, int radius) {
            int height = 2;
            double circularArea = this.getCircularArea(radius, height);
            BlockPos up = origin.up(height - 1);

            BlockPos.stream(up.add(-radius, 0, -radius), up.add(radius, height, radius)).map(BlockPos::toImmutable).forEach(position -> {
                if (position.getSquaredDistance(origin) <= circularArea)
                    world.setBlockState(position, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
            });
        }

        private void generateShell(StructureWorldAccess world, BlockPos origin, Random random, int radius) {
            int height = (radius / 5);
            double circularArea = this.getCircularArea(radius, height);

            BlockPos.stream(origin.add(-radius, -height, -radius), origin.add(radius, 1, radius)).map(BlockPos::toImmutable).forEach(position -> {
                if (position.getSquaredDistance(origin) < circularArea)
                    world.setBlockState(position, random.nextBoolean() ? this.transform(Blocks.GRAVEL) : this.transform(Blocks.DIRT), Block.NOTIFY_LISTENERS);
                else if (position.getSquaredDistance(origin) == circularArea)
                    world.setBlockState(position, this.transform(Blocks.COBBLESTONE), Block.NOTIFY_LISTENERS);
            });
        }

        private void generateSurface(StructureWorldAccess world, BlockPos origin, Random random, int radius) {
            int height = 2;
            double circularArea = this.getCircularArea(radius, height);

            BlockPos.stream(origin.add(-radius, height, -radius), origin.add(radius, 0, radius)).map(BlockPos::toImmutable).forEach(position -> {
                int heightDifference = position.getY() - origin.getY();

                if (position.getSquaredDistance(origin) <= circularArea && heightDifference < 2 + random.nextInt(height) && !world.isAir(position.down())) {
                    if (world.isAir(position.up()))
                        world.setBlockState(position, this.transform(Blocks.GRASS), Block.NOTIFY_LISTENERS);
                    else
                        // TODO :: Usually not much / anything of this survives the next generation steps
                        world.setBlockState(position, this.transform(Blocks.DIRT), Block.NOTIFY_LISTENERS);
                }
            });
        }

        private void generateTreasurePile(StructureWorldAccess world, Random random, BlockPos origin) {
            int layers = random.nextInt(3);

            for (int i = 0; i < layers; i++) {
                int radius = layers - i;
                double circularArea = this.getCircularArea(radius);

                for (BlockPos position : BlockPos.stream(origin.add(-radius, i, -radius), origin.add(radius, i, radius)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
                    if (position.getSquaredDistance(origin) <= circularArea) {
                        position = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, position);

                        if (this.treasureBlock instanceof BlockGoldPile) {
                            BlockState state = world.getBlockState(position);
                            boolean placed = false;
                            if (state.isAir()) {
                                world.setBlockState(position, this.treasureBlock.getDefaultState().with(BlockGoldPile.LAYERS, 1 + random.nextInt(7)), Block.NOTIFY_LISTENERS);
                                placed = true;
                            } else if (state.getBlock() instanceof SnowBlock) {
                                world.setBlockState(position.down(), this.treasureBlock.getDefaultState().with(BlockGoldPile.LAYERS, state.get(SnowBlock.LAYERS)), Block.NOTIFY_LISTENERS);
                                placed = true;
                            }
                            if (placed && world.getBlockState(position.down()).getBlock() instanceof BlockGoldPile)
                                world.setBlockState(position.down(), this.treasureBlock.getDefaultState().with(BlockGoldPile.LAYERS, 8), Block.NOTIFY_LISTENERS);
                        }
                    }
                }
            }
        }

        private void spawnDragon(StructureWorldAccess world, BlockPos origin, Random random, int ageOffset, boolean isMale) {
            EntityDragonBase dragon = this.getDragonType().create(world.toServerWorld());
            assert dragon != null;
            dragon.setGender(isMale);
            dragon.growDragon(40 + ageOffset);
            dragon.setAgingDisabled(true);
            dragon.setHealth(dragon.getMaxHealth());
            List<DragonColor> colors = DragonColor.getColorsByType(DragonType.getTypeByEntityType(this.getDragonType()));
            dragon.setVariant(colors.get(random.nextInt(colors.size())).name());
            dragon.updatePositionAndAngles(origin.getX() + 0.5, world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, origin).getY() + 1.5, origin.getZ() + 0.5, random.nextFloat() * 360, 0);
            dragon.homePos = new HomePosition(origin, world.toServerWorld());
            dragon.hasHomePosition = true;
            dragon.setHunger(50);
            world.spawnEntity(dragon);
        }

        protected abstract EntityType<? extends EntityDragonBase> getDragonType();

        protected abstract Identifier getRoostLootTable();

        protected abstract BlockState transform(BlockState block);

        protected abstract void handleCustomGeneration(StructureWorldAccess world, BlockPos origin, Random random, BlockPos position, double distance);
    }
}