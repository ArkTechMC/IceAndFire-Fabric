package com.github.alexthe666.citadel.server.tick;

import com.github.alexthe666.citadel.server.tick.modifier.TickRateModifier;
import com.github.alexthe666.citadel.server.tick.modifier.TickRateModifierType;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public abstract class TickRateTracker {

    public List<TickRateModifier> tickRateModifierList = new ArrayList<>();
    public List<Entity> specialTickRateEntities = new ArrayList<>();

    private long masterTickCount;

    public void masterTick() {
        this.masterTickCount++;
        this.specialTickRateEntities.forEach(this::tickBlockedEntity);
        this.specialTickRateEntities.removeIf(this::hasNormalTickRate);
        for (TickRateModifier modifier : this.tickRateModifierList) {
            modifier.masterTick();
        }
        if (!this.tickRateModifierList.isEmpty()) {
            if (this.tickRateModifierList.removeIf(TickRateModifier::doRemove)) {
                this.sync();
            }
        }
    }

    protected void sync() {
    }

    public boolean hasModifiersActive() {
        return !this.tickRateModifierList.isEmpty();
    }

    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();
        NbtList list = new NbtList();
        for (TickRateModifier modifier : this.tickRateModifierList) {
            if (!modifier.doRemove()) {
                list.add(modifier.toTag());
            }
        }
        tag.put("TickRateModifiers", list);
        return tag;
    }

    public void fromTag(NbtCompound tag) {
        if (tag.contains("TickRateModifiers")) {
            NbtList list = tag.getList("TickRateModifiers", 10);
            for (int i = 0; i < list.size(); ++i) {
                NbtCompound tag1 = list.getCompound(i);
                TickRateModifier modifier = TickRateModifier.fromTag(tag1);
                if (!modifier.doRemove()) {
                    this.tickRateModifierList.add(modifier);
                }
            }
        }
    }

    public long getDayTimeIncrement(long timeIn) {
        float f = 1F;
        for (TickRateModifier modifier : this.tickRateModifierList) {
            if (modifier.getType() == TickRateModifierType.CELESTIAL) {
                f *= modifier.getTickRateMultiplier();
            }
        }
        if (f < 1F && f > 0F) {
            int inverse = (int) (1 / f);
            return this.masterTickCount % inverse == 0 ? timeIn : 0;
        }
        return (long) (timeIn * f);
    }

    public float getEntityTickLengthModifier(Entity entity) {
        float f = 1.0F;
        for (TickRateModifier modifier : this.tickRateModifierList) {
            if (modifier.getType().isLocal() && modifier.appliesTo(entity.getWorld(), entity.getX(), entity.getY(), entity.getZ())) {
                f *= modifier.getTickRateMultiplier();
            }
        }
        return f;
    }

    public boolean hasNormalTickRate(Entity entity) {
        return this.getEntityTickLengthModifier(entity) == 1.0F;
    }

    public boolean isTickingHandled(Entity entity){
        return this.specialTickRateEntities.contains(entity);
    }

    public void addTickBlockedEntity(Entity entity) {
        if (!this.isTickingHandled(entity)) {
            this.specialTickRateEntities.add(entity);
        }
    }

    protected void tickBlockedEntity(Entity entity){
        this.tickEntityAtCustomRate(entity);
    }


    protected abstract void tickEntityAtCustomRate(Entity entity);

}
