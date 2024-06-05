package com.github.alexthe666.citadel.mixin.client;

import com.github.alexthe666.citadel.client.event.EventRenderSplashText;
import com.iafenvoy.iafextra.event.Event;
import com.iafenvoy.iafextra.event.EventBus;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashTextRenderer.class)
public class SplashRendererMixin {
    @Mutable
    @Shadow
    @Final
    private String text;

    @Unique
    private int splashTextColor = -1;

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V",
                    shift = At.Shift.BEFORE
            ))
    protected void citadel_preRenderSplashText(DrawContext guiGraphics, int width, TextRenderer font, int loadProgress, CallbackInfo ci) {
        guiGraphics.getMatrices().push();
        EventRenderSplashText.Pre event = new EventRenderSplashText.Pre(this.text, guiGraphics, MinecraftClient.getInstance().getTickDelta(), 16776960);
        EventBus.post(event);

        if (event.getResult() == Event.Result.ALLOW) {
            this.text = event.getSplashText();
            this.splashTextColor = event.getSplashTextColor();
        }
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    shift = At.Shift.AFTER
            )
    )
    protected void citadel_postRenderSplashText(DrawContext guiGraphics, int width, TextRenderer font, int loadProgress, CallbackInfo ci) {
        EventRenderSplashText.Post event = new EventRenderSplashText.Post(this.text, guiGraphics, MinecraftClient.getInstance().getTickDelta());
        EventBus.post(event);
        guiGraphics.getMatrices().pop();
    }

    @ModifyConstant(
            method = "render",
            constant = @Constant(intValue = 16776960))
    private int citadel_splashTextColor(int value) {
        return this.splashTextColor == -1 ? value : this.splashTextColor;
    }
}
