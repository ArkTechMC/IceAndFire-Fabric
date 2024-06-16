package com.github.alexthe666.iceandfire.client.render.block;

import com.github.alexthe666.iceandfire.registry.IafRenderLayers;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import org.joml.Matrix4f;

public class RenderFrozenState {
    private static final Identifier TEXTURE_0 = new Identifier("textures/block/frosted_ice_0.png");
    private static final Identifier TEXTURE_1 = new Identifier("textures/block/frosted_ice_1.png");
    private static final Identifier TEXTURE_2 = new Identifier("textures/block/frosted_ice_2.png");
    private static final Identifier TEXTURE_3 = new Identifier("textures/block/frosted_ice_3.png");

    public static void render(LivingEntity entity, MatrixStack matrixStack, VertexConsumerProvider bufferIn, int light, int frozenTicks) {
        float sideExpand = -0.125F;
        float sideExpandY = 0.325F;
        Box axisalignedbb1 = new Box(-entity.getWidth() / 2F - sideExpand, 0, -entity.getWidth() / 2F - sideExpand,
                entity.getWidth() / 2F + sideExpand, entity.getHeight() + sideExpandY, entity.getWidth() / 2F + sideExpand);
        matrixStack.push();
        renderMovingAABB(axisalignedbb1, matrixStack, bufferIn, light, 255, frozenTicks);
        matrixStack.pop();
    }

    private static Identifier getIceTexture(int ticksFrozen) {
        if (ticksFrozen < 100) {
            if (ticksFrozen < 50) {
                if (ticksFrozen < 20) {
                    return TEXTURE_3;
                }
                return TEXTURE_2;
            }
            return TEXTURE_1;
        }
        return TEXTURE_0;
    }

    public static void renderMovingAABB(Box boundingBox, MatrixStack stack, VertexConsumerProvider bufferIn, int light, int alpha, int frozenTicks) {
        RenderLayer rendertype = IafRenderLayers.getIce(getIceTexture(frozenTicks));
        VertexConsumer vertexbuffer = bufferIn.getBuffer(rendertype);
        Matrix4f matrix4f = stack.peek().getPositionMatrix();

        float maxX = (float) boundingBox.maxX * 0.425F;
        float minX = (float) boundingBox.minX * 0.425F;
        float maxY = (float) boundingBox.maxY * 0.425F;
        float minY = (float) boundingBox.minY * 0.425F;
        float maxZ = (float) boundingBox.maxZ * 0.425F;
        float minZ = (float) boundingBox.minZ * 0.425F;

        float maxU = maxZ - minZ;
        float maxV = maxY - minY;
        float minU = minZ - maxZ;
        float minV = minY - maxY;
        // X+
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).color(255, 255, 255, alpha).texture(minU, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(1.0F, 0.0F, 0F).next();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).color(255, 255, 255, alpha).texture(minU, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(1.0F, 0.0F, 0F).next();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).texture(maxU, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(1.0F, 0.0F, 0F).next();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).texture(maxU, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(1.0F, 0.0F, 0F).next();

        // X-
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).texture(minU, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(-1.0F, 0.0F, 0.0F).next();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).texture(minU, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(-1.0F, 0.0F, 0.0F).next();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).color(255, 255, 255, alpha).texture(maxU, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(-1.0F, 0.0F, 0.0F).next();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).color(255, 255, 255, alpha).texture(maxU, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(-1.0F, 0.0F, 0.0F).next();


        maxU = maxX - minX;
        maxV = maxY - minY;
        minU = minX - maxX;
        minV = minY - maxY;
        // Z-
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).color(255, 255, 255, alpha).texture(minU, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0.0F, 0.0F, -1.0F).next();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).color(255, 255, 255, alpha).texture(minU, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0.0F, 0.0F, -1.0F).next();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).color(255, 255, 255, alpha).texture(maxU, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0.0F, 0.0F, -1.0F).next();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).color(255, 255, 255, alpha).texture(maxU, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0.0F, 0.0F, -1.0F).next();

        // Z+
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).texture(minU, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0.0F, 0.0F, 1.0F).next();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).texture(minU, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0.0F, 0.0F, 1.0F).next();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).texture(maxU, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0.0F, 0.0F, 1.0F).next();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).texture(maxU, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0.0F, 0.0F, 1.0F).next();


        maxU = maxZ - minZ;
        maxV = maxX - minX;
        minU = minZ - maxZ;
        minV = minX - maxX;
        // Y+
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).texture(minU, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).color(255, 255, 255, alpha).texture(maxU, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).color(255, 255, 255, alpha).texture(maxU, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).texture(minU, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0.0F, 1.0F, 0.0F).next();

        // Y-
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).texture(minU, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0.0F, -1.0F, 0.0F).next();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).color(255, 255, 255, alpha).texture(maxU, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0.0F, -1.0F, 0.0F).next();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).color(255, 255, 255, alpha).texture(maxU, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0.0F, -1.0F, 0.0F).next();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).texture(minU, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0.0F, -1.0F, 0.0F).next();
    }
}
