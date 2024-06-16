package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.ModelGhost;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.github.alexthe666.iceandfire.registry.IafRenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderGhost extends MobEntityRenderer<EntityGhost, ModelGhost> {

    public static final Identifier TEXTURE_0 = new Identifier(IceAndFire.MOD_ID, "textures/models/ghost/ghost_white.png");
    public static final Identifier TEXTURE_1 = new Identifier(IceAndFire.MOD_ID, "textures/models/ghost/ghost_blue.png");
    public static final Identifier TEXTURE_2 = new Identifier(IceAndFire.MOD_ID, "textures/models/ghost/ghost_green.png");
    public static final Identifier TEXTURE_SHOPPING_LIST = new Identifier(IceAndFire.MOD_ID, "textures/models/ghost/haunted_shopping_list.png");

    public RenderGhost(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ModelGhost(0.0F), 0.55F);

    }

    public static Identifier getGhostOverlayForType(int ghost) {
        return switch (ghost) {
            case 1 -> TEXTURE_1;
            case 2 -> TEXTURE_2;
            case -1 -> TEXTURE_SHOPPING_LIST;
            default -> TEXTURE_0;
        };
    }

    @Override
    public void render(EntityGhost entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        this.shadowRadius = 0;
        //TODO: Event
//        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<EntityGhost, ModelGhost>(entityIn, this, partialTicks, matrixStackIn, bufferIn, packedLightIn)))
//            return;
        matrixStackIn.push();
        this.model.handSwingProgress = this.getHandSwingProgress(entityIn, partialTicks);

        boolean shouldSit = entityIn.hasVehicle() && entityIn.getVehicle() != null;
        this.model.riding = shouldSit;
        this.model.child = entityIn.isBaby();
        float f = MathHelper.lerpAngleDegrees(partialTicks, entityIn.prevBodyYaw, entityIn.bodyYaw);
        float f1 = MathHelper.lerpAngleDegrees(partialTicks, entityIn.prevHeadYaw, entityIn.headYaw);
        float f2 = f1 - f;
        if (shouldSit && entityIn.getVehicle() instanceof LivingEntity livingentity) {
            f = MathHelper.lerpAngleDegrees(partialTicks, livingentity.prevBodyYaw, livingentity.bodyYaw);
            f2 = f1 - f;
            float f3 = MathHelper.wrapDegrees(f2);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f = f1 - f3;
            if (f3 * f3 > 2500.0F) {
                f += f3 * 0.2F;
            }

            f2 = f1 - f;
        }

        float f6 = MathHelper.lerp(partialTicks, entityIn.prevPitch, entityIn.getPitch());
        if (entityIn.getPose() == EntityPose.SLEEPING) {
            Direction direction = entityIn.getSleepingDirection();
            if (direction != null) {
                float f4 = entityIn.getEyeHeight(EntityPose.STANDING) - 0.1F;
                matrixStackIn.translate((float) (-direction.getOffsetX()) * f4, 0.0D, (float) (-direction.getOffsetZ()) * f4);
            }
        }

        float f7 = this.getAnimationProgress(entityIn, partialTicks);
        this.setupTransforms(entityIn, matrixStackIn, f7, f, partialTicks);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        this.scale(entityIn, matrixStackIn, partialTicks);
        matrixStackIn.translate(0.0D, -1.501F, 0.0D);
        float f8 = 0.0F;
        float f5 = 0.0F;
        if (!shouldSit && entityIn.isAlive()) {
            f8 = entityIn.limbAnimator.getSpeed();
            f5 = entityIn.limbAnimator.getPos();
            if (entityIn.isBaby()) {
                f5 *= 3.0F;
            }

            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }

        this.model.animateModel(entityIn, f5, f8, partialTicks);
        this.model.setAngles(entityIn, f5, f8, f7, f2, f6);
        float alphaForRender = this.getAlphaForRender(entityIn, partialTicks);
        RenderLayer rendertype = entityIn.isDaytimeMode() ? IafRenderLayers.getGhostDaytime(this.getTexture(entityIn)) : IafRenderLayers.getGhost(this.getTexture(entityIn));//this.getRenderType(entityIn, flag, flag1, flag2);
        if (!entityIn.isInvisible()) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(rendertype);
            int i = getOverlay(entityIn, this.getAnimationCounter(entityIn, partialTicks));
            if (entityIn.isHauntedShoppingList()) {
                matrixStackIn.push();
                matrixStackIn.translate(0, 0.8F + MathHelper.sin((entityIn.age + partialTicks) * 0.15F) * 0.1F, 0);
                matrixStackIn.scale(0.6F, 0.6F, 0.6F);
                matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
                {
                    matrixStackIn.push();
                    MatrixStack.Entry matrixstack$entry = matrixStackIn.peek();
                    Matrix4f matrix4f = matrixstack$entry.getPositionMatrix();
                    Matrix3f matrix3f = matrixstack$entry.getNormalMatrix();
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), -1, -2, 0, 1F, 0.0F, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), 1, -2, 0, 0.5F, 0.0F, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), 1, 2, 0, 0.5F, 1, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), -1, 2, 0, 1F, 1, 0, 1, 0, 240);
                    matrixStackIn.pop();
                }
                matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
                {
                    matrixStackIn.push();
                    MatrixStack.Entry matrixstack$entry = matrixStackIn.peek();
                    Matrix4f matrix4f = matrixstack$entry.getPositionMatrix();
                    Matrix3f matrix3f = matrixstack$entry.getNormalMatrix();
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), -1, -2, 0, 0.0F, 0.0F, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), 1, -2, 0, 0.5F, 0.0F, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), 1, 2, 0, 0.5F, 1, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), -1, 2, 0, 0.0F, 1, 0, 1, 0, 240);
                    matrixStackIn.pop();
                }
                matrixStackIn.pop();

            } else {
                this.model.render(matrixStackIn, ivertexbuilder, 240, i, 1.0F, 1.0F, 1.0F, alphaForRender);
            }
        }

        if (!entityIn.isSpectator()) {
            for (FeatureRenderer<EntityGhost, ModelGhost> layerrenderer : this.features) {
                layerrenderer.render(matrixStackIn, bufferIn, packedLightIn, entityIn, f5, f8, partialTicks, f7, f2, f6);
            }
        }

        matrixStackIn.pop();
        //TODO: Event
