package com.github.alexthe666.iceandfire.client.particle;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class CockatriceBeamRender {

    public static final RenderLayer TEXTURE_BEAM = RenderLayer.getEntityCutoutNoCull(new Identifier(IceAndFire.MOD_ID, "textures/models/cockatrice/beam.png"));

    private static void vertex(VertexConsumer p_229108_0_, Matrix4f p_229108_1_, Matrix3f p_229108_2_, float p_229108_3_, float p_229108_4_, float p_229108_5_, int p_229108_6_, int p_229108_7_, int p_229108_8_, float p_229108_9_, float p_229108_10_) {
        p_229108_0_.vertex(p_229108_1_, p_229108_3_, p_229108_4_, p_229108_5_).color(p_229108_6_, p_229108_7_, p_229108_8_, 255).texture(p_229108_9_, p_229108_10_).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(p_229108_2_, 0.0F, 1.0F, 0.0F).next();
    }

    public static void render(Entity entityIn, Entity targetEntity, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, float partialTicks) {
        float f = 1;
        if (entityIn instanceof EntityCockatrice)
            f = (((EntityCockatrice) entityIn).getAttackAnimationScale(partialTicks));

        float f1 = (float) entityIn.getWorld().getTime() + partialTicks;
        float f2 = f1 * 0.5F % 1.0F;
        float f3 = entityIn.getStandingEyeHeight();
        matrixStackIn.push();
        matrixStackIn.translate(0.0D, f3, 0.0D);
        Vec3d Vector3d = getPosition(targetEntity, (double) targetEntity.getHeight() * 0.5D, partialTicks);
        Vec3d Vector3d1 = getPosition(entityIn, f3, partialTicks);
        Vec3d Vector3d2 = Vector3d.subtract(Vector3d1);
        float f4 = (float) (Vector3d2.length() + 1.0D);
        Vector3d2 = Vector3d2.normalize();
        float f5 = (float) Math.acos(Vector3d2.y);
        float f6 = (float) Math.atan2(Vector3d2.z, Vector3d2.x);
        matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotation((float) Math.PI / 2.0F - f6));
        matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotation(f5));
        int i = 1;
        float f7 = f1 * 0.05F * -1.5F;
        float f8 = f * f;
        int j = 64 + (int) (f8 * 191.0F);
        int k = 32 + (int) (f8 * 191.0F);
        int l = 128 - (int) (f8 * 64.0F);
        float f9 = 0.2F;
        float f10 = 0.282F;
        float f11 = MathHelper.cos(f7 + 2.3561945F) * 0.282F;
        float f12 = MathHelper.sin(f7 + 2.3561945F) * 0.282F;
        float f13 = MathHelper.cos(f7 + ((float) Math.PI / 4F)) * 0.282F;
        float f14 = MathHelper.sin(f7 + ((float) Math.PI / 4F)) * 0.282F;
        float f15 = MathHelper.cos(f7 + 3.926991F) * 0.282F;
        float f16 = MathHelper.sin(f7 + 3.926991F) * 0.282F;
        float f17 = MathHelper.cos(f7 + 5.4977875F) * 0.282F;
        float f18 = MathHelper.sin(f7 + 5.4977875F) * 0.282F;
        float f19 = MathHelper.cos(f7 + (float) Math.PI) * 0.2F;
        float f20 = MathHelper.sin(f7 + (float) Math.PI) * 0.2F;
        float f21 = MathHelper.cos(f7 + 0.0F) * 0.2F;
        float f22 = MathHelper.sin(f7 + 0.0F) * 0.2F;
        float f23 = MathHelper.cos(f7 + ((float) Math.PI / 2F)) * 0.2F;
        float f24 = MathHelper.sin(f7 + ((float) Math.PI / 2F)) * 0.2F;
        float f25 = MathHelper.cos(f7 + ((float) Math.PI * 1.5F)) * 0.2F;
        float f26 = MathHelper.sin(f7 + ((float) Math.PI * 1.5F)) * 0.2F;
        float f27 = 0.0F;
        float f28 = 0.4999F;
        float f29 = -1.0F + f2;
        float f30 = f4 * 2.5F + f29;
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(TEXTURE_BEAM);
        MatrixStack.Entry matrixstack$entry = matrixStackIn.peek();
        Matrix4f matrix4f = matrixstack$entry.getPositionMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormalMatrix();
        vertex(ivertexbuilder, matrix4f, matrix3f, f19, f4, f20, j, k, l, 0.4999F, f30);
        vertex(ivertexbuilder, matrix4f, matrix3f, f19, 0.0F, f20, j, k, l, 0.4999F, f29);
        vertex(ivertexbuilder, matrix4f, matrix3f, f21, 0.0F, f22, j, k, l, 0.0F, f29);
        vertex(ivertexbuilder, matrix4f, matrix3f, f21, f4, f22, j, k, l, 0.0F, f30);
        vertex(ivertexbuilder, matrix4f, matrix3f, f23, f4, f24, j, k, l, 0.4999F, f30);
        vertex(ivertexbuilder, matrix4f, matrix3f, f23, 0.0F, f24, j, k, l, 0.4999F, f29);
        vertex(ivertexbuilder, matrix4f, matrix3f, f25, 0.0F, f26, j, k, l, 0.0F, f29);
        vertex(ivertexbuilder, matrix4f, matrix3f, f25, f4, f26, j, k, l, 0.0F, f30);
        float f31 = 0.0F;
        if (entityIn.age % 2 == 0) {
            f31 = 0.5F;
        }

        vertex(ivertexbuilder, matrix4f, matrix3f, f11, f4, f12, j, k, l, 0.5F, f31 + 0.5F);
        vertex(ivertexbuilder, matrix4f, matrix3f, f13, f4, f14, j, k, l, 1.0F, f31 + 0.5F);
        vertex(ivertexbuilder, matrix4f, matrix3f, f17, f4, f18, j, k, l, 1.0F, f31);
        vertex(ivertexbuilder, matrix4f, matrix3f, f15, f4, f16, j, k, l, 0.5F, f31);
        matrixStackIn.pop();
    }

    private static Vec3d getPosition(Entity LivingEntityIn, double p_177110_2_, float p_177110_4_) {
        double d0 = LivingEntityIn.lastRenderX + (LivingEntityIn.getX() - LivingEntityIn.lastRenderX) * (double) p_177110_4_;
        double d1 = p_177110_2_ + LivingEntityIn.lastRenderY + (LivingEntityIn.getY() - LivingEntityIn.lastRenderY) * (double) p_177110_4_;
        double d2 = LivingEntityIn.lastRenderZ + (LivingEntityIn.getZ() - LivingEntityIn.lastRenderZ) * (double) p_177110_4_;
        return new Vec3d(d0, d1, d2);
    }

}
