package dev.arktechmc.iafextra.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class PathUtil {
    public static PathNodeType getDanger(PathNodeType type) {
        return type == PathNodeType.DAMAGE_FIRE || type == PathNodeType.DANGER_FIRE ? PathNodeType.DANGER_FIRE :
                type == PathNodeType.DAMAGE_OTHER || type == PathNodeType.DANGER_OTHER ? PathNodeType.DANGER_OTHER :
                        type == PathNodeType.LAVA ? PathNodeType.DAMAGE_FIRE :
                                null;
    }

    public static PathNodeType getAiPathNodeType(BlockState state, WorldView level, BlockPos pos) {
        return state.getBlock() == Blocks.LAVA ? PathNodeType.LAVA : BlockUtil.isBurning(state) ? PathNodeType.DAMAGE_FIRE : null;
    }
}
