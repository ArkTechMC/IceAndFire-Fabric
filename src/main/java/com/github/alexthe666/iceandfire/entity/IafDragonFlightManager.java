package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IFlyingMount;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;


public class IafDragonFlightManager {
    private final EntityDragonBase dragon;
    private Vec3d target;
    private IafDragonAttacks.Air prevAirAttack;
    private Vec3d startAttackVec;
    private Vec3d startPreyVec;
    private boolean hasStartedToScorch = false;
    private LivingEntity prevAttackTarget = null;

    public IafDragonFlightManager(EntityDragonBase dragon) {
        this.dragon = dragon;
    }

    public static float approach(float number, float max, float min) {
        min = Math.abs(min);
        return number < max ? MathHelper.clamp(number + min, number, max) : MathHelper.clamp(number - min, max, number);
    }

    public static float approachDegrees(float number, float max, float min) {
        float add = MathHelper.wrapDegrees(max - number);
        return approach(number, number + add, min);
    }

    public static float degreesDifferenceAbs(float f1, float f2) {
        return Math.abs(MathHelper.wrapDegrees(f2 - f1));
    }

    public void update() {

        if (dragon.getTarget() != null && dragon.getTarget().isAlive()) {
            if (dragon instanceof EntityIceDragon && dragon.isTouchingWater()) {
                if (dragon.getTarget() == null) {
                    dragon.airAttack = IafDragonAttacks.Air.SCORCH_STREAM;
                } else {
                    dragon.airAttack = IafDragonAttacks.Air.TACKLE;
                }
            }
            LivingEntity entity = dragon.getTarget();
            if (dragon.airAttack == IafDragonAttacks.Air.TACKLE) {
                target = new Vec3d(entity.getX(), entity.getY() + entity.getHeight(), entity.getZ());
            }
            if (dragon.airAttack == IafDragonAttacks.Air.HOVER_BLAST) {
                float distY = 5 + dragon.getDragonStage() * 2;
                int randomDist = 20;
                if (dragon.squaredDistanceTo(entity.getX(), dragon.getY(), entity.getZ()) < 16 || dragon.squaredDistanceTo(entity.getX(), dragon.getY(), entity.getZ()) > 900) {
                    target = new Vec3d(entity.getX() + dragon.getRandom().nextInt(randomDist) - randomDist / 2, entity.getY() + distY, entity.getZ() + dragon.getRandom().nextInt(randomDist) - randomDist / 2);
                }
                dragon.stimulateFire(entity.getX(), entity.getY(), entity.getZ(), 3);
            }
            if (dragon.airAttack == IafDragonAttacks.Air.SCORCH_STREAM && startPreyVec != null && startAttackVec != null) {
                float distX = (float) (startPreyVec.x - startAttackVec.x);
                float distY = 5 + dragon.getDragonStage() * 2;
                float distZ = (float) (startPreyVec.z - startAttackVec.z);
                target = new Vec3d(entity.getX() + distX, entity.getY() + distY, entity.getZ() + distZ);
                dragon.tryScorchTarget();
                hasStartedToScorch = true;
                if (target != null && dragon.squaredDistanceTo(target.x, target.y, target.z) < 100) {
                    target = new Vec3d(entity.getX() - distX, entity.getY() + distY, entity.getZ() - distZ);
                }
            }

        } else if (target == null || dragon.squaredDistanceTo(target.x, target.y, target.z) < 4
                || !dragon.getWorld().isAir(BlockPos.ofFloored(target.x, target.y, target.z))
                        && (dragon.isHovering() || dragon.isFlying())
                || dragon.getCommand() == 2 && dragon.shouldTPtoOwner()) {
            BlockPos viewBlock = null;

            if (dragon instanceof EntityIceDragon && dragon.isTouchingWater()) {
                viewBlock = DragonUtils.getWaterBlockInView(dragon);
            }
            if (dragon.getCommand() == 2 && dragon.useFlyingPathFinder()) {
                if (dragon instanceof EntityIceDragon && dragon.isTouchingWater()) {
                    viewBlock = DragonUtils.getWaterBlockInViewEscort(dragon);
                } else {
                    viewBlock = DragonUtils.getBlockInViewEscort(dragon);
                }
            } else if (dragon.lookingForRoostAIFlag) {
                // FIXME :: Unused
//                double xDist = Math.abs(dragon.getX() - dragon.getRestrictCenter().getX() - 0.5F);
//                double zDist = Math.abs(dragon.getZ() - dragon.getRestrictCenter().getZ() - 0.5F);
//                double xzDist = Math.sqrt(xDist * xDist + zDist * zDist);
                BlockPos upPos = dragon.getPositionTarget();
                if (dragon.getDistanceSquared(Vec3d.ofCenter(dragon.getPositionTarget())) > 200) {
                    upPos = upPos.up(30);
                }
                viewBlock = upPos;

            }else if(viewBlock == null){
                viewBlock = DragonUtils.getBlockInView(dragon);
                if (dragon.isTouchingWater()) {
                    // If the dragon is in water, take off to reach the air target
                    dragon.setHovering(true);
                }
            }
            if (viewBlock != null) {
                target = new Vec3d(viewBlock.getX() + 0.5, viewBlock.getY() + 0.5, viewBlock.getZ() + 0.5);
            }
        }
        if (target != null) {
            if (target.y > IafConfig.maxDragonFlight) {
                target = new Vec3d(target.x, IafConfig.maxDragonFlight, target.z);
            }
            if (target.y >= dragon.getY() && !dragon.isModelDead()) {
                dragon.setVelocity(dragon.getVelocity().add(0, 0.1D, 0));

            }
        }

        this.prevAirAttack = dragon.airAttack;
    }

