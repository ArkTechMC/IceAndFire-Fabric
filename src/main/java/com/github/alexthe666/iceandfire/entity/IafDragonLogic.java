package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.message.MessageSpawnParticleAt;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

/*
    dragon logic separation for client, server and shared sides.
 */
public class IafDragonLogic {
    long ticksAfterClearingTarget;

    private final EntityDragonBase dragon;

    public IafDragonLogic(EntityDragonBase dragon) {
        this.dragon = dragon;
    }

    /*
    logic done exclusively on server.
    */
    public void updateDragonServer() {
        // Update dragon rider
        dragon.updateRider();

        // Update dragon pitch
        dragon.updatePitch(dragon.prevY - dragon.getY());

        if (dragon.lookingForRoostAIFlag && dragon.getAttacker() != null || dragon.isSleeping()) {
            dragon.lookingForRoostAIFlag = false;
        }
        if (IafConfig.doDragonsSleep && !dragon.isSleeping() && !dragon.isTimeToWake() && dragon.getPassengerList().isEmpty() && this.dragon.getCommand() != 2) {
            if (dragon.hasHomePosition
                    && dragon.getPositionTarget() != null
                    && DragonUtils.isInHomeDimension(dragon)
                    && dragon.getDistanceSquared(Vec3d.ofCenter(dragon.getPositionTarget())) > dragon.getWidth() * 10
                    && this.dragon.getCommand() != 2 && this.dragon.getCommand() != 1) {
                dragon.lookingForRoostAIFlag = true;
            } else {
                dragon.lookingForRoostAIFlag = false;
                if ((/* Avoid immediately sleeping after killing the target */ dragon.getWorld().getTime() - ticksAfterClearingTarget >= 20) && !dragon.isTouchingWater() && dragon.isOnGround() && !dragon.isFlying() && !dragon.isHovering() && dragon.getTarget() == null) {
                    dragon.setInSittingPose(true);
                }
            }
        } else {
            dragon.lookingForRoostAIFlag = false;
        }
        if (dragon.isSleeping() && (dragon.isFlying() || dragon.isHovering() || dragon.isTouchingWater() || (dragon.getWorld().isSkyVisibleAllowingSea(dragon.getBlockPos()) && dragon.isTimeToWake() && !dragon.isTamed() || dragon.isTimeToWake() && dragon.isTamed()) || dragon.getTarget() != null || !dragon.getPassengerList().isEmpty())) {
            dragon.setInSittingPose(false);
        }
        if (dragon.isSitting() && dragon.getControllingPassenger() != null) {
            dragon.setSitting(false);
        }
        if (dragon.blockBreakCounter <= 0) {
            dragon.blockBreakCounter = IafConfig.dragonBreakBlockCooldown;
        }
        dragon.updateBurnTarget();
        if (dragon.isSitting()) {
            if (dragon.getCommand() != 1 || dragon.getControllingPassenger() != null)
                dragon.setSitting(false);
        } else {
            if (dragon.getCommand() == 1 && dragon.getControllingPassenger() == null)
                dragon.setSitting(true);
        }
        if (dragon.isSitting()) {
            dragon.getNavigation().stop();
        }
        if (dragon.isInLove()) {
            dragon.getWorld().sendEntityStatus(dragon, (byte) 18);
        }
        if (new Vec3i((int) dragon.prevX, (int) dragon.prevY, (int) dragon.prevZ).getSquaredDistance(dragon.getBlockPos()) <= 0.5) {
            dragon.ticksStill++;
        } else {
            dragon.ticksStill = 0;
        }
        if (dragon.getControllingPassenger() == null && dragon.isTackling() && !dragon.isFlying() && dragon.isOnGround()) {
            dragon.tacklingTicks++;
            if (dragon.tacklingTicks == 40) {
                dragon.tacklingTicks = 0;
                dragon.setTackling(false);
                dragon.setFlying(false);
            }
        }
        if (dragon.getRandom().nextInt(500) == 0 && !dragon.isModelDead() && !dragon.isSleeping()) {
            dragon.roar();
        }
        // In air tackle attack
        if (dragon.isFlying() && dragon.getTarget() != null) {
            if (dragon.airAttack == IafDragonAttacks.Air.TACKLE)
                dragon.setTackling(true);

            if (dragon.isTackling()) {
                if (dragon.getBoundingBox().stretch(2.0D, 2.0D, 2.0D).intersects(dragon.getTarget().getBoundingBox())) {
                    dragon.usingGroundAttack = true;
                    dragon.randomizeAttacks();
                    attackTarget(dragon.getTarget(), null, dragon.getDragonStage() * 3);
                    dragon.setFlying(false);
                    dragon.setHovering(false);
                }
            }
        }

        if (dragon.getControllingPassenger() == null && dragon.isTackling() && (dragon.getTarget() == null || dragon.airAttack != IafDragonAttacks.Air.TACKLE)) {
            dragon.setTackling(false);
            dragon.randomizeAttacks();
        }
        if (dragon.hasVehicle()) {
            dragon.setFlying(false);
            dragon.setHovering(false);
            dragon.setInSittingPose(false);
        }
        if (dragon.isFlying() && dragon.age % 40 == 0 || dragon.isFlying() && dragon.isSleeping()) {
            dragon.setInSittingPose(false);
        }
        if (!dragon.canMove()) {
            if (dragon.getTarget() != null) {
                dragon.setTarget(null);
                ticksAfterClearingTarget = dragon.getWorld().getTime();
            }
            dragon.getNavigation().stop();
        }
        if (!dragon.isTamed()) {
            dragon.updateCheckPlayer();
        }
        if (dragon.isModelDead() && (dragon.isFlying() || dragon.isHovering())) {
            dragon.setFlying(false);
            dragon.setHovering(false);
        }
        if (dragon.getControllingPassenger() == null) {
            if ((dragon.useFlyingPathFinder() || dragon.isHovering()) && dragon.navigatorType != 1) {
                dragon.switchNavigator(1);
            }
        } else {
            if ((dragon.useFlyingPathFinder() || dragon.isHovering()) && dragon.navigatorType != 2) {
                dragon.switchNavigator(2);
            }
        }
        if (dragon.getControllingPassenger() == null && !dragon.useFlyingPathFinder() && !dragon.isHovering() && dragon.navigatorType != 0) {
            dragon.switchNavigator(0);
        }
        // Dragon landing
        if (dragon.getControllingPassenger() == null && !dragon.isOverAir() && dragon.doesWantToLand() && (dragon.isFlying() || dragon.isHovering()) && !dragon.isTouchingWater()) {
            dragon.setFlying(false);
            dragon.setHovering(false);
        }
        if (dragon.isHovering()) {
            if (dragon.isFlying() && dragon.flyTicks > 40) {
                dragon.setHovering(false);
                dragon.setFlying(true);
            }
            dragon.hoverTicks++;
        } else {
            dragon.hoverTicks = 0;
        }
        if (dragon.isHovering() && !dragon.isFlying()) {
            if (dragon.isSleeping()) {
                dragon.setHovering(false);
            }
            // Slowly land the hovering dragon
            if (dragon.getControllingPassenger() == null && dragon.doesWantToLand() && !dragon.isOnGround() && !dragon.isTouchingWater()) {
                dragon.setVelocity(dragon.getVelocity().add(0, -0.25, 0));
            } else {
                if ((dragon.getControllingPassenger() == null || dragon.getControllingPassenger() instanceof EntityDreadQueen) && !dragon.isBeyondHeight()) {
                    double up = dragon.isTouchingWater() ? 0.12D : 0.08D;
                    dragon.setVelocity(dragon.getVelocity().add(0, up, 0));
                }
                if (dragon.hoverTicks > 40) {
                    dragon.setHovering(false);
                    dragon.setFlying(true);
                    dragon.flyHovering = 0;
                    dragon.hoverTicks = 0;
                    dragon.flyTicks = 0;
                }
            }
        }
        if (dragon.isSleeping()) {
            dragon.getNavigation().stop();
        }
        if ((dragon.isOnGround() || dragon.isTouchingWater()) && dragon.flyTicks != 0) {
            dragon.flyTicks = 0;
        }
        if (dragon.isAllowedToTriggerFlight() && dragon.isFlying() && dragon.doesWantToLand()) {
            dragon.setFlying(false);
            dragon.setHovering(dragon.isOverAir());
            if (!dragon.isOverAir()) {
                dragon.flyTicks = 0;
                dragon.setFlying(false);
            }
        }
        if (dragon.isFlying()) {
            dragon.flyTicks++;
        }
        if ((dragon.isHovering() || dragon.isFlying()) && dragon.isSleeping()) {
            dragon.setFlying(false);
            dragon.setHovering(false);
        }
        if (!dragon.isFlying() && !dragon.isHovering()) {
            if (dragon.isAllowedToTriggerFlight() || dragon.getY() < dragon.getWorld().getBottomY()) {
                if (dragon.getRandom().nextInt(dragon.getFlightChancePerTick()) == 0 || dragon.getY() < dragon.getWorld().getBottomY() || dragon.getTarget() != null && Math.abs(dragon.getTarget().getY() - dragon.getY()) > 5 || dragon.isTouchingWater()) {
                    dragon.setHovering(true);
                    dragon.setInSittingPose(false);
                    dragon.setSitting(false);
                    dragon.flyHovering = 0;
                    dragon.hoverTicks = 0;
                    dragon.flyTicks = 0;
                }
            }
        }
        if (dragon.getTarget() != null) {
            if (!DragonUtils.isAlive(dragon.getTarget())) {
                dragon.setTarget(null);
                ticksAfterClearingTarget = dragon.getWorld().getTime();
            }
        }
        if (!dragon.isAgingDisabled()) {
            dragon.setAgeInTicks(dragon.getAgeInTicks() + 1);
            if (dragon.getAgeInTicks() % 24000 == 0) {
                dragon.updateAttributes();
                dragon.growDragon(0);
            }
        }
        if (dragon.age % IafConfig.dragonHungerTickRate == 0 && IafConfig.dragonHungerTickRate > 0) {
            if (dragon.getHunger() > 0) {
                dragon.setHunger(dragon.getHunger() - 1);
            }
        }
        if ((dragon.groundAttack == IafDragonAttacks.Ground.FIRE) && dragon.getDragonStage() < 2) {
            dragon.usingGroundAttack = true;
            dragon.randomizeAttacks();
            dragon.playSound(dragon.getBabyFireSound(), 1, 1);
        }
        if (dragon.isBreathingFire()) {
            if (dragon.isSleeping() || dragon.isModelDead()) {
                dragon.setBreathingFire(false);
                dragon.randomizeAttacks();
                dragon.fireTicks = 0;
            }
            if (dragon.burningTarget == null) {
                if (dragon.fireTicks > dragon.getDragonStage() * 25 || dragon.getOwner() != null && dragon.getPassengerList().contains(dragon.getOwner()) && dragon.fireStopTicks <= 0) {
                    dragon.setBreathingFire(false);
                    dragon.randomizeAttacks();
                    dragon.fireTicks = 0;
                }
            }

            if (dragon.fireStopTicks > 0 && dragon.getOwner() != null && dragon.getPassengerList().contains(dragon.getOwner())) {
                dragon.fireStopTicks--;
            }
        }
        if (dragon.isFlying()) {
            if (dragon.getTarget() != null && dragon.getBoundingBox().stretch(3.0F, 3.0F, 3.0F).intersects(dragon.getTarget().getBoundingBox())) {
                dragon.tryAttack(dragon.getTarget());
            }
            if (dragon.airAttack == IafDragonAttacks.Air.TACKLE && (dragon.horizontalCollision || dragon.isOnGround())) {
                dragon.usingGroundAttack = true;
                if (dragon.getControllingPassenger() == null) {
                    dragon.setFlying(false);
                    dragon.setHovering(false);
                }
            }
            if (dragon.usingGroundAttack) {
                dragon.airAttack = IafDragonAttacks.Air.TACKLE;
            }
            if (dragon.airAttack == IafDragonAttacks.Air.TACKLE && dragon.getTarget() != null && dragon.isTargetBlocked(dragon.getTarget().getPos())) {
                dragon.randomizeAttacks();
            }
        }
    }

