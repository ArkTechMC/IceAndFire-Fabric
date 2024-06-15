package com.github.alexthe666.citadel.client.render.pathfinding;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.MNode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.joml.Matrix4f;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Set;

public class PathfindingDebugRenderer {
    /**
     * Set of visited nodes.
     */
    public static Set<MNode> lastDebugNodesVisited = new HashSet<>();

    /**
     * Set of not visited nodes.
     */
    public static Set<MNode> lastDebugNodesNotVisited = new HashSet<>();

    /**
     * Set of nodes that belong to the chosen path.
     */
    public static Set<MNode> lastDebugNodesPath = new HashSet<>();

    /**
     * Render debugging information for the pathfinding system.
     *
     * @param ctx rendering context
     */
    public static void render(final WorldEventContext ctx) {
        try {
            for (final MNode n : lastDebugNodesVisited) {
                debugDrawNode(n, 0xffff0000, ctx);
            }

            for (final MNode n : lastDebugNodesNotVisited) {
                debugDrawNode(n, 0xff0000ff, ctx);
            }

            for (final MNode n : lastDebugNodesPath) {
                if (n.isReachedByWorker()) {
                    debugDrawNode(n, 0xffff6600, ctx);
                } else {
                    debugDrawNode(n, 0xff00ff00, ctx);
                }
            }
        } catch (final ConcurrentModificationException exc) {
            Citadel.LOGGER.catching(exc);
        }
    }

    private static void debugDrawNode(final MNode n, final int argbColor, final WorldEventContext ctx) {
        ctx.poseStack.push();
        ctx.poseStack.translate(n.pos.getX() + 0.375d, n.pos.getY() + 0.375d, n.pos.getZ() + 0.375d);

        final Entity entity = MinecraftClient.getInstance().getCameraEntity();
        assert entity != null;
        if (n.pos.isWithinDistance(entity.getBlockPos(), 5d))
            renderDebugText(n, ctx);

        ctx.poseStack.scale(0.25F, 0.25F, 0.25F);

        WorldRenderMacros.renderBox(ctx.bufferSource, ctx.poseStack, BlockPos.ORIGIN, BlockPos.ORIGIN, argbColor);

        if (n.parent != null) {
            final Matrix4f lineMatrix = ctx.poseStack.peek().getPositionMatrix();

            final float pdx = n.parent.pos.getX() - n.pos.getX() + 0.125f;
            final float pdy = n.parent.pos.getY() - n.pos.getY() + 0.125f;
            final float pdz = n.parent.pos.getZ() - n.pos.getZ() + 0.125f;

            final VertexConsumer buffer = ctx.bufferSource.getBuffer(WorldRenderMacros.LINES);

            buffer.vertex(lineMatrix, 0.5f, 0.5f, 0.5f).color(0.75F, 0.75F, 0.75F, 1.0F).next();
            buffer.vertex(lineMatrix, pdx / 0.25f, pdy / 0.25f, pdz / 0.25f).color(0.75F, 0.75F, 0.75F, 1.0F).next();
        }

        ctx.poseStack.pop();
    }

    private static void renderDebugText(final MNode n, final WorldEventContext ctx) {
        final TextRenderer fontrenderer = MinecraftClient.getInstance().textRenderer;

        final String s1 = String.format("F: %.3f [%d]", n.getCost(), n.getCounterAdded());
        final String s2 = String.format("G: %.3f [%d]", n.getScore(), n.getCounterVisited());
        final int i = Math.max(fontrenderer.getWidth(s1), fontrenderer.getWidth(s2)) / 2;

        ctx.poseStack.push();
        ctx.poseStack.translate(0.0F, 0.75F, 0.0F);

        ctx.poseStack.multiply(MinecraftClient.getInstance().getEntityRenderDispatcher().getRotation());
        ctx.poseStack.scale(-0.014F, -0.014F, 0.014F);
        ctx.poseStack.translate(0.0F, 18F, 0.0F);
        final Matrix4f mat = ctx.poseStack.peek().getPositionMatrix();

        WorldRenderMacros.renderFillRectangle(ctx.bufferSource, ctx.poseStack, -i - 1, -5, 0, 2 * i + 2, 17, 0x7f000000);

        ctx.poseStack.translate(0.0F, -5F, -0.1F);
        fontrenderer.draw(s1, -fontrenderer.getWidth(s1) / 2.0f, 1, 0xFFFFFFFF, false, mat, ctx.bufferSource, TextRenderer.TextLayerType.NORMAL, 0, 15728880);
        ctx.poseStack.translate(0.0F, 8F, -0.1F);
        fontrenderer.draw(s2, -fontrenderer.getWidth(s2) / 2.0f, 1, 0xFFFFFFFF, false, mat, ctx.bufferSource, TextRenderer.TextLayerType.NORMAL, 0, 15728880);

        ctx.poseStack.pop();
    }
}