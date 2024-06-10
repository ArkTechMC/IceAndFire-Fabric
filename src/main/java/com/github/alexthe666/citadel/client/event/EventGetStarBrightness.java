package com.github.alexthe666.citadel.client.event;

import dev.arktechmc.iafextra.event.Event;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;

@Environment(EnvType.CLIENT)
@Event.HasResult
public class EventGetStarBrightness extends Event {
    private final ClientWorld clientLevel;
    private final float partialTicks;
    private float brightness;

    public EventGetStarBrightness(ClientWorld clientLevel, float brightness, float partialTicks) {
        this.clientLevel = clientLevel;
        this.brightness = brightness;
        this.partialTicks = partialTicks;
    }

    public ClientWorld getLevel() {
        return this.clientLevel;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public float getBrightness() {
        return this.brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }
}