    public boolean attackTarget(Entity target, PlayerEntity ridingPlayer, float damage) {
        if (ridingPlayer == null)
            return target.damage(target.getWorld().getDamageSources().mobAttack(dragon), damage);
        else
            return target.damage(target.getWorld().getDamageSources().indirectMagic(dragon, ridingPlayer), damage);
    }

    /*
    logic done exclusively on client.
    */
    public void updateDragonClient() {
        if (!dragon.isModelDead()) {
            dragon.turn_buffer.calculateChainSwingBuffer(50, 0, 4, dragon);
            dragon.tail_buffer.calculateChainSwingBuffer(90, 20, 5F, dragon);
            if (!dragon.isOnGround()) {
                dragon.roll_buffer.calculateChainFlapBuffer(55, 1, 2F, 0.5F, dragon);
                dragon.pitch_buffer.calculateChainWaveBuffer(90, 10, 1F, 0.5F, dragon);
                dragon.pitch_buffer_body.calculateChainWaveBuffer(80, 10, 1, 0.5F, dragon);
            }
        }
        if (dragon.walkCycle < 39) {
            dragon.walkCycle++;
        } else {
            dragon.walkCycle = 0;
        }
        if (dragon.getAnimation() == EntityDragonBase.ANIMATION_WINGBLAST && (dragon.getAnimationTick() == 17 || dragon.getAnimationTick() == 22 || dragon.getAnimationTick() == 28)) {
            dragon.spawnGroundEffects();
        }
        dragon.legSolver.update(dragon, dragon.getRenderSize() / 3F);

        if (dragon.flightCycle == 11) {
            dragon.spawnGroundEffects();
        }
        if (dragon.isModelDead() && dragon.flightCycle != 0) {
            dragon.flightCycle = 0;
        }
    }

