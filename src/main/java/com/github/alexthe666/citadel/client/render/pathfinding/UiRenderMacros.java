package com.github.alexthe666.citadel.client.render.pathfinding;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

/**
 * Our replacement for GuiComponent.
 * TODO: move to shared lib
 */
public class UiRenderMacros {
    public static final double HALF_BIAS = 0.5;

    public static void drawLineRectGradient(final MatrixStack ps,
                                            final int x,
                                            final int y,
                                            final int w,
                                            final int h,
                                            final int argbColorStart,
                                            final int argbColorEnd) {
        drawLineRectGradient(ps, x, y, w, h, argbColorStart, argbColorEnd, 1);
    }

    public static void drawLineRectGradient(final MatrixStack ps,
                                            final int x,
                                            final int y,
                                            final int w,
                                            final int h,
                                            final int argbColorStart,
                                            final int argbColorEnd,
                                            final int lineWidth) {
        drawLineRectGradient(ps,
                x,
                y,
                w,
                h,
                (argbColorStart >> 16) & 0xff,
                (argbColorEnd >> 16) & 0xff,
                (argbColorStart >> 8) & 0xff,
                (argbColorEnd >> 8) & 0xff,
                argbColorStart & 0xff,
                argbColorEnd & 0xff,
                (argbColorStart >> 24) & 0xff,
                (argbColorEnd >> 24) & 0xff,
                lineWidth);
    }

