package com.github.alexthe666.citadel.mixin.client;

import com.github.alexthe666.citadel.client.event.EventGetFluidRenderType;
import com.iafenvoy.iafextra.event.Event;
import com.iafenvoy.iafextra.event.EventBus;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLayers.class)
public class ItemBlockRenderTypesMixin {
    @Inject(at = @At("TAIL"), cancellable = true, method = "getFluidLayer")
    private static void citadel_getFluidRenderLayer(FluidState fluidState, CallbackInfoReturnable<RenderLayer> cir) {
        EventGetFluidRenderType event = new EventGetFluidRenderType(fluidState, cir.getReturnValue());
        EventBus.post(event);
        if (event.getResult() == Event.Result.ALLOW) {
            cir.setReturnValue(event.getRenderType());
        }
    }
}
