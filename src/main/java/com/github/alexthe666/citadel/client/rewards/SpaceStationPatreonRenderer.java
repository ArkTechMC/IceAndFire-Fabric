package com.github.alexthe666.citadel.client.rewards;

import com.github.alexthe666.citadel.CitadelConstants;
import com.github.alexthe666.citadel.ClientProxy;
import com.github.alexthe666.citadel.client.shader.CitadelShaderRenderTypes;
import com.github.alexthe666.citadel.client.shader.PostEffectRegistry;
import com.github.alexthe666.citadel.client.texture.CitadelTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class SpaceStationPatreonRenderer extends CitadelPatreonRenderer {


    private static final Identifier CITADEL_TEXTURE = new Identifier("citadel", "textures/patreon/citadel_model.png");
    private static final Identifier CITADEL_LIGHTS_TEXTURE = new Identifier("citadel", "textures/patreon/citadel_model_glow.png");
    private final Identifier resourceLocation;
    private final int[] colors;

    public SpaceStationPatreonRenderer(Identifier resourceLocation, int[] colors) {
        this.resourceLocation = resourceLocation;
        this.colors = colors;
    }


    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider buffer, int light, float partialTick, LivingEntity entity, float distanceIn, float rotateSpeed, float rotateHeight) {
        float tick = entity.age + partialTick;
        float bob = (float) (Math.sin(tick * 0.1F) * 1 * 0.05F - 1 * 0.05F);
        float scale = 0.4F;
        float rotation = MathHelper.wrapDegrees((tick * rotateSpeed) % 360);
        matrixStackIn.push();
        matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
        matrixStackIn.translate(0, entity.getHeight() + bob + (rotateHeight - 1F), entity.getWidth() * distanceIn);
        matrixStackIn.push();
        matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(75));
        matrixStackIn.scale(scale, scale, scale);
        matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
        matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation * 10));
        ClientProxy.CITADEL_MODEL.resetToDefaultPose();
        if(CitadelConstants.debugShaders()){
            PostEffectRegistry.renderEffectForNextTick(ClientProxy.RAINBOW_AURA_POST_SHADER);
            ClientProxy.CITADEL_MODEL.render(matrixStackIn, buffer.getBuffer(CitadelShaderRenderTypes.getRainbowAura(CITADEL_TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        }else{
            ClientProxy.CITADEL_MODEL.render(matrixStackIn, buffer.getBuffer(RenderLayer.getEntityCutoutNoCull(CitadelTextureManager.getColorMappedTexture(resourceLocation, CITADEL_TEXTURE, colors))), light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            ClientProxy.CITADEL_MODEL.render(matrixStackIn, buffer.getBuffer(RenderLayer.getEyes(CITADEL_LIGHTS_TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        matrixStackIn.pop();
        matrixStackIn.pop();
    }
}
