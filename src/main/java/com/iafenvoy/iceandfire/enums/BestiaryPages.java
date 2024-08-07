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

public final class BestiaryPages {
    private static final List<BestiaryPages> ALL_PAGES = new ArrayList<>();
    private static final Map<String, BestiaryPages> BY_NAME = new HashMap<>();

    public static final BestiaryPages INTRODUCTION = new BestiaryPages("introduction", 2);
    public static final BestiaryPages FIREDRAGON = new BestiaryPages("fire_dragon", 4);
    public static final BestiaryPages FIREDRAGONEGG = new BestiaryPages("fire_dragon_egg", 1);
    public static final BestiaryPages ICEDRAGON = new BestiaryPages("ice_dragon", 4);
    public static final BestiaryPages ICEDRAGONEGG = new BestiaryPages("ice_dragon_egg", 1);
    public static final BestiaryPages LIGHTNINGDRAGON = new BestiaryPages("lightning_dragon", 5);
    public static final BestiaryPages LIGHTNINGDRAGONEGG = new BestiaryPages("lightning_dragon_egg", 1);
    public static final BestiaryPages TAMEDDRAGONS = new BestiaryPages("tamed_dragons", 3);
    public static final BestiaryPages MATERIALS = new BestiaryPages("materials", 2);
    public static final BestiaryPages ALCHEMY = new BestiaryPages("alchemy", 1);
    public static final BestiaryPages DRAGONFORGE = new BestiaryPages("dragon_forge", 3);
    public static final BestiaryPages HIPPOGRYPH = new BestiaryPages("hippogryph", 1);
    public static final BestiaryPages GORGON = new BestiaryPages("gorgon", 1);
    public static final BestiaryPages PIXIE = new BestiaryPages("pixie", 1);
    public static final BestiaryPages CYCLOPS = new BestiaryPages("cyclops", 2);
    public static final BestiaryPages SIREN = new BestiaryPages("siren", 2);
    public static final BestiaryPages HIPPOCAMPUS = new BestiaryPages("hippocampus", 2);
    public static final BestiaryPages DEATHWORM = new BestiaryPages("deathworm", 3);
    public static final BestiaryPages COCKATRICE = new BestiaryPages("cockatrice", 2);
    public static final BestiaryPages STYMPHALIANBIRD = new BestiaryPages("stymphalian_bird", 1);
    public static final BestiaryPages TROLL = new BestiaryPages("troll", 2);
    public static final BestiaryPages MYRMEX = new BestiaryPages("myrmex", 4);
    public static final BestiaryPages AMPHITHERE = new BestiaryPages("amphithere", 2);
    public static final BestiaryPages SEASERPENT = new BestiaryPages("sea_serpent", 2);
    public static final BestiaryPages DREAD_MOBS = new BestiaryPages("dread_mobs", 1);
    public static final BestiaryPages GHOST = new BestiaryPages("ghost", 1);

    private final int id;
    private final String name;
    private final int pages;

    public BestiaryPages(String name, int pages) {
        this.id = ALL_PAGES.size();
        this.name = name;
        this.pages = pages;
        ALL_PAGES.add(this);
        BY_NAME.put(name, this);
    }

    public static Set<BestiaryPages> containedPages(Collection<String> pages) {
        return pages.stream().map(x -> BY_NAME.getOrDefault(x, INTRODUCTION)).collect(Collectors.toSet());
    }

    public static BestiaryPages getRand() {
        return ALL_PAGES.get(ThreadLocalRandom.current().nextInt(ALL_PAGES.size()));
    }

    public static List<BestiaryPages> possiblePages(ItemStack book) {
        if (book.getItem() instanceof ItemBestiary) {
            NbtCompound tag = book.getOrCreateNbt();
            Collection<BestiaryPages> containedPages = containedPages(tag.getList("Pages", NbtElement.STRING_TYPE).stream().map(NbtElement::asString).toList());
            List<BestiaryPages> possiblePages = new ArrayList<>(ALL_PAGES);
            possiblePages.removeAll(containedPages);
            return possiblePages;
        }
        return Collections.emptyList();
    }

    public static void addPage(BestiaryPages page, ItemStack book) {
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

    public static BestiaryPages fromInt(int index) {
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
        var that = (BestiaryPages) obj;
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

    public static List<BestiaryPages> values() {
        return ImmutableList.copyOf(ALL_PAGES);
    }
}
