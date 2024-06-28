package com.iafenvoy.iceandfire.entity.util.dragon;

import com.google.common.base.Predicate;
import com.iafenvoy.iceandfire.config.IafConfig;
import com.iafenvoy.iceandfire.entity.*;
import com.iafenvoy.iceandfire.entity.util.IDeadMob;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import com.iafenvoy.iceandfire.registry.tag.IafBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

import java.util.List;

public class DragonUtils {
    public static BlockPos getBlockInViewEscort(EntityDragonBase dragon) {
        BlockPos escortPos = dragon.getEscortPosition();
        BlockPos ground = dragon.getWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, escortPos);
        int distFromGround = escortPos.getY() - ground.getY();
        for (int i = 0; i < 10; i++) {
            BlockPos pos = new BlockPos(escortPos.getX() + dragon.getRandom().nextInt(IafConfig.getInstance().dragon.behaviour.wanderFromHomeDistance) - IafConfig.getInstance().dragon.behaviour.wanderFromHomeDistance / 2,
                    (distFromGround > 16 ? escortPos.getY() : escortPos.getY() + 8 + dragon.getRandom().nextInt(16)),
                    (escortPos.getZ() + dragon.getRandom().nextInt(IafConfig.getInstance().dragon.behaviour.wanderFromHomeDistance) - IafConfig.getInstance().dragon.behaviour.wanderFromHomeDistance / 2));
            if (dragon.getDistanceSquared(Vec3d.ofCenter(pos)) > 6 && !dragon.isTargetBlocked(Vec3d.ofCenter(pos)))
                return pos;
        }
        return null;
    }

    public static BlockPos getWaterBlockInViewEscort(EntityDragonBase dragon) {
        // In water escort
        BlockPos inWaterEscortPos = dragon.getEscortPosition();
        // We don't need to get too close
        if (Math.abs(dragon.getX() - inWaterEscortPos.getX()) < dragon.getBoundingBox().getXLength()
                && Math.abs(dragon.getZ() - inWaterEscortPos.getZ()) < dragon.getBoundingBox().getZLength())
            return dragon.getBlockPos();
        // Takes off if the escort position is no longer in water, mainly for using elytra to fly out of the water
        if (inWaterEscortPos.getY() - dragon.getY() > 8 + dragon.getYNavSize() && !dragon.getWorld().getFluidState(inWaterEscortPos.down()).isIn(FluidTags.WATER))
            dragon.setHovering(true);
        // Swim directly to the escort position
        return inWaterEscortPos;
    }

    public static BlockPos getBlockInView(EntityDragonBase dragon) {
        float radius = 12 * (0.7F * dragon.getRenderSize() / 3);
        float neg = dragon.getRandom().nextBoolean() ? 1 : -1;
        float renderYawOffset = dragon.bodyYaw;
        if (dragon.hasHomePosition && dragon.homePos != null) {
            BlockPos dragonPos = dragon.getBlockPos();
            BlockPos ground = dragon.getWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, dragonPos);
            int distFromGround = (int) dragon.getY() - ground.getY();
            for (int i = 0; i < 10; i++) {
                BlockPos homePos = dragon.homePos.getPosition();
                BlockPos pos = new BlockPos(homePos.getX() + dragon.getRandom().nextInt(IafConfig.getInstance().dragon.behaviour.wanderFromHomeDistance * 2) - IafConfig.getInstance().dragon.behaviour.wanderFromHomeDistance, (distFromGround > 16 ? (int) Math.min(IafConfig.getInstance().dragon.behaviour.maxFlight, dragon.getY() + dragon.getRandom().nextInt(16) - 8) : (int) dragon.getY() + dragon.getRandom().nextInt(16) + 1), (homePos.getZ() + dragon.getRandom().nextInt(IafConfig.getInstance().dragon.behaviour.wanderFromHomeDistance * 2) - IafConfig.getInstance().dragon.behaviour.wanderFromHomeDistance));
                if (dragon.getDistanceSquared(Vec3d.ofCenter(pos)) > 6 && !dragon.isTargetBlocked(Vec3d.ofCenter(pos)))
                    return pos;
            }
        }
        float angle = (0.01745329251F * renderYawOffset) + 3.15F + (dragon.getRandom().nextFloat() * neg);
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        BlockPos radialPos = BlockPos.ofFloored(dragon.getX() + extraX, 0, dragon.getZ() + extraZ);
        BlockPos ground = dragon.getWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, radialPos);
        int distFromGround = (int) dragon.getY() - ground.getY();
        BlockPos newPos = radialPos.up(distFromGround > 16 ? (int) Math.min(IafConfig.getInstance().dragon.behaviour.maxFlight, dragon.getY() + dragon.getRandom().nextInt(16) - 8) : (int) dragon.getY() + dragon.getRandom().nextInt(16) + 1);
        BlockPos pos = dragon.doesWantToLand() ? ground : newPos;
        if (dragon.getDistanceSquared(Vec3d.ofCenter(newPos)) > 6 && !dragon.isTargetBlocked(Vec3d.ofCenter(newPos)))
            return pos;
        return null;
    }

    public static BlockPos getWaterBlockInView(EntityDragonBase dragon) {
        float radius = 0.75F * (0.7F * dragon.getRenderSize() / 3) * -7 - dragon.getRandom().nextInt(dragon.getDragonStage() * 6);
        float neg = dragon.getRandom().nextBoolean() ? 1 : -1;
        float angle = (0.01745329251F * dragon.bodyYaw) + 3.15F + (dragon.getRandom().nextFloat() * neg);
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        BlockPos radialPos = BlockPos.ofFloored(dragon.getX() + extraX, 0, dragon.getZ() + extraZ);
        BlockPos ground = dragon.getWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, radialPos);
        int distFromGround = (int) dragon.getY() - ground.getY();
        BlockPos newPos = radialPos.up(distFromGround > 16 ? (int) Math.min(IafConfig.getInstance().dragon.behaviour.maxFlight, dragon.getY() + dragon.getRandom().nextInt(16) - 8) : (int) dragon.getY() + dragon.getRandom().nextInt(16) + 1);
        BlockPos pos = dragon.doesWantToLand() ? ground : newPos;
        BlockPos surface = dragon.getWorld().getFluidState(newPos.down(2)).isIn(FluidTags.WATER) ? newPos.down(dragon.getRandom().nextInt(10) + 1) : newPos;
        if (dragon.getDistanceSquared(Vec3d.ofCenter(surface)) > 6 && dragon.getWorld().getFluidState(surface).isIn(FluidTags.WATER))
            return surface;
        return null;
    }

    public static LivingEntity riderLookingAtEntity(LivingEntity dragon, LivingEntity rider, double dist) {
        Vec3d Vector3d = rider.getCameraPosVec(1.0F);
        Vec3d Vector3d1 = rider.getRotationVec(1.0F);
        Vec3d Vector3d2 = Vector3d.add(Vector3d1.x * dist, Vector3d1.y * dist, Vector3d1.z * dist);
        Entity pointedEntity = null;
        List<Entity> list = rider.getWorld().getOtherEntities(rider, rider.getBoundingBox().stretch(Vector3d1.x * dist, Vector3d1.y * dist, Vector3d1.z * dist).expand(1.0D, 1.0D, 1.0D), (Predicate<Entity>) entity -> {
            if (onSameTeam(dragon, entity)) return false;
            return entity != null && entity.canHit() && entity instanceof LivingEntity && !entity.isPartOf(dragon) && !entity.isTeammate(dragon) && (!(entity instanceof IDeadMob) || !((IDeadMob) entity).isMobDead());
        });
        double d2 = dist;
        for (Entity entity1 : list) {
            Box axisalignedbb = entity1.getBoundingBox().expand((double) entity1.getTargetingMargin() + 2F);
            Vec3d raytraceresult = axisalignedbb.raycast(Vector3d, Vector3d2).orElse(Vec3d.ZERO);

            if (axisalignedbb.contains(Vector3d)) {
                if (d2 >= 0.0D) {
                    pointedEntity = entity1;
                    d2 = 0.0D;
                }
            } else {
                double d3 = Vector3d.distanceTo(raytraceresult);
                if (d3 < d2 || d2 == 0.0D) {
                    if (entity1.getRootVehicle() == rider.getRootVehicle()) {
                        if (d2 == 0.0D) pointedEntity = entity1;
                    } else {
                        pointedEntity = entity1;
                        d2 = d3;
                    }
                }
            }
        }
        return (LivingEntity) pointedEntity;
    }

    public static BlockPos getBlockInViewHippogryph(EntityHippogryph hippo, float yawAddition) {
        float radius = 0.75F * (0.7F * 8) * -3 - hippo.getRandom().nextInt(48);
        float neg = hippo.getRandom().nextBoolean() ? 1 : -1;
        float angle = (0.01745329251F * (hippo.bodyYaw + yawAddition)) + 3.15F + (hippo.getRandom().nextFloat() * neg);
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        if (hippo.hasHomePosition && hippo.homePos != null) {
            BlockPos dragonPos = hippo.getBlockPos();
            BlockPos ground = hippo.getWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, dragonPos);
            int distFromGround = (int) hippo.getY() - ground.getY();
            for (int i = 0; i < 10; i++) {
                BlockPos pos = BlockPos.ofFloored(hippo.homePos.getX() + hippo.getRandom().nextInt(IafConfig.getInstance().dragon.behaviour.wanderFromHomeDistance) - IafConfig.getInstance().dragon.behaviour.wanderFromHomeDistance, (distFromGround > 16 ? (int) Math.min(IafConfig.getInstance().dragon.behaviour.maxFlight, hippo.getY() + hippo.getRandom().nextInt(16) - 8) : (int) hippo.getY() + hippo.getRandom().nextInt(16) + 1), (hippo.homePos.getZ() + hippo.getRandom().nextInt(IafConfig.getInstance().dragon.behaviour.wanderFromHomeDistance * 2) - IafConfig.getInstance().dragon.behaviour.wanderFromHomeDistance));
                if (hippo.getDistanceSquared(Vec3d.ofCenter(pos)) > 6 && !hippo.isTargetBlocked(Vec3d.ofCenter(pos)))
                    return pos;
            }
        }
        BlockPos radialPos = BlockPos.ofFloored(hippo.getX() + extraX, 0, hippo.getZ() + extraZ);
        BlockPos ground = hippo.getWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, radialPos);
        int distFromGround = (int) hippo.getY() - ground.getY();
        BlockPos newPos = radialPos.up(distFromGround > 16 ? (int) Math.min(IafConfig.getInstance().dragon.behaviour.maxFlight, hippo.getY() + hippo.getRandom().nextInt(16) - 8) : (int) hippo.getY() + hippo.getRandom().nextInt(16) + 1);
        if (!hippo.isTargetBlocked(Vec3d.ofCenter(newPos)) && hippo.getDistanceSquared(Vec3d.ofCenter(newPos)) > 6)
            return newPos;
        return null;
    }

    public static BlockPos getBlockInViewStymphalian(EntityStymphalianBird bird) {
        float radius = 0.75F * (0.7F * 6) * -3 - bird.getRandom().nextInt(24);
        float neg = bird.getRandom().nextBoolean() ? 1 : -1;
        float renderYawOffset = bird.flock != null && !bird.flock.isLeader(bird) ? getStymphalianFlockDirection(bird) : bird.bodyYaw;
        float angle = (0.01745329251F * renderYawOffset) + 3.15F + (bird.getRandom().nextFloat() * neg);
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        BlockPos radialPos = getStymphalianFearPos(bird, BlockPos.ofFloored(bird.getX() + extraX, 0, bird.getZ() + extraZ));
        BlockPos ground = bird.getWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, radialPos);
        int distFromGround = (int) bird.getY() - ground.getY();
        int flightHeight = Math.min(IafConfig.getInstance().stymphalianBird.flightHeight, ground.getY() + bird.getRandom().nextInt(16));
        BlockPos newPos = radialPos.up(distFromGround > 16 ? flightHeight : (int) bird.getY() + bird.getRandom().nextInt(16) + 1);
        // FIXME :: Unused
