package com.iafenvoy.iceandfire.util;

import java.util.Random;

public class RandomHelper {
    public static int nextInt(int min, int max) {
        return nextInt(new Random(), min, max);
    }

    public static int nextInt(Random random, int min, int max) {
        return min >= max ? min : random.nextInt(max - min + 1) + min;
    }

    public static double nextDouble(double min, double max) {
        return nextDouble(new Random(), min, max);
    }

    public static double nextDouble(Random random, double min, double max) {
        return min >= max ? min : random.nextDouble() * (max - min) + min;
    }

    public static double randomize(double origin, double ratio) {
        double range = Math.abs(origin * ratio);
        return origin + nextDouble(-range, range);
    }
}
