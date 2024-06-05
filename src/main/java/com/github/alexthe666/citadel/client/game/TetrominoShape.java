package com.github.alexthe666.citadel.client.game;

import java.util.List;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;

public enum TetrominoShape {
    FOURBYONE(new Vec3i(0, -1, 0), new Vec3i(0, 0, 0), new Vec3i(0, 1, 0), new Vec3i(0, 2, 0)),
    LLEFT(new Vec3i(0, 1, 0), new Vec3i(0, 0, 0), new Vec3i(0, -1, 0), new Vec3i(1, -1, 0)),
    LRIGHT(new Vec3i(0, 1, 0), new Vec3i(0, 0, 0), new Vec3i(0, -1, 0), new Vec3i(-1, -1, 0)),
    SQUARE(new Vec3i(1, 1, 0), new Vec3i(1, 0, 0), new Vec3i(0, 0, 0), new Vec3i(0, 1, 0)),
    ZLEFT(new Vec3i(-1, -1, 0), new Vec3i(0, -1, 0), new Vec3i(0, 0, 0), new Vec3i(0, 1, 0)),
    ZRIGHT(new Vec3i(0, -1, 0), new Vec3i(1, -1, 0), new Vec3i(0, 0, 0), new Vec3i(-1, 0, 0)),
    T(new Vec3i(0, -1, 0), new Vec3i(1, 0, 0), new Vec3i(0, 0, 0), new Vec3i(0, 1, 0));
    private final List<Vec3i> relativePositions;

    TetrominoShape(Vec3i... relativePositions) {
        this.relativePositions = List.of(relativePositions);
    }

    public static TetrominoShape getRandom(Random randomSource) {
        return values()[randomSource.nextInt(values().length - 1)];
    }

    public List<Vec3i> getRelativePositions() {
        return this.relativePositions;
    }

}