    /*
    logic done on server and client on parallel.
    */
    public void updateDragonCommon() {
        if (dragon.isBreathingFire()) {
            dragon.fireTicks++;
            if (dragon.burnProgress < 40) {
                dragon.burnProgress++;
            }
        } else {
            dragon.burnProgress = 0;
        }

        if (dragon.flightCycle == 2) {
            if (!dragon.isDiving() && (dragon.isFlying() || dragon.isHovering())) {
                float dragonSoundVolume = IafConfig.dragonFlapNoiseDistance;
                float dragonSoundPitch = dragon.getSoundPitch();
                dragon.playSound(IafSoundRegistry.DRAGON_FLIGHT, dragonSoundVolume, dragonSoundPitch);
            }
        }
        if (dragon.flightCycle < 58) {
            dragon.flightCycle += 2;
        } else {
            dragon.flightCycle = 0;
        }

        final boolean flying = dragon.isFlying();
        if (flying) {
            if (dragon.flyProgress < 20.0F)
                dragon.flyProgress += 0.5F;
        } else {
            if (dragon.flyProgress > 0.0F)
                dragon.flyProgress -= 2F;
        }

        final boolean sleeping = dragon.isSleeping() && !dragon.isHovering() && !flying;
        if (sleeping) {
            if (dragon.sleepProgress < 20.0F)
                dragon.sleepProgress += 0.5F;
        } else {
            if (dragon.sleepProgress > 0.0F)
                dragon.sleepProgress -= 0.5F;
        }

        final boolean sitting = dragon.isSitting() && !dragon.isModelDead() && !sleeping;
        if (sitting) {
            if (dragon.sitProgress < 20.0F)
                dragon.sitProgress += 0.5F;
        } else {
            if (dragon.sitProgress > 0.0F)
                dragon.sitProgress -= 0.5F;
        }

        final boolean fireBreathing = dragon.isBreathingFire();
        dragon.prevFireBreathProgress = dragon.fireBreathProgress;
        if (fireBreathing) {
            if (dragon.fireBreathProgress < 10.0F)
                dragon.fireBreathProgress += 0.5F;
        } else {
            if (dragon.fireBreathProgress > 0.0F)
                dragon.fireBreathProgress -= 0.5F;
        }

        final boolean hovering = dragon.isHovering() || dragon.isFlying() && dragon.airAttack == IafDragonAttacks.Air.HOVER_BLAST && dragon.getTarget() != null && dragon.distanceTo(dragon.getTarget()) < 17F;
        if (hovering) {
            if (dragon.hoverProgress < 20.0F)
                dragon.hoverProgress += 0.5F;
        } else {
            if (dragon.hoverProgress > 0.0F)
                dragon.hoverProgress -= 2F;
        }

        final boolean diving = dragon.isDiving();
        if (diving) {
            if (dragon.diveProgress < 10.0F)
                dragon.diveProgress += 1F;
        } else {
            if (dragon.diveProgress > 0.0F)
                dragon.diveProgress -= 2F;
        }

        final boolean tackling = dragon.isTackling() && dragon.isOverAir();
        if (tackling) {
            if (dragon.tackleProgress < 5F)
                dragon.tackleProgress += 0.5F;
        } else {
            if (dragon.tackleProgress > 0.0F)
                dragon.tackleProgress -= 1.5F;
        }

        final boolean modelDead = dragon.isModelDead();
        if (modelDead) {
            if (dragon.modelDeadProgress < 20.0F)
                dragon.modelDeadProgress += 0.5F;
        } else {
            if (dragon.modelDeadProgress > 0.0F)
                dragon.modelDeadProgress -= 0.5F;
        }

        final boolean riding = dragon.hasVehicle() && dragon.getVehicle() != null && dragon.getVehicle() instanceof PlayerEntity;
        if (riding) {
            if (dragon.ridingProgress < 20.0F)
                dragon.ridingProgress += 0.5F;
        } else {
            if (dragon.ridingProgress > 0.0F)
                dragon.ridingProgress -= 0.5F;
        }

        if (dragon.hasHadHornUse) {
            dragon.hasHadHornUse = false;
        }

        if ((dragon.groundAttack == IafDragonAttacks.Ground.FIRE) && dragon.getDragonStage() < 2) {
            if (dragon.getWorld().isClient) {
                dragon.spawnBabyParticles();
            }
            dragon.randomizeAttacks();
        }
    }


