package com.github.alexthe666.iceandfire.datagen.tags;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.datagen.IafPOITypes;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.vanilla.VanillaPointOfInterestTypeTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.PointOfInterestTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class POITagGenerator extends VanillaPointOfInterestTypeTagProvider {

    public POITagGenerator(DataOutput pOutput, CompletableFuture<RegistryWrapper.WrapperLookup> pLookupProvider) {
        super(pOutput, pLookupProvider);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup pProvider) {
        this.getOrCreateTagBuilder(PointOfInterestTypeTags.ACQUIRABLE_JOB_SITE).add(IafPOITypes.SCRIBE_POI);
    }

    private static TagKey<PointOfInterestType> create(String name) {
        return TagKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, new Identifier(IceAndFire.MOD_ID, name));
    }

    @Override
    public String getName() {
        return "Ice and Fire POI Type Tags";
    }
}

