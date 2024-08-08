package com.iafenvoy.iceandfire.data;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.item.armor.ItemTrollArmor;
import com.iafenvoy.iceandfire.item.tool.ItemTrollWeapon;
import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.iceandfire.registry.tag.IafBiomeTags;
import com.iafenvoy.uranus.object.item.CustomArmorMaterial;
import com.iafenvoy.uranus.util.RandomHelper;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class TrollType {
    private static final List<TrollType> TYPES = new ArrayList<>();
    private static final Map<String, TrollType> BY_NAME = new HashMap<>();
    public static final TrollType FOREST = new TrollType("forest", IafItems.TROLL_FOREST_ARMOR_MATERIAL, biome -> biome.isIn(IafBiomeTags.FOREST_TROLL), new Identifier(IceAndFire.MOD_ID, "entities/troll_forest"), BuiltinWeapon.TRUNK, BuiltinWeapon.COLUMN_FOREST, BuiltinWeapon.AXE, BuiltinWeapon.HAMMER);
    public static final TrollType FROST = new TrollType("frost", IafItems.TROLL_FROST_ARMOR_MATERIAL, biome -> biome.isIn(IafBiomeTags.SNOWY_TROLL), new Identifier(IceAndFire.MOD_ID, "entities/troll_frost"), BuiltinWeapon.COLUMN_FROST, BuiltinWeapon.TRUNK_FROST, BuiltinWeapon.AXE, BuiltinWeapon.HAMMER);
    public static final TrollType MOUNTAIN = new TrollType("mountain", IafItems.TROLL_MOUNTAIN_ARMOR_MATERIAL, biome -> biome.isIn(IafBiomeTags.MOUNTAIN_TROLL), new Identifier(IceAndFire.MOD_ID, "entities/troll_mountain"), BuiltinWeapon.COLUMN, BuiltinWeapon.AXE, BuiltinWeapon.HAMMER);
    public final Item leather;
    public final Item helmet;
    public final Item chestplate;
    public final Item leggings;
    public final Item boots;
    private final String name;
    private final CustomArmorMaterial material;
    private final Predicate<RegistryEntry<Biome>> biomePredicate;
    private final Identifier lootTable;
    private final List<BuiltinWeapon> weapons;

    public TrollType(String name, CustomArmorMaterial material, Predicate<RegistryEntry<Biome>> biomePredicate, Identifier lootTable, BuiltinWeapon... weapons) {
        this.name = name;
        this.weapons = List.of(weapons);
        this.material = material;
        this.biomePredicate = biomePredicate;
        this.lootTable = lootTable;
        this.leather = new Item(new FabricItemSettings());
        this.helmet = new ItemTrollArmor(this, material, ArmorItem.Type.HELMET);
        this.chestplate = new ItemTrollArmor(this, material, ArmorItem.Type.CHESTPLATE);
        this.leggings = new ItemTrollArmor(this, material, ArmorItem.Type.LEGGINGS);
        this.boots = new ItemTrollArmor(this, material, ArmorItem.Type.BOOTS);
        TYPES.add(this);
        BY_NAME.put(name, this);
    }

    public static TrollType getBiomeType(RegistryEntry<Biome> biome) {
        List<TrollType> types = TYPES.stream().filter(x -> x.allowSpawn(biome)).toList();
        if (types.isEmpty()) RandomHelper.randomOne(TYPES);
        return RandomHelper.randomOne(types);
    }

    public static BuiltinWeapon getWeaponForType(TrollType troll) {
        return troll.weapons.get(ThreadLocalRandom.current().nextInt(troll.weapons.size()));
    }

    public static void initArmors() {
        for (TrollType troll : TYPES) {
            IafItems.register("troll_leather_%s".formatted(troll.name.toLowerCase(Locale.ROOT)), troll.leather);
            IafItems.register(ItemTrollArmor.getName(troll, EquipmentSlot.HEAD), troll.helmet);
            IafItems.register(ItemTrollArmor.getName(troll, EquipmentSlot.CHEST), troll.chestplate);
            IafItems.register(ItemTrollArmor.getName(troll, EquipmentSlot.LEGS), troll.leggings);
            IafItems.register(ItemTrollArmor.getName(troll, EquipmentSlot.FEET), troll.boots);
        }
    }

    public static List<TrollType> values() {
        return ImmutableList.copyOf(TYPES);
    }

    public static TrollType getByName(String name) {
        return BY_NAME.getOrDefault(name, TrollType.FOREST);
    }

    public String getName() {
        return this.name;
    }

    public Identifier getLootTable() {
        return this.lootTable;
    }

    public CustomArmorMaterial getMaterial() {
        return this.material;
    }

    public Identifier getTexture() {
        return new Identifier(IceAndFire.MOD_ID, "textures/models/troll/troll_" + this.name + ".png");
    }

    public Identifier getStatueTexture() {
        return new Identifier(IceAndFire.MOD_ID, "textures/models/troll/troll_" + this.name + "_stone.png");
    }

    public Identifier getEyesTexture() {
        return new Identifier(IceAndFire.MOD_ID, "textures/models/troll/troll_" + this.name + "_eyes.png");
    }

    public boolean allowSpawn(RegistryEntry<Biome> biome) {
        return this.biomePredicate.test(biome);
    }

    public enum BuiltinWeapon implements ITrollWeapon {
        AXE, COLUMN, COLUMN_FOREST, COLUMN_FROST, HAMMER, TRUNK, TRUNK_FROST;
        private final Item item;

        BuiltinWeapon() {
            this.item = IafItems.register("troll_weapon_" + this.name().toLowerCase(Locale.ROOT), new ItemTrollWeapon(this));
            ITrollWeapon.addWeapons(this);
        }

        @Override
        public String getName() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        @Override
        public Identifier getTexture() {
            return new Identifier(IceAndFire.MOD_ID, "textures/models/troll/weapon/weapon_" + this.name().toLowerCase(Locale.ROOT) + ".png");
        }

        @Override
        public Item getItem() {
            return this.item;
        }
    }

    public interface ITrollWeapon {
        @ApiStatus.Internal
        List<ITrollWeapon> WEAPONS = new ArrayList<>();
        @ApiStatus.Internal
        Map<String, ITrollWeapon> BY_NAME = new HashMap<>();

        static void addWeapons(ITrollWeapon... weapons) {
            for (ITrollWeapon weapon : weapons) {
                WEAPONS.add(weapon);
                BY_NAME.put(weapon.getName(), weapon);
            }
        }

        static List<ITrollWeapon> values() {
            return ImmutableList.copyOf(WEAPONS);
        }

        static ITrollWeapon getByName(String name) {
            return BY_NAME.getOrDefault(name, BuiltinWeapon.AXE);
        }

        String getName();

        Identifier getTexture();

        Item getItem();
    }
}