    /*
    logic handler for the dragon's melee attacks.
    */
    public void updateDragonAttack() {
        PlayerEntity ridingPlayer = dragon.getRidingPlayer();
        if (dragon.isPlayingAttackAnimation() && dragon.getTarget() != null && dragon.canSee(dragon.getTarget())) {
            LivingEntity target = dragon.getTarget();
            final double dist = dragon.distanceTo(target);
            if (dist < dragon.getRenderSize() * 0.2574 * 2 + 2) {
                if (dragon.getAnimation() == EntityDragonBase.ANIMATION_BITE) {
                    if (dragon.getAnimationTick() > 15 && dragon.getAnimationTick() < 25) {
                        attackTarget(target, ridingPlayer, (int) dragon.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue());
                        dragon.usingGroundAttack = dragon.getRandom().nextBoolean();
                        dragon.randomizeAttacks();
                    }
                } else if (dragon.getAnimation() == EntityDragonBase.ANIMATION_TAILWHACK) {
                    if (dragon.getAnimationTick() > 20 && dragon.getAnimationTick() < 30) {
                        attackTarget(target, ridingPlayer, (int) dragon.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue());
                        target.takeKnockback(dragon.getDragonStage() * 0.6F, MathHelper.sin(dragon.getYaw() * 0.017453292F), -MathHelper.cos(dragon.getYaw() * 0.017453292F));
                        dragon.usingGroundAttack = dragon.getRandom().nextBoolean();
                        dragon.randomizeAttacks();
                    }
                } else if (dragon.getAnimation() == EntityDragonBase.ANIMATION_WINGBLAST) {
                    if ((dragon.getAnimationTick() == 15 || dragon.getAnimationTick() == 25 || dragon.getAnimationTick() == 35)) {
                        attackTarget(target, ridingPlayer, (int) dragon.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue());
                        target.takeKnockback(dragon.getDragonStage() * 0.6F, MathHelper.sin(dragon.getYaw() * 0.017453292F), -MathHelper.cos(dragon.getYaw() * 0.017453292F));
                        dragon.usingGroundAttack = dragon.getRandom().nextBoolean();
                        dragon.randomizeAttacks();
                    }
                }
            }
        }
    }

