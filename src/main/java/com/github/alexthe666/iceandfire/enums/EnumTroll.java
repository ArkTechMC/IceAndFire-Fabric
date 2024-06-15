package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.citadel.server.item.CustomArmorMaterial;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemTrollArmor;
import com.github.alexthe666.iceandfire.item.ItemTrollLeather;
import com.github.alexthe666.iceandfire.item.ItemTrollWeapon;
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
import java.util.function.Supplier;

public enum EnumTroll {
    FOREST(IafItemRegistry.TROLL_FOREST_ARMOR_MATERIAL, Weapon.TRUNK, Weapon.COLUMN_FOREST, Weapon.AXE, Weapon.HAMMER),
    FROST(IafItemRegistry.TROLL_FROST_ARMOR_MATERIAL, Weapon.COLUMN_FROST, Weapon.TRUNK_FROST, Weapon.AXE, Weapon.HAMMER),
    MOUNTAIN(IafItemRegistry.TROLL_MOUNTAIN_ARMOR_MATERIAL, Weapon.COLUMN, Weapon.AXE, Weapon.HAMMER);

    public final Identifier TEXTURE;
    public final Identifier TEXTURE_STONE;
    public final Identifier TEXTURE_EYES;
    public final CustomArmorMaterial material;
    private final Weapon[] weapons;
    public Supplier<Item> leather;
    public Supplier<Item> helmet;
    public Supplier<Item> chestplate;
    public Supplier<Item> leggings;
    public Supplier<Item> boots;

    EnumTroll(CustomArmorMaterial material, Weapon... weapons) {
        this.weapons = weapons;
        this.material = material;
        this.TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/troll/troll_" + this.name().toLowerCase(Locale.ROOT) + ".png");
        this.TEXTURE_STONE = new Identifier(IceAndFire.MOD_ID, "textures/models/troll/troll_" + this.name().toLowerCase(Locale.ROOT) + "_stone.png");
        this.TEXTURE_EYES = new Identifier(IceAndFire.MOD_ID, "textures/models/troll/troll_" + this.name().toLowerCase(Locale.ROOT) + "_eyes.png");
        this.leather = () -> new ItemTrollLeather(this);
        this.helmet = () -> new ItemTrollArmor(this, material, ArmorItem.Type.HELMET);
        this.chestplate = () -> new ItemTrollArmor(this, material, ArmorItem.Type.CHESTPLATE);
        this.leggings = () -> new ItemTrollArmor(this, material, ArmorItem.Type.LEGGINGS);
        this.boots = () -> new ItemTrollArmor(this, material, ArmorItem.Type.BOOTS);


        //leather = IafItemRegistry.deferredRegister.register("troll_leather_" + name().toLowerCase(Locale.ROOT), () -> new ItemTrollLeather(this));

        //Function<EquipmentSlot, RegistryObject<Item>> genArmor = (slot) ->
        //        IafItemRegistry.deferredRegister.register(ItemTrollArmor.getName(this, slot), () -> new ItemTrollArmor(this, material, slot));
        //helmet = genArmor.apply(EquipmentSlot.HEAD);
        //chestplate = genArmor.apply(EquipmentSlot.CHEST);
        //leggings = genArmor.apply(EquipmentSlot.LEGS);
        //boots = genArmor.apply(EquipmentSlot.FEET);


    }

    public static EnumTroll getBiomeType(RegistryEntry<Biome> biome) {
        List<EnumTroll> types = new ArrayList<>();
        if (BiomeConfig.test(BiomeConfig.snowyTrollBiomes, biome)) {
            types.add(EnumTroll.FROST);
        }
        if (BiomeConfig.test(BiomeConfig.forestTrollBiomes, biome)) {
            types.add(EnumTroll.FOREST);
        }
        if (BiomeConfig.test(BiomeConfig.mountainTrollBiomes, biome)) {
            types.add(EnumTroll.MOUNTAIN);
        }
        if (types.isEmpty()) {
            return values()[ThreadLocalRandom.current().nextInt(values().length)];
        } else {
            return types.get(ThreadLocalRandom.current().nextInt(types.size()));
        }
    }


    public static Weapon getWeaponForType(EnumTroll troll) {
        return troll.weapons[ThreadLocalRandom.current().nextInt(troll.weapons.length)];
    }

    public static void initArmors() {
        for (EnumTroll troll : EnumTroll.values()) {
            troll.leather = IafItemRegistry.registerItem("troll_leather_%s".formatted(troll.name().toLowerCase(Locale.ROOT)), () -> new ItemTrollLeather(troll));
            troll.helmet = IafItemRegistry.registerItem(ItemTrollArmor.getName(troll, EquipmentSlot.HEAD), () -> new ItemTrollArmor(troll, troll.material, ArmorItem.Type.HELMET));
            troll.chestplate = IafItemRegistry.registerItem(ItemTrollArmor.getName(troll, EquipmentSlot.CHEST), () -> new ItemTrollArmor(troll, troll.material, ArmorItem.Type.CHESTPLATE));
            troll.leggings = IafItemRegistry.registerItem(ItemTrollArmor.getName(troll, EquipmentSlot.LEGS), () -> new ItemTrollArmor(troll, troll.material, ArmorItem.Type.LEGGINGS));
            troll.boots = IafItemRegistry.registerItem(ItemTrollArmor.getName(troll, EquipmentSlot.FEET), () -> new ItemTrollArmor(troll, troll.material, ArmorItem.Type.BOOTS));
        }
    }

    public enum Weapon {
        AXE, COLUMN, COLUMN_FOREST, COLUMN_FROST, HAMMER, TRUNK, TRUNK_FROST;
        public final Identifier TEXTURE;
        public final Supplier<Item> item;

        Weapon() {
            this.TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/troll/weapon/weapon_" + this.name().toLowerCase(Locale.ROOT) + ".png");
            this.item = IafItemRegistry.registerItem("troll_weapon_" + this.name().toLowerCase(Locale.ROOT), () -> new ItemTrollWeapon(this));
        }

    }
}
