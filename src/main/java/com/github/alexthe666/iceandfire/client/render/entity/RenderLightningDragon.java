package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.client.particle.LightningBoltData;
import com.github.alexthe666.iceandfire.client.particle.LightningRender;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityLightningDragon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class RenderLightningDragon extends RenderDragonBase {

    private final LightningRender lightningRender = new LightningRender();

    public RenderLightningDragon(EntityRendererFactory.Context context, AdvancedEntityModel model, int dragonType) {
        super(context, model, dragonType);
    }

    @Override
    public boolean shouldRender(@NotNull EntityDragonBase livingEntityIn, @NotNull Frustum camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntityIn, camera, camX, camY, camZ)) {
            return true;
        } else {
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
    public void render(@NotNull EntityDragonBase entityIn, float entityYaw, float partialTicks, @NotNull MatrixStack matrixStackIn, @NotNull VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        EntityLightningDragon lightningDragon = (EntityLightningDragon) entityIn;
        matrixStackIn.push();
        if (lightningDragon.hasLightningTarget()) {
            double dist = MinecraftClient.getInstance().player.distanceTo(lightningDragon);
            if (dist <= Math.max(256, MinecraftClient.getInstance().options.getViewDistance().getValue() * 16F)) {
                Vec3d Vector3d1 = lightningDragon.getHeadPosition();
                Vec3d Vector3d = new Vec3d(lightningDragon.getLightningTargetX(), lightningDragon.getLightningTargetY(), lightningDragon.getLightningTargetZ());
                float energyScale = 0.4F * lightningDragon.getScaleFactor();
                LightningBoltData bolt = new LightningBoltData(LightningBoltData.BoltRenderInfo.ELECTRICITY, Vector3d1, Vector3d, 15)
                    .size(0.05F * getBoundedScale(energyScale, 0.5F, 2))
                    .lifespan(4)
                    .spawn(LightningBoltData.SpawnFunction.NO_DELAY);
                lightningRender.update(null, bolt, partialTicks);
                matrixStackIn.translate(-lightningDragon.getX(), -lightningDragon.getY(), -lightningDragon.getZ());
                lightningRender.render(partialTicks, matrixStackIn, bufferIn);
            }
        }
        matrixStackIn.pop();

    }

    private static float getBoundedScale(float scale, float min, float max) {
        return min + scale * (max - min);
    }
}
