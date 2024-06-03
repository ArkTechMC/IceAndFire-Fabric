package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import io.github.fabricators_of_create.porting_lib.data.SpriteSourceProvider;
import net.minecraft.client.texture.atlas.SingleAtlasSource;
import net.minecraft.data.DataOutput;

import java.util.Optional;

import static com.github.alexthe666.iceandfire.client.IafClientSetup.*;

public class AtlasGenerator extends SpriteSourceProvider {

    public AtlasGenerator(DataOutput output) {
        super(output, IceAndFire.MOD_ID);
    }

    @Override
    protected void addSources() {
        this.atlas(CHESTS_ATLAS).addSource(new SingleAtlasSource(GHOST_CHEST_LOCATION, Optional.empty()));
        this.atlas(CHESTS_ATLAS).addSource(new SingleAtlasSource(GHOST_CHEST_LEFT_LOCATION, Optional.empty()));
        this.atlas(CHESTS_ATLAS).addSource(new SingleAtlasSource(GHOST_CHEST_RIGHT_LOCATION, Optional.empty()));

        //this.atlas(SHIELD_PATTERNS_ATLAS).addSource(new SingleFile(TwilightForestMod.prefix("model/knightmetal_shield"), Optional.empty()));


    }
}
