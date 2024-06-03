package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.ModelPixie;
import com.github.alexthe666.iceandfire.client.render.entity.RenderPixie;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class LayerPixieGlow extends FeatureRenderer<EntityPixie, ModelPixie> {

    private final RenderPixie render;

    public LayerPixieGlow(RenderPixie renderIn) {
        super(renderIn);
        this.render = renderIn;
    }

    @Override
    public void render(@NotNull MatrixStack matrixStackIn, @NotNull VertexConsumerProvider bufferIn, int packedLightIn, EntityPixie pixie, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        Identifier texture = RenderPixie.TEXTURE_0;
        switch (pixie.getColor()) {
            default:
                texture = RenderPixie.TEXTURE_0;
                break;
            case 1:
                texture = RenderPixie.TEXTURE_1;
                break;
            case 2:
                texture = RenderPixie.TEXTURE_2;
                break;
            case 3:
                texture = RenderPixie.TEXTURE_3;
                break;
            case 4:
                texture = RenderPixie.TEXTURE_4;
                break;
            case 5:
                texture = RenderPixie.TEXTURE_5;
                break;
        }
        RenderLayer eyes = RenderLayer.getEyes(texture);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(eyes);
        this.getContextModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}