package com.iafenvoy.iceandfire.util;

import com.iafenvoy.iceandfire.registry.IafFeatures;
import com.iafenvoy.iceandfire.world.IafWorldData;
import com.iafenvoy.uranus.server.world.WorldChunkUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;

public class WorldUtil {
    public static boolean canGenerate(double configChance, final StructureWorldAccess level, final Random random, final BlockPos origin, final String id, boolean checkFluid) {
        return canGenerate(configChance, level, random, origin, id, IafWorldData.FeatureType.SURFACE, checkFluid);
    }

    public static boolean canGenerate(double configChance, final StructureWorldAccess level, final Random random, final BlockPos origin, final String id, final IafWorldData.FeatureType type, boolean checkFluid) {
        boolean canGenerate = random.nextDouble() < configChance && IafFeatures.isFarEnoughFromSpawn(level, origin) && IafFeatures.isFarEnoughFromDangerousGen(level, origin, id, type);
        if (canGenerate && checkFluid)
            if (!level.getFluidState(origin.down()).isEmpty())
                return false;
        return canGenerate;
    }
}
