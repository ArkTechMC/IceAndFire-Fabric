package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityMutlipartPart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;

import java.util.UUID;

public class EntityUtil {
    public static void updatePart(final EntityMutlipartPart part, final LivingEntity parent) {
        if (part == null || !(parent.getWorld() instanceof ServerWorld serverLevel) || parent.isRemoved()) {
            return;
        }

        if (!part.shouldContinuePersisting()) {
            UUID uuid = part.getUuid();
            Entity existing = serverLevel.getEntity(uuid);

            // Update UUID if a different entity with the same UUID exists already
            if (existing != null && existing != part) {
                while (serverLevel.getEntity(uuid) != null) {
                    uuid = MathHelper.randomUuid(parent.getRandom());
                }

                IceAndFire.LOGGER.debug("Updated the UUID of [{}] due to a clash with [{}]", part, existing);
            }

            part.setUuid(uuid);
            serverLevel.spawnEntity(part);
        }

        part.setParent(parent);
    }
}
