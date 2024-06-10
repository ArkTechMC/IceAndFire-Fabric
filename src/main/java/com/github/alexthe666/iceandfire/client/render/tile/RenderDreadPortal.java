package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.render.IafRenderType;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDreadPortal;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class RenderDreadPortal<T extends TileEntityDreadPortal> implements BlockEntityRenderer<T> {
    public static final Identifier DREAD_PORTAL_BACKGROUND = new Identifier(IceAndFire.MOD_ID, "textures/environment/dread_portal_background.png");
    public static final Identifier DREAD_PORTAL = new Identifier(IceAndFire.MOD_ID, "textures/environment/dread_portal.png");


    public RenderDreadPortal(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(@NotNull T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int combinedLightIn, int combinedOverlayIn) {
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

    private void renderFace(T p_173695_, Matrix4f p_173696_, VertexConsumer p_173697_, float p_173698_, float p_173699_, float p_173700_, float p_173701_, float p_173702_, float p_173703_, float p_173704_, float p_173705_, Direction p_173706_) {
        float r = 1.0F;
        float g = 1.0F;
        float b = 1.0F;
        if (p_173695_.shouldRenderFace(p_173706_)) {
            p_173697_.vertex(p_173696_, p_173698_, p_173700_, p_173702_).color(r, g, b, 1.0F).next();
            p_173697_.vertex(p_173696_, p_173699_, p_173700_, p_173703_).color(r, g, b, 1.0F).next();
            p_173697_.vertex(p_173696_, p_173699_, p_173701_, p_173704_).color(r, g, b, 1.0F).next();
            p_173697_.vertex(p_173696_, p_173698_, p_173701_, p_173705_).color(r, g, b, 1.0F).next();
        }
    }

    protected RenderLayer renderType() {
        return IafRenderType.getDreadlandsPortal();
    }

}