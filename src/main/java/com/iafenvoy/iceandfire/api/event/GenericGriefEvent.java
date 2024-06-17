package com.iafenvoy.iceandfire.api.event;

import com.iafenvoy.iceandfire.event.Cancelable;
import com.iafenvoy.iceandfire.event.LivingEvent;
import net.minecraft.entity.LivingEntity;

/**
 * GenericGriefEvent is fired right before an entity destroys or modifies blocks in some aspect. <br>
 * {@link #targetX} x coordinate being targeted for modification. <br>
 * {@link #targetY} y coordinate being targeted for modification. <br>
 * {@link #targetZ} z coordinate being targeted for modification. <br>
 * <br>
 * This event is {@link Cancelable}.<br>
 * If this event is canceled, no block destruction or explosion will follow.<br>
 * <br>
 * This event does not have a result. {@link HasResult}<br>
 * <br>
 * If you only want to deal with the damage caused by dragon fire, see {@link DragonFireDamageWorldEvent} <br>
 * <br>
 **/
@Cancelable
public class GenericGriefEvent extends LivingEvent {
    private final double targetX;
    private final double targetY;
    private final double targetZ;

    public GenericGriefEvent(LivingEntity griefer, double targetX, double targetY, double targetZ) {
        super(griefer);
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
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
