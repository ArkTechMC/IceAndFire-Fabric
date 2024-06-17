package com.github.alexthe666.iceandfire.entity.data;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.github.alexthe666.iceandfire.registry.IafItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;

public class ChickenData {
    public int timeUntilNextEgg = -1;

    public void tickChicken(final LivingEntity entity) {
        if (!IafConfig.chickensLayRottenEggs || entity.getWorld().isClient() || !ServerEvents.isChicken(entity) || entity.isBaby())
            return;

        if (this.timeUntilNextEgg == -1)
            this.timeUntilNextEgg = this.createDefaultTime(entity.getRandom());

        if (this.timeUntilNextEgg == 0) {
            if (entity.age > 30 && entity.getRandom().nextInt(IafConfig.cockatriceEggChance + 1) == 0) {
                entity.playSound(SoundEvents.ENTITY_CHICKEN_HURT, 2.0F, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2F + 1.0F);
                entity.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2F + 1.0F);
                entity.dropItem(IafItems.ROTTEN_EGG, 1);
            }
            this.timeUntilNextEgg = -1;
        } else
            this.timeUntilNextEgg--;
    }

    public void setTime(int timeUntilNextEgg) {
        this.timeUntilNextEgg = timeUntilNextEgg;
    }

    public void serialize(final NbtCompound tag) {
        NbtCompound chickenData = new NbtCompound();
        chickenData.putInt("timeUntilNextEgg", this.timeUntilNextEgg);
        tag.put("chickenData", chickenData);
    }

    public void deserialize(final NbtCompound tag) {
        NbtCompound chickenData = tag.getCompound("chickenData");
        this.timeUntilNextEgg = chickenData.getInt("timeUntilNextEgg");
    }

    private int createDefaultTime(final Random random) {
        return random.nextInt(6000) + 6000;
    }
}
