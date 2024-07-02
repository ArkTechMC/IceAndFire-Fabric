package com.iafenvoy.iceandfire.enums;

public enum EnumDragonArmorPart {
    HEAD("head"), NECK("neck"), BODY("body"), TAIL("tail");

    private final String id;

    EnumDragonArmorPart(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id.toLowerCase();
    }
}
