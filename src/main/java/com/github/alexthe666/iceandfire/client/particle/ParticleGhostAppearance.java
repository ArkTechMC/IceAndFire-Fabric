package com.github.alexthe666.iceandfire.client.particle;

import com.github.alexthe666.iceandfire.client.model.ModelGhost;
import com.github.alexthe666.iceandfire.client.render.IafRenderType;
import com.github.alexthe666.iceandfire.client.render.entity.RenderGhost;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;

public class ParticleGhostAppearance extends Particle {
    private final ModelGhost model = new ModelGhost(0.0F);
    private final int ghost;
    private boolean fromLeft = false;

    public ParticleGhostAppearance(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, int ghost) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.gravityStrength = 0.0F;
        this.maxAge = 15;
        this.ghost = ghost;
        fromLeft = worldIn.random.nextBoolean();
    }

    @Override
    public @NotNull ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }

    @Override
    public void buildGeometry(@NotNull VertexConsumer buffer, @NotNull Camera renderInfo, float partialTicks) {
        float f = ((float) this.age + partialTicks) / (float) this.maxAge;
        float f1 = 0.05F + 0.5F * MathHelper.sin(f * (float) Math.PI);
        Entity entity = world.getEntityById(ghost);
        if (entity instanceof EntityGhost ghostEntity && MinecraftClient.getInstance().options.getPerspective() == Perspective.FIRST_PERSON) {
            MatrixStack matrixstack = new MatrixStack();
            matrixstack.multiply(renderInfo.getRotation());
            if (fromLeft) {
                matrixstack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(150.0F * f - 60.0F));
                matrixstack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(150.0F * f - 60.0F));

            } else {
                matrixstack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(150.0F * f - 60.0F));
                matrixstack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(150.0F * f - 60.0F));
            }
            matrixstack.scale(-1.0F, -1.0F, 1.0F);
            matrixstack.translate(0.0D, 0.3F, 1.25D);
            VertexConsumerProvider.Immediate irendertypebuffer$impl = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();

            VertexConsumer ivertexbuilder = irendertypebuffer$impl.getBuffer(IafRenderType.getGhost(RenderGhost.getGhostOverlayForType(ghostEntity.getColor())));
            this.model.setAngles(ghostEntity, 0, 0, entity.age + partialTicks, 0, 0);
            this.model.render(matrixstack, ivertexbuilder, 240, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, f1);
            irendertypebuffer$impl.draw();
        }

    }
}

