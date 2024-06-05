package com.iafenvoy.iafextra.client;

import com.github.alexthe666.iceandfire.pathfinding.raycoms.WorldEventContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class IceAndFireClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WorldRenderEvents.LAST.register(context -> {
            WorldEventContext.INSTANCE.
        });
    }
}
