package com.iafenvoy.iceandfire.render.entity;

import com.iafenvoy.iceandfire.data.DragonColor;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.render.entity.layer.*;
import com.iafenvoy.uranus.client.model.AdvancedEntityModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class RenderDragonBase extends MobEntityRenderer<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> {
    public RenderDragonBase(EntityRendererFactory.Context context, AdvancedEntityModel model, int dragonType) {
        super(context, model, 0.15F);
        this.addFeature(new LayerDragonMaleOverlay(this));
        this.addFeature(new LayerDragonEyes(this));
        this.addFeature(new LayerDragonRider(this, false));
        this.addFeature(new LayerDragonBanner(this));
        this.addFeature(new LayerDragonArmor(this, dragonType));
    }

    @Override
    protected void scale(EntityDragonBase entity, MatrixStack matrixStackIn, float partialTickTime) {
        this.shadowRadius = entity.getRenderSize() / 3;
        float f7 = entity.prevDragonPitch + (entity.getDragonPitch() - entity.prevDragonPitch) * partialTickTime;
        matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f7));
        matrixStackIn.scale(this.shadowRadius, this.shadowRadius, this.shadowRadius);
    }

    @Override
    public void render(EntityDragonBase mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(EntityDragonBase entity) {
        DragonColor color = DragonColor.getById(entity.getVariant());
        return color.getTextureByEntity(entity);
    }
}
