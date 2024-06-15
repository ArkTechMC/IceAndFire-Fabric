package com.github.alexthe666.iceandfire.client.model.util;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;


public class HideableLayer<T extends Entity, M extends EntityModel<T>, C extends FeatureRenderer<T, M>> extends FeatureRenderer<T, M> {

    public boolean hidden;
    final C layerRenderer;

    public HideableLayer(C layerRenderer, FeatureRendererContext<T, M> entityRendererIn) {
        super(entityRendererIn);
        this.hidden = false;
        this.layerRenderer = layerRenderer;
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!this.hidden)
            this.layerRenderer.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
    }
}
