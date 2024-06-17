package com.iafenvoy.iceandfire.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

@Cancelable
public class AttackEntityEvent extends LivingEvent {
    private final Entity target;

    public AttackEntityEvent(PlayerEntity player, Entity target) {
        super(player);
        this.target = target;
    }

    public Entity getTarget() {
        return this.target;
    }
}
