package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IafConfig;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemModHoe extends HoeItem implements DragonSteelOverrides<ItemModHoe> {

    private Multimap<EntityAttribute, EntityAttributeModifier> dragonsteelModifiers;

    public ItemModHoe(ToolMaterial toolmaterial) {
        super(toolmaterial, 5, -3.0F, new Settings());
    }

    @Override
    @Deprecated
    public @NotNull Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND && isDragonsteel(getMaterial()) ? this.bakeDragonsteel() : super.getAttributeModifiers(equipmentSlot);
    }

    @Override
    @Deprecated
    public Multimap<EntityAttribute, EntityAttributeModifier> bakeDragonsteel() {
        if (getMaterial().getAttackDamage() != IafConfig.dragonsteelBaseDamage || dragonsteelModifiers == null) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 1F, EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -3F, EntityAttributeModifier.Operation.ADDITION));
            this.dragonsteelModifiers = builder.build();
            return this.dragonsteelModifiers;
        } else {
            return dragonsteelModifiers;
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return isDragonsteel(getMaterial()) ? IafConfig.dragonsteelBaseDurability : getMaterial().getDurability();
    }

    @Override
    public boolean postHit(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        hurtEnemy(this, stack, target, attacker);
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void appendTooltip(@NotNull ItemStack stack, World worldIn, @NotNull List<Text> tooltip, @NotNull TooltipContext flagIn) {
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
        appendHoverText(getMaterial(), stack, worldIn, tooltip, flagIn);
    }
}
