package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.google.common.base.Predicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.NotNull;

public class StymphalianBirdAITarget extends ActiveTargetGoal<LivingEntity> {
    private final EntityStymphalianBird bird;

    public StymphalianBirdAITarget(EntityStymphalianBird entityIn, Class<LivingEntity> classTarget, boolean checkSight) {
        super(entityIn, classTarget, 0, checkSight, false, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(LivingEntity entity) {
                return !EntityGorgon.isStoneMob(entity) && (entity instanceof PlayerEntity && !((PlayerEntity) entity).isCreative() || entity instanceof MerchantEntity || entity instanceof GolemEntity || entity instanceof AnimalEntity && IafConfig.stympahlianBirdAttackAnimals);
            }
        });
        this.bird = entityIn;
    }


    @Override
    public boolean canStart() {
        boolean supe = super.canStart();
        if (this.targetEntity != null && this.bird.getVictor() != null && this.bird.getVictor().getUuid().equals(this.targetEntity.getUuid())) {
            return false;
        }
        return supe && this.targetEntity != null && !this.targetEntity.getClass().equals(this.bird.getClass());
    }

    @Override
    protected @NotNull Box getSearchBox(double targetDistance) {
        return this.bird.getBoundingBox().expand(targetDistance, targetDistance, targetDistance);
    }
}