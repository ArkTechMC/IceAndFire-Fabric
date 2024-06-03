package com.github.alexthe666.citadel.mixin.client;

import com.github.alexthe666.citadel.CitadelConstants;
import com.github.alexthe666.citadel.client.event.EventGetFluidRenderType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.fluid.FluidState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLayers.class)
public class ItemBlockRenderTypesMixin  {


    @Inject(at = @At("TAIL"), remap = CitadelConstants.REMAPREFS, cancellable = true,
            method = "Lnet/minecraft/client/renderer/ItemBlockRenderTypes;getRenderLayer(Lnet/minecraft/world/level/material/FluidState;)Lnet/minecraft/client/renderer/RenderType;")
    private static void citadel_getFluidRenderLayer(FluidState fluidState, CallbackInfoReturnable<RenderLayer> cir) {
        EventGetFluidRenderType event = new EventGetFluidRenderType(fluidState, cir.getReturnValue());
        MinecraftForge.EVENT_BUS.post(event);
        if(event.getResult() == Event.Result.ALLOW){
            cir.setReturnValue(event.getRenderType());
        }
    }
}