    public Vec3d getFlightTarget() {
        return target == null ? Vec3d.ZERO : target;
    }

    public void setFlightTarget(Vec3d target) {
        this.target = target;
    }

    private float getDistanceXZ(double x, double z) {
        float f = (float) (dragon.getX() - x);
        float f2 = (float) (dragon.getZ() - z);
        return f * f + f2 * f2;
    }

    public void onSetAttackTarget(LivingEntity LivingEntityIn) {
        if (prevAttackTarget != LivingEntityIn) {
            if (LivingEntityIn != null) {
                startPreyVec = new Vec3d(LivingEntityIn.getX(), LivingEntityIn.getY(), LivingEntityIn.getZ());
            } else {
                startPreyVec = new Vec3d(dragon.getX(), dragon.getY(), dragon.getZ());
            }
            startAttackVec = new Vec3d(dragon.getX(), dragon.getY(), dragon.getZ());
        }
        prevAttackTarget = LivingEntityIn;
    }

    protected static class GroundMoveHelper extends MoveControl {
        public GroundMoveHelper(MobEntity LivingEntityIn) {
            super(LivingEntityIn);
        }

        public float distance(float rotateAngleFrom, float rotateAngleTo) {
            return (float) IAFMath.atan2_accurate(MathHelper.sin(rotateAngleTo - rotateAngleFrom), MathHelper.cos(rotateAngleTo - rotateAngleFrom));
        }

        @Override
        public void tick() {
            if (this.state == State.STRAFE) {
                float f = (float) this.entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getValue();
                float f1 = (float) this.speed * f;
                float f2 = this.forwardMovement;
                float f3 = this.sidewaysMovement;
                float f4 = MathHelper.sqrt(f2 * f2 + f3 * f3);

                if (f4 < 1.0F) {
                    f4 = 1.0F;
                }

                f4 = f1 / f4;
                f2 = f2 * f4;
                f3 = f3 * f4;
                float f5 = MathHelper.sin(this.entity.getYaw() * 0.017453292F);
                float f6 = MathHelper.cos(this.entity.getYaw() * 0.017453292F);
                float f7 = f2 * f6 - f3 * f5;
                float f8 = f3 * f6 + f2 * f5;
                EntityNavigation pathnavigate = this.entity.getNavigation();
                if (pathnavigate != null) {
                    PathNodeMaker nodeprocessor = pathnavigate.getNodeMaker();
                    if (nodeprocessor != null && nodeprocessor.getDefaultNodeType(this.entity.getWorld(), MathHelper.floor(this.entity.getX() + (double) f7), MathHelper.floor(this.entity.getY()), MathHelper.floor(this.entity.getZ() + (double) f8)) != PathNodeType.WALKABLE) {
                        this.forwardMovement = 1.0F;
                        this.sidewaysMovement = 0.0F;
                        f1 = f;
                    }
                }
                this.entity.setMovementSpeed(f1);
                this.entity.setForwardSpeed(this.forwardMovement);
                this.entity.setSidewaysSpeed(this.sidewaysMovement);
                this.state = State.WAIT;
            } else if (this.state == State.MOVE_TO) {
                this.state = State.WAIT;
                EntityDragonBase dragonBase = (EntityDragonBase) entity;
                double d0 = this.getTargetX() - this.entity.getX();
                double d1 = this.getTargetZ() - this.entity.getZ();
                double d2 = this.getTargetY() - this.entity.getY();
                double d3 = d0 * d0 + d2 * d2 + d1 * d1;

                if (d3 < 2.500000277905201E-7D) {
                    this.entity.setForwardSpeed(0.0F);
                    return;
                }
                float targetDegree = (float) (MathHelper.atan2(d1, d0) * (180D / Math.PI)) - 90.0F;
                float changeRange = 70F;
                if (Math.ceil(dragonBase.getWidth()) > 2F) {
                    float ageMod = 1F - Math.min(dragonBase.getAgeInDays(), 125) / 125F;
                    changeRange = 5 + ageMod * 10;
                }
                this.entity.setYaw(this.wrapDegrees(this.entity.getYaw(), targetDegree, changeRange));
                this.entity.setMovementSpeed((float) (this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));
                if (d2 > (double) this.entity.getStepHeight() && d0 * d0 + d1 * d1 < (double) Math.max(1.0F, this.entity.getWidth() / 2)) {
                    this.entity.getJumpControl().setActive();
                    this.state = State.JUMPING;
                }
            } else if (this.state == State.JUMPING) {
                this.entity.setMovementSpeed((float) (this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));

                if (this.entity.isOnGround()) {
                    this.state = State.WAIT;
                }
            } else {
                this.entity.setForwardSpeed(0.0F);
            }
        }

    }

