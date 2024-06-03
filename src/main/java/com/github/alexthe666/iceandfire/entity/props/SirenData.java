package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.github.alexthe666.iceandfire.entity.util.IHearsSiren;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.UUID;


public class SirenData {
    public EntitySiren charmedBy;
    public int charmTime;
    public boolean isCharmed;

    private UUID charmedByUUID;
    private int charmedById;
    private boolean isInitialized;
    private boolean triggerClientUpdate;

    public void tickCharmed(final LivingEntity holder) {
        if (!(holder instanceof PlayerEntity || holder instanceof MerchantEntity || holder instanceof IHearsSiren)) {
            return;
        }

        if (!isInitialized) {
            initialize(holder.getWorld());
        }

        if (charmedBy == null) {
            return;
        }

        if (charmedBy.isActuallySinging()) {
            if (EntitySiren.isWearingEarplugs(holder) || charmTime > IafConfig.sirenMaxSingTime) {
                charmedBy.singCooldown = IafConfig.sirenTimeBetweenSongs;
                clearCharm();
                return;
            }

            if (!charmedBy.isAlive() || holder.distanceTo(charmedBy) > EntitySiren.SEARCH_RANGE * 2 || holder instanceof PlayerEntity player && (player.isCreative() || player.isSpectator())) {
                clearCharm();
                return;
            }

            if (holder.distanceTo(charmedBy) < 5) {
                charmedBy.singCooldown = IafConfig.sirenTimeBetweenSongs;
                charmedBy.setSinging(false);
                charmedBy.setTarget(holder);
                charmedBy.setAttacking(true);
                charmedBy.triggerOtherSirens(holder);
                clearCharm();
                return;
            }

            isCharmed = true;
            charmTime++;
            if (holder.getRandom().nextInt(7) == 0) {
                for (int i = 0; i < 5; i++) {
                    holder.getWorld().addParticle(ParticleTypes.HEART,
                            holder.getX() + ((holder.getRandom().nextDouble() - 0.5D) * 3),
                            holder.getY() + ((holder.getRandom().nextDouble() - 0.5D) * 3),
                            holder.getZ() + ((holder.getRandom().nextDouble() - 0.5D) * 3),
                            0, 0, 0);
                }
            }

            if (holder.horizontalCollision) {
                holder.setJumping(true);
            }

            double motionXAdd = (Math.signum(charmedBy.getX() - holder.getX()) * 0.5D - holder.getVelocity().x) * 0.100000000372529;
            double motionYAdd = (Math.signum(charmedBy.getY() - holder.getY() + 1) * 0.5D - holder.getVelocity().y) * 0.100000000372529;
            double motionZAdd = (Math.signum(charmedBy.getZ() - holder.getZ()) * 0.5D - holder.getVelocity().z) * 0.100000000372529;

            holder.setVelocity(holder.getVelocity().add(motionXAdd, motionYAdd, motionZAdd));

            if (holder.hasVehicle()) {
                holder.stopRiding();
            }

            if (!(holder instanceof PlayerEntity)) {
                double x = charmedBy.getX() - holder.getX();
                double y = charmedBy.getY() - 1 - holder.getY();
                double z = charmedBy.getZ() - holder.getZ();
                double radius = Math.sqrt(x * x + z * z);
                float xRot = (float) (-(MathHelper.atan2(y, radius) * (180D / Math.PI)));
                float yRot = (float) (MathHelper.atan2(z, x) * (180D / Math.PI)) - 90.0F;
                holder.setPitch(updateRotation(holder.getPitch(), xRot, 30F));
                holder.setYaw(updateRotation(holder.getYaw(), yRot, 30F));
            }
        }
    }

    public void setCharmed(final Entity entity) {
        if (!(entity instanceof EntitySiren siren)) {
            return;
        }

        charmedBy = siren;
        isCharmed = true;
        triggerClientUpdate = true;
    }

    public void clearCharm() {
        charmTime = 0;
        isCharmed = false;
        charmedBy = null;
        triggerClientUpdate = true;
    }

    public void serialize(final NbtCompound tag) {
        NbtCompound sirenData = new NbtCompound();

        if (charmedBy != null) {
            sirenData.put("charmedByUUID", NbtHelper.fromUuid(charmedBy.getUuid()));
            sirenData.putInt("charmedById", charmedBy.getId());
        } else {
            sirenData.putInt("charmedById", -1);
        }

        sirenData.putInt("charmTime", charmTime);
        sirenData.putBoolean("isCharmed", isCharmed);

        tag.put("sirenData", sirenData);
    }

    public void deserialize(final NbtCompound tag) {
        NbtCompound sirenData = tag.getCompound("sirenData");
        NbtElement uuidTag = sirenData.get("charmedByUUID");

        if (uuidTag != null) {
            charmedByUUID = NbtHelper.toUuid(uuidTag);
        }

        charmedById = sirenData.getInt("charmedById");
        charmTime = sirenData.getInt("charmTime");
        isCharmed = sirenData.getBoolean("isCharmed");
        isInitialized = false;
    }

    public boolean doesClientNeedUpdate() {
        if (triggerClientUpdate) {
            triggerClientUpdate = false;
            return true;
        }

        return false;
    }

    private float updateRotation(float angle, float targetAngle, float maxIncrease) {
        float f = MathHelper.wrapDegrees(targetAngle - angle);

        if (f > maxIncrease) {
            f = maxIncrease;
        }

        if (f < -maxIncrease) {
            f = -maxIncrease;
        }

        return angle + f;
    }

    private void initialize(final World level) {
        charmedBy = null;

        // Make sure server gets the new entity ids on re-join and syncs it to the client
        if (charmedByUUID != null && level instanceof ServerWorld serverLevel) {
            Entity entity = serverLevel.getEntity(charmedByUUID);

            if (entity instanceof EntitySiren siren) {
                triggerClientUpdate = true;
                charmedByUUID = null;
                charmedBy = siren;
            }
        } else if (charmedById != -1) {
            Entity entity = level.getEntityById(charmedById);

            if (entity instanceof EntitySiren siren) {
                charmedBy = siren;
            }
        }

        isInitialized = true;
    }
}
