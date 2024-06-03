package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.NotNull;

public class ChickenData {
    public int timeUntilNextEgg = -1;

    public void tickChicken(final LivingEntity entity) {
        if (!IafConfig.chickensLayRottenEggs || entity.getWorld().isClient() || !ServerEvents.isChicken(entity) || entity.isBaby()) {
            return;
        }

        if (timeUntilNextEgg == -1) {
            timeUntilNextEgg = createDefaultTime(entity.getRandom());
        }

        if (timeUntilNextEgg == 0) {
            if (entity.age > 30 && entity.getRandom().nextInt(IafConfig.cockatriceEggChance + 1) == 0) {
                entity.playSound(SoundEvents.ENTITY_CHICKEN_HURT, 2.0F, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2F + 1.0F);
                entity.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2F + 1.0F);
                entity.dropItem(IafItemRegistry.ROTTEN_EGG.get(), 1);
            }

            timeUntilNextEgg = -1;
        } else {
            timeUntilNextEgg--;
        }
    }

    public void setTime(int timeUntilNextEgg) {
        this.timeUntilNextEgg = timeUntilNextEgg;
    }

    public void serialize(final NbtCompound tag) {
        NbtCompound chickenData = new NbtCompound();
        chickenData.putInt("timeUntilNextEgg", timeUntilNextEgg);
        tag.put("chickenData", chickenData);
    }

    public void deserialize(final NbtCompound tag) {
        NbtCompound chickenData = tag.getCompound("chickenData");
        timeUntilNextEgg = chickenData.getInt("timeUntilNextEgg");
    }

    private int createDefaultTime(@NotNull final Random random) {
        return random.nextInt(6000) + 6000;
    }
}
