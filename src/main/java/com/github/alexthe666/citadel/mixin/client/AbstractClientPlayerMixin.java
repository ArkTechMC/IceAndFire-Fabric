package com.github.alexthe666.citadel.mixin.client;

import com.github.alexthe666.citadel.CitadelConstants;
import com.github.alexthe666.citadel.client.rewards.CitadelCapes;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerMixin extends PlayerEntity {

    public AbstractClientPlayerMixin(World p_250508_, BlockPos p_250289_, float p_251702_, GameProfile p_252153_) {
        super(p_250508_, p_250289_, p_251702_, p_252153_);
    }

    @Inject(at = @At("HEAD"), remap = CitadelConstants.REMAPREFS, method = "Lnet/minecraft/client/player/AbstractClientPlayer;getCloakTextureLocation()Lnet/minecraft/resources/ResourceLocation;", cancellable = true)
    private void citadel_getCapeLocation(CallbackInfoReturnable<Identifier> cir) {
        CitadelCapes.Cape cape = CitadelCapes.getCurrentCape(this);
        if(cape != null){
            cir.setReturnValue(cape.getTexture());
        }
    }

    @Inject(at = @At("HEAD"), remap = CitadelConstants.REMAPREFS, method = "Lnet/minecraft/client/player/AbstractClientPlayer;getElytraTextureLocation()Lnet/minecraft/resources/ResourceLocation;", cancellable = true)
    private void citadel_getElytraLocation(CallbackInfoReturnable<Identifier> cir) {
        CitadelCapes.Cape cape = CitadelCapes.getCurrentCape(this);
        if(cape != null){
            cir.setReturnValue(cape.getTexture());
        }
    }
}
