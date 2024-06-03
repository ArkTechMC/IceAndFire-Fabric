package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;

public class MyrmexAIWander extends WanderAroundFarGoal {

    protected EntityMyrmexBase myrmex;

    public MyrmexAIWander(EntityMyrmexBase myrmex, double speed) {
        super(myrmex, speed);
        this.myrmex = myrmex;
    }

    @Override
    public boolean canStart() {
        return myrmex.canMove() && myrmex.shouldWander() && super.canStart();
    }
}
