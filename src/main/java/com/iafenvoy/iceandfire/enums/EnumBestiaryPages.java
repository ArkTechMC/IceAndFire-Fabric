package com.iafenvoy.iceandfire.enums;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import com.iafenvoy.iceandfire.item.ItemBestiary;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum EnumBestiaryPages {

    INTRODUCTION(2),
    FIREDRAGON(4),
    FIREDRAGONEGG(1),
    ICEDRAGON(4),
    ICEDRAGONEGG(1),
    TAMEDDRAGONS(3),
    MATERIALS(2),
    ALCHEMY(1),
    DRAGONFORGE(3),
    HIPPOGRYPH(1),
    GORGON(1),
    PIXIE(1),
    CYCLOPS(2),
    SIREN(2),
    HIPPOCAMPUS(2),
    DEATHWORM(3),
    COCKATRICE(2),
    STYMPHALIANBIRD(1),
    TROLL(2),
    MYRMEX(4),
    AMPHITHERE(2),
    SEASERPENT(2),
    HYDRA(2),
    DREAD_MOBS(1),
    LIGHTNINGDRAGON(5),
    LIGHTNINGDRAGONEGG(1),
    GHOST(1);

    public static final ImmutableList<EnumBestiaryPages> ALL_PAGES = ImmutableList.copyOf(EnumBestiaryPages.values());
    public static final ImmutableList<Integer> ALL_INDEXES = ImmutableList
            .copyOf(IntStream.range(0, EnumBestiaryPages.values().length).iterator());

    public final int pages;

    EnumBestiaryPages(int pages) {
        this.pages = pages;
    }

    public static Set<EnumBestiaryPages> containedPages(Collection<Integer> pages) {
        return pages.stream().map(ALL_PAGES::get).collect(Collectors.toSet());
    }

    public static boolean hasAllPages(ItemStack book) {
        assert book.getNbt() != null;
        return new HashSet<>(Ints.asList(book.getNbt().getIntArray("Pages"))).containsAll(ALL_INDEXES);
    }

    public static List<Integer> enumToInt(List<EnumBestiaryPages> pages) {
        return pages.stream().map(EnumBestiaryPages::ordinal).collect(Collectors.toList());
    }

    public static EnumBestiaryPages getRand() {
        return EnumBestiaryPages.values()[ThreadLocalRandom.current().nextInt(EnumBestiaryPages.values().length)];

    }

    public static void addRandomPage(ItemStack book) {
        if (book.getItem() instanceof ItemBestiary) {
            List<EnumBestiaryPages> list = EnumBestiaryPages.possiblePages(book);
            if (!list.isEmpty()) {
                addPage(list.get(ThreadLocalRandom.current().nextInt(list.size())), book);
            }
        }
    }

    public static List<EnumBestiaryPages> possiblePages(ItemStack book) {
        if (book.getItem() instanceof ItemBestiary) {
            NbtCompound tag = book.getOrCreateNbt();
            Collection<EnumBestiaryPages> containedPages = containedPages(Ints.asList(tag.getIntArray("Pages")));
            List<EnumBestiaryPages> possiblePages = new ArrayList<>(ALL_PAGES);
            possiblePages.removeAll(containedPages);
            return possiblePages;
        }
        return Collections.emptyList();
    }

    public static void addPage(EnumBestiaryPages page, ItemStack book) {
        if (book.getItem() instanceof ItemBestiary) {
            NbtCompound tag = book.getOrCreateNbt();
            final List<Integer> already = new ArrayList<>(Ints.asList(tag.getIntArray("Pages")));
            if (!already.contains(page.ordinal()))
                already.add(page.ordinal());
            tag.putIntArray("Pages", Ints.toArray(already));
        }
    }

    public static EnumBestiaryPages fromInt(int index) {
        if (index < 0) return null;
        int length = values().length;
        return values()[index % length];
    }
}
