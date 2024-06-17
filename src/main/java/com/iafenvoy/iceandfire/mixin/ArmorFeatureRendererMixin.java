package com.iafenvoy.iceandfire.mixin;

import com.iafenvoy.iceandfire.interfaces.IArmorTextureProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {
    @Inject(method = "getArmorTexture", at = @At("TAIL"), cancellable = true)
    private void pushTexture(ArmorItem item, boolean secondLayer, String overlay, CallbackInfoReturnable<Identifier> cir) {
        if (item instanceof IArmorTextureProvider provider)
            cir.setReturnValue(provider.getArmorTexture(item.getDefaultStack(), null, item.getSlotType(), overlay));
    }
}
