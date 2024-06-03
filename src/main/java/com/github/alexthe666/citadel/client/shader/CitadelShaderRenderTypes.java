package com.github.alexthe666.citadel.client.shader;

import com.github.alexthe666.citadel.ClientProxy;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class CitadelShaderRenderTypes extends RenderLayer {

    protected static final ShaderProgram RENDERTYPE_RAINBOW_AURA_SHADER = new ShaderProgram(CitadelInternalShaders::getRenderTypeRainbowAura);
    protected static final Target RAINBOW_AURA_OUTPUT = new Target("rainbow_aura_target", () -> {
        Framebuffer target = PostEffectRegistry.getRenderTargetFor(ClientProxy.RAINBOW_AURA_POST_SHADER);
        if (target != null) {
            target.copyDepthFrom(MinecraftClient.getInstance().getFramebuffer());
            target.beginWrite(false);
        }
    }, () -> {
        MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
    });

    private CitadelShaderRenderTypes(String s, VertexFormat format, VertexFormat.DrawMode mode, int i, boolean b1, boolean b2, Runnable runnable1, Runnable runnable2) {
        super(s, format, mode, i, b1, b2, runnable1, runnable2);
    }

    public static RenderLayer getRainbowAura(Identifier locationIn) {
        return of("rainbow_aura", VertexFormats.POSITION_COLOR_TEXTURE, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder()
                .program(RENDERTYPE_RAINBOW_AURA_SHADER)
                .cull(DISABLE_CULLING)
                .texture(new Texture(locationIn, false, false))
                .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                .depthTest(LEQUAL_DEPTH_TEST)
                .target(RAINBOW_AURA_OUTPUT)
                .build(true));
    }
}
