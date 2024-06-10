package com.github.alexthe666.citadel.client.event;

import dev.arktechmc.iafextra.event.Event;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;

@Environment(EnvType.CLIENT)
@Event.HasResult
public class EventGetOutlineColor extends Event {
    private Entity entityIn;
    private int color;

    public EventGetOutlineColor(Entity entityIn, int color) {
        this.entityIn = entityIn;
        this.color = color;
    }

    public Entity getEntityIn() {
        return this.entityIn;
    }

    public void setEntityIn(Entity entityIn) {
        this.entityIn = entityIn;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }


}
