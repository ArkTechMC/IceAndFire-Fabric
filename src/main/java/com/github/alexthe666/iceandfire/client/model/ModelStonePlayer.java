package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import org.jetbrains.annotations.NotNull;

public class ModelStonePlayer extends BipedEntityModel<EntityStoneStatue> {

    public ModelStonePlayer(ModelPart p_170677_) {
        super(p_170677_);
    }

    @Override
    public void setAngles(EntityStoneStatue entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
}
