package com.github.alexthe666.citadel.server.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

/**
 * @author Alexthe666
 * @since 1.7.0
 * <p>
 * CitadelTag is a datasynced tag for LivingEntity provided by citadel to be used by various mods.
 */
public class CitadelEntityData {

    public static NbtCompound getCitadelTag(LivingEntity entity) {
        return entity instanceof ICitadelDataEntity ? ((ICitadelDataEntity) entity).getCitadelEntityData() : new NbtCompound();
    }

    public static void setCitadelTag(LivingEntity entity, NbtCompound tag) {
        if (entity instanceof ICitadelDataEntity) {
            ((ICitadelDataEntity) entity).setCitadelEntityData(tag);
        }
    }
}
