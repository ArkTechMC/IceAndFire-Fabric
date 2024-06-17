package com.iafenvoy.iceandfire.client.render.entity;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.client.model.ModelDreadLichSkull;
import com.iafenvoy.iceandfire.entity.EntityDreadLichSkull;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class RenderDreadLichSkull extends EntityRenderer<EntityDreadLichSkull> {
    public static final Identifier TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_lich_skull.png");
    private static final ModelDreadLichSkull MODEL_SPIRIT = new ModelDreadLichSkull();

    public RenderDreadLichSkull(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(EntityDreadLichSkull entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        if (entity.age > 3) {
            matrixStackIn.push();
            matrixStackIn.scale(1.5F, -1.5F, 1.5F);
            float yaw = entity.prevYaw + (entity.getYaw() - entity.prevYaw) * partialTicks;
            matrixStackIn.translate(0F, 0F, 0F);
            matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw - 180.0F));
            VertexConsumer ivertexbuilder = ItemRenderer.getItemGlintConsumer(bufferIn, RenderLayer.getEyes(TEXTURE), false, false);
            MODEL_SPIRIT.render(matrixStackIn, ivertexbuilder, 240, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.pop();
        }
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public Identifier getTexture(EntityDreadLichSkull entity) {
        return TEXTURE;
    }
}
