package com.iafenvoy.iceandfire.render.entity;

import com.google.common.collect.Maps;
import com.iafenvoy.uranus.client.model.AdvancedEntityModel;
import com.iafenvoy.uranus.client.model.TabulaModel;
import com.iafenvoy.uranus.client.model.basic.BasicModelPart;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityMobSkull;
import com.iafenvoy.iceandfire.enums.EnumSkullType;
import com.iafenvoy.iceandfire.render.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.util.Locale;
import java.util.Map;

public class RenderMobSkull extends EntityRenderer<EntityMobSkull> {
    private static final Map<String, Identifier> SKULL_TEXTURE_CACHE = Maps.newHashMap();
    private final ModelHippogryph hippogryphModel;
    private final ModelCyclops cyclopsModel;
    private final ModelCockatrice cockatriceModel;
    private final ModelStymphalianBird stymphalianBirdModel;
    private final ModelTroll trollModel;
    private final ModelAmphithere amphithereModel;
    private final ModelHydraHead hydraModel;
    private final TabulaModel seaSerpentModel;

    public RenderMobSkull(EntityRendererFactory.Context context, AdvancedEntityModel seaSerpentModel) {
        super(context);
        this.hippogryphModel = new ModelHippogryph();
        this.cyclopsModel = new ModelCyclops();
        this.cockatriceModel = new ModelCockatrice();
        this.stymphalianBirdModel = new ModelStymphalianBird();
        this.trollModel = new ModelTroll();
        this.amphithereModel = new ModelAmphithere();
        this.seaSerpentModel = (TabulaModel) seaSerpentModel;
        this.hydraModel = new ModelHydraHead(0);
    }

    private static void setRotationAngles(BasicModelPart cube, float rotX) {
        cube.rotateAngleX = rotX;
        cube.rotateAngleY = (float) 0;
        cube.rotateAngleZ = (float) 0;
    }

    @Override
    public void render(EntityMobSkull entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.push();
        matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-180.0F));
        matrixStackIn.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180.0F - entity.getYaw()));
        float f = 0.0625F;
        float size = 1.0F;
        matrixStackIn.scale(size, size, size);
        matrixStackIn.translate(0, entity.isOnWall() ? -0.24F : -0.12F, 0.5F);
        this.renderForEnum(entity.getSkullType(), entity.isOnWall(), matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.pop();
    }

    private void renderForEnum(EnumSkullType skull, boolean onWall, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderLayer.getEntityTranslucent(this.getSkullTexture(skull)));
        switch (skull) {
            case HIPPOGRYPH -> {
                matrixStackIn.translate(0, -0.0F, -0.2F);
                matrixStackIn.scale(1.2F, 1.2F, 1.2F);
                this.hippogryphModel.resetToDefaultPose();
                setRotationAngles(this.hippogryphModel.Head, onWall ? (float) Math.toRadians(50F) : (float) Math.toRadians(-5));
                this.hippogryphModel.Head.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            case CYCLOPS -> {
                matrixStackIn.translate(0, 1.8F, -0.5F);
                matrixStackIn.scale(2.25F, 2.25F, 2.25F);
                this.cyclopsModel.resetToDefaultPose();
                setRotationAngles(this.cyclopsModel.Head, onWall ? (float) Math.toRadians(50F) : 0F);
                this.cyclopsModel.Head.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            case COCKATRICE -> {
                if (onWall) matrixStackIn.translate(0, 0F, 0.35F);
                this.cockatriceModel.resetToDefaultPose();
                setRotationAngles(this.cockatriceModel.head, onWall ? (float) Math.toRadians(50F) : 0F);
                this.cockatriceModel.head.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            case STYMPHALIAN -> {
                if (!onWall) matrixStackIn.translate(0, 0F, -0.35F);
                this.stymphalianBirdModel.resetToDefaultPose();
                setRotationAngles(this.stymphalianBirdModel.HeadBase, onWall ? (float) Math.toRadians(50F) : 0F);
                this.stymphalianBirdModel.HeadBase.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            case TROLL -> {
                matrixStackIn.translate(0, 1F, -0.35F);
                if (onWall) matrixStackIn.translate(0, 0F, 0.35F);
                this.trollModel.resetToDefaultPose();
                setRotationAngles(this.trollModel.head, onWall ? (float) Math.toRadians(50F) : (float) Math.toRadians(-20));
                this.trollModel.head.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            case AMPHITHERE -> {
                matrixStackIn.translate(0, -0.2F, 0.7F);
                matrixStackIn.scale(2.0F, 2.0F, 2.0F);
                this.amphithereModel.resetToDefaultPose();
                setRotationAngles(this.amphithereModel.Head, onWall ? (float) Math.toRadians(50F) : 0F);
                this.amphithereModel.Head.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            case SEASERPENT -> {
                matrixStackIn.translate(0, -0.35F, 0.8F);
                matrixStackIn.scale(2.5F, 2.5F, 2.5F);
                this.seaSerpentModel.resetToDefaultPose();
                setRotationAngles(this.seaSerpentModel.getCube("Head"), onWall ? (float) Math.toRadians(50F) : 0F);
                this.seaSerpentModel.getCube("Head").render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            case HYDRA -> {
                matrixStackIn.translate(0, -0.2F, -0.1F);
                matrixStackIn.scale(2.0F, 2.0F, 2.0F);
                this.hydraModel.resetToDefaultPose();
                setRotationAngles(this.hydraModel.Head1, onWall ? (float) Math.toRadians(50F) : 0F);
                this.hydraModel.Head1.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public Identifier getTexture(EntityMobSkull entity) {
        return this.getSkullTexture(entity.getSkullType());
    }

    public Identifier getSkullTexture(EnumSkullType skull) {
        String s = IceAndFire.MOD_ID + ":textures/models/skulls/skull_" + skull.name().toLowerCase(Locale.ROOT) + ".png";
        Identifier resourcelocation = SKULL_TEXTURE_CACHE.get(s);
        if (resourcelocation == null) {
            resourcelocation = new Identifier(s);
            SKULL_TEXTURE_CACHE.put(s, resourcelocation);
        }
        return resourcelocation;
    }

}
