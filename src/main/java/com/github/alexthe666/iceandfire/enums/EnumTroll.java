package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.citadel.server.item.CustomArmorMaterial;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.item.ItemTrollLeather;
import com.github.alexthe666.iceandfire.item.armor.ItemTrollArmor;
import com.github.alexthe666.iceandfire.item.tool.ItemTrollWeapon;
import com.github.alexthe666.iceandfire.registry.IafItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public enum EnumTroll {
    FOREST(IafItems.TROLL_FOREST_ARMOR_MATERIAL, Weapon.TRUNK, Weapon.COLUMN_FOREST, Weapon.AXE, Weapon.HAMMER),
    FROST(IafItems.TROLL_FROST_ARMOR_MATERIAL, Weapon.COLUMN_FROST, Weapon.TRUNK_FROST, Weapon.AXE, Weapon.HAMMER),
    MOUNTAIN(IafItems.TROLL_MOUNTAIN_ARMOR_MATERIAL, Weapon.COLUMN, Weapon.AXE, Weapon.HAMMER);

    public final Identifier TEXTURE;
    public final Identifier TEXTURE_STONE;
    public final Identifier TEXTURE_EYES;
    public final CustomArmorMaterial material;
    public final Item leather;
    public final Item helmet;
    public final Item chestplate;
    public final Item leggings;
    public final Item boots;
    private final Weapon[] weapons;

    EnumTroll(CustomArmorMaterial material, Weapon... weapons) {
        this.weapons = weapons;
        this.material = material;
        this.TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/troll/troll_" + this.name().toLowerCase(Locale.ROOT) + ".png");
        this.TEXTURE_STONE = new Identifier(IceAndFire.MOD_ID, "textures/models/troll/troll_" + this.name().toLowerCase(Locale.ROOT) + "_stone.png");
        this.TEXTURE_EYES = new Identifier(IceAndFire.MOD_ID, "textures/models/troll/troll_" + this.name().toLowerCase(Locale.ROOT) + "_eyes.png");
        this.leather = new ItemTrollLeather(this);
        this.helmet = new ItemTrollArmor(this, material, ArmorItem.Type.HELMET);
        this.chestplate = new ItemTrollArmor(this, material, ArmorItem.Type.CHESTPLATE);
        this.leggings = new ItemTrollArmor(this, material, ArmorItem.Type.LEGGINGS);
        this.boots = new ItemTrollArmor(this, material, ArmorItem.Type.BOOTS);
    }

    public static EnumTroll getBiomeType(RegistryEntry<Biome> biome) {
        List<EnumTroll> types = new ArrayList<>();
        if (BiomeConfig.test(BiomeConfig.snowyTrollBiomes, biome))
            types.add(EnumTroll.FROST);
        if (BiomeConfig.test(BiomeConfig.forestTrollBiomes, biome))
            types.add(EnumTroll.FOREST);
        if (BiomeConfig.test(BiomeConfig.mountainTrollBiomes, biome))
            types.add(EnumTroll.MOUNTAIN);
        if (types.isEmpty())
            return values()[ThreadLocalRandom.current().nextInt(values().length)];
        else
            return types.get(ThreadLocalRandom.current().nextInt(types.size()));
    }


    public static Weapon getWeaponForType(EnumTroll troll) {
        return troll.weapons[ThreadLocalRandom.current().nextInt(troll.weapons.length)];
    }

    public static void initArmors() {
        for (EnumTroll troll : EnumTroll.values()) {
            IafItems.register("troll_leather_%s".formatted(troll.name().toLowerCase(Locale.ROOT)), troll.leather);
            IafItems.register(ItemTrollArmor.getName(troll, EquipmentSlot.HEAD), troll.helmet);
            IafItems.register(ItemTrollArmor.getName(troll, EquipmentSlot.CHEST), troll.chestplate);
            IafItems.register(ItemTrollArmor.getName(troll, EquipmentSlot.LEGS), troll.leggings);
            IafItems.register(ItemTrollArmor.getName(troll, EquipmentSlot.FEET), troll.boots);
        }
    }

    public enum Weapon {
        AXE, COLUMN, COLUMN_FOREST, COLUMN_FROST, HAMMER, TRUNK, TRUNK_FROST;
        public final Identifier TEXTURE;
        public final Item item;

        Weapon() {
            this.TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/troll/weapon/weapon_" + this.name().toLowerCase(Locale.ROOT) + ".png");
            this.item = IafItems.register("troll_weapon_" + this.name().toLowerCase(Locale.ROOT), new ItemTrollWeapon(this));
        }
    }
}
