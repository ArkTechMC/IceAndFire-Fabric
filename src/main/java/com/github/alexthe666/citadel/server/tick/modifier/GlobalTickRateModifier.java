package com.github.alexthe666.citadel.server.tick.modifier;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class GlobalTickRateModifier extends TickRateModifier {

    public GlobalTickRateModifier(int durationInMasterTicks, float tickRateMultiplier) {
        super(TickRateModifierType.GLOBAL, durationInMasterTicks, tickRateMultiplier);
    }

    public GlobalTickRateModifier(NbtCompound tag) {
        super(tag);
    }

    @Override
    public boolean appliesTo(World level, double x, double y, double z) {
        return true;
    }

}
