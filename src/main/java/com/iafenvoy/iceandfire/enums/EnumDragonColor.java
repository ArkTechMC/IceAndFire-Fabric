package com.iafenvoy.iceandfire.enums;

import com.iafenvoy.iceandfire.entity.util.dragon.DragonType;
import net.minecraft.util.Formatting;

public enum EnumDragonColor {
    RED(Formatting.DARK_RED, DragonType.FIRE),
    GREEN(Formatting.DARK_GREEN, DragonType.FIRE),
    BRONZE(Formatting.GOLD, DragonType.FIRE),
    GRAY(Formatting.GRAY, DragonType.FIRE),
    BLUE(Formatting.AQUA, DragonType.ICE),
    WHITE(Formatting.WHITE, DragonType.ICE),
    SAPPHIRE(Formatting.BLUE, DragonType.ICE),
    SILVER(Formatting.DARK_GRAY, DragonType.ICE),
    ELECTRIC(Formatting.DARK_BLUE, DragonType.LIGHTNING),
    AMETHYST(Formatting.LIGHT_PURPLE, DragonType.LIGHTNING),
    COPPER(Formatting.GOLD, DragonType.LIGHTNING),
    BLACK(Formatting.DARK_GRAY, DragonType.LIGHTNING);

    public final Formatting color;
    public final DragonType dragonType;

    EnumDragonColor(final Formatting color, final DragonType dragonType) {
        this.color = color;
        this.dragonType = dragonType;
    }

    public static EnumDragonColor byMetadata(int meta) {
        EnumDragonColor i = values()[meta];
        return i == null ? RED : i;
    }
}
