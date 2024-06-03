package com.github.alexthe666.iceandfire.compat.jei.icedragonforge;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class IceDragonForgeDrawable implements IDrawable {
    private static final Identifier TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/gui/dragonforge_ice.png");

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 120;
    }

    @Override
    public void draw(@NotNull DrawContext ms, int xOffset, int yOffset) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.drawTexturedModalRect(ms, xOffset, yOffset, 3, 4, 170, 79);
        int scaledProgress = (MinecraftClient.getInstance().player.age % 100) * 128 / 100;
        this.drawTexturedModalRect(ms, xOffset + 9, yOffset + 19, 0, 166, scaledProgress, 38);
    }

    public void drawTexturedModalRect(DrawContext ms, int x, int y, int textureX, int textureY, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        Matrix4f matrix4f = ms.getMatrices().peek().getPositionMatrix();
        bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferbuilder.vertex(matrix4f, x, y + height, 0).texture((float) (textureX) * 0.00390625F, (float) (textureY + height) * 0.00390625F).next();
        bufferbuilder.vertex(matrix4f, x + width, y + height, 0).texture((float) (textureX + width) * 0.00390625F, (float) (textureY + height) * 0.00390625F).next();
        bufferbuilder.vertex(matrix4f, x + width, y, 0).texture((float) (textureX + width) * 0.00390625F, (float) (textureY) * 0.00390625F).next();
        bufferbuilder.vertex(matrix4f, x, y, 0).texture((float) (textureX) * 0.00390625F, (float) (textureY) * 0.00390625F).next();
        tessellator.draw();
    }
}