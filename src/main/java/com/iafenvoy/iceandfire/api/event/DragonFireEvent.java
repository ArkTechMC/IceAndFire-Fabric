package com.iafenvoy.iceandfire.api.event;

import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.event.Cancelable;
import com.iafenvoy.iceandfire.event.LivingEvent;

/**
 * DragonFireEvent is fired right before a Dragon breathes fire or ice. <br>
 * {@link #dragonBase} dragon in question. <br>
 * {@link #targetX} x coordinate being targeted for burning/freezing. <br>
 * {@link #targetY} y coordinate being targeted for burning/freezing. <br>
 * {@link #targetZ} z coordinate being targeted for burning/freezing. <br>
 * <br>
 * This event is {@link Cancelable}.<br>
 * If this event is canceled, no fire will be spawned from the dragon's mouth.<br>
 * <br>
 * This event does not have a result. {@link HasResult}<br>
 * <br>
 * <br>
 * If you only want to deal with the damage caused by dragon fire, see {@link DragonFireDamageWorldEvent} <br>
 * <br>
 **/
@Cancelable
public class DragonFireEvent extends LivingEvent {
    private final EntityDragonBase dragonBase;
    private final double targetX;
    private final double targetY;
    private final double targetZ;

    public DragonFireEvent(EntityDragonBase dragonBase, double targetX, double targetY, double targetZ) {
        super(dragonBase);
        this.dragonBase = dragonBase;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }

    public EntityDragonBase getDragon() {
        return this.dragonBase;
    }

    public double getTargetX() {
        return this.targetX;
    }

    public double getTargetY() {
        return this.targetY;
    }

    public double getTargetZ() {
        return this.targetZ;
    }

}
