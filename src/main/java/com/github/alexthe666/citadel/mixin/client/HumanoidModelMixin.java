package com.github.alexthe666.citadel.mixin.client;

import com.github.alexthe666.citadel.CitadelConstants;
import com.github.alexthe666.citadel.client.event.EventPosePlayerHand;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(BipedEntityModel.class)
public abstract class HumanoidModelMixin extends Model {

    public HumanoidModelMixin(Function<Identifier, RenderLayer> p_103110_) {
        super(p_103110_);
    }

    @Inject(at = @At("HEAD"), remap = CitadelConstants.REMAPREFS, method = "Lnet/minecraft/client/model/HumanoidModel;poseRightArm(Lnet/minecraft/world/entity/LivingEntity;)V", cancellable = true)
    private void citadel_poseRightArm(LivingEntity entity, CallbackInfo ci) {
        EventPosePlayerHand event = new EventPosePlayerHand(entity, (BipedEntityModel) ((Model) this), false);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Event.Result.ALLOW) {
            ci.cancel();
        }
    }


    @Inject(at = @At("HEAD"), remap = CitadelConstants.REMAPREFS, method = "Lnet/minecraft/client/model/HumanoidModel;poseLeftArm(Lnet/minecraft/world/entity/LivingEntity;)V", cancellable = true)
    private void citadel_poseLeftArm(LivingEntity entity, CallbackInfo ci) {
        EventPosePlayerHand event = new EventPosePlayerHand(entity, (BipedEntityModel) ((Model) this), true);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Event.Result.ALLOW) {
            ci.cancel();
        }
    }


}
