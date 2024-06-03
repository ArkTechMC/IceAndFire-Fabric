package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.ModelTroll;
import com.github.alexthe666.iceandfire.client.render.entity.RenderTroll;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;

public class LayerTrollEyes extends FeatureRenderer<EntityTroll, ModelTroll> {

    private final RenderTroll renderer;

    public LayerTrollEyes(RenderTroll renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    @Override
    public void render(@NotNull MatrixStack matrixStackIn, @NotNull VertexConsumerProvider bufferIn, int packedLightIn, @NotNull EntityTroll troll, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!EntityGorgon.isStoneMob(troll)) {
            RenderLayer tex = RenderLayer.getEyes(troll.getTrollType().TEXTURE_EYES);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(tex);
            this.getContextModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

        }
    }
}
