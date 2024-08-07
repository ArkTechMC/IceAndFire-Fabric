package com.iafenvoy.iceandfire.render.entity.layer;

import com.iafenvoy.iceandfire.entity.EntityGorgon;
import com.iafenvoy.iceandfire.entity.EntityTroll;
import com.iafenvoy.iceandfire.render.entity.RenderTroll;
import com.iafenvoy.iceandfire.render.model.ModelTroll;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class LayerTrollWeapon extends FeatureRenderer<EntityTroll, ModelTroll> {
    public LayerTrollWeapon(RenderTroll renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, EntityTroll troll, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (troll.getWeaponType() != null && !EntityGorgon.isStoneMob(troll)) {
            RenderLayer tex = RenderLayer.getEntityCutout(troll.getWeaponType().getTexture());
            this.getContextModel().render(matrixStackIn, bufferIn.getBuffer(tex), packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}