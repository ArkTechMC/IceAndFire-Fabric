package com.github.alexthe666.citadel.server.event;

import com.iafenvoy.iafextra.event.Cancelable;
import com.iafenvoy.iafextra.event.Event;
import net.minecraft.entity.Entity;

@Cancelable
public class EventChangeEntityTickRate extends Event {
    private final Entity entity;
    private final float targetTickRate;

    public EventChangeEntityTickRate(Entity entity, float targetTickRate) {
        this.entity = entity;
        this.targetTickRate = targetTickRate;
    }

    public Entity getEntity() {
        return entity;
    }

    public float getTargetTickRate() {
        return targetTickRate;
    }
}