//        BlockPos pos = bird.doesWantToLand() ? ground : newPos;
        if (bird.getDistanceSquared(Vec3d.ofCenter(newPos)) > 6 && !bird.isTargetBlocked(Vec3d.ofCenter(newPos)))
            return newPos;
        return null;
    }

    private static BlockPos getStymphalianFearPos(EntityStymphalianBird bird, BlockPos fallback) {
        if (bird.getVictor() != null && bird.getVictor() instanceof PathAwareEntity) {
            Vec3d Vector3d = NoPenaltyTargeting.findFrom((PathAwareEntity) bird.getVictor(), 16, IafConfig.getInstance().stymphalianBird.flightHeight, new Vec3d(bird.getVictor().getX(), bird.getVictor().getY(), bird.getVictor().getZ()));
            if (Vector3d != null) {
                BlockPos pos = BlockPos.ofFloored(Vector3d);
                return new BlockPos(pos.getX(), 0, pos.getZ());
            }
        }
        return fallback;
    }

    private static float getStymphalianFlockDirection(EntityStymphalianBird bird) {
        EntityStymphalianBird leader = bird.flock.getLeader();
        if (bird.squaredDistanceTo(leader) > 2) {
            double d0 = leader.getX() - bird.getX();
            double d2 = leader.getZ() - bird.getZ();
            float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            float degrees = MathHelper.wrapDegrees(f - bird.getYaw());

            return bird.getYaw() + degrees;
        } else return leader.bodyYaw;
    }

    public static BlockPos getBlockInTargetsViewCockatrice(EntityCockatrice cockatrice, LivingEntity target) {
        float radius = 10 + cockatrice.getRandom().nextInt(10);
        float angle = (0.01745329251F * target.headYaw);
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        BlockPos radialPos = BlockPos.ofFloored(target.getX() + extraX, 0, target.getZ() + extraZ);
        BlockPos ground = target.getWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, radialPos);
        if (cockatrice.squaredDistanceTo(Vec3d.ofCenter(ground)) > 30 && !cockatrice.isTargetBlocked(Vec3d.ofCenter(ground)))
            return ground;
        return target.getBlockPos();
    }


    public static BlockPos getBlockInTargetsViewGhost(EntityGhost ghost, LivingEntity target) {
        float radius = 4 + ghost.getRandom().nextInt(5);
        float angle = (0.01745329251F * (target.headYaw + 90F + ghost.getRandom().nextInt(180)));
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        if (ghost.squaredDistanceTo(Vec3d.ofCenter(BlockPos.ofFloored(target.getX() + extraX, target.getY(), target.getZ() + extraZ))) > 30)
            return BlockPos.ofFloored(target.getX() + extraX, target.getY(), target.getZ() + extraZ);
        return ghost.getBlockPos();
    }

    public static BlockPos getBlockInTargetsViewGorgon(EntityGorgon cockatrice, LivingEntity target) {
        float radius = 6;
        float angle = (0.01745329251F * target.headYaw);
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        BlockPos radialPos = BlockPos.ofFloored(target.getX() + extraX, target.getY(), target.getZ() + extraZ);
        if (cockatrice.squaredDistanceTo(Vec3d.ofCenter(radialPos)) < 300 && !cockatrice.isTargetBlocked(Vec3d.ofCenter(radialPos).add(0, 0.75, 0)))
            return radialPos;
        return target.getBlockPos();
    }


    public static BlockPos getBlockInTargetsViewSeaSerpent(EntitySeaSerpent serpent, LivingEntity target) {
        float radius = 10 * serpent.getSeaSerpentScale() + serpent.getRandom().nextInt(10);
        float angle = (0.01745329251F * target.headYaw);
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        BlockPos radialPos = BlockPos.ofFloored(target.getX() + extraX, 0, target.getZ() + extraZ);
        BlockPos ground = target.getWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, radialPos);
        if (serpent.squaredDistanceTo(Vec3d.ofCenter(ground)) > 30)
            return ground;
        return target.getBlockPos();
    }

    public static boolean canTameDragonAttack(TameableEntity dragon, Entity entity) {
        if (isVillager(entity)) return false;
        if (entity instanceof MerchantEntity || entity instanceof GolemEntity || entity instanceof PlayerEntity)
            return false;
        if (entity instanceof TameableEntity tameable) return !tameable.isTamed();
        return true;
    }

    public static boolean isVillager(Entity entity) {
        return false;
    }

    public static boolean isAnimaniaMob(Entity entity) {
        return false;
    }

    public static boolean isDragonTargetable(Entity entity, TagKey<EntityType<?>> tag) {
        return entity.getType().isIn(tag);
    }

    public static String getDimensionName(World world) {
        return world.getRegistryKey().getValue().toString();
    }

    public static boolean isInHomeDimension(EntityDragonBase dragonBase) {
        return (dragonBase.getHomeDimensionName() == null || getDimensionName(dragonBase.getWorld()).equals(dragonBase.getHomeDimensionName()));
    }

    public static boolean canDragonBreak(final BlockState state, final Entity entity) {
        if (!entity.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) return false;
        Block block = state.getBlock();
        return block.getBlastResistance() < 1200 && !state.isIn(IafBlockTags.DRAGON_BLOCK_BREAK_BLACKLIST);
    }

    public static boolean hasSameOwner(TameableEntity cockatrice, Entity entity) {
        if (entity instanceof TameableEntity tameable)
            return tameable.getOwnerUuid() != null && cockatrice.getOwnerUuid() != null && tameable.getOwnerUuid().equals(cockatrice.getOwnerUuid());
        return false;
    }

    public static boolean isAlive(final LivingEntity entity) {
        if (entity instanceof EntityDragonBase dragon && dragon.isMobDead())
            return false;
        return (!(entity instanceof IDeadMob deadMob) || !deadMob.isMobDead()) && !EntityGorgon.isStoneMob(entity);
    }


    public static boolean canGrief(EntityDragonBase dragon) {
        if (dragon.isTamed() && !IafConfig.getInstance().dragon.behaviour.tamedGriefing) return false;
        return IafConfig.getInstance().dragon.behaviour.griefing < 2;

    }

    public static boolean canHostilesTarget(Entity entity) {
        if (entity instanceof PlayerEntity && (entity.getWorld().getDifficulty() == Difficulty.PEACEFUL || ((PlayerEntity) entity).isCreative()))
            return false;
        if (entity instanceof EntityDragonBase dragonBase && dragonBase.isMobDead())
            return false;
        else
            return entity instanceof LivingEntity livingEntity && isAlive(livingEntity);
    }

    public static boolean onSameTeam(Entity entity1, Entity entity2) {
        Entity owner1 = null;
        Entity owner2 = null;
        boolean def = entity1.isTeammate(entity2);
        if (entity1 instanceof TameableEntity tameable)
            owner1 = tameable.getOwner();
        if (entity2 instanceof TameableEntity tameable)
            owner2 = tameable.getOwner();
        if (entity1 instanceof EntityMutlipartPart mutlipartPart) {
            Entity multipart = mutlipartPart.getParent();
            if (multipart instanceof TameableEntity tameable)
                owner1 = tameable.getOwner();
        }
        if (entity2 instanceof EntityMutlipartPart mutlipartPart) {
            Entity multipart = mutlipartPart.getParent();
            if (multipart instanceof TameableEntity tameable)
                owner2 = tameable.getOwner();
        }
        if (owner1 != null && owner2 != null)
            return owner1.isPartOf(owner2);
        return def;
    }

    public static boolean isDreadBlock(BlockState state) {
        Block block = state.getBlock();
        return block == IafBlocks.DREAD_STONE || block == IafBlocks.DREAD_STONE_BRICKS || block == IafBlocks.DREAD_STONE_BRICKS_CHISELED ||
                block == IafBlocks.DREAD_STONE_BRICKS_CRACKED || block == IafBlocks.DREAD_STONE_BRICKS_MOSSY || block == IafBlocks.DREAD_STONE_TILE ||
                block == IafBlocks.DREAD_STONE_FACE || block == IafBlocks.DREAD_TORCH || block == IafBlocks.DREAD_STONE_BRICKS_STAIRS ||
                block == IafBlocks.DREAD_STONE_BRICKS_SLAB || block == IafBlocks.DREADWOOD_LOG ||
                block == IafBlocks.DREADWOOD_PLANKS || block == IafBlocks.DREADWOOD_PLANKS_LOCK || block == IafBlocks.DREAD_PORTAL ||
                block == IafBlocks.DREAD_SPAWNER;
    }
}
