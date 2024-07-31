package com.iafenvoy.iceandfire.mixin;

import com.iafenvoy.iceandfire.config.IafClientConfig;
import com.iafenvoy.iceandfire.screen.TitleScreenRenderManager;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    @Shadow
    @Nullable
    private SplashTextRenderer splashText;

    @Shadow
    @Final
    private boolean doBackgroundFade;

    @Shadow
    private long backgroundFadeStart;

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        if (!IafClientConfig.INSTANCE.customMainMenu.getBooleanValue()) return;
        SplashTextRenderer renderer = TitleScreenRenderManager.getSplash();
        if (renderer != null)
            this.splashText = renderer;
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {
        if (!IafClientConfig.INSTANCE.customMainMenu.getBooleanValue()) return;
        TitleScreenRenderManager.tick();
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/RotatingCubeMapRenderer;render(FF)V"))
    private void onRenderBackground(RotatingCubeMapRenderer instance, float delta, float alpha, @Local(ordinal = 0, argsOnly = true) DrawContext context) {
        if (!IafClientConfig.INSTANCE.customMainMenu.getBooleanValue()) {
            instance.render(delta, alpha);
            return;
        }
        TitleScreenRenderManager.renderBackground(context, this.width, this.height);
        float f = this.doBackgroundFade ? (float) (Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0F : 1.0F;
        float g = this.doBackgroundFade ? MathHelper.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
        int i = MathHelper.ceil(g * 255.0F) << 24;
        if ((i & -67108864) != 0)
            TitleScreenRenderManager.drawModName(context, this.width, this.height, i);
    }
}
