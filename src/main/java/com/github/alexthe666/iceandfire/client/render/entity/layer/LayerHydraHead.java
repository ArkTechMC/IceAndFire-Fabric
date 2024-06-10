package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.ModelHydraBody;
import com.github.alexthe666.iceandfire.client.model.ModelHydraHead;
import com.github.alexthe666.iceandfire.client.render.entity.RenderHydra;
import com.github.alexthe666.iceandfire.entity.EntityHydra;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;

public class LayerHydraHead extends FeatureRenderer<EntityHydra, ModelHydraBody> {
    public static final Identifier TEXTURE_STONE = new Identifier(IceAndFire.MOD_ID, "textures/models/hydra/stone.png");
    private static final float[][] TRANSLATE = new float[][]{
            {0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 1 total heads
            {-0.15F, 0.15F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 2 total heads
            {-0.3F, 0F, 0.3F, 0F, 0F, 0F, 0F, 0F, 0F},// 3 total heads
            {-0.4F, -0.1F, 0.1F, 0.4F, 0F, 0F, 0F, 0F, 0F},//etc...
            {-0.5F, -0.2F, 0F, 0.2F, 0.5F, 0F, 0F, 0F, 0F},
            {-0.7F, -0.4F, -0.2F, 0.2F, 0.4F, 0.7F, 0F, 0F, 0F},
            {-0.7F, -0.4F, -0.2F, 0, 0.2F, 0.4F, 0.7F, 0F, 0F},
            {-0.6F, -0.4F, -0.2F, -0.1F, 0.1F, 0.2F, 0.4F, 0.6F, 0F},
            {-0.6F, -0.4F, -0.2F, -0.1F, 0.0F, 0.1F, 0.2F, 0.4F, 0.6F},
    };
    private static final float[][] ROTATE = new float[][]{
            {0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 1 total heads
            {10F, -10F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 2 total heads
            {10F, 0F, -10F, 0F, 0F, 0F, 0F, 0F, 0F},// 3 total heads
            {25F, 10F, -10F, -25F, 0F, 0F, 0F, 0F, 0F},//etc...
            {30F, 15F, 0F, -15F, -30F, 0F, 0F, 0F, 0F},
            {40F, 25F, 5F, -5F, -25F, -40F, 0F, 0F, 0F},
            {40F, 30F, 15F, 0F, -15F, -30F, -40F, 0F, 0F},
            {45F, 30F, 20F, 5F, -5F, -20F, -30F, -45F, 0F},
            {50F, 37F, 25F, 15F, 0, -15F, -25F, -37F, -50F},
    };
    private static final ModelHydraHead[] modelArr = new ModelHydraHead[EntityHydra.HEADS];

    static {
        for (int i = 0; i < modelArr.length; i++) {
            modelArr[i] = new ModelHydraHead(i);
        }
    }

    private final RenderHydra renderer;

    public LayerHydraHead(RenderHydra renderer) {
        super(renderer);
        this.renderer = renderer;

    }

    public static void renderHydraHeads(ModelHydraBody model, boolean stone, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, EntityHydra hydra, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.push();
        int heads = hydra.getHeadCount();
        translateToBody(model, matrixStackIn);
        RenderLayer type = RenderLayer.getEntityCutout(stone ? TEXTURE_STONE : getHeadTexture(hydra));
        for (int head = 1; head <= heads; head++) {
            matrixStackIn.push();
            float bodyWidth = 0.5F;
            matrixStackIn.translate(TRANSLATE[heads - 1][head - 1] * bodyWidth, 0, 0);
            matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(ROTATE[heads - 1][head - 1]));
            modelArr[head - 1].setAngles(hydra, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            modelArr[head - 1].render(matrixStackIn, bufferIn.getBuffer(type), packedLightIn, LivingEntityRenderer.getOverlay(hydra, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.pop();
        }
        matrixStackIn.pop();
    }

    public static Identifier getHeadTexture(EntityHydra gorgon) {
        switch (gorgon.getVariant()) {
            default:
                return RenderHydra.TEXUTURE_0;
            case 1:
                return RenderHydra.TEXUTURE_1;
            case 2:
                return RenderHydra.TEXUTURE_2;
        }
    }

    protected static void translateToBody(ModelHydraBody model, MatrixStack stack) {
        postRender(model.BodyUpper, stack, 0.0625F);
    }

    protected static void postRender(AdvancedModelBox renderer, MatrixStack matrixStackIn, float scale) {
        if (renderer.rotateAngleX == 0.0F && renderer.rotateAngleY == 0.0F && renderer.rotateAngleZ == 0.0F) {
            if (renderer.rotationPointX != 0.0F || renderer.rotationPointY != 0.0F || renderer.rotateAngleZ != 0.0F) {
                matrixStackIn.translate(renderer.rotationPointX * scale, renderer.rotationPointY * scale, renderer.rotateAngleZ * scale);
            }
        } else {
            matrixStackIn.translate(renderer.rotationPointX * scale, renderer.rotationPointY * scale, renderer.rotateAngleZ * scale);
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

    @Override
    public void render(@NotNull MatrixStack matrixStackIn, @NotNull VertexConsumerProvider bufferIn, int packedLightIn, EntityHydra entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isInvisible()) {
            return;
        }
        renderHydraHeads(this.renderer.getModel(), false, matrixStackIn, bufferIn, packedLightIn, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
    }

    @Override
    public @NotNull Identifier getTexture(EntityHydra gorgon) {
        switch (gorgon.getVariant()) {
            default:
                return RenderHydra.TEXUTURE_0;
            case 1:
                return RenderHydra.TEXUTURE_1;
            case 2:
                return RenderHydra.TEXUTURE_2;
        }
    }
}