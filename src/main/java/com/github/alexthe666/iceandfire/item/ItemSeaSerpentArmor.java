package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.citadel.server.item.CustomArmorMaterial;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemSeaSerpentArmor extends ArmorItem {

    public EnumSeaSerpent armor_type;

    public ItemSeaSerpentArmor(EnumSeaSerpent armorType, CustomArmorMaterial material, Type slot) {
        super(material, slot, new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
        this.armor_type = armorType;
    }

    @Override
    public @NotNull String getTranslationKey() {
        return switch (this.type) {
            case HELMET -> "item.iceandfire.sea_serpent_helmet";
            case CHESTPLATE -> "item.iceandfire.sea_serpent_chestplate";
            case LEGGINGS -> "item.iceandfire.sea_serpent_leggings";
            case BOOTS -> "item.iceandfire.sea_serpent_boots";
        };
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 50, 0, false, false));
            if (player.isTouchingWaterOrRain()) {
                int headMod = player.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof ItemSeaSerpentArmor ? 1 : 0;
                int chestMod = player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ItemSeaSerpentArmor ? 1 : 0;
                int legMod = player.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof ItemSeaSerpentArmor ? 1 : 0;
                int footMod = player.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof ItemSeaSerpentArmor ? 1 : 0;
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 50, headMod + chestMod + legMod + footMod - 1, false, false));
            }
        }
    }

    @Override
    public void appendTooltip(@NotNull ItemStack stack, World worldIn, List<Text> tooltip, @NotNull TooltipContext flagIn) {

        tooltip.add(Text.translatable("sea_serpent." + this.armor_type.resourceName).formatted(this.armor_type.color));
        tooltip.add(Text.translatable("item.iceandfire.sea_serpent_armor.desc_0").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.iceandfire.sea_serpent_armor.desc_1").formatted(Formatting.GRAY));
    }
}