    public static void drawLineRectGradient(final MatrixStack ps,
                                            final int x,
                                            final int y,
                                            final int w,
                                            final int h,
                                            final int redStart,
                                            final int redEnd,
                                            final int greenStart,
                                            final int greenEnd,
                                            final int blueStart,
                                            final int blueEnd,
                                            final int alphaStart,
                                            final int alphaEnd,
                                            final int lineWidth) {
        if (lineWidth < 1 || (alphaStart == 0 && alphaEnd == 0)) {
            return;
        }

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        if (alphaStart != 255 || alphaEnd != 255) {
            RenderSystem.enableBlend();
        } else {
            RenderSystem.disableBlend();
        }

        final Matrix4f m = ps.peek().getPositionMatrix();
        final BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        buffer.vertex(m, x, y, 0).color(redStart, greenStart, blueStart, alphaStart).next();
        buffer.vertex(m, x, y + h, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).next();
        buffer.vertex(m, x + lineWidth, y + h - lineWidth, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).next();
        buffer.vertex(m, x + lineWidth, y + lineWidth, 0).color(redStart, greenStart, blueStart, alphaStart).next();
        buffer.vertex(m, x + w - lineWidth, y + lineWidth, 0).color(redStart, greenStart, blueStart, alphaStart).next();
        buffer.vertex(m, x + w, y, 0).color(redStart, greenStart, blueStart, alphaStart).next();
        Tessellator.getInstance().draw();

        buffer.begin(DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        buffer.vertex(m, x + w, y + h, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).next();
        buffer.vertex(m, x + w, y, 0).color(redStart, greenStart, blueStart, alphaStart).next();
        buffer.vertex(m, x + w - lineWidth, y + lineWidth, 0).color(redStart, greenStart, blueStart, alphaStart).next();
        buffer.vertex(m, x + w - lineWidth, y + h - lineWidth, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).next();
        buffer.vertex(m, x + lineWidth, y + h - lineWidth, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).next();
        buffer.vertex(m, x, y + h, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).next();
        Tessellator.getInstance().draw();

        RenderSystem.disableBlend();
    }

    public static void drawLineRect(final MatrixStack ps, final int x, final int y, final int w, final int h, final int argbColor) {
        drawLineRect(ps, x, y, w, h, argbColor, 1);
    }

    public static void drawLineRect(final MatrixStack ps,
                                    final int x,
                                    final int y,
                                    final int w,
                                    final int h,
                                    final int argbColor,
                                    final int lineWidth) {
        drawLineRect(ps,
                x,
                y,
                w,
                h,
                (argbColor >> 16) & 0xff,
                (argbColor >> 8) & 0xff,
                argbColor & 0xff,
                (argbColor >> 24) & 0xff,
                lineWidth);
    }

    public static void drawLineRect(final MatrixStack ps,
                                    final int x,
                                    final int y,
                                    final int w,
                                    final int h,
                                    final int red,
                                    final int green,
                                    final int blue,
                                    final int alpha,
                                    final int lineWidth) {
        if (lineWidth < 1 || alpha == 0) {
            return;
        }

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        if (alpha != 255) {
            RenderSystem.enableBlend();
        } else {
            RenderSystem.disableBlend();
        }

        final Matrix4f m = ps.peek().getPositionMatrix();
        final BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        buffer.vertex(m, x, y, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, x, y + h, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, x + lineWidth, y + h - lineWidth, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, x + lineWidth, y + lineWidth, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, x + w - lineWidth, y + lineWidth, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, x + w, y, 0).color(red, green, blue, alpha).next();
        Tessellator.getInstance().draw();

        buffer.begin(DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        buffer.vertex(m, x + w, y + h, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, x + w, y, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, x + w - lineWidth, y + lineWidth, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, x + w - lineWidth, y + h - lineWidth, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, x + lineWidth, y + h - lineWidth, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, x, y + h, 0).color(red, green, blue, alpha).next();
        Tessellator.getInstance().draw();

        RenderSystem.disableBlend();
    }

    public static void fill(final MatrixStack ps, final int x, final int y, final int w, final int h, final int argbColor) {
        fill(ps, x, y, w, h, (argbColor >> 16) & 0xff, (argbColor >> 8) & 0xff, argbColor & 0xff, (argbColor >> 24) & 0xff);
    }

    public static void fill(final MatrixStack ps,
                            final int x,
                            final int y,
                            final int w,
                            final int h,
                            final int red,
                            final int green,
                            final int blue,
                            final int alpha) {
        if (alpha == 0) {
            return;
        }

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        if (alpha != 255) {
            RenderSystem.enableBlend();
        } else {
            RenderSystem.disableBlend();
        }

        final Matrix4f m = ps.peek().getPositionMatrix();
        final BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        buffer.vertex(m, x, y, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, x, y + h, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, x + w, y + h, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, x + w, y, 0).color(red, green, blue, alpha).next();
        Tessellator.getInstance().draw();

        RenderSystem.disableBlend();
    }


    public static void fillGradient(final MatrixStack ps,
                                    final int x,
                                    final int y,
                                    final int w,
                                    final int h,
                                    final int argbColorStart,
                                    final int argbColorEnd) {
        fillGradient(ps,
                x,
                y,
                w,
                h,
                (argbColorStart >> 16) & 0xff,
                (argbColorEnd >> 16) & 0xff,
                (argbColorStart >> 8) & 0xff,
                (argbColorEnd >> 8) & 0xff,
                argbColorStart & 0xff,
                argbColorEnd & 0xff,
                (argbColorStart >> 24) & 0xff,
                (argbColorEnd >> 24) & 0xff);
    }

    public static void fillGradient(final MatrixStack ps,
                                    final int x,
                                    final int y,
                                    final int w,
                                    final int h,
                                    final int redStart,
                                    final int redEnd,
                                    final int greenStart,
                                    final int greenEnd,
                                    final int blueStart,
                                    final int blueEnd,
                                    final int alphaStart,
                                    final int alphaEnd) {
        if (alphaStart == 0 && alphaEnd == 0) {
            return;
        }

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        if (alphaStart != 255 || alphaEnd != 255) {
            RenderSystem.enableBlend();
        } else {
            RenderSystem.disableBlend();
        }

        final Matrix4f m = ps.peek().getPositionMatrix();
        final BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        buffer.vertex(m, x, y, 0).color(redStart, greenStart, blueStart, alphaStart).next();
        buffer.vertex(m, x, y + h, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).next();
        buffer.vertex(m, x + w, y + h, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).next();
        buffer.vertex(m, x + w, y, 0).color(redStart, greenStart, blueStart, alphaStart).next();
        Tessellator.getInstance().draw();

        RenderSystem.disableBlend();
    }

    public static void hLine(final MatrixStack ps, final int x, final int xEnd, final int y, final int argbColor) {
        line(ps, x, y, xEnd, y, (argbColor >> 16) & 0xff, (argbColor >> 8) & 0xff, argbColor & 0xff, (argbColor >> 24) & 0xff);
    }

    public static void hLine(final MatrixStack ps,
                             final int x,
                             final int xEnd,
                             final int y,
                             final int red,
                             final int green,
                             final int blue,
                             final int alpha) {
        line(ps, x, y, xEnd, y, red, green, blue, alpha);
    }

    public static void vLine(final MatrixStack ps, final int x, final int y, final int yEnd, final int argbColor) {
        line(ps, x, y, x, yEnd, (argbColor >> 16) & 0xff, (argbColor >> 8) & 0xff, argbColor & 0xff, (argbColor >> 24) & 0xff);
    }

    public static void vLine(final MatrixStack ps,
                             final int x,
                             final int y,
                             final int yEnd,
                             final int red,
                             final int green,
                             final int blue,
                             final int alpha) {
        line(ps, x, y, x, yEnd, red, green, blue, alpha);
    }

    public static void line(final MatrixStack ps, final int x, final int y, final int xEnd, final int yEnd, final int argbColor) {
        line(ps, x, y, xEnd, yEnd, (argbColor >> 16) & 0xff, (argbColor >> 8) & 0xff, argbColor & 0xff, (argbColor >> 24) & 0xff);
    }

    public static void line(final MatrixStack ps,
                            final int x,
                            final int y,
                            final int xEnd,
                            final int yEnd,
                            final int red,
                            final int green,
                            final int blue,
                            final int alpha) {
        if (alpha == 0) {
            return;
        }

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        if (alpha != 255) {
            RenderSystem.enableBlend();
        } else {
            RenderSystem.disableBlend();
        }

        final Matrix4f m = ps.peek().getPositionMatrix();
        final BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        buffer.vertex(m, x, y, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, xEnd, yEnd, 0).color(red, green, blue, alpha).next();
        Tessellator.getInstance().draw();

        RenderSystem.disableBlend();
    }

    public static void blit(final MatrixStack ps, final Identifier rl, final int x, final int y, final int w, final int h) {
        blit(ps, rl, x, y, w, h, 0.0f, 0.0f, 1.0f, 1.0f);
    }

    public static void blit(final MatrixStack ps,
                            final Identifier rl,
                            final int x,
                            final int y,
                            final int w,
                            final int h,
                            final int u,
                            final int v,
                            final int mapW,
                            final int mapH) {
        blit(ps, rl, x, y, w, h, (float) u / mapW, (float) v / mapH, (float) (u + w) / mapW, (float) (v + h) / mapH);
    }

    public static void blit(final MatrixStack ps,
                            final Identifier rl,
                            final int x,
                            final int y,
                            final int w,
                            final int h,
                            final int u,
                            final int v,
                            final int uW,
                            final int vH,
                            final int mapW,
                            final int mapH) {
        blit(ps, rl, x, y, w, h, (float) u / mapW, (float) v / mapH, (float) (u + uW) / mapW, (float) (v + vH) / mapH);
    }

    public static void blit(final MatrixStack ps,
                            final Identifier rl,
                            final int x,
                            final int y,
                            final int w,
                            final int h,
                            final float uMin,
                            final float vMin,
                            final float uMax,
                            final float vMax) {
        MinecraftClient.getInstance().getTextureManager().bindTexture(rl);
        RenderSystem.setShaderTexture(0, rl);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);

        final Matrix4f m = ps.peek().getPositionMatrix();
        final BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(m, x, y, 0).texture(uMin, vMin).next();
        buffer.vertex(m, x, y + h, 0).texture(uMin, vMax).next();
        buffer.vertex(m, x + w, y + h, 0).texture(uMax, vMax).next();
        buffer.vertex(m, x + w, y, 0).texture(uMax, vMin).next();
        Tessellator.getInstance().draw();
    }

    /**
     * Draws texture without scaling so one texel is one pixel, using repeatable texture center.
     * TODO: Nightenom - rework to better algoritm from pgr, also texture extensions?
     *
     * @param ps            MatrixStack
     * @param rl            image ResLoc
     * @param x             start target coords [pixels]
     * @param y             start target coords [pixels]
     * @param width         target rendering box [pixels]
     * @param height        target rendering box [pixels]
     * @param u             texture start offset [texels]
     * @param v             texture start offset [texels]
     * @param uWidth        texture rendering box [texels]
     * @param vHeight       texture rendering box [texels]
     * @param textureWidth  texture file size [texels]
     * @param textureHeight texture file size [texels]
     * @param uRepeat       offset relative to u, v [texels], smaller than uWidth
     * @param vRepeat       offset relative to u, v [texels], smaller than vHeight
     * @param repeatWidth   size of repeatable box in texture [texels], smaller than or equal uWidth - uRepeat
     * @param repeatHeight  size of repeatable box in texture [texels], smaller than or equal vHeight - vRepeat
     */
    protected static void blitRepeatable(final MatrixStack ps,
                                         final Identifier rl,
                                         final int x, final int y,
                                         final int width, final int height,
                                         final int u, final int v,
                                         final int uWidth, final int vHeight,
                                         final int textureWidth, final int textureHeight,
                                         final int uRepeat, final int vRepeat,
                                         final int repeatWidth, final int repeatHeight) {
        if (uRepeat < 0 || vRepeat < 0 || uRepeat >= uWidth || vRepeat >= vHeight || repeatWidth < 1 || repeatHeight < 1
                || repeatWidth > uWidth - uRepeat || repeatHeight > vHeight - vRepeat) {
            throw new IllegalArgumentException("Repeatable box is outside of texture box");
        }

        final int repeatCountX = Math.max(1, Math.max(0, width - (uWidth - repeatWidth)) / repeatWidth);
        final int repeatCountY = Math.max(1, Math.max(0, height - (vHeight - repeatHeight)) / repeatHeight);

        final Matrix4f mat = ps.peek().getPositionMatrix();
        final BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(DrawMode.TRIANGLES, VertexFormats.POSITION_TEXTURE);

        // main
        for (int i = 0; i < repeatCountX; i++) {
            final int uAdjust = i == 0 ? 0 : uRepeat;
            final int xStart = x + uAdjust + i * repeatWidth;
            final int w = Math.min(repeatWidth + uRepeat - uAdjust, width - (uWidth - uRepeat - repeatWidth));
            final float minU = (float) (u + uAdjust) / textureWidth;
            final float maxU = (float) (u + uAdjust + w) / textureWidth;

            for (int j = 0; j < repeatCountY; j++) {
                final int vAdjust = j == 0 ? 0 : vRepeat;
                final int yStart = y + vAdjust + j * repeatHeight;
                final int h = Math.min(repeatHeight + vRepeat - vAdjust, height - (vHeight - vRepeat - repeatHeight));
                final float minV = (float) (v + vAdjust) / textureHeight;
                final float maxV = (float) (v + vAdjust + h) / textureHeight;

                populateBlitTriangles(buffer, mat, xStart, xStart + w, yStart, yStart + h, minU, maxU, minV, maxV);
            }
        }

        final int xEnd = x + Math.min(uRepeat + repeatCountX * repeatWidth, width - (uWidth - uRepeat - repeatWidth));
        final int yEnd = y + Math.min(vRepeat + repeatCountY * repeatHeight, height - (vHeight - vRepeat - repeatHeight));
        final int uLeft = width - (xEnd - x);
        final int vLeft = height - (yEnd - y);
        final float restMinU = (float) (u + uWidth - uLeft) / textureWidth;
        final float restMaxU = (float) (u + uWidth) / textureWidth;
        final float restMinV = (float) (v + vHeight - vLeft) / textureHeight;
        final float restMaxV = (float) (v + vHeight) / textureHeight;

        // bot border
        for (int i = 0; i < repeatCountX; i++) {
            final int uAdjust = i == 0 ? 0 : uRepeat;
            final int xStart = x + uAdjust + i * repeatWidth;
            final int w = Math.min(repeatWidth + uRepeat - uAdjust, width - uLeft);
            final float minU = (float) (u + uAdjust) / textureWidth;
            final float maxU = (float) (u + uAdjust + w) / textureWidth;

            populateBlitTriangles(buffer, mat, xStart, xStart + w, yEnd, yEnd + vLeft, minU, maxU, restMinV, restMaxV);
        }

        // left border
        for (int j = 0; j < repeatCountY; j++) {
            final int vAdjust = j == 0 ? 0 : vRepeat;
            final int yStart = y + vAdjust + j * repeatHeight;
            final int h = Math.min(repeatHeight + vRepeat - vAdjust, height - vLeft);
            float minV = (float) (v + vAdjust) / textureHeight;
            float maxV = (float) (v + vAdjust + h) / textureHeight;

            populateBlitTriangles(buffer, mat, xEnd, xEnd + uLeft, yStart, yStart + h, restMinU, restMaxU, minV, maxV);
        }

        // bot left corner
        populateBlitTriangles(buffer, mat, xEnd, xEnd + uLeft, yEnd, yEnd + vLeft, restMinU, restMaxU, restMinV, restMaxV);

        MinecraftClient.getInstance().getTextureManager().bindTexture(rl);
        RenderSystem.setShaderTexture(0, rl);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);

        Tessellator.getInstance().draw();
    }

