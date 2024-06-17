package com.iafenvoy.iceandfire.client.render.entity;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.client.model.ModelTideTrident;
import com.iafenvoy.iceandfire.entity.EntityTideTrident;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class RenderTideTrident extends EntityRenderer<EntityTideTrident> {
    public static final Identifier TRIDENT = new Identifier(IceAndFire.MOD_ID, "textures/models/misc/tide_trident.png");
    private final ModelTideTrident tridentModel = new ModelTideTrident();

    public RenderTideTrident(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(EntityTideTrident entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevYaw, entityIn.getYaw()) - 90.0F));
        matrixStackIn.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevPitch, entityIn.getPitch()) + 90.0F));
        VertexConsumer ivertexbuilder = net.minecraft.client.render.item.ItemRenderer.getItemGlintConsumer(bufferIn, this.tridentModel.getLayer(this.getTexture(entityIn)), false, entityIn.isEnchanted());
        this.tridentModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public Identifier getTexture(EntityTideTrident entity) {
        return TRIDENT;
    }
}