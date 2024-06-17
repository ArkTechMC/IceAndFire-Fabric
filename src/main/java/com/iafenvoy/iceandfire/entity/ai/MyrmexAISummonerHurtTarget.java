package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.entity.EntityMyrmexSwarmer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TrackTargetGoal;

import java.util.EnumSet;

public class MyrmexAISummonerHurtTarget extends TrackTargetGoal {
    final EntityMyrmexSwarmer tameable;
    LivingEntity attacker;
    private int timestamp;

    public MyrmexAISummonerHurtTarget(EntityMyrmexSwarmer theEntityMyrmexSwarmerIn) {
        super(theEntityMyrmexSwarmerIn, false);
        this.tameable = theEntityMyrmexSwarmerIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        LivingEntity living = this.tameable.getSummoner();
        if (living == null) return false;
        else {
            this.attacker = living.getAttacking();
            int i = living.getLastAttackTime();
            return i != this.timestamp && this.canTrack(this.attacker, TargetPredicate.DEFAULT) && this.tameable.shouldAttackEntity(this.attacker, living);
        }
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacker);
        LivingEntity LivingEntity = this.tameable.getSummoner();
        if (LivingEntity != null)
            this.timestamp = LivingEntity.getLastAttackTime();
        super.start();
    }
}