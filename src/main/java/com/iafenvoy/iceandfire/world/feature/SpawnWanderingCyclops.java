package com.iafenvoy.iceandfire.world.feature;

import com.iafenvoy.iceandfire.config.IafConfig;
import com.iafenvoy.iceandfire.entity.EntityCyclops;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.registry.IafFeatures;
import com.mojang.serialization.Codec;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SpawnWanderingCyclops extends Feature<DefaultFeatureConfig> {

    public SpawnWanderingCyclops(Codec<DefaultFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess worldIn = context.getWorld();
        Random rand = context.getRandom();
        BlockPos position = context.getOrigin();

        position = worldIn.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, position.add(8, 0, 8));

        if (IafFeatures.isFarEnoughFromSpawn(worldIn, position)) {
            if (rand.nextInt(IafConfig.getInstance().spawnWanderingCyclopsChance + 1) == 0 && rand.nextInt(12) == 0) {
                EntityCyclops cyclops = IafEntities.CYCLOPS.create(worldIn.toServerWorld());
                assert cyclops != null;
                cyclops.setPosition(position.getX() + 0.5F, position.getY() + 1, position.getZ() + 0.5F);
                cyclops.initialize(worldIn, worldIn.getLocalDifficulty(position), SpawnReason.SPAWNER, null, null);
                worldIn.spawnEntity(cyclops);
                for (int i = 0; i < 3 + rand.nextInt(3); i++) {
                    SheepEntity sheep = EntityType.SHEEP.create(worldIn.toServerWorld());
                    assert sheep != null;
                    sheep.setPosition(position.getX() + 0.5F, position.getY() + 1, position.getZ() + 0.5F);
                    sheep.setColor(SheepEntity.generateDefaultColor(rand));
                    worldIn.spawnEntity(sheep);
                }
            }
        }

        return true;
    }
}
