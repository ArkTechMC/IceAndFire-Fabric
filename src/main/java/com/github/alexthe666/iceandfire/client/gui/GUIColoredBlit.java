package com.github.alexthe666.iceandfire.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

public class GUIColoredBlit {
    public static void blit(MatrixStack p_93161_, int p_93162_, int p_93163_, int p_93164_, int p_93165_, float p_93166_, float p_93167_, int p_93168_, int p_93169_, int p_93170_, int p_93171_, float alpha) {
        innerBlit(p_93161_, p_93162_, p_93162_ + p_93164_, p_93163_, p_93163_ + p_93165_, p_93168_, p_93169_, p_93166_, p_93167_, p_93170_, p_93171_, alpha);
    }

    public static void blit(MatrixStack p_93134_, int p_93135_, int p_93136_, float p_93137_, float p_93138_, int p_93139_, int p_93140_, int p_93141_, int p_93142_, float alpha) {
        blit(p_93134_, p_93135_, p_93136_, p_93139_, p_93140_, p_93137_, p_93138_, p_93139_, p_93140_, p_93141_, p_93142_, alpha);
    }


    private static void innerBlit(MatrixStack p_93188_, int p_93189_, int p_93190_, int p_93191_, int p_93192_, int p_93194_, int p_93195_, float p_93196_, float p_93197_, int p_93198_, int p_93199_, float alpha) {
        innerBlit(p_93188_.peek().getPositionMatrix(), p_93189_, p_93190_, p_93191_, p_93192_, 0, (p_93196_ + 0.0F) / (float) p_93198_, (p_93196_ + (float) p_93194_) / (float) p_93198_, (p_93197_ + 0.0F) / (float) p_93199_, (p_93197_ + (float) p_93195_) / (float) p_93199_, alpha);
    }

    private static void innerBlit(Matrix4f p_93113_, int p_93114_, int p_93115_, int p_93116_, int p_93117_, int p_93118_, float p_93119_, float p_93120_, float p_93121_, float p_93122_, float alpha) {
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferbuilder.vertex(p_93113_, (float) p_93114_, (float) p_93117_, (float) p_93118_).texture(p_93119_, p_93122_).color(1.0F, 1.0F, 1.0F, alpha).next();
        bufferbuilder.vertex(p_93113_, (float) p_93115_, (float) p_93117_, (float) p_93118_).texture(p_93120_, p_93122_).color(1.0F, 1.0F, 1.0F, alpha).next();
        bufferbuilder.vertex(p_93113_, (float) p_93115_, (float) p_93116_, (float) p_93118_).texture(p_93120_, p_93121_).color(1.0F, 1.0F, 1.0F, alpha).next();
        bufferbuilder.vertex(p_93113_, (float) p_93114_, (float) p_93116_, (float) p_93118_).texture(p_93119_, p_93121_).color(1.0F, 1.0F, 1.0F, alpha).next();
        bufferbuilder.end();
        BufferRenderer.reset();
    }
}
