package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.ModelChainTie;
import com.github.alexthe666.iceandfire.entity.EntityChainTie;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;


public class RenderChainTie extends EntityRenderer<EntityChainTie> {
    private static final Identifier TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/misc/chain_tie.png");
    private final ModelChainTie leashKnotModel = new ModelChainTie();

    public RenderChainTie(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(EntityChainTie entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.translate(0, 0.5F, 0);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        this.leashKnotModel.setAngles(entityIn, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
        this.leashKnotModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public Identifier getTexture(EntityChainTie entity) {
        return TEXTURE;
    }
}