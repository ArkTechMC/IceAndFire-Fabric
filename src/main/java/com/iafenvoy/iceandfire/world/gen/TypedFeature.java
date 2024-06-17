package com.iafenvoy.iceandfire.world.gen;

import com.iafenvoy.iceandfire.world.IafWorldData;

public interface TypedFeature {
    IafWorldData.FeatureType getFeatureType();

    String getId();
}
