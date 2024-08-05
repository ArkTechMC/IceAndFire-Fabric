package com.iafenvoy.iceandfire.enums;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.entity.util.dragon.DragonType;
import com.iafenvoy.iceandfire.registry.IafItems;
import net.minecraft.item.Item;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public record EnumDragonColor(String id, Formatting color, DragonType dragonType, Supplier<Item> eggItem,
                              Supplier<Item> scaleItem) {
    private static final List<EnumDragonColor> VALUES = new ArrayList<>();
    private static final Map<String, EnumDragonColor> ID_MAP = new HashMap<>();
    private static final Map<DragonType, List<EnumDragonColor>> BY_DRAGON_TYPE = new HashMap<>();
    public static final EnumDragonColor RED = new EnumDragonColor("red", Formatting.DARK_RED, DragonType.FIRE, () -> IafItems.DRAGONEGG_RED, () -> IafItems.DRAGONSCALES_RED);
    public static final EnumDragonColor GREEN = new EnumDragonColor("green", Formatting.DARK_GREEN, DragonType.FIRE, () -> IafItems.DRAGONEGG_GREEN, () -> IafItems.DRAGONSCALES_GREEN);
    public static final EnumDragonColor BRONZE = new EnumDragonColor("bronze", Formatting.GOLD, DragonType.FIRE, () -> IafItems.DRAGONEGG_BRONZE, () -> IafItems.DRAGONSCALES_BRONZE);
    public static final EnumDragonColor GRAY = new EnumDragonColor("gray", Formatting.GRAY, DragonType.FIRE, () -> IafItems.DRAGONEGG_GRAY, () -> IafItems.DRAGONSCALES_GRAY);
    public static final EnumDragonColor BLUE = new EnumDragonColor("blue", Formatting.AQUA, DragonType.ICE, () -> IafItems.DRAGONEGG_BLUE, () -> IafItems.DRAGONSCALES_BLUE);
    public static final EnumDragonColor WHITE = new EnumDragonColor("white", Formatting.WHITE, DragonType.ICE, () -> IafItems.DRAGONEGG_WHITE, () -> IafItems.DRAGONSCALES_WHITE);
    public static final EnumDragonColor SAPPHIRE = new EnumDragonColor("sapphire", Formatting.BLUE, DragonType.ICE, () -> IafItems.DRAGONEGG_SAPPHIRE, () -> IafItems.DRAGONSCALES_SAPPHIRE);
    public static final EnumDragonColor SILVER = new EnumDragonColor("silver", Formatting.DARK_GRAY, DragonType.ICE, () -> IafItems.DRAGONEGG_SILVER, () -> IafItems.DRAGONSCALES_SILVER);
    public static final EnumDragonColor ELECTRIC = new EnumDragonColor("electric", Formatting.DARK_BLUE, DragonType.LIGHTNING, () -> IafItems.DRAGONEGG_ELECTRIC, () -> IafItems.DRAGONSCALES_ELECTRIC);
    public static final EnumDragonColor AMETHYST = new EnumDragonColor("amethyst", Formatting.LIGHT_PURPLE, DragonType.LIGHTNING, () -> IafItems.DRAGONEGG_AMETHYST, () -> IafItems.DRAGONSCALES_AMETHYST);
    public static final EnumDragonColor COPPER = new EnumDragonColor("copper", Formatting.GOLD, DragonType.LIGHTNING, () -> IafItems.DRAGONEGG_COPPER, () -> IafItems.DRAGONSCALES_COPPER);
    public static final EnumDragonColor BLACK = new EnumDragonColor("black", Formatting.DARK_GRAY, DragonType.LIGHTNING, () -> IafItems.DRAGONEGG_BLACK, () -> IafItems.DRAGONSCALES_BLACK);

    public EnumDragonColor(String id, Formatting color, DragonType dragonType, Supplier<Item> eggItem, Supplier<Item> scaleItem) {
        this.id = id;
        this.color = color;
        this.dragonType = dragonType;
        this.eggItem = eggItem;
        this.scaleItem = scaleItem;
        VALUES.add(this);
        ID_MAP.put(id, this);
        if (!BY_DRAGON_TYPE.containsKey(dragonType)) BY_DRAGON_TYPE.put(dragonType, new ArrayList<>());
        BY_DRAGON_TYPE.get(dragonType).add(this);
    }

    public Item getEggItem() {
        return this.eggItem.get();
    }

    public Item getScaleItem() {
        return this.scaleItem.get();
    }

    public static EnumDragonColor[] values() {
        return VALUES.toArray(EnumDragonColor[]::new);
    }

    public static EnumDragonColor byMetadata(int meta) {
        EnumDragonColor i = values()[meta];
        return i == null ? RED : i;
    }

    public Identifier getEggTexture() {
        return new Identifier(IceAndFire.MOD_ID, String.format("textures/models/%sdragon/egg_%s.png", this.dragonType.getName(), this.id));
    }

    @Override
    public String toString() {
        return "EnumDragonColor[" +
                "id=" + this.id + ", " +
                "color=" + this.color + ", " +
                "dragonType=" + this.dragonType + ']';
    }

    public Identifier getTextureByEntity(EntityDragonBase dragon) {
        int stage = dragon.getDragonStage();
        if (dragon.isModelDead()) {
            if (dragon.getDeathStage() >= (dragon.getAgeInDays() / 5) / 2) return this.getSkeletonTexture(stage);
            else return this.getSleepTexture(stage);
        } else if (dragon.isSleeping() || dragon.isBlinking()) return this.getSleepTexture(stage);
        else return this.getBodyTexture(stage);
    }

    public Identifier getBodyTexture(int stage) {
        return new Identifier(IceAndFire.MOD_ID, String.format("textures/models/%sdragon/%s_%d.png", this.dragonType.getName(), this.id, stage));
    }

    public Identifier getSleepTexture(int stage) {
        return new Identifier(IceAndFire.MOD_ID, String.format("textures/models/%sdragon/%s_%d_sleeping.png", this.dragonType.getName(), this.id, stage));
    }

    public Identifier getEyesTexture(int stage) {
        return new Identifier(IceAndFire.MOD_ID, String.format("textures/models/%sdragon/%s_%d_eyes.png", this.dragonType.getName(), this.id, stage));
    }

    public Identifier getSkeletonTexture(int stage) {
        return new Identifier(IceAndFire.MOD_ID, String.format("textures/models/%sdragon/%s_skeleton_%d.png", this.dragonType.getName(), this.dragonType.getName(), stage));
    }

    public Identifier getMaleOverlay() {
        return new Identifier(IceAndFire.MOD_ID, String.format("textures/models/%sdragon/male_%s.png", this.dragonType.getName(), this.id));
    }

    public static EnumDragonColor getById(String id) {
        return ID_MAP.getOrDefault(id, RED);
    }

    public static List<EnumDragonColor> getColorsByType(DragonType type) {
        return BY_DRAGON_TYPE.getOrDefault(type, new ArrayList<>());
    }
}
