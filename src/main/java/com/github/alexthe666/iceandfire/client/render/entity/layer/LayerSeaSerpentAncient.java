package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class LayerSeaSerpentAncient extends FeatureRenderer<EntitySeaSerpent, AdvancedEntityModel<EntitySeaSerpent>> {

    private static final Identifier TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/seaserpent/ancient_overlay.png");
    private static final Identifier TEXTURE_BLINK = new Identifier(IceAndFire.MOD_ID, "textures/models/seaserpent/ancient_overlay_blink.png");

    public LayerSeaSerpentAncient(MobEntityRenderer<EntitySeaSerpent, AdvancedEntityModel<EntitySeaSerpent>> renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, EntitySeaSerpent serpent, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (serpent.isAncient()) {
            RenderLayer tex = RenderLayer.getEntityNoOutline(serpent.isBlinking() ? TEXTURE_BLINK : TEXTURE);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(tex);
            this.getContextModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

        }
    }
}
