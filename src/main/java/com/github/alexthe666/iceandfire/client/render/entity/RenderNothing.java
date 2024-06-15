package com.github.alexthe666.iceandfire.client.render.entity;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class RenderNothing<T extends Entity> extends EntityRenderer<T> {

    public RenderNothing(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    // Only render if the debug bboxes are enabled
    @Override
    public boolean shouldRender(T livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
        if (!this.dispatcher.shouldRenderHitboxes())
            return false;
        return super.shouldRender(livingEntityIn, camera, camX, camY, camZ);
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return null;
    }
}
