package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.ModelTroll;
import com.github.alexthe666.iceandfire.client.render.entity.RenderTroll;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;

public class LayerTrollWeapon extends FeatureRenderer<EntityTroll, ModelTroll> {
    private final RenderTroll renderer;

    public LayerTrollWeapon(RenderTroll renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    public void render(EntityTroll entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {

    }

    @Override
    public void render(@NotNull MatrixStack matrixStackIn, @NotNull VertexConsumerProvider bufferIn, int packedLightIn, EntityTroll troll, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (troll.getWeaponType() != null && !EntityGorgon.isStoneMob(troll)) {
            RenderLayer tex = RenderLayer.getEntityCutout(troll.getWeaponType().TEXTURE);
            this.getContextModel().render(matrixStackIn, bufferIn.getBuffer(tex), packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}