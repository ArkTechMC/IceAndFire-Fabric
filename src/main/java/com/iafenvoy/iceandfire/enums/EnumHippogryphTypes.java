package com.iafenvoy.iceandfire.enums;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.registry.tag.IafBiomeTags;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public enum EnumHippogryphTypes {
    BLACK(false),
    BROWN(false),
    GRAY(false),
    CHESTNUT(false),
    CREAMY(false),
    DARK_BROWN(false),
    WHITE(false),
    RAPTOR(true),
    ALEX(true),
    DODO(true);

    public final boolean developer;
    public final Identifier TEXTURE;
    public final Identifier TEXTURE_BLINK;

    EnumHippogryphTypes(boolean developer) {
        this.developer = developer;
        this.TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/hippogryph/" + this.name().toLowerCase(Locale.ROOT) + ".png");
        this.TEXTURE_BLINK = new Identifier(IceAndFire.MOD_ID, "textures/models/hippogryph/" + this.name().toLowerCase(Locale.ROOT) + "_blink.png");
    }

    public static EnumHippogryphTypes[] getWildTypes() {
        return new EnumHippogryphTypes[]{BLACK, BROWN, GRAY, CHESTNUT, CREAMY, DARK_BROWN, WHITE};
    }

    public static EnumHippogryphTypes getRandomType() {
        return getWildTypes()[ThreadLocalRandom.current().nextInt(getWildTypes().length - 1)];
    }

    public static EnumHippogryphTypes getBiomeType(RegistryEntry<Biome> biome) {
        List<EnumHippogryphTypes> types = new ArrayList<>();
        if (biome.isIn(IafBiomeTags.HIPPOGRYPH_BLACK)) types.add(BLACK);
        if (biome.isIn(IafBiomeTags.HIPPOGRYPH_BROWN)) types.add(BROWN);
        if (biome.isIn(IafBiomeTags.HIPPOGRYPH_GRAY)) types.add(GRAY);
        if (biome.isIn(IafBiomeTags.HIPPOGRYPH_CHESTNUT)) types.add(CHESTNUT);
        if (biome.isIn(IafBiomeTags.HIPPOGRYPH_CREAMY)) types.add(CREAMY);
        if (biome.isIn(IafBiomeTags.HIPPOGRYPH_DARK_BROWN)) types.add(DARK_BROWN);
        if (biome.isIn(IafBiomeTags.HIPPOGRYPH_WHITE)) types.add(WHITE);
        if (types.isEmpty()) return getRandomType();
        else {
            if (types.contains(GRAY) && types.contains(CHESTNUT))
                return GRAY;
            return types.get(ThreadLocalRandom.current().nextInt(types.size()));
        }
    }
}
