package com.iafenvoy.iceandfire.world.gen;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.entity.EntityHydra;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.registry.IafFeatures;
import com.iafenvoy.iceandfire.world.IafWorldData;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;
import java.util.stream.Collectors;

public class WorldGenHydraCave extends Feature<DefaultFeatureConfig> implements TypedFeature {

    public static final Identifier HYDRA_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/hydra_cave");
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    public WorldGenHydraCave(Codec<DefaultFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess worldIn = context.getWorld();
        net.minecraft.util.math.random.Random rand = context.getRandom();
        BlockPos position = context.getOrigin();
        ChunkGenerator generator = context.getGenerator();

        if (rand.nextDouble() < IafCommonConfig.INSTANCE.hydra.spawnChance.getDoubleValue() || !IafFeatures.isFarEnoughFromSpawn(worldIn, position) || !IafFeatures.isFarEnoughFromDangerousGen(worldIn, position, this.getId())) {
            return false;
        }

        int i1 = 8;
        int i2 = i1 - 2;
        int dist = 6;
        if (worldIn.isAir(position.add(i1 - dist, -3, -i1 + dist)) || worldIn.isAir(position.add(i1 - dist, -3, i1 - dist)) || worldIn.isAir(position.add(-i1 + dist, -3, -i1 + dist)) || worldIn.isAir(position.add(-i1 + dist, -3, i1 - dist))) {
            return false;
        }

        {
            int ySize = rand.nextInt(2);
            int j = i1 + rand.nextInt(2);
            int k = 5 + ySize;
            int l = i1 + rand.nextInt(2);
            float f = (j + k + l) * 0.333F + 0.5F;


            for (BlockPos blockpos : BlockPos.stream(position.add(-j, -k, -l), position.add(j, k, l)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
                boolean doorwayX = blockpos.getX() >= position.getX() - 2 + rand.nextInt(2) && blockpos.getX() <= position.getX() + 2 + rand.nextInt(2);
                boolean doorwayZ = blockpos.getZ() >= position.getZ() - 2 + rand.nextInt(2) && blockpos.getZ() <= position.getZ() + 2 + rand.nextInt(2);
                boolean isNotInDoorway = !doorwayX && !doorwayZ && blockpos.getY() > position.getY() || blockpos.getY() > position.getY() + k - (1 + rand.nextInt(2));
                if (blockpos.getSquaredDistance(position) <= f * f) {
                    if (!(worldIn.getBlockState(position).getBlock() instanceof ChestBlock) && isNotInDoorway) {
                        worldIn.setBlockState(blockpos, Blocks.GRASS_BLOCK.getDefaultState(), 3);
                        if (worldIn.getBlockState(position.down()).getBlock() == Blocks.GRASS_BLOCK) {
                            worldIn.setBlockState(blockpos.down(), Blocks.DIRT.getDefaultState(), 3);
                        }
                        if (rand.nextInt(4) == 0) {
                            worldIn.setBlockState(blockpos.up(), Blocks.GRASS.getDefaultState(), 2);
                        }
                        if (rand.nextInt(9) == 0) {
                            context.getWorld().getRegistryManager().get(RegistryKeys.CONFIGURED_FEATURE).getEntry(TreeConfiguredFeatures.SWAMP_OAK).ifPresent(holder -> holder.value().generate(worldIn, generator, rand, blockpos.up()));
                        }

                    }
                    if (blockpos.getY() == position.getY()) {
                        worldIn.setBlockState(blockpos, Blocks.GRASS_BLOCK.getDefaultState(), 3);
                    }
                    if (blockpos.getY() <= position.getY() - 1 && !worldIn.getBlockState(blockpos).isOpaque()) {
                        worldIn.setBlockState(blockpos, Blocks.STONE.getDefaultState(), 3);

                    }
                }
            }


        }
        {
            int ySize = rand.nextInt(2);
            int j = i2 + rand.nextInt(2);
            int k = 4 + ySize;
            int l = i2 + rand.nextInt(2);
            float f = (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.stream(position.add(-j, -k, -l), position.add(j, k, l)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
                if (blockpos.getSquaredDistance(position) <= f * f && blockpos.getY() > position.getY()) {
                    if (!(worldIn.getBlockState(position).getBlock() instanceof ChestBlock)) {
                        worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 3);

                    }
                }
            }
            for (BlockPos blockpos : BlockPos.stream(position.add(-j, -k, -l), position.add(j, k + 8, l)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
                if (blockpos.getSquaredDistance(position) <= f * f && blockpos.getY() == position.getY()) {
                    if (rand.nextInt(30) == 0 && this.isTouchingAir(worldIn, blockpos.up())) {
                        worldIn.setBlockState(blockpos.up(1), Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, HORIZONTALS[new Random().nextInt(3)]), 2);
                        if (worldIn.getBlockState(blockpos.up(1)).getBlock() instanceof ChestBlock) {
                            BlockEntity tileentity1 = worldIn.getBlockEntity(blockpos.up(1));
                            if (tileentity1 instanceof ChestBlockEntity) {
                                ((ChestBlockEntity) tileentity1).setLootTable(HYDRA_CHEST, rand.nextLong());
                            }
                        }
                        continue;
                    }
                    if (rand.nextInt(45) == 0 && this.isTouchingAir(worldIn, blockpos.up())) {
                        worldIn.setBlockState(blockpos.up(), Blocks.SKELETON_SKULL.getDefaultState().with(SkullBlock.ROTATION, rand.nextInt(15)), 2);
                        continue;
                    }
                    if (rand.nextInt(35) == 0 && this.isTouchingAir(worldIn, blockpos.up())) {
                        worldIn.setBlockState(blockpos.up(), Blocks.OAK_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 2);
                        for (Direction facing : Direction.values()) {
                            if (rand.nextFloat() < 0.3F && facing != Direction.DOWN) {
                                worldIn.setBlockState(blockpos.up().offset(facing), Blocks.OAK_LEAVES.getDefaultState(), 2);
                            }
                        }
                        continue;
                    }
                    if (rand.nextInt(15) == 0 && this.isTouchingAir(worldIn, blockpos.up())) {
                        worldIn.setBlockState(blockpos.up(), Blocks.TALL_GRASS.getDefaultState(), 2);
                        continue;
                    }
                    if (rand.nextInt(15) == 0 && this.isTouchingAir(worldIn, blockpos.up())) {
                        worldIn.setBlockState(blockpos.up(), rand.nextBoolean() ? Blocks.BROWN_MUSHROOM.getDefaultState() : Blocks.RED_MUSHROOM.getDefaultState(), 2);
                    }
                }
            }
        }
        EntityHydra hydra = new EntityHydra(IafEntities.HYDRA, worldIn.toServerWorld());
        hydra.setVariant(rand.nextInt(3));
        hydra.setPositionTarget(position, 15);
        hydra.updatePositionAndAngles(position.getX() + 0.5, position.getY() + 1.5, position.getZ() + 0.5, rand.nextFloat() * 360, 0);
        worldIn.spawnEntity(hydra);
        return true;
    }

    private boolean isTouchingAir(WorldAccess worldIn, BlockPos pos) {
        boolean isTouchingAir = true;
        for (Direction direction : HORIZONTALS) {
            if (!worldIn.isAir(pos.offset(direction))) {
                isTouchingAir = false;
            }
        }
        return isTouchingAir;
    }

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.SURFACE;
    }

    @Override
    public String getId() {
        return "hydra_cave";
    }
}
