package com.github.alexthe666.iceandfire.pathfinding;

import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PathNavigateFlyingCreature extends BirdNavigation {

    public PathNavigateFlyingCreature(MobEntity entity, World world) {
        super(entity, world);
    }

    @Override
    public boolean isValidPosition(BlockPos pos) {
        return this.world.isAir(pos.down());
    }
}
