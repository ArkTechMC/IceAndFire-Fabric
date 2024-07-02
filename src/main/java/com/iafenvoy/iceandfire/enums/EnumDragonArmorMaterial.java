package com.iafenvoy.iceandfire.enums;

import java.util.Locale;

public enum EnumDragonArmorMaterial {
    IRON,
    COPPER,
    SILVER,
    GOLD,
    DIAMOND,
    NETHERITE,
    DRAGON_STEEL_FIRE,
    DRAGON_STEEL_ICE,
    DRAGON_STEEL_LIGHTNING;

    public String getId() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
