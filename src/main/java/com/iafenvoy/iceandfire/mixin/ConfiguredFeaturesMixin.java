package com.iafenvoy.iceandfire.mixin;

import com.iafenvoy.iceandfire.registry.IafConfiguredFeatures;
import net.minecraft.registry.Registerable;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConfiguredFeatures.class)
public class ConfiguredFeaturesMixin {
    @Inject(method = "bootstrap", at = @At("RETURN"))
    private static void registerConfiguredFeatures(Registerable<ConfiguredFeature<?, ?>> featureRegisterable, CallbackInfo ci) {
        IafConfiguredFeatures.bootstrap(featureRegisterable);
    }
}
