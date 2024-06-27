package com.iafenvoy.iceandfire.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class VecUtil {
    public static BlockPos createBlockPos(double x, double y, double z) {
        return new BlockPos((int) x, (int) y, (int) z);
    }

    public static Vec3d createBottomCenter(double x, double y, double z) {
        return Vec3d.ofBottomCenter(createBlockPos(x, y, z));
    }
}
