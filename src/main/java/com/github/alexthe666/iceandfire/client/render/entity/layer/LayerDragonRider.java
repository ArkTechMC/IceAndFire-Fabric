package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.iceandfire.client.ClientProxy;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDreadQueen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;

public class LayerDragonRider extends FeatureRenderer<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> {
    private final MobEntityRenderer render;
    private final boolean excludeDreadQueenMob;

    public LayerDragonRider(MobEntityRenderer renderIn, boolean excludeDreadQueenMob) {
        super(renderIn);
        this.render = renderIn;
        this.excludeDreadQueenMob = excludeDreadQueenMob;
    }

    @Override
    public void render(MatrixStack matrixStackIn, @NotNull VertexConsumerProvider bufferIn, int packedLightIn, EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.push();
        if (!dragon.getPassengerList().isEmpty()) {
            float dragonScale = dragon.getRenderSize() / 3;
            for (Entity passenger : dragon.getPassengerList()) {
                boolean prey = dragon.getControllingPassenger() == null || dragon.getControllingPassenger().getId() != passenger.getId();
                if (this.excludeDreadQueenMob && passenger instanceof EntityDreadQueen) {
                    prey = false;
                }
                ClientProxy.currentDragonRiders.remove(passenger.getUuid());
                float riderRot = passenger.prevYaw + (passenger.getYaw() - passenger.prevYaw) * partialTicks;
                int animationTicks = 0;
                if (dragon.getAnimation() == EntityDragonBase.ANIMATION_SHAKEPREY) {
                    animationTicks = dragon.getAnimationTick();
                }
                if (animationTicks == 0 || animationTicks >= 15) {
                    this.translateToBody(matrixStackIn);
                }
                if (prey) {
                    if (animationTicks == 0 || animationTicks >= 15 || dragon.isFlying()) {
                        this.translateToHead(matrixStackIn);
                        this.offsetPerDragonType(dragon.dragonType, matrixStackIn);
                        EntityRenderer<? super Entity> render = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(passenger);
                        EntityModel modelBase = null;
                        if (render instanceof MobEntityRenderer) {
                            modelBase = ((MobEntityRenderer<?, ?>) render).getModel();
                        }
                        if ((passenger.getHeight() > passenger.getWidth() || modelBase instanceof BipedEntityModel) && !(modelBase instanceof QuadrupedEntityModel) && !(modelBase instanceof HorseEntityModel)) {
                            matrixStackIn.translate(-0.15F * passenger.getHeight(), 0.1F * dragonScale - 0.1F * passenger.getHeight(), -0.1F * dragonScale - 0.1F * passenger.getWidth());
                            matrixStackIn.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
                            matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45.0F));
                        } else {
                            boolean horse = modelBase instanceof HorseEntityModel;
                            matrixStackIn.translate((horse ? -0.08F : -0.15F) * passenger.getWidth(), 0.1F * dragonScale - 0.15F * passenger.getWidth(), -0.1F * dragonScale - 0.1F * passenger.getWidth());
                            matrixStackIn.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90.0F));
                        }
                    } else {
                        matrixStackIn.translate(0, 0.555F * dragonScale, -0.5F * dragonScale);
                    }

                } else {
                    matrixStackIn.translate(0, -0.01F * dragonScale, -0.035F * dragonScale);
                }
                matrixStackIn.push();
                matrixStackIn.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
                matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(riderRot + 180));
                matrixStackIn.scale(1 / dragonScale, 1 / dragonScale, 1 / dragonScale);
                matrixStackIn.translate(0, -0.25F, 0);
                this.renderEntity(passenger, 0, 0, 0, 0.0F, partialTicks, matrixStackIn, bufferIn, packedLightIn);
                matrixStackIn.pop();
                ClientProxy.currentDragonRiders.add(passenger.getUuid());
            }
        }
        matrixStackIn.pop();
    }

    protected void translateToBody(MatrixStack stack) {
        this.postRender(((TabulaModel) this.render.getModel()).getCube("BodyUpper"), stack);
        this.postRender(((TabulaModel) this.render.getModel()).getCube("Neck1"), stack);
    }

    protected void translateToHead(MatrixStack stack) {
        this.postRender(((TabulaModel) this.render.getModel()).getCube("Neck2"), stack);
        this.postRender(((TabulaModel) this.render.getModel()).getCube("Neck3"), stack);
        this.postRender(((TabulaModel) this.render.getModel()).getCube("Head"), stack);
    }

    protected void postRender(AdvancedModelBox renderer, MatrixStack matrixStackIn) {
        if (renderer.rotateAngleX == 0.0F && renderer.rotateAngleY == 0.0F && renderer.rotateAngleZ == 0.0F) {
            if (renderer.rotationPointX != 0.0F || renderer.rotationPointY != 0.0F || renderer.rotationPointZ != 0.0F) {
                matrixStackIn.translate(renderer.rotationPointX * (float) 0.0625, renderer.rotationPointY * (float) 0.0625, renderer.rotationPointZ * (float) 0.0625);
            }
        } else {
            matrixStackIn.translate(renderer.rotationPointX * (float) 0.0625, renderer.rotationPointY * (float) 0.0625, renderer.rotationPointZ * (float) 0.0625);

            if (renderer.rotateAngleZ != 0.0F) {
                matrixStackIn.multiply(RotationAxis.POSITIVE_Z.rotation(renderer.rotateAngleZ));
            }

            if (renderer.rotateAngleY != 0.0F) {
                matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotation(renderer.rotateAngleY));
            }

            if (renderer.rotateAngleX != 0.0F) {
                matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotation(renderer.rotateAngleX));
            }
        }
    }

    private void offsetPerDragonType(DragonType dragonType, MatrixStack stackIn) {
        if (dragonType == DragonType.LIGHTNING) {
            stackIn.translate(0.1F, -0.2F, -0.1F);
        }
    }


    public <E extends Entity> void renderEntity(E entityIn, int x, int y, int z, float yaw, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider bufferIn, int packedLight) {
        EntityRenderer<? super E> render = null;
        EntityRenderDispatcher manager = MinecraftClient.getInstance().getEntityRenderDispatcher();
        try {
            render = manager.getRenderer(entityIn);

            if (render != null) {
                try {
                    render.render(entityIn, 0, partialTicks, matrixStack, bufferIn, packedLight);
                } catch (Throwable throwable1) {
                    throw new CrashException(CrashReport.create(throwable1, "Rendering entity in world"));
                }
            }
        } catch (Throwable throwable3) {
            CrashReport crashreport = CrashReport.create(throwable3, "Rendering entity in world");
            CrashReportSection crashreportcategory = crashreport.addElement("Entity being rendered");
            entityIn.populateCrashReport(crashreportcategory);
            CrashReportSection crashreportcategory1 = crashreport.addElement("Renderer details");
            crashreportcategory1.add("Assigned renderer", render);
            crashreportcategory1.add("Location", new BlockPos(x, y, z));
            crashreportcategory1.add("Rotation", yaw);
            crashreportcategory1.add("Delta", partialTicks);
            throw new CrashException(crashreport);
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }


}