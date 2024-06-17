package com.iafenvoy.citadel.server.tick.modifier;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public abstract class TickRateModifier {

    private final TickRateModifierType type;
    private float maxDuration;
    private float duration;
    private float tickRateMultiplier;

    public TickRateModifier(TickRateModifierType type, int maxDuration, float tickRateMultiplier) {
        this.type = type;
        this.maxDuration = maxDuration;
        this.tickRateMultiplier = tickRateMultiplier;
    }

    public TickRateModifier(NbtCompound tag) {
        this.type = TickRateModifierType.fromId(tag.getInt("TickRateType"));
        this.maxDuration = tag.getFloat("MaxDuration");
        this.duration = tag.getFloat("Duration");
        this.tickRateMultiplier = tag.getFloat("SpeedMultiplier");
    }

    public static TickRateModifier fromTag(NbtCompound tag) {
        TickRateModifierType typeFromNbt = TickRateModifierType.fromId(tag.getInt("TickRateType"));
        try {
            return typeFromNbt.getTickRateClass().getConstructor(NbtCompound.class).newInstance(tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TickRateModifierType getType() {
        return this.type;
    }

    public float getMaxDuration() {
        return this.maxDuration;
    }

    public void setMaxDuration(float maxDuration) {
        this.maxDuration = maxDuration;
    }

    public float getTickRateMultiplier() {
        return this.tickRateMultiplier;
    }

    public void setTickRateMultiplier(float tickRateMultiplier) {
        this.tickRateMultiplier = tickRateMultiplier;
    }

    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();
        tag.putInt("TickRateType", this.type.toId());
        tag.putFloat("MaxDuration", this.maxDuration);
        tag.putFloat("Duration", this.duration);
        tag.putFloat("SpeedMultiplier", this.tickRateMultiplier);
        return tag;
    }

    public boolean isGlobal() {
        return this.type.isLocal();
    }

    public void masterTick() {
        this.duration++;
    }


    public boolean doRemove() {
        float f = this.tickRateMultiplier == 0 || this.getType() == TickRateModifierType.CELESTIAL ? 1.0F : 1F / this.tickRateMultiplier;
        return this.duration >= this.maxDuration * f;
    }

    public abstract boolean appliesTo(World level, double x, double y, double z);
}
