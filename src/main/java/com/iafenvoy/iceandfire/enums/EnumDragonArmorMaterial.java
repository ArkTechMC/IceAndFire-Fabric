package com.iafenvoy.iceandfire.enums;

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

    public String getName() {
        return this.name().toLowerCase();
    }
}
