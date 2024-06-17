package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.IceAndFire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.mob.MobEntity;

public class IAFLookHelper extends LookControl {

    public IAFLookHelper(MobEntity LivingEntityIn) {
        super(LivingEntityIn);
    }

    @Override
    public void lookAt(Entity entityIn, float deltaYaw, float deltaPitch) {
        try {
            super.lookAt(entityIn, deltaYaw, deltaPitch);//rarely causes crash with vanilla
        } catch (Exception e) {
            IceAndFire.LOGGER.warn("Stopped a crash from happening relating to faulty looking AI.");
        }
    }
}
