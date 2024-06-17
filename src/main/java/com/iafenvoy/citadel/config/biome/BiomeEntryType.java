package com.iafenvoy.citadel.config.biome;

public enum BiomeEntryType {
    REGISTRY_NAME(false), BIOME_TAG(false), BIOME_DICT(true), BIOME_CATEGORY(true);

    private final boolean depreciated;

    BiomeEntryType(boolean depreciated) {
        this.depreciated = depreciated;
    }

    public boolean isDepreciated() {
        return this.depreciated;
    }
}