    protected static class FlightMoveHelper extends MoveControl {

        private final EntityDragonBase dragon;

        protected FlightMoveHelper(EntityDragonBase dragonBase) {
            super(dragonBase);
            this.dragon = dragonBase;
        }

        @Override
        public void tick() {
            if (dragon.horizontalCollision) {
                dragon.setYaw(dragon.getYaw() + 180.0F);
                this.speed = 0.1F;
                dragon.flightManager.target = null;
                return;
            }
            float distX = (float) (dragon.flightManager.getFlightTarget().x - dragon.getX());
            float distY = (float) (dragon.flightManager.getFlightTarget().y - dragon.getY());
            float distZ = (float) (dragon.flightManager.getFlightTarget().z - dragon.getZ());
            double planeDist = Math.sqrt(distX * distX + distZ * distZ);
            double yDistMod = 1.0D - (double) MathHelper.abs(distY * 0.7F) / planeDist;
            distX = (float) ((double) distX * yDistMod);
            distZ = (float) ((double) distZ * yDistMod);
            planeDist = MathHelper.sqrt(distX * distX + distZ * distZ);
            double dist = Math.sqrt(distX * distX + distZ * distZ + distY * distY);
            if (dist > 1.0F) {
                float yawCopy = dragon.getYaw();
                float atan = (float) MathHelper.atan2(distZ, distX);
                float yawTurn = MathHelper.wrapDegrees(dragon.getYaw() + 90);
                float yawTurnAtan = MathHelper.wrapDegrees(atan * 57.295776F);
                dragon.setYaw(IafDragonFlightManager.approachDegrees(yawTurn, yawTurnAtan, dragon.airAttack == IafDragonAttacks.Air.TACKLE && dragon.getTarget() != null ? 10 : 4.0F) - 90.0F);
                dragon.bodyYaw = dragon.getYaw();
                if (IafDragonFlightManager.degreesDifferenceAbs(yawCopy, dragon.getYaw()) < 3.0F) {
                    speed = IafDragonFlightManager.approach((float) speed, 1.8F, 0.005F * (1.8F / (float) speed));
                } else {
                    speed = IafDragonFlightManager.approach((float) speed, 0.2F, 0.025F);
                    if (dist < 100D && dragon.getTarget() != null) {
                        speed = speed * (dist / 100D);
                    }
                }
                float finPitch = (float) (-(MathHelper.atan2(-distY, planeDist) * 57.2957763671875D));
                dragon.setPitch(finPitch);
                float yawTurnHead = dragon.getYaw() + 90.0F;
                speed *= dragon.getFlightSpeedModifier();
                speed *= Math.min(1, dist / 50 + 0.3);//Make the dragon fly slower when close to target
                double x = speed * MathHelper.cos(yawTurnHead * 0.017453292F) * Math.abs((double) distX / dist);
                double y = speed * MathHelper.sin(finPitch * 0.017453292F) * Math.abs((double) distY / dist);
                double z = speed * MathHelper.sin(yawTurnHead * 0.017453292F) * Math.abs((double) distZ / dist);
                double motionCap = 0.2D;
                dragon.setVelocity(dragon.getVelocity().add(Math.min(x * 0.2D, motionCap), Math.min(y * 0.2D, motionCap), Math.min(z * 0.2D, motionCap)));
            }
        }


    }

    protected static class PlayerFlightMoveHelper<T extends MobEntity & IFlyingMount> extends MoveControl {

        private final T dragon;

        public PlayerFlightMoveHelper(T dragon) {
            super(dragon);
            this.dragon = dragon;
        }

        @Override
        public void tick() {
            if (dragon instanceof EntityDragonBase theDragon && theDragon.getControllingPassenger() != null) {
                // New ride system doesn't need move controller
                // The flight move control is disabled here, the walking move controller will stay Operation.WAIT so nothing will happen too
                return;
            }

            double flySpeed = speed * speedMod() * 3;
            Vec3d dragonVec = dragon.getPos();
            Vec3d moveVec = new Vec3d(targetX, targetY, targetZ);
            Vec3d normalized = moveVec.subtract(dragonVec).normalize();
            double dist = dragonVec.distanceTo(moveVec);
            dragon.setVelocity(normalized.x * flySpeed, normalized.y * flySpeed, normalized.z * flySpeed);
            if (dist > 2.5E-7) {
                float yaw = (float) Math.toDegrees(Math.PI * 2 - Math.atan2(normalized.x, normalized.y));
                dragon.setYaw(wrapDegrees(dragon.getYaw(), yaw, 5));
                dragon.setMovementSpeed((float) (speed));
            }
            dragon.move(MovementType.SELF, dragon.getVelocity());
        }

        public double speedMod() {
            return (dragon instanceof EntityAmphithere ? 0.6D : 1.25D) * IafConfig.dragonFlightSpeedMod * dragon.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        }
    }
}
