package com.iafenvoy.citadel.server.entity;

import com.iafenvoy.citadel.server.tick.modifier.TickRateModifier;

public interface IModifiesTime {
    boolean isTimeModificationValid(TickRateModifier modifier);
}
