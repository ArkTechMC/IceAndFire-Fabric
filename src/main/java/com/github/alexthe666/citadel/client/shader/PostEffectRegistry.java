package com.github.alexthe666.citadel.client.shader;

import com.github.alexthe666.citadel.Citadel;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.util.Identifier;

public class PostEffectRegistry {

    private static final List<Identifier> registry = new ArrayList<>();

    private static final Map<Identifier, PostEffect> postEffects = new HashMap<>();

    public static void clear(){
        for(PostEffect postEffect : postEffects.values()){
            postEffect.close();
        }
        postEffects.clear();
    }

    public static void registerEffect(Identifier resourceLocation) {
        registry.add(resourceLocation);
    }

    public static void onInitializeOutline() {
        clear();
        MinecraftClient minecraft = MinecraftClient.getInstance();
        for (Identifier resourceLocation : registry) {
            PostEffectProcessor postChain;
            Framebuffer renderTarget;
            try {
                postChain = new PostEffectProcessor(minecraft.getTextureManager(), minecraft.getResourceManager(), minecraft.getFramebuffer(), resourceLocation);
                postChain.setupDimensions(minecraft.getWindow().getFramebufferWidth(), minecraft.getWindow().getFramebufferHeight());
                renderTarget = postChain.getSecondaryTarget("final");
            } catch (IOException ioexception) {
                Citadel.LOGGER.warn("Failed to load shader: {}", resourceLocation, ioexception);
                postChain = null;
                renderTarget = null;
            } catch (JsonSyntaxException jsonsyntaxexception) {
                Citadel.LOGGER.warn("Failed to parse shader: {}", resourceLocation, jsonsyntaxexception);
                postChain = null;
                renderTarget = null;
            }
            postEffects.put(resourceLocation, new PostEffect(postChain, renderTarget, false));
        }
    }

    public static void resize(int x, int y) {
        for (PostEffect postEffect : postEffects.values()) {
            postEffect.resize(x, y);
        }
    }

    public static Framebuffer getRenderTargetFor(Identifier resourceLocation) {
        PostEffect effect = postEffects.get(resourceLocation);
        return effect == null ? null : effect.getRenderTarget();
    }

    public static void renderEffectForNextTick(Identifier resourceLocation) {
        PostEffect effect = postEffects.get(resourceLocation);
        if (effect != null) {
            effect.setEnabled(true);
        }
    }

    public static void blitEffects() {
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        for (PostEffect postEffect : postEffects.values()) {
            if (postEffect.postChain != null && postEffect.isEnabled()) {
                postEffect.getRenderTarget().draw(MinecraftClient.getInstance().getWindow().getFramebufferWidth(), MinecraftClient.getInstance().getWindow().getFramebufferHeight(), false);
                postEffect.getRenderTarget().clear(MinecraftClient.IS_SYSTEM_MAC);
                MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
                postEffect.setEnabled(false);
            }
        }
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    }

    public static void clearAndBindWrite(Framebuffer mainTarget) {
        for (PostEffect postEffect : postEffects.values()) {
            if (postEffect.isEnabled() && postEffect.postChain != null) {
                postEffect.getRenderTarget().clear(MinecraftClient.IS_SYSTEM_MAC);
                mainTarget.beginWrite(false);
            }
        }
    }

    public static void processEffects(Framebuffer mainTarget, float f) {
        for (PostEffect postEffect : postEffects.values()) {
            if (postEffect.isEnabled() && postEffect.postChain != null) {
                postEffect.postChain.render(MinecraftClient.getInstance().getTickDelta());
                mainTarget.beginWrite(false);
            }
        }
    }

    private static class PostEffect {
        private final PostEffectProcessor postChain;
        private final Framebuffer renderTarget;
        private boolean enabled;

        public PostEffect(PostEffectProcessor postChain, Framebuffer renderTarget, boolean enabled) {
            this.postChain = postChain;
            this.renderTarget = renderTarget;
            this.enabled = enabled;
        }

        public PostEffectProcessor getPostChain() {
            return this.postChain;
        }

        public Framebuffer getRenderTarget() {
            return this.renderTarget;
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public void close() {
            if (this.postChain != null) {
                this.postChain.close();
            }
        }

        public void resize(int x, int y) {
            if (this.postChain != null) {
                this.postChain.setupDimensions(x, y);
            }
        }
    }
}