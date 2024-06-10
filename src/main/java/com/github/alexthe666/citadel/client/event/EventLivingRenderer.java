package com.github.alexthe666.citadel.client.event;

import dev.arktechmc.iafextra.event.Event;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class EventLivingRenderer extends Event {

    private final LivingEntity entity;
    private final EntityModel model;
    private final MatrixStack poseStack;
    private final float partialTicks;

    public EventLivingRenderer(LivingEntity entity, EntityModel model, MatrixStack poseStack, float partialTicks) {
        this.entity = entity;
        this.model = model;
        this.poseStack = poseStack;
        this.partialTicks = partialTicks;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public EntityModel getModel() {
        return this.model;
    }

    public MatrixStack getPoseStack() {
        return this.poseStack;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public static class SetupRotations extends EventLivingRenderer {
        private final float bodyYRot;

        public SetupRotations(LivingEntity entity, EntityModel model, MatrixStack poseStack, float bodyYRot, float partialTicks) {
            super(entity, model, poseStack, partialTicks);
            this.bodyYRot = bodyYRot;
        }

        public float getBodyYRot() {
            return this.bodyYRot;
        }
    }

    public static class AccessToBufferSource extends EventLivingRenderer {
        private final float bodyYRot;
        private final VertexConsumerProvider bufferSource;
        private final int packedLight;

        public AccessToBufferSource(LivingEntity entity, EntityModel model, MatrixStack poseStack, float bodyYRot, float partialTicks, VertexConsumerProvider bufferSource, int packedLight) {
            super(entity, model, poseStack, partialTicks);
            this.bodyYRot = bodyYRot;
            this.bufferSource = bufferSource;
            this.packedLight = packedLight;
        }

        public float getBodyYRot() {
            return this.bodyYRot;
        }

        public VertexConsumerProvider getBufferSource() {
            return this.bufferSource;
        }

        public int getPackedLight() {
            return this.packedLight;
        }
    }

    public static class PreSetupAnimations extends AccessToBufferSource {

        public PreSetupAnimations(LivingEntity entity, EntityModel model, MatrixStack poseStack, float bodyYRot, float partialTicks, VertexConsumerProvider bufferSource, int packedLight) {
            super(entity, model, poseStack, bodyYRot, partialTicks, bufferSource, packedLight);
        }
    }

    public static class PostSetupAnimations extends AccessToBufferSource {

        public PostSetupAnimations(LivingEntity entity, EntityModel model, MatrixStack poseStack, float bodyYRot, float partialTicks, VertexConsumerProvider bufferSource, int packedLight) {
            super(entity, model, poseStack, bodyYRot, partialTicks, bufferSource, packedLight);
        }
    }

    public static class PostRenderModel extends AccessToBufferSource {

        public PostRenderModel(LivingEntity entity, EntityModel model, MatrixStack poseStack, float bodyYRot, float partialTicks, VertexConsumerProvider bufferSource, int packedLight) {
            super(entity, model, poseStack, bodyYRot, partialTicks, bufferSource, packedLight);
        }
    }
}
