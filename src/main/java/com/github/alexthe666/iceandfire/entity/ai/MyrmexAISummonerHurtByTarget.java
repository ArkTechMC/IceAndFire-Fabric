package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSwarmer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TrackTargetGoal;

import java.util.EnumSet;

public class MyrmexAISummonerHurtByTarget extends TrackTargetGoal {
    EntityMyrmexSwarmer tameable;
    LivingEntity attacker;
    private int timestamp;

    public MyrmexAISummonerHurtByTarget(EntityMyrmexSwarmer theDefendingTameableIn) {
        super(theDefendingTameableIn, false);
        this.tameable = theDefendingTameableIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        LivingEntity living = this.tameable.getSummoner();

        if (living == null) {
            return false;
        } else {
            this.attacker = living.getAttacker();
            int i = living.getLastAttackedTime();
            return i != this.timestamp && this.canTrack(this.attacker, TargetPredicate.DEFAULT) && this.tameable.shouldAttackEntity(this.attacker, living);
        }
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacker);
        LivingEntity LivingEntity = this.tameable.getSummoner();

        if (LivingEntity != null) {
            this.timestamp = LivingEntity.getLastAttackedTime();
        }

        super.start();
    }
}