package com.iafenvoy.iceandfire.enums;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.item.armor.ItemDragonArmor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EnumDragonArmorMaterial {
    //FIXME: private
    public static final List<EnumDragonArmorMaterial> MATERIALS = new ArrayList<>();
    public static final EnumDragonArmorMaterial IRON = new EnumDragonArmorMaterial("iron");
    public static final EnumDragonArmorMaterial COPPER = new EnumDragonArmorMaterial("copper");
    public static final EnumDragonArmorMaterial SILVER = new EnumDragonArmorMaterial("silver");
    public static final EnumDragonArmorMaterial GOLD = new EnumDragonArmorMaterial("gold");
    public static final EnumDragonArmorMaterial DIAMOND = new EnumDragonArmorMaterial("diamond");
    public static final EnumDragonArmorMaterial NETHERITE = new EnumDragonArmorMaterial("netherite");
    public static final EnumDragonArmorMaterial DRAGON_STEEL_FIRE = new EnumDragonArmorMaterial("dragon_steel_fire");
    public static final EnumDragonArmorMaterial DRAGON_STEEL_ICE = new EnumDragonArmorMaterial("dragon_steel_ice");
    public static final EnumDragonArmorMaterial DRAGON_STEEL_LIGHTNING = new EnumDragonArmorMaterial("dragon_steel_lightning");
    private final String name;

    public EnumDragonArmorMaterial(String name) {
        this.name = name;
        MATERIALS.add(this);
    }

    public String getId() {
        return this.name.toLowerCase(Locale.ROOT);
    }

    public Identifier getTexture(EquipmentSlot slot) {
        return switch (slot) {
            case MAINHAND, OFFHAND -> null;
            case FEET ->
                    Identifier.of(IceAndFire.MOD_ID, "textures/models/dragon_armor/armor_tail_" + this.name + ".png");
            case LEGS ->
                    Identifier.of(IceAndFire.MOD_ID, "textures/models/dragon_armor/armor_body_" + this.name + ".png");
            case CHEST ->
                    Identifier.of(IceAndFire.MOD_ID, "textures/models/dragon_armor/armor_neck_" + this.name + ".png");
            case HEAD ->
                    Identifier.of(IceAndFire.MOD_ID, "textures/models/dragon_armor/armor_head_" + this.name + ".png");
        };
    }

    public static Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemDragonArmor armorItem)
            return armorItem.type.getTexture(slot);
        else return new Identifier("missing");
    }
}
