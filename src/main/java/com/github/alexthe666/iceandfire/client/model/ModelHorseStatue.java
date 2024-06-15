package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.entity.passive.AbstractHorseEntity;
import org.jetbrains.annotations.NotNull;

public class ModelHorseStatue extends HorseEntityModel {

    public ModelHorseStatue(ModelPart part) {
        super(part);
    }

    @Override
    public void setAngles(AbstractHorseEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}