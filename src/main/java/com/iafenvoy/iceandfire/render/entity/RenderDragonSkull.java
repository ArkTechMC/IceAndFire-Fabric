package com.iafenvoy.iceandfire.render.entity;

import com.iafenvoy.iceandfire.data.DragonType;
import com.iafenvoy.iceandfire.entity.EntityDragonSkull;
import com.iafenvoy.iceandfire.registry.IafRenderers;
import com.iafenvoy.uranus.client.model.TabulaModel;
import com.iafenvoy.uranus.client.model.basic.BasicModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.util.HashMap;
import java.util.Map;

public class RenderDragonSkull extends EntityRenderer<EntityDragonSkull> {
    public static final Map<DragonType, TabulaModel> MODELS = new HashMap<>();
    public static final float[] growth_stage_1 = new float[]{1F, 3F};
    public static final float[] growth_stage_2 = new float[]{3F, 7F};
    public static final float[] growth_stage_3 = new float[]{7F, 12.5F};
    public static final float[] growth_stage_4 = new float[]{12.5F, 20F};
    public static final float[] growth_stage_5 = new float[]{20F, 30F};

    static {
        MODELS.put(DragonType.FIRE, IafRenderers.FIRE_DRAGON_BASE_MODEL);
        MODELS.put(DragonType.ICE, IafRenderers.ICE_DRAGON_BASE_MODEL);
        MODELS.put(DragonType.LIGHTNING, IafRenderers.LIGHTNING_DRAGON_BASE_MODEL);
    }

    public final float[][] growth_stages;

    public RenderDragonSkull(EntityRendererFactory.Context context) {
        super(context);
        this.growth_stages = new float[][]{growth_stage_1, growth_stage_2, growth_stage_3, growth_stage_4, growth_stage_5};
    }

    private static void setRotationAngles(BasicModelPart cube, float rotX) {
        cube.rotateAngleX = rotX;
        cube.rotateAngleY = (float) 0;
        cube.rotateAngleZ = (float) 0;
    }

    @Override
    public void render(EntityDragonSkull entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        TabulaModel model = MODELS.get(DragonType.getTypeById(entity.getDragonType()));
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(entity)));
        matrixStackIn.push();
        matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-180.0F));
        matrixStackIn.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(-180.0F - entity.getYaw()));
        matrixStackIn.scale(1.0F, 1.0F, 1.0F);
        float size = this.getRenderSize(entity) / 3;
        matrixStackIn.scale(size, size, size);
        matrixStackIn.translate(0, entity.isOnWall() ? -0.24F : -0.12F, entity.isOnWall() ? 0.4F : 0.5F);
        model.resetToDefaultPose();
        setRotationAngles(model.getCube("Head"), entity.isOnWall() ? (float) Math.toRadians(50F) : 0F);
        model.getCube("Head").render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
    }

    @Override
    public Identifier getTexture(EntityDragonSkull entity) {
        return DragonType.getTypeById(entity.getDragonType()).getSkeletonTexture(entity.getDragonStage());
    }

    public float getRenderSize(EntityDragonSkull skull) {
        float step = (this.growth_stages[skull.getDragonStage() - 1][1] - this.growth_stages[skull.getDragonStage() - 1][0]) / 25;
        if (skull.getDragonAge() > 125)
            return this.growth_stages[skull.getDragonStage() - 1][0] + ((step * 25));
        return this.growth_stages[skull.getDragonStage() - 1][0] + ((step * this.getAgeFactor(skull)));
    }

    private int getAgeFactor(EntityDragonSkull skull) {
        return (skull.getDragonStage() > 1 ? skull.getDragonAge() - (25 * (skull.getDragonStage() - 1)) : skull.getDragonAge());
    }
}
