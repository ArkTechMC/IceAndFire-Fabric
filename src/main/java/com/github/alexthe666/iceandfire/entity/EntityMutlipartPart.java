package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.message.MessageMultipartInteract;
import dev.arktechmc.iafextra.network.IafClientNetworkHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class EntityMutlipartPart extends Entity {

    private static final TrackedData<Optional<UUID>> PARENT_UUID = DataTracker.registerData(EntityMutlipartPart.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Float> SCALE_WIDTH = DataTracker.registerData(EntityMutlipartPart.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> SCALE_HEIGHT = DataTracker.registerData(EntityMutlipartPart.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> PART_YAW = DataTracker.registerData(EntityMutlipartPart.class, TrackedDataHandlerRegistry.FLOAT);
    public EntityDimensions multipartSize;
    protected float radius;
    protected float angleYaw;
    protected float offsetY;
    protected float damageMultiplier;

    protected EntityMutlipartPart(EntityType<?> t, World world) {
        super(t, world);
        this.multipartSize = t.getDimensions();
    }

    public EntityMutlipartPart(EntityType<?> t, Entity parent, float radius, float angleYaw, float offsetY, float sizeX,
                               float sizeY, float damageMultiplier) {
        super(t, parent.getWorld());
        this.setParent(parent);
        this.setScaleX(sizeX);
        this.setScaleY(sizeY);
        this.radius = radius;
        this.angleYaw = (angleYaw + 90.0F) * ((float) Math.PI / 180.0F);
        this.offsetY = offsetY;

        this.damageMultiplier = damageMultiplier;
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return MobEntity.createMobAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 2D)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1D);
    }

    public static boolean sharesRider(Entity parent, Entity entityIn) {
        for (Entity entity : parent.getPassengerList()) {
            if (entity.equals(entityIn)) {
                return true;
            }

            if (sharesRider(entity, entityIn)) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void readCustomDataFromNbt(@NotNull NbtCompound compound) {

    }

    @Override
    protected void writeCustomDataToNbt(@NotNull NbtCompound compound) {

    }

    @Override
    protected void onSwimmingStart() {

    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull EntityPose poseIn) {
        return new EntityDimensions(this.getScaleX(), this.getScaleY(), false);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(PARENT_UUID, Optional.empty());
        this.dataTracker.startTracking(SCALE_WIDTH, 0.5F);
        this.dataTracker.startTracking(SCALE_HEIGHT, 0.5F);
        this.dataTracker.startTracking(PART_YAW, 0F);
    }

    public UUID getParentId() {
        return this.dataTracker.get(PARENT_UUID).orElse(null);
    }

    public void setParentId(UUID uniqueId) {
        this.dataTracker.set(PARENT_UUID, Optional.ofNullable(uniqueId));
    }

    private float getScaleX() {
        return this.dataTracker.get(SCALE_WIDTH);
    }

    private void setScaleX(float scale) {
        this.dataTracker.set(SCALE_WIDTH, scale);
    }

    private float getScaleY() {
        return this.dataTracker.get(SCALE_HEIGHT);
    }

    private void setScaleY(float scale) {
        this.dataTracker.set(SCALE_HEIGHT, scale);
    }

    public float getPartYaw() {
        return this.dataTracker.get(PART_YAW);
    }

    private void setPartYaw(float yaw) {
        this.dataTracker.set(PART_YAW, yaw % 360);
    }

    @Override
    public void tick() {
        this.touchingWater = false;
        if (this.age > 10) {
            Entity parent = this.getParent();
            this.calculateDimensions();
            if (parent != null && !this.getWorld().isClient) {
                float renderYawOffset = parent.getYaw();
                if (parent instanceof LivingEntity) {
                    renderYawOffset = ((LivingEntity) parent).bodyYaw;
                }
                if (this.isSlowFollow()) {
                    this.setPosition(parent.prevX + this.radius * MathHelper.cos((float) (renderYawOffset * (Math.PI / 180.0F) + this.angleYaw)), parent.prevY + this.offsetY, parent.prevZ + this.radius * MathHelper.sin((float) (renderYawOffset * (Math.PI / 180.0F) + this.angleYaw)));
                    double d0 = parent.getX() - this.getX();
                    double d1 = parent.getY() - this.getY();
                    double d2 = parent.getZ() - this.getZ();
                    float f2 = -((float) (MathHelper.atan2(d1, MathHelper.sqrt((float) (d0 * d0 + d2 * d2))) * (180F / (float) Math.PI)));
                    this.setPitch(this.limitAngle(this.getPitch(), f2, 5.0F));
                    this.scheduleVelocityUpdate();
                    this.setYaw(renderYawOffset);
                    this.setPartYaw(this.getYaw());
                    if (!this.getWorld().isClient) {
                        this.collideWithNearbyEntities();
                    }
                } else {
                    this.setPosition(parent.getX() + this.radius * MathHelper.cos((float) (renderYawOffset * (Math.PI / 180.0F) + this.angleYaw)), parent.getY() + this.offsetY, parent.getZ() + this.radius * MathHelper.sin((float) (renderYawOffset * (Math.PI / 180.0F) + this.angleYaw)));
                    this.scheduleVelocityUpdate();
                }
                if (!this.getWorld().isClient) {
                    this.collideWithNearbyEntities();
                }
                if (parent.isRemoved() && !this.getWorld().isClient) {
                    this.remove(RemovalReason.DISCARDED);
                }
            } else if (this.age > 20 && !this.getWorld().isClient) {
                this.remove(RemovalReason.DISCARDED);
            }
        }
        super.tick();
    }

    protected boolean isSlowFollow() {
        return false;
    }

    protected float limitAngle(float sourceAngle, float targetAngle, float maximumChange) {
        float f = MathHelper.wrapDegrees(targetAngle - sourceAngle);
        if (f > maximumChange) {
            f = maximumChange;
        }

        if (f < -maximumChange) {
            f = -maximumChange;
        }

        float f1 = sourceAngle + f;
        if (f1 < 0.0F) {
            f1 += 360.0F;
        } else if (f1 > 360.0F) {
            f1 -= 360.0F;
        }

        return f1;
    }

    @Override
    public void remove(@NotNull RemovalReason reason) {
        super.remove(RemovalReason.DISCARDED);
    }

    public Entity getParent() {
        UUID id = this.getParentId();

        if (id != null && this.getWorld() instanceof ServerWorld serverLevel) {
            return serverLevel.getEntity(id);
        }

        return null;
    }

    public void setParent(Entity entity) {
        this.setParentId(entity.getUuid());
    }

    @Override
    public boolean isPartOf(@NotNull Entity entity) {
        return this == entity || this.getParent() == entity;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    public void collideWithNearbyEntities() {
        List<Entity> entities = this.getWorld().getOtherEntities(this, this.getBoundingBox().stretch(0.20000000298023224D, 0.0D, 0.20000000298023224D));
        Entity parent = this.getParent();
        if (parent != null) {
            entities.stream().filter(entity -> entity != parent && !sharesRider(parent, entity) && !(entity instanceof EntityMutlipartPart) && entity.isPushable()).forEach(entity -> entity.pushAwayFrom(parent));

        }
    }

    @Override
    public @NotNull ActionResult interact(@NotNull PlayerEntity player, @NotNull Hand hand) {
        Entity parent = this.getParent();
        if (this.getWorld().isClient && parent != null) {
            IafClientNetworkHandler.send(new MessageMultipartInteract(parent.getId(), 0));
        }
        return parent != null ? parent.interact(player, hand) : ActionResult.PASS;
    }

    @Override
    public boolean damage(DamageSource source, float damage) {
        Entity parent = this.getParent();
        if (this.getWorld().isClient && source.getAttacker() instanceof PlayerEntity && parent != null) {
            IafClientNetworkHandler.send(new MessageMultipartInteract(parent.getId(), damage * this.damageMultiplier));
        }
        return parent != null && parent.damage(source, damage * this.damageMultiplier);
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        return source.isOf(DamageTypes.FALL) || source.isOf(DamageTypes.DROWN) || source.isOf(DamageTypes.IN_WALL) || source.isOf(DamageTypes.FALLING_BLOCK) || source.isOf(DamageTypes.LAVA) || source.isIn(DamageTypeTags.IS_FIRE) || super.isInvulnerableTo(source);
    }

    public boolean shouldContinuePersisting() {
        return this.getWorld() != null || this.isRemoved();
    }
}
