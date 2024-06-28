package com.iafenvoy.iceandfire.enums;

public enum EnumDragonArmorPart {
    HEAD, NECK, BODY, TAIL;

    public String getName() {
        return this.name().toLowerCase();
    }
}
