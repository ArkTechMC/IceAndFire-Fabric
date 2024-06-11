package com.github.alexthe666.citadel.client.render.pathfinding;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class WorldEventContext {
    public static final WorldEventContext INSTANCE = new WorldEventContext();
    public VertexConsumerProvider.Immediate bufferSource;
    public MatrixStack poseStack;
    public float partialTicks;
    public ClientWorld clientLevel;
    public ClientPlayerEntity clientPlayer;
    public ItemStack mainHandItem;
    /**
     * In chunks
     */
    int clientRenderDist;

    private WorldEventContext() {
        // singleton
    }

    public void renderWorldLastEvent(WorldRenderContext context) {
        this.bufferSource = WorldRenderMacros.getBufferSource();
        this.poseStack = context.matrixStack();
        this.partialTicks = context.tickDelta();
        this.clientLevel = MinecraftClient.getInstance().world;
        this.clientPlayer = MinecraftClient.getInstance().player;
        this.mainHandItem = this.clientPlayer.getMainHandStack();
        this.clientRenderDist = MinecraftClient.getInstance().options.getViewDistance().getValue();

        final Vec3d cameraPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        this.poseStack.push();
        this.poseStack.translate(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());

//        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS)
        {
            PathfindingDebugRenderer.render(this);

            this.bufferSource.draw();
        }
//        else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS)
        {
            this.bufferSource.draw();
        }

        this.poseStack.pop();
    }

}
