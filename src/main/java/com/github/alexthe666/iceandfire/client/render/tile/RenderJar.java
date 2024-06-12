package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.ModelPixie;
import com.github.alexthe666.iceandfire.client.render.entity.RenderPixie;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;

public class RenderJar<T extends TileEntityJar> implements BlockEntityRenderer<T> {

    public static final RenderLayer TEXTURE_0 = RenderLayer.getEntityCutoutNoCull(RenderPixie.TEXTURE_0, false);
    public static final RenderLayer TEXTURE_1 = RenderLayer.getEntityCutoutNoCull(RenderPixie.TEXTURE_1, false);
    public static final RenderLayer TEXTURE_2 = RenderLayer.getEntityCutoutNoCull(RenderPixie.TEXTURE_2, false);
    public static final RenderLayer TEXTURE_3 = RenderLayer.getEntityCutoutNoCull(RenderPixie.TEXTURE_3, false);
    public static final RenderLayer TEXTURE_4 = RenderLayer.getEntityCutoutNoCull(RenderPixie.TEXTURE_4, false);
    public static final RenderLayer TEXTURE_5 = RenderLayer.getEntityCutoutNoCull(RenderPixie.TEXTURE_5, false);
    public static final RenderLayer TEXTURE_0_GLO = RenderLayer.getEyes(RenderPixie.TEXTURE_0);
    public static final RenderLayer TEXTURE_1_GLO = RenderLayer.getEyes(RenderPixie.TEXTURE_1);
    public static final RenderLayer TEXTURE_2_GLO = RenderLayer.getEyes(RenderPixie.TEXTURE_2);
    public static final RenderLayer TEXTURE_3_GLO = RenderLayer.getEyes(RenderPixie.TEXTURE_3);
    public static final RenderLayer TEXTURE_4_GLO = RenderLayer.getEyes(RenderPixie.TEXTURE_4);
    public static final RenderLayer TEXTURE_5_GLO = RenderLayer.getEyes(RenderPixie.TEXTURE_5);
    private static ModelPixie MODEL_PIXIE;

    public RenderJar(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(@NotNull T entity, float partialTicks, @NotNull MatrixStack matrixStackIn, @NotNull VertexConsumerProvider bufferIn, int combinedLightIn, int combinedOverlayIn) {
        int meta = 0;
        boolean hasPixie = false;
        if (MODEL_PIXIE == null) {
            MODEL_PIXIE = new ModelPixie();
        }
        if (entity != null && entity.getWorld() != null) {
            meta = entity.pixieType;
            hasPixie = entity.hasPixie;
        }
        if (hasPixie) {
            matrixStackIn.push();
            matrixStackIn.translate(0.5F, 1.501F, 0.5F);
            matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
            matrixStackIn.push();
            RenderLayer type = TEXTURE_0;
            RenderLayer typeGlow = switch (meta) {
                default -> {
                    type = TEXTURE_0;
                    yield TEXTURE_0_GLO;
                }
                case 1 -> {
                    type = TEXTURE_1;
                    yield TEXTURE_1_GLO;
                }
                case 2 -> {
                    type = TEXTURE_2;
                    yield TEXTURE_2_GLO;
                }
                case 3 -> {
                    type = TEXTURE_3;
                    yield TEXTURE_3_GLO;
                }
                case 4 -> {
                    type = TEXTURE_4;
                    yield TEXTURE_4_GLO;
                }
            };
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(type);
            if (entity != null && entity.getWorld() != null) {

                if (entity.hasProduced) {
                    matrixStackIn.translate(0F, 0.90F, 0F);
                } else {
                    matrixStackIn.translate(0F, 0.60F, 0F);
                }
                matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(this.interpolateRotation(entity.prevRotationYaw, entity.rotationYaw, partialTicks)));
                matrixStackIn.scale(0.50F, 0.50F, 0.50F);
                MODEL_PIXIE.animateInJar(entity.hasProduced, entity, 0);
                MODEL_PIXIE.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                MODEL_PIXIE.render(matrixStackIn, bufferIn.getBuffer(typeGlow), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            matrixStackIn.pop();
            matrixStackIn.pop();
        }
    }

    protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
        float f;

        for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F) {
        }

        while (f >= 180.0F) {
            f -= 360.0F;
        }

        return prevYawOffset + partialTicks * f;
    }


}
