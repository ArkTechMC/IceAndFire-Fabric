package com.github.alexthe666.citadel.client.model;

import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface ITabulaModelAnimator<T extends Entity> {
    void setRotationAngles(TabulaModel model, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale);
}