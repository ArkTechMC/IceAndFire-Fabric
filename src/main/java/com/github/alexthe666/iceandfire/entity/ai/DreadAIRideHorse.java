package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDreadKnight;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AbstractHorseEntity;

import java.util.EnumSet;
import java.util.List;

public class DreadAIRideHorse extends Goal {
    private final EntityDreadKnight knight;
    private AbstractHorseEntity horse;

    private List<AbstractHorseEntity> list = IAFMath.emptyAbstractHorseEntityList;

    public DreadAIRideHorse(EntityDreadKnight knight) {
        this.knight = knight;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (this.knight.hasVehicle()) {
            list = IAFMath.emptyAbstractHorseEntityList;
            return false;
        } else {

            if (this.knight.getWorld().getTime() % 4 == 0) // only update the list every 4 ticks
                list = this.knight.getWorld().getEntitiesByClass(AbstractHorseEntity.class,
                        this.knight.getBoundingBox().expand(16.0D, 7.0D, 16.0D), entity -> !entity.hasPassengers());

            if (list.isEmpty()) {
                return false;
            } else {
                this.horse = list.get(0);
                return true;
            }
        }
    }

    @Override
    public boolean shouldContinue() {
        return !this.knight.hasVehicle() && this.horse != null && !this.horse.hasPassengers();
    }

    @Override
    public void start() {
        this.horse.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.horse = null;
        this.knight.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.knight.getLookControl().lookAt(this.horse, 30.0F, 30.0F);

        this.knight.getNavigation().startMovingTo(this.horse, 1.2D);

        if (this.knight.squaredDistanceTo(this.horse) < 4.0D) {
            this.horse.setTame(true);
            this.knight.getNavigation().stop();
            this.knight.startRiding(horse);
        }
    }
}