    public void debug() {
        String side = dragon.getWorld().isClient ? "CLIENT" : "SERVER";
        String owner = dragon.getOwner() == null ? "null" : dragon.getOwner().getName().getString();
        String attackTarget = dragon.getTarget() == null ? "null" : dragon.getTarget().getName().getString();
        IceAndFire.LOGGER.warn("DRAGON DEBUG[" + side + "]:"
                + "\nStage: " + dragon.getDragonStage()
                + "\nAge: " + dragon.getAgeInDays()
                + "\nVariant: " + dragon.getVariantName(dragon.getVariant())
                + "\nOwner: " + owner
                + "\nAttack Target: " + attackTarget
                + "\nFlying: " + dragon.isFlying()
                + "\nHovering: " + dragon.isHovering()
                + "\nHovering Time: " + dragon.hoverTicks
                + "\nWidth: " + dragon.getWidth()
                + "\nMoveHelper: " + dragon.getMoveControl()
                + "\nGround Attack: " + dragon.groundAttack
                + "\nAir Attack: " + dragon.airAttack
                + "\nTackling: " + dragon.isTackling()

        );
    }

    public void debugPathfinder(Path currentPath) {
        if (IceAndFire.DEBUG) {
            try {
                for (int i = 0; i < currentPath.getLength(); i++) {
                    final PathNode point = currentPath.getNode(i);
                    IceAndFire.sendMSGToAll(new MessageSpawnParticleAt(point.x, point.y, point.z, 2));
                }
                if (currentPath.getCurrentNodePos() != null) {
                    final Vec3d point = Vec3d.ofCenter(currentPath.getCurrentNodePos());
                    IceAndFire.sendMSGToAll(new MessageSpawnParticleAt(point.x, point.y, point.z, 1));

                }
            } catch (Exception e) {
                //Pathfinders are always unfriendly.
            }

        }
    }
}