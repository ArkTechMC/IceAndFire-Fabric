package com.github.alexthe666.citadel.client.event;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

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
        return entity;
    }

    public EntityModel getModel() {
        return model;
    }

    public MatrixStack getPoseStack() {
        return poseStack;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public static class SetupRotations extends EventLivingRenderer {
        private final float bodyYRot;

        public SetupRotations(LivingEntity entity, EntityModel model, MatrixStack poseStack, float bodyYRot, float partialTicks) {
            super(entity, model, poseStack, partialTicks);
            this.bodyYRot = bodyYRot;
        }

        public float getBodyYRot() {
            return bodyYRot;
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
            return bodyYRot;
        }

        public VertexConsumerProvider getBufferSource() {
            return bufferSource;
        }

        public int getPackedLight() {
            return packedLight;
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
