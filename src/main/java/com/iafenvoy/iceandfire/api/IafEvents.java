package com.iafenvoy.iceandfire.api;

import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

public class IafEvents {
    public static final Event<GriefBreakBlock> ON_GRIEF_BREAK_BLOCK = EventFactory.createArrayBacked(GriefBreakBlock.class, callbacks -> (griefer, targetX, targetY, targetZ) -> {
        for (GriefBreakBlock callback : callbacks)
            if (callback.onBreakBlock(griefer, targetX, targetY, targetZ))
                return true;
        return false;
    });
    public static final Event<DragonFireDamageWorld> ON_DRAGON_DAMAGE_BLOCK = EventFactory.createArrayBacked(DragonFireDamageWorld.class, callbacks -> (dragonBase, targetX, targetY, targetZ) -> {
        for (DragonFireDamageWorld callback : callbacks)
            if (callback.onDamageBlock(dragonBase, targetX, targetY, targetZ))
                return true;
        return false;
    });
    public static final Event<DragonFire> ON_DRAGON_FIRE_BLOCK = EventFactory.createArrayBacked(DragonFire.class, callbacks -> (dragonBase, targetX, targetY, targetZ) -> {
        for (DragonFire callback : callbacks)
            if (callback.onFireBlock(dragonBase, targetX, targetY, targetZ))
                return true;
        return false;
    });

    @FunctionalInterface
    public interface GriefBreakBlock {
        /**
         * GenericGriefEvent is fired right before an entity destroys or modifies blocks in some aspect. <br>
         * {@param #targetX} x coordinate being targeted for modification. <br>
         * {@param #targetY} y coordinate being targeted for modification. <br>
         * {@param #targetZ} z coordinate being targeted for modification. <br>
         * <br>
         * If this event is canceled, no block destruction or explosion will follow.<br>
         * <br>
         * If you only want to deal with the damage caused by dragon fire, see {@link DragonFireDamageWorld} <br>
         * <br>
         **/
        boolean onBreakBlock(LivingEntity griefer, double targetX, double targetY, double targetZ);
    }

    @FunctionalInterface
    public interface DragonFireDamageWorld {
        /**
         * DragonFireDamageWorldEvent is fired right before a Dragon damages/changes terrain fire, lightning or ice. <br>
         * {@param #dragonBase} dragon in question. <br>
         * {@param #targetX} x coordinate being targeted for burning/freezing. <br>
         * {@param #targetY} y coordinate being targeted for burning/freezing. <br>
         * {@param #targetZ} z coordinate being targeted for burning/freezing. <br>
         * <br>
         * If this event is canceled, no fire will be spawned from the dragon's mouth.<br>
         * <br>
         * If you want to cancel all aspects of dragon fire, see {@link DragonFire} <br>
         * <br>
         **/
        boolean onDamageBlock(EntityDragonBase dragonBase, double targetX, double targetY, double targetZ);
    }

    @FunctionalInterface
    public interface DragonFire {
        /**
         * DragonFireEvent is fired right before a Dragon breathes fire or ice. <br>
         * {@param #dragonBase} dragon in question. <br>
         * {@param #targetX} x coordinate being targeted for burning/freezing. <br>
         * {@param #targetY} y coordinate being targeted for burning/freezing. <br>
         * {@param #targetZ} z coordinate being targeted for burning/freezing. <br>
         * <br>
         * If this event is canceled, no fire will be spawned from the dragon's mouth.<br>
         * <br>
         * If you only want to deal with the damage caused by dragon fire, see {@link DragonFireDamageWorld} <br>
         * <br>
         **/
        boolean onFireBlock(EntityDragonBase dragonBase, double targetX, double targetY, double targetZ);
    }
}
