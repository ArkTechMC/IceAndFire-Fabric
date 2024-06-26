package com.iafenvoy.iceandfire.enums;

public enum EnumDragonArmorMaterial {
    IRON,
    GOLD,
    DIAMOND,
    SILVER,
    DRAGONSTEEL_FIRE,
    DRAGONSTEEL_ICE,
    COPPER,
    DRAGONSTEEL_LIGHTNING;

    public String getName() {
        return this.name().toLowerCase();
    }
}
