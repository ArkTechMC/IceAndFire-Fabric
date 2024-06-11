package com.github.alexthe666.citadel.mixin.client;

import com.github.alexthe666.citadel.client.event.EventLivingRenderer;
import dev.arktechmc.iafextra.event.EventBus;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Shadow
    protected EntityModel<?> model;

    @Inject(method = "setupTransforms", at = @At("RETURN"))
    protected void citadel_setupRotations(LivingEntity livingEntity, MatrixStack poseStack, float ageInTicks, float bodyYRot, float partialTick, CallbackInfo ci) {
        EventLivingRenderer.SetupRotations event = new EventLivingRenderer.SetupRotations(livingEntity, this.model, poseStack, bodyYRot, partialTick);
        EventBus.post(event);
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;setAngles(Lnet/minecraft/entity/Entity;FFFFF)V"))
    protected void citadel_render_setupAnim_before(LivingEntity livingEntity, float yaw, float partialTicks, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, CallbackInfo ci) {
        EventLivingRenderer.PreSetupAnimations event = new EventLivingRenderer.PreSetupAnimations(livingEntity, this.model, poseStack, yaw, partialTicks, bufferSource, packedLight);
        EventBus.post(event);
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;setAngles(Lnet/minecraft/entity/Entity;FFFFF)V", shift = At.Shift.AFTER))
    protected void citadel_render_setupAnim_after(LivingEntity livingEntity, float yaw, float partialTicks, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, CallbackInfo ci) {
        EventLivingRenderer.PostSetupAnimations event = new EventLivingRenderer.PostSetupAnimations(livingEntity, this.model, poseStack, yaw, partialTicks, bufferSource, packedLight);
        EventBus.post(event);
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "RETURN"))
    protected void citadel_render_renderToBuffer(LivingEntity livingEntity, float yaw, float partialTicks, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, CallbackInfo ci) {
        EventLivingRenderer.PostRenderModel event = new EventLivingRenderer.PostRenderModel(livingEntity, this.model, poseStack, yaw, partialTicks, bufferSource, packedLight);
        EventBus.post(event);
    }
}
