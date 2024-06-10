package dev.arktechmc.iafextra.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @ModifyReturnValue(method = "getWindowTitle", at = @At("TAIL"))
    private String appendDevelopmentTitle(String original) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment())
            original += " | Ice And Fire For Fabric Loader (Development Mode)";
        return original;
    }
}
