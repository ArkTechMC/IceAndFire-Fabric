package com.iafenvoy.iceandfire.world.gen;

import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.entity.EntityPixie;
import com.iafenvoy.iceandfire.item.block.BlockPixieHouse;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.registry.IafFeatures;
import com.iafenvoy.iceandfire.world.IafWorldData;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class WorldGenPixieVillage extends Feature<DefaultFeatureConfig> implements TypedFeature {
    public WorldGenPixieVillage(Codec<DefaultFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess worldIn = context.getWorld();
        Random rand = context.getRandom();
        BlockPos position = context.getOrigin();

        if (rand.nextDouble() >= IafCommonConfig.INSTANCE.pixie.spawnChance.getDoubleValue() || !IafFeatures.isFarEnoughFromSpawn(worldIn, position)) {
            return false;
        }

        int maxRoads = IafCommonConfig.INSTANCE.pixie.size.getIntegerValue() + rand.nextInt(5);
        BlockPos buildPosition = position;
        int placedRoads = 0;
        while (placedRoads < maxRoads) {
            int roadLength = 10 + rand.nextInt(15);
            Direction buildingDirection = Direction.fromHorizontal(rand.nextInt(3));
            for (int i = 0; i < roadLength; i++) {
                BlockPos buildPosition2 = buildPosition.offset(buildingDirection, i);
                buildPosition2 = worldIn.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, buildPosition2).down();
                if (worldIn.getBlockState(buildPosition2).getFluidState().isEmpty()) {
                    worldIn.setBlockState(buildPosition2, Blocks.DIRT_PATH.getDefaultState(), 2);
                } else {
                    worldIn.setBlockState(buildPosition2, Blocks.SPRUCE_PLANKS.getDefaultState(), 2);
                }
                if (rand.nextInt(8) == 0) {
                    Direction houseDir = rand.nextBoolean() ? buildingDirection.rotateYClockwise() : buildingDirection.rotateYCounterclockwise();
                    int houseColor = rand.nextInt(5);
                    BlockState  houseState = switch (houseColor) {
                        case 0 ->
                                IafBlocks.PIXIE_HOUSE_MUSHROOM_RED.getDefaultState().with(BlockPixieHouse.FACING, houseDir.getOpposite());
                        case 1 ->
                                IafBlocks.PIXIE_HOUSE_MUSHROOM_BROWN.getDefaultState().with(BlockPixieHouse.FACING, houseDir.getOpposite());
                        case 2 ->
                                IafBlocks.PIXIE_HOUSE_OAK.getDefaultState().with(BlockPixieHouse.FACING, houseDir.getOpposite());
                        case 3 ->
                                IafBlocks.PIXIE_HOUSE_BIRCH.getDefaultState().with(BlockPixieHouse.FACING, houseDir.getOpposite());
                        case 4 ->
                                IafBlocks.PIXIE_HOUSE_SPRUCE.getDefaultState().with(BlockPixieHouse.FACING, houseDir.getOpposite());
                        case 5 ->
                                IafBlocks.PIXIE_HOUSE_DARK_OAK.getDefaultState().with(BlockPixieHouse.FACING, houseDir.getOpposite());
                        default -> IafBlocks.PIXIE_HOUSE_OAK.getDefaultState();
                    };
                    EntityPixie pixie = IafEntities.PIXIE.create(worldIn.toServerWorld());
                    assert pixie != null;
                    pixie.initialize(worldIn, worldIn.getLocalDifficulty(buildPosition2.up()), SpawnReason.SPAWNER, null, null);
                    pixie.setPosition(buildPosition2.getX(), buildPosition2.getY() + 2, buildPosition2.getZ());
                    pixie.setPersistent();
                    worldIn.spawnEntity(pixie);

                    worldIn.setBlockState(buildPosition2.offset(houseDir).up(), houseState, 2);
                    if (!worldIn.getBlockState(buildPosition2.offset(houseDir)).isOpaque()) {
                        worldIn.setBlockState(buildPosition2.offset(houseDir), Blocks.COARSE_DIRT.getDefaultState(), 2);
                        worldIn.setBlockState(buildPosition2.offset(houseDir).down(), Blocks.COARSE_DIRT.getDefaultState(), 2);
                    }
                }
            }
            buildPosition = buildPosition.offset(buildingDirection, rand.nextInt(roadLength));
            placedRoads++;
        }

        return true;
    }

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.SURFACE;
    }

    @Override
    public String getId() {
        return "pixie_village";
    }
}
