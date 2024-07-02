package com.iafenvoy.iceandfire.enums;

import java.util.Locale;

public enum EnumDragonArmorPart {
    HEAD, NECK, BODY, TAIL;

    public String getId() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
