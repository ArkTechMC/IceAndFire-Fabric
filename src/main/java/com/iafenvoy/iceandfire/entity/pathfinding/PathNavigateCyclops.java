package com.iafenvoy.iceandfire.entity.pathfinding;

import com.iafenvoy.citadel.server.entity.collision.CustomCollisionsNavigator;
import com.iafenvoy.iceandfire.entity.EntityCyclops;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeNavigator;
import net.minecraft.world.World;

public class PathNavigateCyclops extends CustomCollisionsNavigator {

    public PathNavigateCyclops(EntityCyclops LivingEntityIn, World worldIn) {
        super(LivingEntityIn, worldIn);
    }

    @Override
    protected PathNodeNavigator createPathNodeNavigator(int i) {
        this.nodeMaker = new LandPathNodeMaker();
        this.nodeMaker.setCanOpenDoors(true);
        this.nodeMaker.setCanSwim(true);
        return new PathNodeNavigator(this.nodeMaker, i);
    }
}