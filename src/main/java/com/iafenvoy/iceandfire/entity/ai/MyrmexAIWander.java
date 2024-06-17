package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;

public class MyrmexAIWander extends WanderAroundFarGoal {
    protected final EntityMyrmexBase myrmex;

    public MyrmexAIWander(EntityMyrmexBase myrmex, double speed) {
        super(myrmex, speed);
        this.myrmex = myrmex;
    }

    @Override
    public boolean canStart() {
        return this.myrmex.canMove() && this.myrmex.shouldWander() && super.canStart();
    }
}
