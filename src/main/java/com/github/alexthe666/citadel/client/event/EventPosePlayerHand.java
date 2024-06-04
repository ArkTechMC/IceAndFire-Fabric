package com.github.alexthe666.citadel.client.event;

import com.iafenvoy.iafextra.event.Event;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
@Event.HasResult
public class EventPosePlayerHand extends Event {
    private final LivingEntity entityIn;
    private final BipedEntityModel model;
    private final boolean left;

    public EventPosePlayerHand(LivingEntity entityIn, BipedEntityModel model, boolean left) {
        this.entityIn = entityIn;
        this.model = model;
        this.left = left;
    }

    public Entity getEntityIn() {
        return entityIn;
    }

    public BipedEntityModel getModel() {
        return model;
    }

    public boolean isLeftHand() {
        return left;
    }


}
