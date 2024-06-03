package com.github.alexthe666.iceandfire.mixin.gen;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

/**
 * Avoid log spam for dragon caves (some blocks can exceed the writable worldgen area due to their size)
 */
@Mixin(ChunkRegion.class)
public class WorldGenRegionMixin {
    @Shadow
    private Supplier<String> currentlyGeneratingStructureName;

    @Inject(method = "isValidForSetBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;error(Ljava/lang/String;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void skipLog(final BlockPos position, final CallbackInfoReturnable<Boolean> callback) {
        if (this.currentlyGeneratingStructureName != null && this.currentlyGeneratingStructureName.get().contains(IceAndFire.MOD_ID)) {
            callback.setReturnValue(false);
        }
    }
}
