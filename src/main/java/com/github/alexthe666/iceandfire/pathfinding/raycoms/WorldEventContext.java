package com.github.alexthe666.iceandfire.pathfinding.raycoms;

import com.github.alexthe666.iceandfire.client.render.pathfinding.PathfindingDebugRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderLevelStageEvent;

public class WorldEventContext {
    public static final WorldEventContext INSTANCE = new WorldEventContext();

    private WorldEventContext()
    {
        // singleton
    }

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

    public void renderWorldLastEvent(final RenderLevelStageEvent event)
    {
        bufferSource = WorldRenderMacros.getBufferSource();
        poseStack = event.getPoseStack();
        partialTicks = event.getPartialTick();
        clientLevel = MinecraftClient.getInstance().world;
        clientPlayer = MinecraftClient.getInstance().player;
        mainHandItem = clientPlayer.getMainHandStack();
        clientRenderDist = MinecraftClient.getInstance().options.getViewDistance().getValue();

        final Vec3d cameraPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        poseStack.push();
        poseStack.translate(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());

        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS)
        {
            PathfindingDebugRenderer.render(this);

            bufferSource.draw();
        }
        else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS)
        {
            bufferSource.draw();
        }

        poseStack.pop();
    }

}
