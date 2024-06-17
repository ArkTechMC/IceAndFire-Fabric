package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.item.ItemHippogryphEgg;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;

public class HippogryphAIMate extends Goal {
    final World world;
    final double moveSpeed;
    private final EntityHippogryph hippo;
    int spawnBabyDelay;
    private EntityHippogryph targetMate;

    public HippogryphAIMate(EntityHippogryph animal, double speedIn) {
        this(animal, speedIn, animal.getClass());
    }

    public HippogryphAIMate(EntityHippogryph hippogryph, double speed, Class<? extends AnimalEntity> mate) {
        this.hippo = hippogryph;
        this.world = hippogryph.getWorld();
        this.moveSpeed = speed;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!this.hippo.isInLove() || this.hippo.isSitting()) return false;
        else {
            this.targetMate = this.getNearbyMate();
            return this.targetMate != null;
        }
    }

    @Override
    public boolean shouldContinue() {
        return this.targetMate.isAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
    }

    @Override
    public void stop() {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }

    @Override
    public void tick() {
        this.hippo.getLookControl().lookAt(this.targetMate, 10.0F, this.hippo.getMaxLookPitchChange());
        this.hippo.getNavigation().startMovingTo(this.targetMate, this.moveSpeed);
        ++this.spawnBabyDelay;

        if (this.spawnBabyDelay >= 60 && this.hippo.squaredDistanceTo(this.targetMate) < 9.0D)
            this.spawnBaby();
    }

    private EntityHippogryph getNearbyMate() {
        List<EntityHippogryph> list = this.world.getNonSpectatingEntities(EntityHippogryph.class, this.hippo.getBoundingBox().expand(8.0D));
        double d0 = Double.MAX_VALUE;
        EntityHippogryph entityanimal = null;

        for (EntityHippogryph entityanimal1 : list)
            if (this.hippo.canBreedWith(entityanimal1) && this.hippo.squaredDistanceTo(entityanimal1) < d0) {
                entityanimal = entityanimal1;
                d0 = this.hippo.squaredDistanceTo(entityanimal1);
            }

        return entityanimal;
    }

    private void spawnBaby() {
        ItemEntity egg = new ItemEntity(this.world, this.hippo.getX(), this.hippo.getY(), this.hippo.getZ(), ItemHippogryphEgg.createEggStack(this.hippo.getEnumVariant(), this.targetMate.getEnumVariant()));
        this.hippo.setBreedingAge(6000);
        this.targetMate.setBreedingAge(6000);
        this.hippo.resetLoveTicks();
        this.targetMate.resetLoveTicks();
        egg.refreshPositionAndAngles(this.hippo.getX(), this.hippo.getY(), this.hippo.getZ(), 0.0F, 0.0F);
        if (!this.world.isClient) this.world.spawnEntity(egg);
        Random random = this.hippo.getRandom();

        for (int i = 0; i < 7; ++i) {
            final double d0 = random.nextGaussian() * 0.02D;
            final double d1 = random.nextGaussian() * 0.02D;
            final double d2 = random.nextGaussian() * 0.02D;
            final double d3 = random.nextDouble() * this.hippo.getWidth() * 2.0D - this.hippo.getWidth();
            final double d4 = 0.5D + random.nextDouble() * this.hippo.getHeight();
            final double d5 = random.nextDouble() * this.hippo.getWidth() * 2.0D - this.hippo.getWidth();
            this.world.addParticle(ParticleTypes.HEART, this.hippo.getX() + d3, this.hippo.getY() + d4, this.hippo.getZ() + d5, d0, d1, d2);
        }

        if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT))
            this.world.spawnEntity(new ExperienceOrbEntity(this.world, this.hippo.getX(), this.hippo.getY(), this.hippo.getZ(), random.nextInt(7) + 1));
    }
}
