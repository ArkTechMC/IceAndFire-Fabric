package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.GorgonAIStareAttack;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.entity.ai.goal.*;
import org.jetbrains.annotations.NotNull;

public class EntityGorgon extends HostileEntity implements IAnimatedEntity, IVillagerFear, IAnimalFear, IHumanoid, IHasCustomizableAttributes {

    public static Animation ANIMATION_SCARE;
    public static Animation ANIMATION_HIT;
    private int animationTick;
    private Animation currentAnimation;
    private GorgonAIStareAttack aiStare;
    private MeleeAttackGoal aiMelee;
    private int playerStatueCooldown;

    public EntityGorgon(EntityType<EntityGorgon> type, World worldIn) {
        super(type, worldIn);
        ANIMATION_SCARE = Animation.create(30);
        ANIMATION_HIT = Animation.create(10);
    }

    public static boolean isEntityLookingAt(LivingEntity looker, LivingEntity seen, double degree) {
        degree *= 1 + (looker.distanceTo(seen) * 0.1);
        Vec3d Vector3d = looker.getRotationVec(1.0F).normalize();
        Vec3d Vector3d1 = new Vec3d(seen.getX() - looker.getX(), seen.getBoundingBox().minY + (double) seen.getStandingEyeHeight() - (looker.getY() + (double) looker.getStandingEyeHeight()), seen.getZ() - looker.getZ());
        double d0 = Vector3d1.length();
        Vector3d1 = Vector3d1.normalize();
        double d1 = Vector3d.dotProduct(Vector3d1);
        return d1 > 1.0D - degree / d0 && (looker.canSee(seen) && !isStoneMob(seen));
    }

    public static boolean isStoneMob(LivingEntity mob) {
        return mob instanceof EntityStoneStatue;
    }

