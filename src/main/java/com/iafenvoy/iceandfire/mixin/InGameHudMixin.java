package com.iafenvoy.iceandfire.mixin;

import com.iafenvoy.iceandfire.data.component.PortalDataComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow
    protected abstract void renderOverlay(DrawContext context, Identifier texture, float opacity);

    @Shadow
    @Final
    private static Identifier POWDER_SNOW_OUTLINE;

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getFrozenTicks()I"))
    private void renderDreadPortalOverlay(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (this.client.player == null) return;
        PortalDataComponent component = PortalDataComponent.get(this.client.player);
        int renderTick = component.getRenderTick(), i = this.client.player.getMinFreezeDamageTicks();
        if (renderTick > 0)
            this.renderOverlay(context, POWDER_SNOW_OUTLINE, (float) Math.min(renderTick, i) / i);
    }
}