//        net.minecraftforge.client.event.RenderNameTagEvent renderNameplateEvent = new net.minecraftforge.client.event.RenderNameTagEvent(entityIn, entityIn.getDisplayName(), this, matrixStackIn, bufferIn, packedLightIn, partialTicks);
//        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
//        if (renderNameplateEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameplateEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.hasLabel(entityIn))) {
//            this.renderLabelIfPresent(entityIn, renderNameplateEvent.getContent(), matrixStackIn, bufferIn, packedLightIn);
//        }
//        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<EntityGhost, ModelGhost>(entityIn, this, partialTicks, matrixStackIn, bufferIn, packedLightIn));
    }

    @Override
    protected float getLyingAngle(EntityGhost ghost) {
        return 0.0F;
    }

    public float getAlphaForRender(EntityGhost entityIn, float partialTicks) {
        if (entityIn.isDaytimeMode()) {
            return MathHelper.clamp((101 - Math.min(entityIn.getDaytimeCounter(), 100)) / 100F, 0, 1);
        }
        return MathHelper.clamp((MathHelper.sin((entityIn.age + partialTicks) * 0.1F) + 1F) * 0.5F + 0.1F, 0F, 1F);
    }

    @Override
    public void scale(EntityGhost LivingEntityIn, MatrixStack stack, float partialTickTime) {
    }

    @Override
    public Identifier getTexture(EntityGhost ghost) {
        return switch (ghost.getColor()) {
            case 1 -> TEXTURE_1;
            case 2 -> TEXTURE_2;
            case -1 -> TEXTURE_SHOPPING_LIST;
            default -> TEXTURE_0;
        };
    }

    public void drawVertex(Matrix4f stack, Matrix3f normal, VertexConsumer builder, int packedRed, int alphaInt, int x, int y, int z, float u, float v, int lightmap, int lightmap3, int lightmap2, int lightmap4) {
        builder.vertex(stack, (float) x, (float) y, (float) z).color(255, 255, 255, alphaInt).texture(u, v).overlay(packedRed).light(lightmap4).normal(normal, (float) lightmap, (float) lightmap2, (float) lightmap3).next();
    }
}
