package com.iafenvoy.iceandfire.client.render.entity;

import com.iafenvoy.iceandfire.entity.EntityGhostSword;
import com.iafenvoy.iceandfire.registry.IafItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class RenderGhostSword extends EntityRenderer<EntityGhostSword> {

    public RenderGhostSword(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(EntityGhostSword entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }

    @Override
    public void render(EntityGhostSword entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevYaw, entityIn.getYaw()) - 90.0F));
        matrixStackIn.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevPitch, entityIn.getPitch())));
        matrixStackIn.translate(0, 0.5F, 0);
        matrixStackIn.scale(2F, 2F, 2F);
        matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(0.0F));
        matrixStackIn.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees((entityIn.age + partialTicks) * 30.0F));
        matrixStackIn.translate(0, -0.15F, 0);
        MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(IafItems.GHOST_SWORD), ModelTransformationMode.GROUND, 240, OverlayTexture.DEFAULT_UV, matrixStackIn, bufferIn, MinecraftClient.getInstance().world, 0);
        matrixStackIn.pop();
    }
}
