package com.iafenvoy.iceandfire.render.entity;

import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.entity.EntityLightningDragon;
import com.iafenvoy.iceandfire.particle.LightningBoltData;
import com.iafenvoy.iceandfire.particle.LightningRender;
import com.iafenvoy.uranus.client.model.AdvancedEntityModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class RenderLightningDragon extends RenderDragonBase {
    private final LightningRender lightningRender = new LightningRender();

    public RenderLightningDragon(EntityRendererFactory.Context context, AdvancedEntityModel model, int dragonType) {
        super(context, model, dragonType);
    }

    private static float getBoundedScale(float scale) {
        return (float) 0.5 + scale * ((float) 2 - (float) 0.5);
    }

    @Override
    public boolean shouldRender(EntityDragonBase livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntityIn, camera, camX, camY, camZ))
            return true;
        else {
            EntityLightningDragon lightningDragon = (EntityLightningDragon) livingEntityIn;
            if (lightningDragon.hasLightningTarget()) {
                Vec3d Vector3d1 = lightningDragon.getHeadPosition();
                Vec3d Vector3d = new Vec3d(lightningDragon.getLightningTargetX(), lightningDragon.getLightningTargetY(), lightningDragon.getLightningTargetZ());
                return camera.isVisible(new Box(Vector3d1.x, Vector3d1.y, Vector3d1.z, Vector3d.x, Vector3d.y, Vector3d.z));
            }
            return false;
        }
    }

    @Override
    public void render(EntityDragonBase entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        EntityLightningDragon lightningDragon = (EntityLightningDragon) entityIn;
        matrixStackIn.push();
        if (lightningDragon.hasLightningTarget()) {
            assert MinecraftClient.getInstance().player != null;
            double dist = MinecraftClient.getInstance().player.distanceTo(lightningDragon);
            if (dist <= Math.max(256, MinecraftClient.getInstance().options.getViewDistance().getValue() * 16F)) {
                Vec3d Vector3d1 = lightningDragon.getHeadPosition();
                Vec3d Vector3d = new Vec3d(lightningDragon.getLightningTargetX(), lightningDragon.getLightningTargetY(), lightningDragon.getLightningTargetZ());
                float energyScale = 0.4F * lightningDragon.getScaleFactor();
                LightningBoltData bolt = new LightningBoltData(LightningBoltData.BoltRenderInfo.ELECTRICITY, Vector3d1, Vector3d, 15)
                        .size(0.05F * getBoundedScale(energyScale))
                        .lifespan(4)
                        .spawn(LightningBoltData.SpawnFunction.NO_DELAY);
                this.lightningRender.update(null, bolt, partialTicks);
                matrixStackIn.translate(-lightningDragon.getX(), -lightningDragon.getY(), -lightningDragon.getZ());
                this.lightningRender.render(partialTicks, matrixStackIn, bufferIn);
            }
        }
        matrixStackIn.pop();
    }
}
