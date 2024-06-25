package com.iafenvoy.iceandfire.render.block;

import com.iafenvoy.iceandfire.entity.block.BlockEntityPodium;
import com.iafenvoy.iceandfire.enums.EnumDragonColor;
import com.iafenvoy.iceandfire.item.ItemDragonEgg;
import com.iafenvoy.iceandfire.item.ItemMyrmexEgg;
import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.iceandfire.render.entity.RenderDragonEgg;
import com.iafenvoy.iceandfire.render.entity.RenderMyrmexEgg;
import com.iafenvoy.iceandfire.render.model.ModelDragonEgg;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class RenderPodium<T extends BlockEntityPodium> implements BlockEntityRenderer<T> {
    public RenderPodium(BlockEntityRendererFactory.Context context) {
    }

    protected static RenderLayer getEggTexture(EnumDragonColor type) {
        return switch (type) {
            default -> RenderLayer.getEntityCutout(RenderDragonEgg.EGG_RED);
            case GREEN -> RenderLayer.getEntityCutout(RenderDragonEgg.EGG_GREEN);
            case BRONZE -> RenderLayer.getEntityCutout(RenderDragonEgg.EGG_BRONZE);
            case GRAY -> RenderLayer.getEntityCutout(RenderDragonEgg.EGG_GREY);
            case BLUE -> RenderLayer.getEntityCutout(RenderDragonEgg.EGG_BLUE);
            case WHITE -> RenderLayer.getEntityCutout(RenderDragonEgg.EGG_WHITE);
            case SAPPHIRE -> RenderLayer.getEntityCutout(RenderDragonEgg.EGG_SAPPHIRE);
            case SILVER -> RenderLayer.getEntityCutout(RenderDragonEgg.EGG_SILVER);
            case ELECTRIC -> RenderLayer.getEntityCutout(RenderDragonEgg.EGG_ELECTRIC);
            case AMETHYST -> RenderLayer.getEntityCutout(RenderDragonEgg.EGG_AMYTHEST);
            case COPPER -> RenderLayer.getEntityCutout(RenderDragonEgg.EGG_COPPER);
            case BLACK -> RenderLayer.getEntityCutout(RenderDragonEgg.EGG_BLACK);
        };
    }

    @Override
    public void render(T entity, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ModelDragonEgg model = new ModelDragonEgg();
        if (!entity.getStack(0).isEmpty()) {
            if (entity.getStack(0).getItem() instanceof ItemDragonEgg item) {
                matrixStackIn.push();
                matrixStackIn.translate(0.5F, 0.475F, 0.5F);
                matrixStackIn.push();
                matrixStackIn.push();
                model.renderPodium();
                model.render(matrixStackIn, bufferIn.getBuffer(RenderPodium.getEggTexture(item.type)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                matrixStackIn.pop();
                matrixStackIn.pop();
                matrixStackIn.pop();
            } else if (entity.getStack(0).getItem() instanceof ItemMyrmexEgg) {
                boolean jungle = entity.getStack(0).getItem() == IafItems.MYRMEX_JUNGLE_EGG;
                matrixStackIn.push();
                matrixStackIn.translate(0.5F, 0.475F, 0.5F);
                matrixStackIn.push();
                matrixStackIn.push();
                model.renderPodium();
                model.render(matrixStackIn, bufferIn.getBuffer(RenderLayer.getEntityCutout(jungle ? RenderMyrmexEgg.EGG_JUNGLE : RenderMyrmexEgg.EGG_DESERT)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                matrixStackIn.pop();
                matrixStackIn.pop();
                matrixStackIn.pop();
            } else if (!entity.getStack(0).isEmpty()) {
                matrixStackIn.push();
                float f2 = ((float) entity.prevTicksExisted + (entity.ticksExisted - entity.prevTicksExisted) * partialTicks);
                float f3 = MathHelper.sin(f2 / 10.0F) * 0.1F + 0.1F;
                matrixStackIn.translate(0.5F, 1.55F + f3, 0.5F);
                float f4 = (f2 / 20.0F);
                matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotation(f4));
                matrixStackIn.push();
                matrixStackIn.translate(0, 0.2F, 0);
                matrixStackIn.scale(0.65F, 0.65F, 0.65F);
                MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(0), ModelTransformationMode.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn, entity.getWorld(), 0);
                matrixStackIn.pop();
                matrixStackIn.pop();
            }
        }
    }
}
