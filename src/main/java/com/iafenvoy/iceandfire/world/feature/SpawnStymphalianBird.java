package com.iafenvoy.iceandfire.world.feature;

import com.iafenvoy.iceandfire.config.IafConfig;
import com.iafenvoy.iceandfire.entity.EntityStymphalianBird;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.registry.IafFeatures;
import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SpawnStymphalianBird extends Feature<DefaultFeatureConfig> {

    public SpawnStymphalianBird(Codec<DefaultFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess worldIn = context.getWorld();
        Random rand = context.getRandom();
        BlockPos position = context.getOrigin();

        position = worldIn.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, position.add(8, 0, 8));

        if (IafFeatures.isFarEnoughFromSpawn(worldIn, position) && rand.nextInt(IafConfig.getInstance().stymphalianBird.spawnChance + 1) == 0)
            for (int i = 0; i < 4 + rand.nextInt(4); i++) {
                BlockPos pos = position.add(rand.nextInt(10) - 5, 0, rand.nextInt(10) - 5);
                pos = worldIn.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, pos);
                if (worldIn.getBlockState(pos.down()).isOpaque()) {
                    EntityStymphalianBird bird = IafEntities.STYMPHALIAN_BIRD.create(worldIn.toServerWorld());
                    assert bird != null;
                    bird.refreshPositionAndAngles(pos.getX() + 0.5F, pos.getY() + 1.5F, pos.getZ() + 0.5F, 0, 0);
                    worldIn.spawnEntity(bird);
                }
            }
        return true;
    }
}
