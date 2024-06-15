package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.ModelDreadLichSkull;
import com.github.alexthe666.iceandfire.entity.EntityDragonLightningCharge;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;

public class RenderDragonLightningCharge extends EntityRenderer<EntityDragonLightningCharge> {

    public static final Identifier TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/charge.png");
    public static final Identifier TEXTURE_CORE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/charge_core.png");
    private static final ModelDreadLichSkull MODEL_SPIRIT = new ModelDreadLichSkull();

    public RenderDragonLightningCharge(EntityRendererFactory.Context context) {
        super(context);
    }


    @Override
    public void render(EntityDragonLightningCharge entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        float f = (float) entity.age + partialTicks;
        float yaw = entity.prevYaw + (entity.getYaw() - entity.prevYaw) * partialTicks;
        VertexConsumer ivertexbuilder2 = bufferIn.getBuffer(RenderLayer.getEyes(TEXTURE_CORE));
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderLayer.getEnergySwirl(TEXTURE, f * 0.01F, f * 0.01F));

        matrixStackIn.push();
        matrixStackIn.translate(0F, 0.5F, 0F);
        matrixStackIn.translate(0F, -0.25F, 0F);
        matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw - 180.0F));
        matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f * 20.0F));
        matrixStackIn.translate(0F, 0.25F, 0F);
        MODEL_SPIRIT.render(matrixStackIn, ivertexbuilder2, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0F, 0.5F, 0F);
        matrixStackIn.translate(0F, -0.25F, 0F);
        matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw - 180.0F));
        matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f * 15.0F));
        matrixStackIn.translate(0F, 0.25F, 0F);
        matrixStackIn.scale(1.5F, 1.5F, 1.5F);
        MODEL_SPIRIT.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0F, 0.75F, 0F);
        matrixStackIn.translate(0F, -0.25F, 0F);
        matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw - 180.0F));
        matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f * 10.0F));
        matrixStackIn.translate(0F, 0.75F, 0F);
        matrixStackIn.scale(2.5F, 2.5F, 2.5F);
        MODEL_SPIRIT.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();

        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private float interpolateValue(float start, float end, float pct) {
        return start + (end - start) * pct;
    }

    @Override
    public Identifier getTexture(EntityDragonLightningCharge entity) {
        return TEXTURE;
    }
}
