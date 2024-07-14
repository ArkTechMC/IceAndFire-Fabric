package com.iafenvoy.iceandfire.render.entity.layer;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityGorgon;
import com.iafenvoy.iceandfire.render.entity.RenderGorgon;
import com.iafenvoy.iceandfire.render.model.ModelGorgon;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class LayerGorgonEyes extends FeatureRenderer<EntityGorgon, ModelGorgon> {
    private static final Identifier TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/gorgon/gorgon_eyes.png");

    public LayerGorgonEyes(RenderGorgon renderIn) {
        super(renderIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, EntityGorgon entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.getAnimation() == EntityGorgon.ANIMATION_SCARE || entity.getAnimation() == EntityGorgon.ANIMATION_HIT) {
            RenderLayer eyes = RenderLayer.getEyes(TEXTURE);
            VertexConsumer vertexConsumer = bufferIn.getBuffer(eyes);
            this.getContextModel().render(matrixStackIn, vertexConsumer, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}