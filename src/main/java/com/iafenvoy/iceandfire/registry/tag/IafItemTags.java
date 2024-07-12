package com.iafenvoy.iceandfire.registry.tag;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import com.iafenvoy.iceandfire.registry.IafItems;
import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.ItemTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class IafItemTags {
    public static final TagKey<Item> DRAGON_ARROWS = createKey("dragon_arrows");
    public static final TagKey<Item> DRAGON_BLOODS = createKey("dragon_bloods");
    public static final TagKey<Item> DRAGON_HEARTS = createKey("dragon_hearts");
    public static final TagKey<Item> BREED_AMPITHERE = createKey("breed_ampithere");
    public static final TagKey<Item> BREED_HIPPOCAMPUS = createKey("breed_hippocampus");
    public static final TagKey<Item> BREED_HIPPOGRYPH = createKey("breed_hippogryph");
    public static final TagKey<Item> HEAL_AMPITHERE = createKey("heal_ampithere");
    public static final TagKey<Item> HEAL_COCKATRICE = createKey("heal_cockatrice");
    public static final TagKey<Item> HEAL_HIPPOCAMPUS = createKey("heal_hippocampus");
    public static final TagKey<Item> HEAL_PIXIE = createKey("heal_pixie");
    public static final TagKey<Item> TAME_HIPPOGRYPH = createKey("tame_hippogryph");
    public static final TagKey<Item> TAME_PIXIE = createKey("tame_pixie");
    public static final TagKey<Item> TEMPT_DRAGON = createKey("tempt_dragon");
    public static final TagKey<Item> TEMPT_HIPPOCAMPUS = createKey("tempt_hippocampus");
    public static final TagKey<Item> TEMPT_HIPPOGRYPH = createKey("tempt_hippogryph");
    private final static String INGOTS_PATH = CommonTags.Items.INGOTS.id().getPath();
    public static final TagKey<Item> INGOTS_SILVER = createForgeKey(INGOTS_PATH + "/silver");

    private static TagKey<Item> createKey(final String name) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier(IceAndFire.MOD_ID, name));
    }

    private static TagKey<Item> createForgeKey(final String name) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier("forge", name));
    }
}
