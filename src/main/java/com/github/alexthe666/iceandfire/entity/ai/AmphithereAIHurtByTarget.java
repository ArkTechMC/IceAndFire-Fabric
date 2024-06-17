package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;

public class AmphithereAIHurtByTarget extends RevengeGoal {
    public AmphithereAIHurtByTarget(EntityAmphithere amphithere, boolean help, Class<?>[] classes) {
        super(amphithere, classes);
    }

    protected static void setEntityAttackTarget(MobEntity creatureIn, LivingEntity LivingEntityIn) {
        EntityAmphithere amphithere = (EntityAmphithere) creatureIn;
        if (amphithere.isTamed() || !(LivingEntityIn instanceof PlayerEntity)) {
            amphithere.setTarget(LivingEntityIn);
        }
    }
}