    public static void populateFillTriangles(final Matrix4f m,
                                             final BufferBuilder buffer,
                                             final int x,
                                             final int y,
                                             final int w,
                                             final int h,
                                             final int red,
                                             final int green,
                                             final int blue,
                                             final int alpha) {
        buffer.vertex(m, x, y, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, x, y + h, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, x + w, y, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, x + w, y, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, x, y + h, 0).color(red, green, blue, alpha).next();
        buffer.vertex(m, x + w, y + h, 0).color(red, green, blue, alpha).next();
    }

    public static void populateFillGradientTriangles(final Matrix4f m,
                                                     final BufferBuilder buffer,
                                                     final int x,
                                                     final int y,
                                                     final int w,
                                                     final int h,
                                                     final int redStart,
                                                     final int redEnd,
                                                     final int greenStart,
                                                     final int greenEnd,
                                                     final int blueStart,
                                                     final int blueEnd,
                                                     final int alphaStart,
                                                     final int alphaEnd) {
        buffer.vertex(m, x, y, 0).color(redStart, greenStart, blueStart, alphaStart).next();
        buffer.vertex(m, x, y + h, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).next();
        buffer.vertex(m, x + w, y, 0).color(redStart, greenStart, blueStart, alphaStart).next();
        buffer.vertex(m, x + w, y, 0).color(redStart, greenStart, blueStart, alphaStart).next();
        buffer.vertex(m, x, y + h, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).next();
        buffer.vertex(m, x + w, y + h, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).next();
    }

