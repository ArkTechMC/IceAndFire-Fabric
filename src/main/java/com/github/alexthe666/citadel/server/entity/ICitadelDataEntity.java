package com.github.alexthe666.citadel.server.entity;

import net.minecraft.nbt.NbtCompound;
/**
 * @author Alexthe666
 * @since 1.7.0
 */
public interface ICitadelDataEntity {

    NbtCompound getCitadelEntityData();

    void setCitadelEntityData(NbtCompound nbt);
}
