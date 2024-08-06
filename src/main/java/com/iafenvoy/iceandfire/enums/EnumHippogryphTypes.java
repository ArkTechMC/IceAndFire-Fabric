package com.iafenvoy.iceandfire.enums;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.registry.tag.IafBiomeTags;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class EnumHippogryphTypes {
    private static final List<EnumHippogryphTypes> TYPES = new ArrayList<>();
    private static final Map<String,EnumHippogryphTypes> BY_NAME =new HashMap<>();
    public static final EnumHippogryphTypes BLACK = new EnumHippogryphTypes("black", false, biome -> biome.isIn(IafBiomeTags.HIPPOGRYPH_BLACK));
    public static final EnumHippogryphTypes BROWN = new EnumHippogryphTypes("brown", false, biome -> biome.isIn(IafBiomeTags.HIPPOGRYPH_BROWN));
    public static final EnumHippogryphTypes GRAY = new EnumHippogryphTypes("gray", false, biome -> biome.isIn(IafBiomeTags.HIPPOGRYPH_GRAY));
    public static final EnumHippogryphTypes CHESTNUT = new EnumHippogryphTypes("chestnut", false, biome -> biome.isIn(IafBiomeTags.HIPPOGRYPH_CHESTNUT));
    public static final EnumHippogryphTypes CREAMY = new EnumHippogryphTypes("creamy", false, biome -> biome.isIn(IafBiomeTags.HIPPOGRYPH_CREAMY));
    public static final EnumHippogryphTypes DARK_BROWN = new EnumHippogryphTypes("dark_brown", false, biome -> biome.isIn(IafBiomeTags.HIPPOGRYPH_DARK_BROWN));
    public static final EnumHippogryphTypes WHITE = new EnumHippogryphTypes("white", false, biome -> biome.isIn(IafBiomeTags.HIPPOGRYPH_WHITE));
    public static final EnumHippogryphTypes RAPTOR = new EnumHippogryphTypes("raptor", true, biome -> false);
    public static final EnumHippogryphTypes ALEX = new EnumHippogryphTypes("alex", true, biome -> false);
    public static final EnumHippogryphTypes DODO = new EnumHippogryphTypes("dodo", true, biome -> false);

    private final String name;
    public final boolean developer;
    private final Predicate<RegistryEntry<Biome>> biomePredicate;

    public EnumHippogryphTypes(String name, boolean developer, Predicate<RegistryEntry<Biome>> biomePredicate) {
        this.name = name;
        this.developer = developer;
        this.biomePredicate = biomePredicate;
        TYPES.add(this);
        BY_NAME.put(name,this);
    }

    public String getName() {
        return name;
    }

    public boolean allowSpawn(RegistryEntry<Biome> biome) {
        return this.biomePredicate.test(biome);
    }

    public Identifier getTexture(boolean blink) {
        return new Identifier(IceAndFire.MOD_ID, "textures/models/hippogryph/" + this.name.toLowerCase(Locale.ROOT) + (blink ? "_blink" : "") + ".png");
    }

    public static List<EnumHippogryphTypes> values() {
        return ImmutableList.copyOf(TYPES);
    }

    public static EnumHippogryphTypes getByName(String name){
        return BY_NAME.getOrDefault(name,BLACK);
    }

    public static EnumHippogryphTypes[] getWildTypes() {
        return new EnumHippogryphTypes[]{BLACK, BROWN, GRAY, CHESTNUT, CREAMY, DARK_BROWN, WHITE};
    }

    public static EnumHippogryphTypes getRandomType() {
        return getWildTypes()[ThreadLocalRandom.current().nextInt(getWildTypes().length - 1)];
    }

    public static EnumHippogryphTypes getBiomeType(RegistryEntry<Biome> biome) {
        List<EnumHippogryphTypes> types = values().stream().filter(x -> x.allowSpawn(biome)).toList();
        if (types.isEmpty()) return getRandomType();
        else {
            if (types.contains(GRAY) && types.contains(CHESTNUT))
                return GRAY;
            return types.get(ThreadLocalRandom.current().nextInt(types.size()));
        }
    }
}
