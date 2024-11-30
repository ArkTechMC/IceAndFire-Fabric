package com.iafenvoy.iceandfire.data;

import java.util.Locale;

public enum DragonArmorPart {
    HEAD, NECK, BODY, TAIL;

    public String getId() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
