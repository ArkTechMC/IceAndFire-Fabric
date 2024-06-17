package com.iafenvoy.iceandfire.enums;

import com.iafenvoy.iceandfire.item.ItemMobSkull;
import com.iafenvoy.iceandfire.registry.IafItems;
import net.minecraft.item.Item;

import java.util.Locale;

public enum EnumSkullType {
    HIPPOGRYPH,
    CYCLOPS,
    COCKATRICE,
    STYMPHALIAN,
    TROLL,
    AMPHITHERE,
    SEASERPENT,
    HYDRA;

    public final String itemResourceName;
    public Item skull_item;

    EnumSkullType() {
        this.itemResourceName = this.name().toLowerCase(Locale.ROOT) + "_skull";
    }

    public static void initItems() {
        for (EnumSkullType skull : EnumSkullType.values())
            skull.skull_item = IafItems.register(skull.itemResourceName, new ItemMobSkull(skull));
    }
}
