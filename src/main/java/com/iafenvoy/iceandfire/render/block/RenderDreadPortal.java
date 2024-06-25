package com.iafenvoy.iceandfire.render.block;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.block.BlockEntityDreadPortal;
import com.iafenvoy.iceandfire.registry.IafRenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.joml.Matrix4f;

public class RenderDreadPortal<T extends BlockEntityDreadPortal> implements BlockEntityRenderer<T> {
    public static final Identifier DREAD_PORTAL_BACKGROUND = new Identifier(IceAndFire.MOD_ID, "textures/environment/dread_portal_background.png");
    public static final Identifier DREAD_PORTAL = new Identifier(IceAndFire.MOD_ID, "textures/environment/dread_portal.png");

    public RenderDreadPortal(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Matrix4f matrix4f = matrixStackIn.peek().getPositionMatrix();
        this.renderCube(tileEntityIn, matrix4f, bufferIn.getBuffer(this.renderType()));
    }

    private void renderCube(T tileEntityIn, Matrix4f matrix4f, VertexConsumer consumer) {
        float f = 1.0F;
        float f1 = 1.0F;
        this.renderFace(tileEntityIn, matrix4f, consumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
        this.renderFace(tileEntityIn, matrix4f, consumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
        this.renderFace(tileEntityIn, matrix4f, consumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
        this.renderFace(tileEntityIn, matrix4f, consumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
        this.renderFace(tileEntityIn, matrix4f, consumer, 0.0F, 1.0F, f, f, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
        this.renderFace(tileEntityIn, matrix4f, consumer, 0.0F, 1.0F, f1, f1, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
    }

    private void renderFace(T model, Matrix4f matrix4f, VertexConsumer consumer, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Direction direction) {
        float r = 1.0F;
        float g = 1.0F;
        float b = 1.0F;
        if (model.shouldRenderFace(direction)) {
            consumer.vertex(matrix4f, x1, y1, z1).color(r, g, b, 1.0F).next();
            consumer.vertex(matrix4f, x2, y1, z2).color(r, g, b, 1.0F).next();
            consumer.vertex(matrix4f, x2, y2, z3).color(r, g, b, 1.0F).next();
            consumer.vertex(matrix4f, x1, y2, z4).color(r, g, b, 1.0F).next();
        }
    }

    protected RenderLayer renderType() {
        return IafRenderLayers.getDreadlandsPortal();
    }
}