    public static void populateBlitTriangles(final BufferBuilder buffer,
                                             final Matrix4f mat,
                                             final float xStart,
                                             final float xEnd,
                                             final float yStart,
                                             final float yEnd,
                                             final float uMin,
                                             final float uMax,
                                             final float vMin,
                                             final float vMax) {
        buffer.vertex(mat, xStart, yStart, 0).texture(uMin, vMin).next();
        buffer.vertex(mat, xStart, yEnd, 0).texture(uMin, vMax).next();
        buffer.vertex(mat, xEnd, yStart, 0).texture(uMax, vMin).next();
        buffer.vertex(mat, xEnd, yStart, 0).texture(uMax, vMin).next();
        buffer.vertex(mat, xStart, yEnd, 0).texture(uMin, vMax).next();
        buffer.vertex(mat, xEnd, yEnd, 0).texture(uMax, vMax).next();
    }

    /**
     * Render an entity on a GUI.
     *
     * @param poseStack matrix
     * @param x         horizontal center position
     * @param y         vertical bottom position
     * @param scale     scaling factor
     * @param headYaw   adjusts look rotation
     * @param yaw       adjusts body rotation
     * @param pitch     adjusts look rotation
     * @param entity    the entity to render
     */
    public static void drawEntity(final MatrixStack poseStack, final int x, final int y, final double scale,
                                  final float headYaw, final float yaw, final float pitch, final Entity entity) {
        // INLINE: vanilla from InventoryScreen
        final LivingEntity livingEntity = (entity instanceof LivingEntity) ? (LivingEntity) entity : null;
        final MinecraftClient mc = MinecraftClient.getInstance();
        if (entity.getWorld() == null)
            return; // this was entity.setLevel, not sure why cuz sus, dont care if entity has no level
        poseStack.push();
        poseStack.translate((float) x, (float) y, 1050.0F);
        poseStack.scale(1.0F, 1.0F, -1.0F);
        poseStack.translate(0.0D, 0.0D, 1000.0D);
        poseStack.scale((float) scale, (float) scale, (float) scale);
        final Quaternionf pitchRotation = RotationAxis.POSITIVE_X.rotationDegrees(pitch);
        poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
        poseStack.multiply(pitchRotation);
        final float oldYaw = entity.getYaw();
        final float oldPitch = entity.getPitch();
        final float oldYawOffset = livingEntity == null ? 0F : livingEntity.bodyYaw;
        final float oldPrevYawHead = livingEntity == null ? 0F : livingEntity.prevHeadYaw;
        final float oldYawHead = livingEntity == null ? 0F : livingEntity.headYaw;
        entity.setYaw(180.0F + headYaw);
        entity.setPitch(-pitch);
        if (livingEntity != null) {
            livingEntity.bodyYaw = 180.0F + yaw;
            livingEntity.headYaw = entity.getYaw();
            livingEntity.prevHeadYaw = entity.getYaw();
        }
        DiffuseLighting.method_34742();
        final EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
        pitchRotation.conjugate();
        dispatcher.setRotation(pitchRotation);
        dispatcher.setRenderShadows(false);
        final VertexConsumerProvider.Immediate buffers = mc.getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> dispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, poseStack, buffers, 0x00F000F0));
        buffers.draw();
        dispatcher.setRenderShadows(true);
        entity.setYaw(oldYaw);
        entity.setPitch(oldPitch);
        if (livingEntity != null) {
            livingEntity.bodyYaw = oldYawOffset;
            livingEntity.prevHeadYaw = oldPrevYawHead;
            livingEntity.headYaw = oldYawHead;
        }
        poseStack.pop();
        DiffuseLighting.enableGuiDepthLighting();
    }
}
