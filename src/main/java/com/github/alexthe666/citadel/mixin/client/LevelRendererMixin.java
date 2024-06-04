package com.github.alexthe666.citadel.mixin.client;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.client.event.EventGetOutlineColor;
import com.github.alexthe666.citadel.client.shader.PostEffectRegistry;
import com.iafenvoy.iafextra.event.Event;
import com.iafenvoy.iafextra.event.EventBus;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class LevelRendererMixin {
    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(method = "loadEntityOutlinePostProcessor", at = @At("TAIL"))
    private void citadel_initOutline(CallbackInfo ci) {
        PostEffectRegistry.onInitializeOutline();
    }

    @Inject(method = "onResized",
            at = @At("TAIL"))
    private void citadel_resize(int x, int y, CallbackInfo ci) {
        PostEffectRegistry.resize(x, y);
    }


    @Inject(method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/BufferBuilderStorage;getEntityVertexConsumers()Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;",
                    shift = At.Shift.BEFORE
            ))
    private void citadel_renderLevel_beforeEntities(MatrixStack poseStack, float f, long l, boolean b, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
        PostEffectRegistry.clearAndBindWrite(this.client.getFramebuffer());
    }

    @Inject(method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/OutlineVertexConsumerProvider;draw()V",
                    shift = At.Shift.BEFORE
            ))
    private void citadel_renderLevel_process(MatrixStack poseStack, float f, long l, boolean b, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
        PostEffectRegistry.processEffects(this.client.getFramebuffer(), f);
    }

    @Inject(method = "render",
            at = @At(
                    value = "TAIL"
            ))
    private void citadel_renderLevel_end(MatrixStack poseStack, float f, long l, boolean b, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
        PostEffectRegistry.blitEffects();
    }

    @Redirect(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getTeamColorValue()I")
    )
    private int citadel_getTeamColor(Entity entity) {
        EventGetOutlineColor event = new EventGetOutlineColor(entity, entity.getTeamColorValue());
        EventBus.post(event);
        int color = entity.getTeamColorValue();
        if (event.getResult() == Event.Result.ALLOW) {
            color = event.getColor();
        }
        return color;
    }

    @Redirect(
            method = "renderSky",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getSkyAngle(F)F"),
            expect = 2
    )
    private float citadel_getTimeOfDay(ClientWorld instance, float partialTicks) {
        //default implementation does not lerp the time of day
        float lerpBy = Citadel.PROXY.isGamePaused() ? 0F : partialTicks;
        float lerpedDayTime = (instance.getDimension().fixedTime().orElse(instance.getLunarTime()) + lerpBy) / 24000.0F;
        double d0 = MathHelper.fractionalPart((double) lerpedDayTime - 0.25D);
        double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
        return (float) (d0 * 2.0D + d1) / 3.0F;
    }
}