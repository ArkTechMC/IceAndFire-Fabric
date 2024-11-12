package com.iafenvoy.iceandfire.world;

import com.iafenvoy.iceandfire.StaticVariables;
import com.iafenvoy.iceandfire.config.IafCommonConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Optional;

public class GenerationConstant {
    public static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    public static boolean isFarEnoughFromSpawn(final BlockPos position) {
        BlockPos spawnRelative = Optional.ofNullable(StaticVariables.server.getWorld(World.OVERWORLD)).map(World::getLevelProperties).map(x->new BlockPos(x.getSpawnX(), position.getY(), x.getSpawnY())).orElse(new BlockPos(0, position.getY(),0));
        return !spawnRelative.isWithinDistance(position, IafCommonConfig.INSTANCE.worldGen.dangerousDistanceLimit.getValue().floatValue());
    }
}
