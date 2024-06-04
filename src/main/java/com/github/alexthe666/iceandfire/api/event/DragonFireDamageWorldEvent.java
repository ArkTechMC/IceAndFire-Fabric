package com.github.alexthe666.iceandfire.api.event;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iafextra.event.Cancelable;
import com.iafenvoy.iafextra.event.LivingEvent;

/**
 * DragonFireDamageWorldEvent is fired right before a Dragon damages/changes terrain fire, lightning or ice. <br>
 * {@link #dragonBase} dragon in question. <br>
 * {@link #targetX} x coordinate being targeted for burning/freezing. <br>
 * {@link #targetY} y coordinate being targeted for burning/freezing. <br>
 * {@link #targetZ} z coordinate being targeted for burning/freezing. <br>
 * <br>
 * This event is {@link Cancelable}.<br>
 * If this event is canceled, no blocks will be modified by the dragons breath.<br>
 * <br>
 * This event does not have a result. {@link HasResult}<br>
 * <br>
 * <br>
 * If you want to cancel all aspects of dragon fire, see {@link DragonFireEvent} <br>
 * <br>
 **/
@Cancelable
public class DragonFireDamageWorldEvent extends LivingEvent {
    private final EntityDragonBase dragonBase;
    private final double targetX;
    private final double targetY;
    private final double targetZ;

    public DragonFireDamageWorldEvent(EntityDragonBase dragonBase, double targetX, double targetY, double targetZ) {
        super(dragonBase);
        this.dragonBase = dragonBase;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }

    public EntityDragonBase getDragon() {
        return dragonBase;
    }

    public double getTargetX() {
        return targetX;
    }

    public double getTargetY() {
        return targetY;
    }

    public double getTargetZ() {
        return targetZ;
    }

}
