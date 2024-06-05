package com.github.alexthe666.citadel.client.event;

import com.iafenvoy.iafextra.event.Event;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.fluid.FluidState;

@Environment(EnvType.CLIENT)
@Event.HasResult
public class EventGetFluidRenderType extends Event {
    private final FluidState fluidState;
    private RenderLayer renderType;

    public EventGetFluidRenderType(FluidState fluidState, RenderLayer renderType) {
        this.fluidState = fluidState;
        this.renderType = renderType;
    }

    public FluidState getFluidState() {
        return this.fluidState;
    }

    public RenderLayer getRenderType() {
        return this.renderType;
    }

    public void setRenderType(RenderLayer renderType) {
        this.renderType = renderType;
    }
}
