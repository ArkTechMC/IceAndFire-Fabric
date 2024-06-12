package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TrackTargetGoal;

import java.util.EnumSet;

public class MyrmexAIDefendHive extends TrackTargetGoal {
    final EntityMyrmexBase myrmex;
    LivingEntity villageAgressorTarget;

    public MyrmexAIDefendHive(EntityMyrmexBase myrmex) {
        super(myrmex, false, true);
        this.myrmex = myrmex;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        MyrmexHive village = this.myrmex.getHive();

        if (!this.myrmex.canMove() || village == null) {
            return false;
        } else {
            this.villageAgressorTarget = village.findNearestVillageAggressor(this.myrmex);
            if (this.canTrack(this.villageAgressorTarget, TargetPredicate.DEFAULT)) {
                return true;
            } else if (this.mob.getRandom().nextInt(20) == 0) {
                this.villageAgressorTarget = village.getNearestTargetPlayer(this.myrmex, this.myrmex.getWorld());
                return this.canTrack(this.villageAgressorTarget, TargetPredicate.DEFAULT);
            } else {
                return false;
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        this.myrmex.setTarget(this.villageAgressorTarget);
        super.start();
    }
}