package com.iafenvoy.citadel.mixin.client;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashTextRenderer.class)
public class SplashTextRendererMixin {

    @Unique
    private int splashTextColor = -1;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V"))
    protected void citadel_preRenderSplashText(DrawContext guiGraphics, int width, TextRenderer font, int loadProgress, CallbackInfo ci) {
        guiGraphics.getMatrices().push();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V", shift = At.Shift.AFTER))
    protected void citadel_postRenderSplashText(DrawContext guiGraphics, int width, TextRenderer font, int loadProgress, CallbackInfo ci) {
        guiGraphics.getMatrices().pop();
    }

    @ModifyConstant(method = "render", constant = @Constant(intValue = 16776960))
    private int citadel_splashTextColor(int value) {
        return this.splashTextColor == -1 ? value : this.splashTextColor;
    }
}
