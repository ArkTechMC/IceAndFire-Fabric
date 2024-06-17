package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;
import java.util.concurrent.ThreadLocalRandom;

public class SeaSerpentAIGetInWater extends Goal {
    private final EntitySeaSerpent creature;
    private BlockPos targetPos;

    public SeaSerpentAIGetInWater(EntitySeaSerpent creature) {
        this.creature = creature;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if ((this.creature.jumpCooldown == 0 || this.creature.isOnGround()) && !this.creature.getWorld().getFluidState(this.creature.getBlockPos()).isIn(FluidTags.WATER)) {
            this.targetPos = this.generateTarget();
            return this.targetPos != null;
        }
        return false;
    }

    @Override
    public void start() {
        if (this.targetPos != null)
            this.creature.getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 1.5D);
    }

    @Override
    public boolean shouldContinue() {
        return !this.creature.getNavigation().isIdle() && this.targetPos != null && !this.creature.getWorld().getFluidState(this.creature.getBlockPos()).isIn(FluidTags.WATER);
    }

    public BlockPos generateTarget() {
        BlockPos blockpos = null;
        final int range = 16;
        for (int i = 0; i < 15; i++) {
            BlockPos blockpos1 = this.creature.getBlockPos().add(ThreadLocalRandom.current().nextInt(range) - range / 2, 3, ThreadLocalRandom.current().nextInt(range) - range / 2);
            while (this.creature.getWorld().isAir(blockpos1) && blockpos1.getY() > 1)
                blockpos1 = blockpos1.down();
            if (this.creature.getWorld().getFluidState(blockpos1).isIn(FluidTags.WATER))
                blockpos = blockpos1;
        }
        return blockpos;
    }
}
