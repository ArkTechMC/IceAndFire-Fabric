package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;

public class CockatriceAIFollowOwner extends FollowOwnerGoal {
    final EntityCockatrice cockatrice;

    public CockatriceAIFollowOwner(EntityCockatrice cockatrice, double speed, float minDist, float maxDist) {
        super(cockatrice, speed, minDist, maxDist, true);
        this.cockatrice = cockatrice;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && this.cockatrice.getCommand() == 2;
    }
}