    public static boolean isBlindfolded(LivingEntity attackTarget) {
        return attackTarget != null && (attackTarget.getEquippedStack(EquipmentSlot.HEAD).getItem() == IafItemRegistry.BLINDFOLD.get() || attackTarget.hasStatusEffect(StatusEffects.BLINDNESS) || ServerEvents.isBlindMob(attackTarget));
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return MobEntity.createMobAttributes()
            //HEALTH
            .add(EntityAttributes.GENERIC_MAX_HEALTH, IafConfig.gorgonMaxHealth)
            //SPEED
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
            //ATTACK
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0D)
            //ARMOR
            .add(EntityAttributes.GENERIC_ARMOR, 1.0D);
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(IafConfig.gorgonMaxHealth);
    }

    public boolean isTargetBlocked(Vec3d target) {
        Vec3d Vector3d = new Vec3d(this.getX(), this.getEyeY(), this.getZ());
        HitResult result = this.getWorld().raycast(new RaycastContext(Vector3d, target, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
        return result.getType() != HitResult.Type.MISS;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new AvoidSunlightGoal(this));
        this.goalSelector.add(3, new EscapeSunlightGoal(this, 1.0D));
        this.goalSelector.add(3, aiStare = new GorgonAIStareAttack(this, 1.0D, 0, 15.0F));
        this.goalSelector.add(3, aiMelee = new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D) {
            @Override
            public boolean canStart() {
                chance = 20;
                return super.canStart();
            }
        });
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F, 1.0F) {
            @Override
            public boolean shouldContinue() {
                if (this.target != null && this.target instanceof PlayerEntity && ((PlayerEntity) this.target).isCreative()) {
                    return false;
                }
                return super.shouldContinue();
            }
        });
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal(this, PlayerEntity.class, 10, false, false, new Predicate<Entity>() {
            @Override
            public boolean apply(Entity entity) {
                return entity.isAlive();
            }
        }));
        this.targetSelector.add(3, new ActiveTargetGoal(this, LivingEntity.class, 10, true, false, new Predicate<Entity>() {
            @Override
            public boolean apply(Entity entity) {
                return entity instanceof LivingEntity && DragonUtils.isAlive((LivingEntity) entity) || (entity instanceof IBlacklistedFromStatues && ((IBlacklistedFromStatues) entity).canBeTurnedToStone());
            }
        }));
        this.goalSelector.remove(aiMelee);
    }

    public void attackEntityWithRangedAttack(LivingEntity entity) {
        if (!(entity instanceof MobEntity) && entity instanceof LivingEntity) {
            forcePreyToLook(entity);
        }
    }

    @Override
    public boolean tryAttack(@NotNull Entity entityIn) {
        boolean blindness = this.hasStatusEffect(StatusEffects.BLINDNESS) || this.getTarget() != null && this.getTarget().hasStatusEffect(StatusEffects.BLINDNESS) || this.getTarget() != null && this.getTarget() instanceof IBlacklistedFromStatues && !((IBlacklistedFromStatues) this.getTarget()).canBeTurnedToStone();
        if (blindness && this.deathTime == 0) {
            if (this.getAnimation() != ANIMATION_HIT) {
                this.setAnimation(ANIMATION_HIT);
            }
            if (entityIn instanceof LivingEntity) {
                ((LivingEntity) entityIn).addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 2, false, true));
            }
        }
        return super.tryAttack(entityIn);
    }

    @Override
    public void setTarget(LivingEntity LivingEntityIn) {
        super.setTarget(LivingEntityIn);
        if (LivingEntityIn != null && !getWorld().isClient) {


            boolean blindness = this.hasStatusEffect(StatusEffects.BLINDNESS) || LivingEntityIn.hasStatusEffect(StatusEffects.BLINDNESS) || LivingEntityIn instanceof IBlacklistedFromStatues && !((IBlacklistedFromStatues) LivingEntityIn).canBeTurnedToStone() || isBlindfolded(LivingEntityIn);
            if (blindness && this.deathTime == 0) {
                this.goalSelector.add(3, aiMelee);
                this.goalSelector.remove(aiStare);
            } else {
                this.goalSelector.add(3, aiStare);
                this.goalSelector.remove(aiMelee);
            }
        }
    }

    @Override
    public int getXpToDrop() {
        return 30;
    }

    @Override
    protected void updatePostDeath() {
        ++this.deathTime;
        this.ambientSoundChance = 20;
        if (this.getWorld().isClient) {
            for (int k = 0; k < 5; ++k) {
                double d2 = 0.4;
                double d0 = 0.1;
                double d1 = 0.1;
                IceAndFire.PROXY.spawnParticle(EnumParticles.Blood, this.getX() + (double) (this.random.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getY(), this.getZ() + (double) (this.random.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d2, d0, d1);
            }
        }
        if (this.deathTime >= 200) {
            if (!this.getWorld().isClient && (this.shouldAlwaysDropXp() || this.playerHitTimer > 0 && this.shouldDropXp() && this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))) {
                int i = this.getXpToDrop();
                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0) {
                    int j = ExperienceOrbEntity.roundToOrbSize(i);
                    i -= j;
                    this.getWorld().spawnEntity(new ExperienceOrbEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), j));
                }
            }
            this.remove(RemovalReason.KILLED);

            for (int k = 0; k < 20; ++k) {
                double d2 = this.random.nextGaussian() * 0.02D;
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                this.getWorld().addParticle(ParticleTypes.CLOUD, this.getX() + (double) (this.random.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getY() + (double) (this.random.nextFloat() * this.getHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d2, d0, d1);
            }
        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (playerStatueCooldown > 0) {
            playerStatueCooldown--;
        }
        LivingEntity attackTarget = this.getTarget();
        if (attackTarget != null) {
            boolean blindness = this.hasStatusEffect(StatusEffects.BLINDNESS) || attackTarget.hasStatusEffect(StatusEffects.BLINDNESS);
            if (!blindness && this.deathTime == 0 && attackTarget instanceof MobEntity && !(attackTarget instanceof PlayerEntity)) {
                forcePreyToLook(attackTarget);
            }
            if (isEntityLookingAt(attackTarget, this, 0.4)) {
                this.getLookControl().lookAt(attackTarget.getX(), attackTarget.getY() + (double) attackTarget.getStandingEyeHeight(), attackTarget.getZ(), (float) this.getMaxHeadRotation(), (float) this.getMaxLookPitchChange());
            }
        }


        if (attackTarget != null && isEntityLookingAt(this, attackTarget, 0.4) && isEntityLookingAt(attackTarget, this, 0.4) && !isBlindfolded(attackTarget)) {
            boolean blindness = this.hasStatusEffect(StatusEffects.BLINDNESS) || attackTarget.hasStatusEffect(StatusEffects.BLINDNESS) || attackTarget instanceof IBlacklistedFromStatues && !((IBlacklistedFromStatues) attackTarget).canBeTurnedToStone();
            if (!blindness && this.deathTime == 0) {
                if (this.getAnimation() != ANIMATION_SCARE) {
                    this.playSound(IafSoundRegistry.GORGON_ATTACK, 1, 1);
                    this.setAnimation(ANIMATION_SCARE);
                }
                if (this.getAnimation() == ANIMATION_SCARE) {
                    if (this.getAnimationTick() > 10) {
                        if (!getWorld().isClient) {
                            if (playerStatueCooldown == 0) {
                                EntityStoneStatue statue = EntityStoneStatue.buildStatueEntity(attackTarget);
                                statue.updatePositionAndAngles(attackTarget.getX(), attackTarget.getY(), attackTarget.getZ(), attackTarget.getYaw(), attackTarget.getPitch());
                                if (!getWorld().isClient) {
                                    getWorld().spawnEntity(statue);
                                }
                                statue.setYaw(attackTarget.getYaw());
                                statue.setYaw(attackTarget.getYaw());
                                statue.headYaw = attackTarget.getYaw();
                                statue.bodyYaw = attackTarget.getYaw();
                                statue.prevBodyYaw = attackTarget.getYaw();
                                playerStatueCooldown = 40;
                                if (attackTarget instanceof PlayerEntity) {

                                    attackTarget.damage(IafDamageRegistry.causeGorgonDamage(this), Integer.MAX_VALUE);
                                } else {
                                    attackTarget.remove(RemovalReason.KILLED);
                                }
                                this.setTarget(null);

                            }
                        }
                    }
                }
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public int getMaxLookPitchChange() {
        return 10;
    }

    @Override
    public int getMaxHeadRotation() {
        return 30;
    }

    @Override
    public @NotNull EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    public void forcePreyToLook(LivingEntity mob) {
        if (mob instanceof MobEntity mobEntity) {
            mobEntity.getLookControl().lookAt(this.getX(), this.getY() + (double) this.getStandingEyeHeight(), this.getZ(), (float) mobEntity.getMaxHeadRotation(), (float) mobEntity.getMaxLookPitchChange());

        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound pCompound) {
        super.readCustomDataFromNbt(pCompound);
        this.setConfigurableAttributes();
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_SCARE, ANIMATION_HIT};
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.GORGON_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return IafSoundRegistry.GORGON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.GORGON_DIE;
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceToClosestPlayer) {
        return false;
    }
}
