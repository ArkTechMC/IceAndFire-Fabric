package com.iafenvoy.iceandfire.enums;

public enum EnumDragonArmorMaterial {
    IRON("iron"),
    COPPER("copper"),
    SILVER("silver"),
    GOLD("gold"),
    DIAMOND("diamond"),
    NETHERITE("netherite"),
    DRAGON_STEEL_FIRE("dragon_steel_fire"),
    DRAGON_STEEL_ICE("dragon_steel_ice"),
    DRAGON_STEEL_LIGHTNING("dragon_steel_lightning");

    private final String id;

    EnumDragonArmorMaterial(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id.toLowerCase();
    }
}
