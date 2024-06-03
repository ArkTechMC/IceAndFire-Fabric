package com.github.alexthe666.citadel.server.tick.modifier;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class CelestialTickRateModifier extends TickRateModifier {

    public CelestialTickRateModifier(int durationInMasterTicks, float tickRateMultiplier) {
        super(TickRateModifierType.CELESTIAL, durationInMasterTicks, tickRateMultiplier);
    }

    public CelestialTickRateModifier(NbtCompound tag) {
        super(tag);
    }

    @Override
    public boolean appliesTo(World level, double x, double y, double z) {
        return false;
    }
}
