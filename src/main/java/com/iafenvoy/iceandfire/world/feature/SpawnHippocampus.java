package com.iafenvoy.iceandfire.world.feature;

import com.iafenvoy.iceandfire.config.IafConfig;
import com.iafenvoy.iceandfire.entity.EntityHippocampus;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.mojang.serialization.Codec;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SpawnHippocampus extends Feature<DefaultFeatureConfig> {

    public SpawnHippocampus(Codec<DefaultFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess worldIn = context.getWorld();
        Random rand = context.getRandom();
        BlockPos position = context.getOrigin();

        position = worldIn.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, position.add(8, 0, 8));
        BlockPos oceanPos = worldIn.getTopPosition(Heightmap.Type.OCEAN_FLOOR_WG, position.add(8, 0, 8));

        if (rand.nextInt(IafConfig.getInstance().hippocampusSpawnChance + 1) == 0) {
            for (int i = 0; i < rand.nextInt(5); i++) {
                BlockPos pos = oceanPos.add(rand.nextInt(10) - 5, rand.nextInt(30), rand.nextInt(10) - 5);
                if (worldIn.getFluidState(pos).getFluid() == Fluids.WATER) {
                    EntityHippocampus campus = IafEntities.HIPPOCAMPUS.create(worldIn.toServerWorld());
                    assert campus != null;
                    campus.setVariant(rand.nextInt(6));
                    campus.refreshPositionAndAngles(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0, 0);
                    worldIn.spawnEntity(campus);
                }
            }
        }

        return true;
    }
}
