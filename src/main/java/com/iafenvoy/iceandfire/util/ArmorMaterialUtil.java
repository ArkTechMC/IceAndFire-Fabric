package com.iafenvoy.iceandfire.util;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.EnumMap;

public class ArmorMaterialUtil {
    public static ArmorMaterial of(String name, int[] baseDurability, int durabilityMul, int[] protection, int enchantAbility, SoundEvent equipSound, float toughness, float knockBackResistance, ItemConvertible... repairIngredients) {
        return of(name, Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
            map.put(ArmorItem.Type.HELMET, baseDurability[3]);
            map.put(ArmorItem.Type.CHESTPLATE, baseDurability[2]);
            map.put(ArmorItem.Type.LEGGINGS, baseDurability[1]);
            map.put(ArmorItem.Type.BOOTS, baseDurability[0]);
        }), durabilityMul, Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
            map.put(ArmorItem.Type.HELMET, protection[3]);
            map.put(ArmorItem.Type.CHESTPLATE, protection[2]);
            map.put(ArmorItem.Type.LEGGINGS, protection[1]);
            map.put(ArmorItem.Type.BOOTS, protection[0]);
        }), enchantAbility, equipSound, toughness, knockBackResistance, repairIngredients);
    }

    public static ArmorMaterial of(String name, EnumMap<ArmorItem.Type, Integer> baseDurability, int durabilityMul, EnumMap<ArmorItem.Type, Integer> protection, int enchantAbility, SoundEvent equipSound, float toughness, float knockBackResistance, ItemConvertible... repairIngredients) {
        if (equipSound == null) equipSound = Registries.SOUND_EVENT.get(new Identifier(""));
        SoundEvent finalEquipSound = equipSound;
        return new ArmorMaterial() {
            @Override
            public int getDurability(ArmorItem.Type slot) {
                return baseDurability.get(slot) * durabilityMul;
            }

            @Override
            public int getProtection(ArmorItem.Type slot) {
                return protection.get(slot);
            }

            @Override
            public int getEnchantability() {
                return enchantAbility;
            }

            @Override
            public SoundEvent getEquipSound() {
                return finalEquipSound;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return Ingredient.ofItems(repairIngredients);
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public float getToughness() {
                return toughness;
            }

            @Override
            public float getKnockbackResistance() {
                return knockBackResistance;
            }
        };
    }
}
