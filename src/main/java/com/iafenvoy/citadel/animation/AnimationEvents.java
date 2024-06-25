package com.iafenvoy.citadel.animation;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class AnimationEvents {
    public static final Event<Start> START = EventFactory.createArrayBacked(Start.class, callbacks -> (entity, animation) -> {
        for (Start callback : callbacks)
            if (callback.onStart(entity, animation))
                return true;
        return false;
    });
    public static final Event<Tick> TICK = EventFactory.createArrayBacked(Tick.class, callbacks -> (entity, animation, tick) -> {
        for (Tick callback : callbacks)
            callback.onTick(entity, animation, tick);
    });


    public interface Start {
        //return true to cancel
        boolean onStart(IAnimatedEntity entity, Animation animation);
    }

    public interface Tick {
        void onTick(IAnimatedEntity entity, Animation animation, int tick);
    }
}
