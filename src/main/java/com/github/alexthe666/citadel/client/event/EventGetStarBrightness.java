package com.github.alexthe666.citadel.client.event;

import com.iafenvoy.iafextra.event.Event;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;

@Environment(EnvType.CLIENT)
@Event.HasResult
public class EventGetStarBrightness extends Event {
    private final ClientWorld clientLevel;
    private float brightness;
    private final float partialTicks;

    public EventGetStarBrightness(ClientWorld clientLevel, float brightness, float partialTicks) {
        this.clientLevel = clientLevel;
        this.brightness = brightness;
        this.partialTicks = partialTicks;
    }

    public ClientWorld getLevel() {
        return clientLevel;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }
}
