package com.iafenvoy.iceandfire.render.entity.layer;

import com.iafenvoy.iceandfire.render.entity.RenderPixie;
import com.iafenvoy.iceandfire.entity.EntityPixie;
import com.iafenvoy.iceandfire.render.model.ModelPixie;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;

public class LayerPixieItem extends FeatureRenderer<EntityPixie, ModelPixie> {
    final RenderPixie renderer;

    public LayerPixieItem(RenderPixie renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, EntityPixie entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = entity.getStackInHand(Hand.MAIN_HAND);
        if (!itemstack.isEmpty()) {
            matrixStackIn.push();
            matrixStackIn.translate(-0.0625F, 0.53125F, 0.21875F);
            matrixStackIn.translate(-0.075F, 0, -0.05F);
            matrixStackIn.translate(0.05F, 0.55F, -0.4F);
            matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(200.0F));
            matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
            MinecraftClient.getInstance().getItemRenderer().renderItem(itemstack, ModelTransformationMode.FIXED, packedLightIn, OverlayTexture.DEFAULT_UV, matrixStackIn, bufferIn, MinecraftClient.getInstance().world, 0);
            matrixStackIn.pop();
        }
    }
}