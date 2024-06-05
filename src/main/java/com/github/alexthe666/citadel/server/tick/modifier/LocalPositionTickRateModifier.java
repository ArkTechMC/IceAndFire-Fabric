package com.github.alexthe666.citadel.server.tick.modifier;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LocalPositionTickRateModifier extends LocalTickRateModifier {

    private Vec3d center;

    public LocalPositionTickRateModifier(Vec3d center, double range, RegistryKey<World> dimension, int durationInMasterTicks, float tickRateMultiplier) {
        super(TickRateModifierType.LOCAL_POSITION, range, dimension, durationInMasterTicks, tickRateMultiplier);
        this.center = center;
    }

    public LocalPositionTickRateModifier(NbtCompound tag) {
        super(tag);
        this.center = new Vec3d(tag.getDouble("CenterX"), tag.getDouble("CenterY"), tag.getDouble("CenterZ"));
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = super.toTag();
        tag.putDouble("CenterX", this.center.x);
        tag.putDouble("CenterY", this.center.y);
        tag.putDouble("CenterZ", this.center.z);
        return tag;
    }

    public Vec3d getCenter() {
        return this.center;
    }

    public Vec3d getCenter(World level) {
        return this.getCenter();
    }

    public void setCenter(Vec3d center) {
        this.center = center;
    }


}
