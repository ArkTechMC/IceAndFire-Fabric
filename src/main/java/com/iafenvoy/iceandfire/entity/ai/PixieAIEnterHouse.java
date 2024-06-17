package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.entity.EntityPixie;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import java.util.EnumSet;

public class PixieAIEnterHouse extends Goal {
    final EntityPixie pixie;
    final Random random;

    public PixieAIEnterHouse(EntityPixie entityPixieIn) {
        this.pixie = entityPixieIn;
        this.random = entityPixieIn.getRandom();
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (this.pixie.isOwnerClose() || this.pixie.getMoveControl().isMoving() || this.pixie.isPixieSitting() || this.random.nextInt(20) != 0 || this.pixie.ticksUntilHouseAI != 0)
            return false;

        BlockPos blockpos1 = EntityPixie.findAHouse(this.pixie, this.pixie.getWorld());
        return !blockpos1.toString().equals(this.pixie.getBlockPos().toString());
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 3; ++i) {
            BlockPos blockpos1 = EntityPixie.findAHouse(this.pixie, this.pixie.getWorld());
            this.pixie.getMoveControl().moveTo(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.5D, blockpos1.getZ() + 0.5D, 0.25D);
            this.pixie.setHousePosition(blockpos1);
            if (this.pixie.getTarget() == null)
                this.pixie.getLookControl().lookAt(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.5D, blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
        }
    }
}
