package com.iafenvoy.iceandfire.world.feature;

import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.entity.EntitySeaSerpent;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.world.GenerationConstant;
import com.mojang.serialization.Codec;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SpawnSeaSerpent extends Feature<DefaultFeatureConfig> {
    public SpawnSeaSerpent(Codec<DefaultFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess worldIn = context.getWorld();
        Random rand = context.getRandom();
        BlockPos position = context.getOrigin();

        position = worldIn.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, position.add(8, 0, 8));
        BlockPos oceanPos = worldIn.getTopPosition(Heightmap.Type.OCEAN_FLOOR_WG, position.add(8, 0, 8));

        if (GenerationConstant.isFarEnoughFromSpawn(position)) {
            if (rand.nextDouble() < IafCommonConfig.INSTANCE.seaSerpent.spawnChance.getValue()) {
                BlockPos pos = oceanPos.add(rand.nextInt(10) - 5, rand.nextInt(30), rand.nextInt(10) - 5);
                if (worldIn.getFluidState(pos).getFluid() == Fluids.WATER) {
                    EntitySeaSerpent serpent = IafEntities.SEA_SERPENT.create(worldIn.toServerWorld());
                    assert serpent != null;
                    serpent.onWorldSpawn(rand);
                    serpent.refreshPositionAndAngles(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0, 0);
                    worldIn.spawnEntity(serpent);
                }
            }
        }

        return true;
    }
}
