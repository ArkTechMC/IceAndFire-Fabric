package com.iafenvoy.iceandfire.mixin;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.event.ClientEvents;
import com.iafenvoy.iceandfire.render.RenderVariables;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow
    @Final
    private Camera camera;

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V", shift = At.Shift.AFTER))
    private void onCameraSetup(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
        ClientEvents.onCameraSetup(this.camera);
    }

    @Inject(method = "loadPrograms", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;clearPrograms()V"))
    private void registerProgram(ResourceFactory factory, CallbackInfo ci, @Local(ordinal = 1) List<Pair<ShaderProgram, Consumer<ShaderProgram>>> list2) {
        try {
            list2.add(Pair.of(new ShaderProgram(factory, "rendertype_dread_portal", VertexFormats.POSITION_COLOR), program -> RenderVariables.DREAD_PORTAL_PROGRAM = program));
        } catch (Exception e) {
            IceAndFire.LOGGER.error("Failed to load dread portal program", e);
        }
    }
}
