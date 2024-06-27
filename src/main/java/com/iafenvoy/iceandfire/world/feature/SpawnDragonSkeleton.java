package com.iafenvoy.iceandfire.world.feature;

import com.iafenvoy.iceandfire.config.IafConfig;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.mojang.serialization.Codec;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SpawnDragonSkeleton extends Feature<DefaultFeatureConfig> {

    protected final EntityType<? extends EntityDragonBase> dragonType;

    public SpawnDragonSkeleton(EntityType<? extends EntityDragonBase> dt, Codec<DefaultFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
        this.dragonType = dt;
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess worldIn = context.getWorld();
        Random rand = context.getRandom();
        BlockPos position = context.getOrigin();

        position = worldIn.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, position.add(8, 0, 8));

        if (IafConfig.getInstance().dragon.generate.skeletons) {
            if (rand.nextInt(IafConfig.getInstance().dragon.generate.skeletonChance + 1) == 0) {
                EntityDragonBase dragon = this.dragonType.create(worldIn.toServerWorld());
                assert dragon != null;
                dragon.setPosition(position.getX() + 0.5F, position.getY() + 1, position.getZ() + 0.5F);
                int dragonage = 10 + rand.nextInt(100);
                dragon.growDragon(dragonage);
                dragon.modelDeadProgress = 20;
                dragon.setModelDead(true);
                dragon.setDeathStage((dragonage / 5) / 2);
                dragon.setYaw(rand.nextInt(360));
                worldIn.spawnEntity(dragon);
            }
        }

        return true;
    }
}
