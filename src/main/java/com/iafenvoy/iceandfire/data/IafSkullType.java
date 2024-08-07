package com.iafenvoy.iceandfire.data;

import com.iafenvoy.iceandfire.item.ItemMobSkull;
import com.iafenvoy.iceandfire.registry.IafItems;
import net.minecraft.block.SkullBlock;
import net.minecraft.item.Item;

import java.util.Locale;

public enum IafSkullType implements SkullBlock.SkullType {
    HIPPOGRYPH,
    CYCLOPS,
    COCKATRICE,
    STYMPHALIAN,
    TROLL,
    AMPHITHERE,
    SEASERPENT,
    HYDRA;

    private final String itemResourceName;
    private Item skullItem;

    IafSkullType() {
        this.itemResourceName = this.name().toLowerCase(Locale.ROOT) + "_skull";
    }

    public static void initItems() {
        for (IafSkullType skull : IafSkullType.values())
            skull.skullItem = IafItems.register(skull.itemResourceName, new ItemMobSkull(skull));
    }

    public Item getSkullItem() {
        return this.skullItem;
    }
}
