package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.client.model.ModelMyrmexPupa;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerMyrmexItem;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;


public class RenderMyrmexBase extends MobEntityRenderer<EntityMyrmexBase, AdvancedEntityModel<EntityMyrmexBase>> {

    private static final AdvancedEntityModel<EntityMyrmexBase> LARVA_MODEL = new ModelMyrmexPupa();
    private static final AdvancedEntityModel<EntityMyrmexBase> PUPA_MODEL = new ModelMyrmexPupa();
    private final AdvancedEntityModel<EntityMyrmexBase> adultModel;

    public RenderMyrmexBase(EntityRendererFactory.Context context, AdvancedEntityModel<EntityMyrmexBase> model, float shadowSize) {
        super(context, model, shadowSize);
        this.addFeature(new LayerMyrmexItem(this));
        this.adultModel = model;
    }

    @Override
    public void render(EntityMyrmexBase entityIn, float entityYaw, float partialTicks, @NotNull MatrixStack matrixStackIn, @NotNull VertexConsumerProvider bufferIn, int packedLightIn) {
        if (entityIn.getGrowthStage() == 0) {
            this.model = LARVA_MODEL;
        } else if (entityIn.getGrowthStage() == 1) {
            this.model = PUPA_MODEL;
        } else {
            this.model = this.adultModel;
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

    }

    @Override
    protected void scale(EntityMyrmexBase myrmex, @NotNull MatrixStack matrixStackIn, float partialTickTime) {
        float scale = myrmex.getModelScale();
        if (myrmex.getGrowthStage() == 0) {
            scale /= 2;
        }
        if (myrmex.getGrowthStage() == 1) {
            scale /= 1.5F;
        }
        matrixStackIn.scale(scale, scale, scale);
        if (myrmex.hasVehicle() && myrmex.getGrowthStage() < 2) {
            matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
        }
    }

    @Override
    public @NotNull Identifier getTexture(EntityMyrmexBase myrmex) {
        return myrmex.getTexture();
    }

}
