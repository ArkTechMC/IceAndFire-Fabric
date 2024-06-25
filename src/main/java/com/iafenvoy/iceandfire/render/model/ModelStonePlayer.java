package com.iafenvoy.iceandfire.render.model;

import com.iafenvoy.iceandfire.entity.EntityStoneStatue;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;

public class ModelStonePlayer extends BipedEntityModel<EntityStoneStatue> {

    public ModelStonePlayer(ModelPart p_170677_) {
        super(p_170677_);
    }

    @Override
    public void setAngles(EntityStoneStatue entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
}
