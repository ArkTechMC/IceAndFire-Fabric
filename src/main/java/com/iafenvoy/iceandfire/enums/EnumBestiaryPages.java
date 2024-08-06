package com.iafenvoy.iceandfire.enums;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.iceandfire.item.ItemBestiary;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public final class EnumBestiaryPages {
    private static final List<EnumBestiaryPages> ALL_PAGES = new ArrayList<>();
    private static final Map<String, EnumBestiaryPages> BY_NAME = new HashMap<>();

    public static final EnumBestiaryPages INTRODUCTION = new EnumBestiaryPages("introduction", 2);
    public static final EnumBestiaryPages FIREDRAGON = new EnumBestiaryPages("fire_dragon", 4);
    public static final EnumBestiaryPages FIREDRAGONEGG = new EnumBestiaryPages("fire_dragon_egg", 1);
    public static final EnumBestiaryPages ICEDRAGON = new EnumBestiaryPages("ice_dragon", 4);
    public static final EnumBestiaryPages ICEDRAGONEGG = new EnumBestiaryPages("ice_dragon_egg", 1);
    public static final EnumBestiaryPages LIGHTNINGDRAGON = new EnumBestiaryPages("lightning_dragon", 5);
    public static final EnumBestiaryPages LIGHTNINGDRAGONEGG = new EnumBestiaryPages("lightning_dragon_egg", 1);
    public static final EnumBestiaryPages TAMEDDRAGONS = new EnumBestiaryPages("tamed_dragons", 3);
    public static final EnumBestiaryPages MATERIALS = new EnumBestiaryPages("materials", 2);
    public static final EnumBestiaryPages ALCHEMY = new EnumBestiaryPages("alchemy", 1);
    public static final EnumBestiaryPages DRAGONFORGE = new EnumBestiaryPages("dragon_forge", 3);
    public static final EnumBestiaryPages HIPPOGRYPH = new EnumBestiaryPages("hippogryph", 1);
    public static final EnumBestiaryPages GORGON = new EnumBestiaryPages("gorgon", 1);
    public static final EnumBestiaryPages PIXIE = new EnumBestiaryPages("pixie", 1);
    public static final EnumBestiaryPages CYCLOPS = new EnumBestiaryPages("cyclops", 2);
    public static final EnumBestiaryPages SIREN = new EnumBestiaryPages("siren", 2);
    public static final EnumBestiaryPages HIPPOCAMPUS = new EnumBestiaryPages("hippocampus", 2);
    public static final EnumBestiaryPages DEATHWORM = new EnumBestiaryPages("deathworm", 3);
    public static final EnumBestiaryPages COCKATRICE = new EnumBestiaryPages("cockatrice", 2);
    public static final EnumBestiaryPages STYMPHALIANBIRD = new EnumBestiaryPages("stymphalian_bird", 1);
    public static final EnumBestiaryPages TROLL = new EnumBestiaryPages("troll", 2);
    public static final EnumBestiaryPages MYRMEX = new EnumBestiaryPages("myrmex", 4);
    public static final EnumBestiaryPages AMPHITHERE = new EnumBestiaryPages("amphithere", 2);
    public static final EnumBestiaryPages SEASERPENT = new EnumBestiaryPages("sea_serpent", 2);
    public static final EnumBestiaryPages DREAD_MOBS = new EnumBestiaryPages("dread_mobs", 1);
    public static final EnumBestiaryPages GHOST = new EnumBestiaryPages("ghost", 1);

    private final int id;
    private final String name;
    private final int pages;

    public EnumBestiaryPages(String name, int pages) {
        this.id = ALL_PAGES.size();
        this.name = name;
        this.pages = pages;
        ALL_PAGES.add(this);
        BY_NAME.put(name, this);
    }

    public static Set<EnumBestiaryPages> containedPages(Collection<String> pages) {
        return pages.stream().map(x -> BY_NAME.getOrDefault(x, INTRODUCTION)).collect(Collectors.toSet());
    }

    public static EnumBestiaryPages getRand() {
        return ALL_PAGES.get(ThreadLocalRandom.current().nextInt(ALL_PAGES.size()));
    }

    public static List<EnumBestiaryPages> possiblePages(ItemStack book) {
        if (book.getItem() instanceof ItemBestiary) {
            NbtCompound tag = book.getOrCreateNbt();
            Collection<EnumBestiaryPages> containedPages = containedPages(tag.getList("Pages", NbtElement.STRING_TYPE).stream().map(NbtElement::asString).toList());
            List<EnumBestiaryPages> possiblePages = new ArrayList<>(ALL_PAGES);
            possiblePages.removeAll(containedPages);
            return possiblePages;
        }
        return Collections.emptyList();
    }

    public static void addPage(EnumBestiaryPages page, ItemStack book) {
        if (book.getItem() instanceof ItemBestiary) {
            NbtCompound tag = book.getOrCreateNbt();
            final List<String> already = tag.getList("Pages", NbtElement.STRING_TYPE).stream().map(NbtElement::asString).collect(Collectors.toList());
            if (!already.contains(page.name))
                already.add(page.name);
            tag.put("Pages", already.stream().reduce(new NbtList(), (p, c) -> {
                p.add(NbtString.of(c));
                return p;
            }, (a1, a2) -> a1));
        }
    }

    public static EnumBestiaryPages fromInt(int index) {
        if (index < 0) return null;
        int length = ALL_PAGES.size();
        return ALL_PAGES.get(index % length);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPagesCount() {
        return pages;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EnumBestiaryPages) obj;
        return Objects.equals(this.name, that.name) &&
                this.pages == that.pages;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pages);
    }

    @Override
    public String toString() {
        return "EnumBestiaryPages[" +
                "name=" + name + ", " +
                "pages=" + pages + ']';
    }

    public static List<EnumBestiaryPages> values() {
        return ImmutableList.copyOf(ALL_PAGES);
    }
}
