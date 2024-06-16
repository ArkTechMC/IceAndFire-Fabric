package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.entity.util.dragon.DragonType;
import net.minecraft.util.Formatting;

public enum EnumDragonEgg {
    RED(Formatting.DARK_RED, DragonType.FIRE), GREEN(Formatting.DARK_GREEN, DragonType.FIRE),
    BRONZE(Formatting.GOLD, DragonType.FIRE), GRAY(Formatting.GRAY, DragonType.FIRE),
    BLUE(Formatting.AQUA, DragonType.ICE), WHITE(Formatting.WHITE, DragonType.ICE),
    SAPPHIRE(Formatting.BLUE, DragonType.ICE), SILVER(Formatting.DARK_GRAY, DragonType.ICE),
    ELECTRIC(Formatting.DARK_BLUE, DragonType.LIGHTNING),
    AMYTHEST(Formatting.LIGHT_PURPLE, DragonType.LIGHTNING), COPPER(Formatting.GOLD, DragonType.LIGHTNING),
    BLACK(Formatting.DARK_GRAY, DragonType.LIGHTNING);

    public final Formatting color;
    public final DragonType dragonType;

    EnumDragonEgg(final Formatting color, final DragonType dragonType) {
        this.color = color;
        this.dragonType = dragonType;
    }

    public static EnumDragonEgg byMetadata(int meta) {
        EnumDragonEgg i = values()[meta];
        return i == null ? RED : i;
    }
}
