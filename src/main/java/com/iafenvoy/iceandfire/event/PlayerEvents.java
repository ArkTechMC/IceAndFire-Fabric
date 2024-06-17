package com.iafenvoy.iceandfire.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerEvents {

    public static final Event<PlayerLoggedInOrOut> LOGGED_OUT = EventFactory.createArrayBacked(PlayerLoggedInOrOut.class, callbacks -> player -> {
        for (PlayerLoggedInOrOut e : callbacks)
            e.handleConnection(player);
    });

    /**
     * Use this interface to handle players logging in and out.
     */
    @FunctionalInterface
    public interface PlayerLoggedInOrOut {
        void handleConnection(PlayerEntity player);
    }
}
