package com.iafenvoy.iceandfire.particle;

import com.iafenvoy.iceandfire.render.entity.RenderSiren;
import com.iafenvoy.iceandfire.render.model.ModelSiren;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class ParticleSirenAppearance extends Particle {
    private final Model model = new ModelSiren();
    private final int sirenType;

    public ParticleSirenAppearance(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, int sirenType) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.gravityStrength = 0.0F;
        this.maxAge = 30;
        this.sirenType = sirenType;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }

    @Override
    public void buildGeometry(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        float f = ((float) this.age + partialTicks) / (float) this.maxAge;
        float f1 = 0.05F + 0.5F * MathHelper.sin(f * (float) Math.PI);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.multiply(renderInfo.getRotation());
        matrixstack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(150.0F * f - 60.0F));
        matrixstack.scale(-1.0F, -1.0F, 1.0F);
        matrixstack.translate(0.0D, -1.101F, 1.5D);
        VertexConsumerProvider.Immediate irendertypebuffer$impl = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        VertexConsumer ivertexbuilder = irendertypebuffer$impl.getBuffer(RenderLayer.getEntityTranslucent(RenderSiren.getSirenOverlayTexture(this.sirenType)));
        this.model.render(matrixstack, ivertexbuilder, 15728880, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, f1);
        irendertypebuffer$impl.draw();
    }
}

