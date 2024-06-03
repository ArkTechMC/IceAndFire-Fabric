package com.github.alexthe666.iceandfire.util;

import java.util.Collections;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;

public class IAFMath {

    public static final List<PlayerEntity> emptyPlayerEntityList = Collections.emptyList();
    public static final List<ItemEntity> emptyItemEntityList = Collections.emptyList();
    public static final List<AbstractHorseEntity> emptyAbstractHorseEntityList = Collections.emptyList();
    public static final List<Entity> emptyEntityList = Collections.emptyList();
    public static final List<LivingEntity> emptyLivingEntityList = Collections.emptyList();
    private static final double coeff_1 = Math.PI / 4;
    private static final double coeff_2 = coeff_1 * 3;

    public static double atan2_accurate(double y, double x) {
        final double r;
        if (y < 0) {
            y = -y;
            if (x > 0) {
                r = (x - y) / (x + y);
                return -(0.1963 * r * r * r - 0.9817 * r + coeff_1);
            } else {
                r = (x + y) / (y - x);
                return -(0.1963 * r * r * r - 0.9817 * r + coeff_2);
            }
        } else {
            if (y == 0) {
                y = 1.0E-25;
            }
            if (x > 0) {
                r = (x - y) / (x + y);
                return 0.1963 * r * r * r - 0.9817 * r + coeff_1;
            } else {
                r = (x + y) / (y - x);
                return 0.1963 * r * r * r - 0.9817 * r + coeff_2;
            }
        }
    }
}
