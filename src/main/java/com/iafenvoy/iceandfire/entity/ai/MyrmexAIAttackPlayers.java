package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Predicate;

public class MyrmexAIAttackPlayers extends ActiveTargetGoal {
    private final EntityMyrmexBase myrmex;

    @SuppressWarnings("unchecked")
    public MyrmexAIAttackPlayers(EntityMyrmexBase myrmex) {
        super(myrmex, PlayerEntity.class, 10, true, true, (Predicate<PlayerEntity>) entity -> entity != null && (myrmex.getHive() == null
                || myrmex.getHive().isPlayerReputationLowEnoughToFight(entity.getUuid())));
        this.myrmex = myrmex;
    }

    @Override
    public boolean canStart() {
        return this.myrmex.shouldHaveNormalAI() && super.canStart();
    }
}
