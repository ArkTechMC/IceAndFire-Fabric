package com.iafenvoy.iceandfire.world.feature;

import com.iafenvoy.iceandfire.IafConfig;
import com.iafenvoy.iceandfire.entity.EntityDeathWorm;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.registry.IafFeatures;
import com.mojang.serialization.Codec;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SpawnDeathWorm extends Feature<DefaultFeatureConfig> {

    public SpawnDeathWorm(Codec<DefaultFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess worldIn = context.getWorld();
        Random rand = context.getRandom();
        BlockPos position = context.getOrigin();
        position = worldIn.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, position.add(8, 0, 8));

        if (IafFeatures.isFarEnoughFromSpawn(worldIn, position)) {
            if (rand.nextInt(IafConfig.deathWormSpawnRate + 1) == 0) {
                EntityDeathWorm deathWorm = IafEntities.DEATH_WORM.create(worldIn.toServerWorld());
                assert deathWorm != null;
                deathWorm.setPosition(position.getX() + 0.5F, position.getY() + 1, position.getZ() + 0.5F);
                deathWorm.initialize(worldIn, worldIn.getLocalDifficulty(position), SpawnReason.CHUNK_GENERATION, null, null);
                worldIn.spawnEntity(deathWorm);
            }
        }

        return true;
    }
}
