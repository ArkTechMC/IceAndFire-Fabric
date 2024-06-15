package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;

import java.util.stream.StreamSupport;

public class LayerDragonBanner extends FeatureRenderer<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> {
    private final FeatureRendererContext<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> renderer;

    public LayerDragonBanner(FeatureRendererContext<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> renderIn) {
        super(renderIn);
        this.renderer = renderIn;
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, EntityDragonBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = entity.getStackInHand(Hand.OFF_HAND);
        matrixStackIn.push();
        if (!itemstack.isEmpty() && itemstack.getItem() instanceof BannerItem) {
            float f = (entity.getRenderSize() / 3F);
            float f2 = 1F / f;
            matrixStackIn.push();
            this.postRender(StreamSupport.stream(this.renderer.getModel().getAllParts().spliterator(), false).filter(cube -> cube.boxName.equals("BodyUpper")).findFirst().get(), matrixStackIn);
            matrixStackIn.translate(0, -0.2F, 0.4F);
            matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
            matrixStackIn.push();
            matrixStackIn.scale(f2, f2, f2);
            MinecraftClient.getInstance().getItemRenderer().renderItem(itemstack, ModelTransformationMode.NONE, packedLightIn, OverlayTexture.DEFAULT_UV, matrixStackIn, bufferIn, MinecraftClient.getInstance().world, 0);
            matrixStackIn.pop();
            matrixStackIn.pop();
        }
        matrixStackIn.pop();
    }

    protected void postRender(AdvancedModelBox renderer, MatrixStack matrixStackIn) {
        if (renderer.rotateAngleX == 0.0F && renderer.rotateAngleY == 0.0F && renderer.rotateAngleZ == 0.0F) {
            if (renderer.rotationPointX != 0.0F || renderer.rotationPointY != 0.0F || renderer.offsetZ != 0.0F) {
                matrixStackIn.translate(renderer.rotationPointX * (float) 0.0625, renderer.rotationPointY * (float) 0.0625, renderer.rotationPointZ * (float) 0.0625);
            }
        } else {
            matrixStackIn.translate(renderer.rotationPointX * (float) 0.0625, renderer.rotationPointY * (float) 0.0625, renderer.rotationPointZ * (float) 0.0625);
            if (renderer.rotateAngleZ != 0.0F) {
                matrixStackIn.multiply(RotationAxis.POSITIVE_Z.rotation(renderer.rotateAngleZ));
            }

            if (renderer.rotateAngleY != 0.0F) {
                matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotation(renderer.rotateAngleY));
            }

            if (renderer.rotateAngleX != 0.0F) {
                matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotation(renderer.rotateAngleX));
            }
        }
    }

}