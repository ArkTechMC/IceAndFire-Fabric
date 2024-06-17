package com.iafenvoy.iceandfire.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class EntityEvents {
    public static final Event<JoinWorld> ON_JOIN_WORLD = EventFactory.createArrayBacked(JoinWorld.class, callbacks -> (entity, world) -> {
        for (JoinWorld callback : callbacks)
            if (!callback.onJoinWorld(entity, world))
                return true;
        return false;
    });

    public static final Event<Tracking> START_TRACKING_TAIL = EventFactory.createArrayBacked(Tracking.class, callbacks -> (entity, player) -> {
        for (Tracking callback : callbacks)
            callback.onTrackingStart(entity, player);
    });

    @FunctionalInterface
    public interface JoinWorld {
        boolean onJoinWorld(Entity entity, World world);
    }

    public interface Tracking {
        void onTrackingStart(Entity tracking, ServerPlayerEntity player);
    }
}
