package com.github.alexthe666.citadel.client.event;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.fluid.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

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
        return fluidState;
    }

    public RenderLayer getRenderType() {
        return renderType;
    }

    public void setRenderType(RenderLayer renderType) {
        this.renderType = renderType;
    }
}
