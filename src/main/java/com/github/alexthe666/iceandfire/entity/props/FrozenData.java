package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;

public class FrozenData {
    public int frozenTicks;
    public boolean isFrozen;

    private boolean triggerClientUpdate;

    public void tickFrozen(final LivingEntity entity) {
        if (!isFrozen) {
            return;
        }

        if (entity instanceof EntityIceDragon) {
            clearFrozen(entity);
            return;
        }

        if (entity.isOnFire()) {
            clearFrozen(entity);
            entity.extinguish();
            return;
        }

        if (entity.isDead()) {
            clearFrozen(entity);
            return;
        }

        if (frozenTicks > 0) {
            frozenTicks--;
        } else {
            clearFrozen(entity);
        }

        if (isFrozen && !(entity instanceof PlayerEntity player && player.isCreative())) {
            entity.setVelocity(entity.getVelocity().multiply(0.25F, 1, 0.25F));

            if (!(entity instanceof EnderDragonEntity) && !entity.isOnGround()) {
                entity.setVelocity(entity.getVelocity().add(0, -0.2, 0));
            }
        }
    }

    public void setFrozen(final LivingEntity target, int duration) {
        if (!isFrozen) {
            target.playSound(SoundEvents.BLOCK_GLASS_PLACE, 1, 1);
        }

        frozenTicks = duration;
        isFrozen = true;
        triggerClientUpdate = true;
    }

    private void clearFrozen(final LivingEntity entity) {
        for (int i = 0; i < 15; i++) {
            entity.getWorld().addParticle(
                    new BlockStateParticleEffect(ParticleTypes.BLOCK,
                            IafBlockRegistry.DRAGON_ICE.get().getDefaultState()),
                    entity.getX() + ((entity.getRandom().nextDouble() - 0.5D) * entity.getWidth()),
                    entity.getY() + ((entity.getRandom().nextDouble()) * entity.getHeight()),
                    entity.getZ() + ((entity.getRandom().nextDouble() - 0.5D) * entity.getWidth()),
                    0, 0, 0);
        }

        entity.playSound(SoundEvents.BLOCK_GLASS_BREAK, 3, 1);

        isFrozen = false;
        frozenTicks = 0;
        triggerClientUpdate = true;
    }

    public void serialize(final NbtCompound tag) {
        NbtCompound frozenData = new NbtCompound();
        frozenData.putInt("frozenTicks", frozenTicks);
        frozenData.putBoolean("isFrozen", isFrozen);

        tag.put("frozenData", frozenData);
    }

    public void deserialize(final NbtCompound tag) {
        NbtCompound frozenData = tag.getCompound("frozenData");
        frozenTicks = frozenData.getInt("frozenTicks");
        isFrozen = frozenData.getBoolean("isFrozen");
    }

    public boolean doesClientNeedUpdate() {
        if (triggerClientUpdate) {
            triggerClientUpdate = false;
            return true;
        }

        return false;
    }
}
