package com.github.alexthe666.citadel.server.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;

public class CustomArmorMaterial implements ArmorMaterial {
    private final String name;
    private final int durability;
    private final int[] damageReduction;
    private final int encantability;
    private final SoundEvent sound;
    private final float toughness;
    private Ingredient ingredient = null;
    public float knockbackResistance = 0.0F;

    public CustomArmorMaterial(String name, int durability, int[] damageReduction, int encantability, SoundEvent sound, float toughness, float knockbackResistance) {
        this.name = name;
        this.durability = durability;
        this.damageReduction = damageReduction;
        this.encantability = encantability;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
    }

    @Override
    public int getDurability(ArmorItem.Type slotIn) {
        return durability;
    }

    @Override
    public int getProtection(ArmorItem.Type slotIn) {
        return damageReduction[slotIn.ordinal()];
    }

    @Override
    public int getEnchantability() {
        return encantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return sound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return ingredient == null ? Ingredient.EMPTY : ingredient;
    }

    public void setRepairMaterial(Ingredient ingredient){
        this.ingredient = ingredient;
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
        return knockbackResistance;
    }
}
