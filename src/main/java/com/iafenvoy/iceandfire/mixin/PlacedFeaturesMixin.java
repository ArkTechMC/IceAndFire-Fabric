package com.iafenvoy.iceandfire.mixin;

import com.iafenvoy.iceandfire.registry.IafPlacedFeatures;
import net.minecraft.registry.Registerable;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlacedFeatures.class)
public class PlacedFeaturesMixin {
    @Inject(method = "bootstrap", at = @At("RETURN"))
    private static void registerPlacedFeatures(Registerable<PlacedFeature> featureRegisterable, CallbackInfo ci) {
        IafPlacedFeatures.bootstrap(featureRegisterable);
    }
}
