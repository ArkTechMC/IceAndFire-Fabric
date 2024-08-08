package com.iafenvoy.iceandfire.data;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.registry.tag.IafBiomeTags;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class HippogryphTypes {
    private static final List<HippogryphTypes> TYPES = new ArrayList<>();
    private static final Map<String, HippogryphTypes> BY_NAME = new HashMap<>();
    public static final HippogryphTypes BLACK = new HippogryphTypes("black", false, biome -> biome.isIn(IafBiomeTags.HIPPOGRYPH_BLACK));
    public static final HippogryphTypes BROWN = new HippogryphTypes("brown", false, biome -> biome.isIn(IafBiomeTags.HIPPOGRYPH_BROWN));
    public static final HippogryphTypes GRAY = new HippogryphTypes("gray", false, biome -> biome.isIn(IafBiomeTags.HIPPOGRYPH_GRAY));
    public static final HippogryphTypes CHESTNUT = new HippogryphTypes("chestnut", false, biome -> biome.isIn(IafBiomeTags.HIPPOGRYPH_CHESTNUT));
    public static final HippogryphTypes CREAMY = new HippogryphTypes("creamy", false, biome -> biome.isIn(IafBiomeTags.HIPPOGRYPH_CREAMY));
    public static final HippogryphTypes DARK_BROWN = new HippogryphTypes("dark_brown", false, biome -> biome.isIn(IafBiomeTags.HIPPOGRYPH_DARK_BROWN));
    public static final HippogryphTypes WHITE = new HippogryphTypes("white", false, biome -> biome.isIn(IafBiomeTags.HIPPOGRYPH_WHITE));
    public static final HippogryphTypes RAPTOR = new HippogryphTypes("raptor", true, biome -> false);
    public static final HippogryphTypes ALEX = new HippogryphTypes("alex", true, biome -> false);
    public static final HippogryphTypes DODO = new HippogryphTypes("dodo", true, biome -> false);
    public final boolean developer;
    private final String name;
    private final Predicate<RegistryEntry<Biome>> biomePredicate;

    public HippogryphTypes(String name, boolean developer, Predicate<RegistryEntry<Biome>> biomePredicate) {
        this.name = name;
        this.developer = developer;
        this.biomePredicate = biomePredicate;
        TYPES.add(this);
        BY_NAME.put(name, this);
    }

    public static List<HippogryphTypes> values() {
        return ImmutableList.copyOf(TYPES);
    }

    public static HippogryphTypes getByName(String name) {
        return BY_NAME.getOrDefault(name, BLACK);
    }

    public static HippogryphTypes[] getWildTypes() {
        return new HippogryphTypes[]{BLACK, BROWN, GRAY, CHESTNUT, CREAMY, DARK_BROWN, WHITE};
    }

    public static HippogryphTypes getRandomType() {
        return getWildTypes()[ThreadLocalRandom.current().nextInt(getWildTypes().length - 1)];
    }

    public static HippogryphTypes getBiomeType(RegistryEntry<Biome> biome) {
        List<HippogryphTypes> types = values().stream().filter(x -> x.allowSpawn(biome)).toList();
        if (types.isEmpty()) return getRandomType();
        else {
            if (types.contains(GRAY) && types.contains(CHESTNUT))
                return GRAY;
            return types.get(ThreadLocalRandom.current().nextInt(types.size()));
        }
    }

    public String getName() {
        return this.name;
    }

    public boolean allowSpawn(RegistryEntry<Biome> biome) {
        return this.biomePredicate.test(biome);
    }

    public Identifier getTexture(boolean blink) {
        return new Identifier(IceAndFire.MOD_ID, "textures/models/hippogryph/" + this.name.toLowerCase(Locale.ROOT) + (blink ? "_blink" : "") + ".png");
    }
}
