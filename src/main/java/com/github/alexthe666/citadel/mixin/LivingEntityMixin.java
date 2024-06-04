package com.github.alexthe666.citadel.mixin;

import com.github.alexthe666.citadel.server.entity.ICitadelDataEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ICitadelDataEntity {

    @Unique
    private static final TrackedData<NbtCompound> CITADEL_DATA = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);

    protected LivingEntityMixin(EntityType<? extends Entity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("TAIL"), method = "initDataTracker")
    private void citadel_registerData(CallbackInfo ci) {
        dataTracker.startTracking(CITADEL_DATA, new NbtCompound());
    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
    private void citadel_writeAdditional(NbtCompound compoundNBT, CallbackInfo ci) {
        NbtCompound citadelDat = getCitadelEntityData();
        if (citadelDat != null) {
            compoundNBT.put("CitadelData", citadelDat);
        }
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    private void citadel_readAdditional(NbtCompound compoundNBT, CallbackInfo ci) {
        if (compoundNBT.contains("CitadelData")) {
            setCitadelEntityData(compoundNBT.getCompound("CitadelData"));
        }
    }

    public NbtCompound getCitadelEntityData() {
        return dataTracker.get(CITADEL_DATA);
    }

    public void setCitadelEntityData(NbtCompound nbt) {
        dataTracker.set(CITADEL_DATA, nbt);
    }
}
