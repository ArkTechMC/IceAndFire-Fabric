package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.client.model.ModelMyrmexBase;
import com.github.alexthe666.iceandfire.client.render.entity.RenderMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;

public class LayerMyrmexItem extends FeatureRenderer<EntityMyrmexBase, AdvancedEntityModel<EntityMyrmexBase>> {

    protected final RenderMyrmexBase livingEntityRenderer;

    public LayerMyrmexItem(RenderMyrmexBase livingEntityRendererIn) {
        super(livingEntityRendererIn);
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    private void renderHeldItem(EntityMyrmexBase myrmex, ItemStack stack, ModelTransformationMode transform, Arm handSide) {

    }

    protected void translateToHand(MatrixStack stack) {
        ((ModelMyrmexBase<?>) this.livingEntityRenderer.getModel()).postRenderArm(0, stack);
    }

    public boolean shouldCombineTextures() {
        return false;
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, EntityMyrmexBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn instanceof EntityMyrmexWorker) {
            ItemStack itemstack = entitylivingbaseIn.getStackInHand(Hand.MAIN_HAND);
            if (!itemstack.isEmpty()) {
                matrixStackIn.push();
                if (!itemstack.isEmpty()) {
                    matrixStackIn.push();

                    if (entitylivingbaseIn.isSneaking()) {
                        matrixStackIn.translate(0.0F, 0.2F, 0.0F);
                    }
                    this.translateToHand(matrixStackIn);
                    matrixStackIn.translate(0F, 0.3F, -1.6F);
                    if (itemstack.getItem() instanceof BlockItem) {
                        matrixStackIn.translate(0F, 0, 0.2F);
                    } else {
                        matrixStackIn.translate(0F, 0.2F, 0.3F);
                    }
                    matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(160.0F));
                    matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
                    MinecraftClient.getInstance().getItemRenderer().renderItem(itemstack, ModelTransformationMode.FIXED, packedLightIn, OverlayTexture.DEFAULT_UV, matrixStackIn, bufferIn, MinecraftClient.getInstance().world, 0);
                    matrixStackIn.pop();
                }
                matrixStackIn.pop();
            }
        }
    }
}