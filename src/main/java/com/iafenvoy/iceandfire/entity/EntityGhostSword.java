package com.iafenvoy.iceandfire.entity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;

public class EntityGhostSword extends PersistentProjectileEntity {

    private IntOpenHashSet piercedEntities;
    private List<Entity> hitEntities;
    private int knockbackStrength;

    public EntityGhostSword(EntityType<? extends PersistentProjectileEntity> type, World worldIn) {
        super(type, worldIn);
        this.setDamage(9F);
    }

    public EntityGhostSword(EntityType<? extends PersistentProjectileEntity> type, World worldIn, double x, double y, double z,
                            float r, float g, float b) {
        this(type, worldIn);
        this.setPosition(x, y, z);
        this.setDamage(9F);
    }

    public EntityGhostSword(EntityType<? extends PersistentProjectileEntity> type, World worldIn, LivingEntity shooter,
                            double dmg) {
        super(type, shooter, worldIn);
        this.setDamage(dmg);
    }

    @Override
    public boolean isTouchingWater() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.noClip = true;
        float sqrt = MathHelper.sqrt((float) (this.getVelocity().x * this.getVelocity().x + this.getVelocity().z * this.getVelocity().z));
        if (sqrt < 0.1F && this.age > 200)
            this.remove(RemovalReason.DISCARDED);
        double d0 = 0;
        double d1 = 0.0D;
        double d2 = 0.01D;
        double x = this.getX() + this.random.nextFloat() * this.getWidth() * 2.0F - this.getWidth();
        double y = this.getY() + this.random.nextFloat() * this.getHeight() - this.getHeight();
        double z = this.getZ() + this.random.nextFloat() * this.getWidth() * 2.0F - this.getWidth();
        float f = (this.getWidth() + this.getHeight() + this.getWidth()) * 0.333F + 0.5F;
        if (this.particleDistSq(x, y, z) < f * f)
            this.getWorld().addParticle(ParticleTypes.SNEEZE, x, y + 0.5D, z, d0, d1, d2);
        Vec3d vector3d = this.getVelocity();
        double f3 = vector3d.horizontalLength();
        this.setYaw((float) (MathHelper.atan2(vector3d.x, vector3d.z) * (180F / (float) Math.PI)));
        this.setPitch((float) (MathHelper.atan2(vector3d.y, f3) * (180F / (float) Math.PI)));
        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();
        Vec3d vector3d2 = this.getPos();
        Vec3d vector3d3 = vector3d2.add(vector3d);
        HitResult raytraceresult = this.getWorld().raycast(new RaycastContext(vector3d2, vector3d3, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
        if (raytraceresult.getType() != HitResult.Type.MISS)
            vector3d3 = raytraceresult.getPos();
        while (!this.isRemoved()) {
            EntityHitResult entityraytraceresult = this.getEntityCollision(vector3d2, vector3d3);
            if (entityraytraceresult != null)
                raytraceresult = entityraytraceresult;
            if (raytraceresult != null && raytraceresult.getType() == HitResult.Type.ENTITY) {
                assert raytraceresult instanceof EntityHitResult;
                Entity entity = ((EntityHitResult) raytraceresult).getEntity();
                Entity entity1 = this.getOwner();
                if (entity instanceof PlayerEntity && entity1 instanceof PlayerEntity && !((PlayerEntity) entity1).shouldDamagePlayer((PlayerEntity) entity)) {
                    raytraceresult = null;
                    entityraytraceresult = null;
                }
            }

            if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS) {
                if (raytraceresult.getType() != HitResult.Type.BLOCK)
                    this.onCollision(raytraceresult);
                this.velocityDirty = true;
            }
            if (entityraytraceresult == null || this.getPierceLevel() <= 0)
                break;
            raytraceresult = null;
        }
    }

    public double particleDistSq(double toX, double toY, double toZ) {
        double d0 = this.getX() - toX;
        double d1 = this.getY() - toY;
        double d2 = this.getZ() - toZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (!this.isSilent() && soundIn != SoundEvents.ENTITY_ARROW_HIT && soundIn != SoundEvents.ENTITY_ARROW_HIT_PLAYER) {
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), soundIn, this.getSoundCategory(), volume, pitch);
        }
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public void setPunch(int knockbackStrengthIn) {
        this.knockbackStrength = knockbackStrengthIn;
    }

    @Override
    protected void onEntityHit(EntityHitResult result) {
        Entity entity = result.getEntity();
        float f = (float) this.getVelocity().length();
        int i = MathHelper.ceil(Math.max(f * this.getDamage(), 0.0D));
        if (this.getPierceLevel() > 0) {
            if (this.piercedEntities == null) {
                this.piercedEntities = new IntOpenHashSet(5);
            }

            if (this.hitEntities == null) {
                this.hitEntities = Lists.newArrayListWithCapacity(5);
            }

            if (this.piercedEntities.size() >= this.getPierceLevel() + 1) {
                this.remove(RemovalReason.DISCARDED);
                return;
            }

            this.piercedEntities.add(entity.getId());
        }

        if (this.isCritical()) {
            i += this.random.nextInt(i / 2 + 2);
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.getWorld().getDamageSources().magic();

        if (entity1 != null) {
            if (entity1 instanceof LivingEntity) {
                damagesource = this.getWorld().getDamageSources().indirectMagic(this, entity1);
                ((LivingEntity) entity1).onAttacking(entity);
            }
        }

        boolean flag = entity.getType() == EntityType.ENDERMAN;
        int j = entity.getFireTicks();
        if (this.isOnFire() && !flag) {
            entity.setOnFireFor(5);
        }

        if (entity.damage(damagesource, i)) {
            if (flag) {
                return;
            }

            if (entity instanceof LivingEntity livingentity) {

                if (this.knockbackStrength > 0) {
                    Vec3d vec3d = this.getVelocity().multiply(1.0D, 0.0D, 1.0D).normalize()
                            .multiply(this.knockbackStrength * 0.6D);
                    if (vec3d.lengthSquared() > 0.0D) {
                        livingentity.addVelocity(vec3d.x, 0.1D, vec3d.z);
                    }
                }

                this.onHit(livingentity);
                if (livingentity != entity1 && livingentity instanceof PlayerEntity && entity1 instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) entity1).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, 0.0F));
                }

                if (!entity.isAlive() && this.hitEntities != null) {
                    this.hitEntities.add(livingentity);
                }

            }

            this.playSound(this.getSound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            if (this.getPierceLevel() <= 0) {
                this.remove(RemovalReason.DISCARDED);
            }
        } else {
            this.setVelocity(this.getVelocity().multiply(-0.1D));
            //this.ticksInAir = 0;
            if (!this.getWorld().isClient && this.getVelocity().lengthSquared() < 1.0E-7D) {
                this.remove(RemovalReason.DISCARDED);
            }
        }

    }
}
