package com.iafenvoy.iceandfire.item.tool;

import com.iafenvoy.iceandfire.IafConfig;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

public class ItemModAxe extends AxeItem implements DragonSteelOverrides<ItemModAxe> {

    private final ToolMaterial tier;
    private Multimap<EntityAttribute, EntityAttributeModifier> dragonsteelModifiers;

    public ItemModAxe(ToolMaterial toolmaterial) {
        super(toolmaterial, 5.0F, -3.0F, (new Settings()));
        this.tier = toolmaterial;
    }

    @Override
    @Deprecated
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND && this.isDragonsteel(this.getMaterial()) ? this.bakeDragonsteel() : super.getAttributeModifiers(equipmentSlot);
    }

    @Override
    @Deprecated
    public Multimap<EntityAttribute, EntityAttributeModifier> bakeDragonsteel() {
        if (this.tier.getAttackDamage() != IafConfig.dragonsteelBaseDamage || this.dragonsteelModifiers == null) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> lvt_5_1_ = ImmutableMultimap.builder();
            lvt_5_1_.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", IafConfig.dragonsteelBaseDamage - 1F + 5F, EntityAttributeModifier.Operation.ADDITION));
            lvt_5_1_.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -3.0F, EntityAttributeModifier.Operation.ADDITION));
            this.dragonsteelModifiers = lvt_5_1_.build();
            return this.dragonsteelModifiers;
        } else {
            return this.dragonsteelModifiers;
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return this.isDragonsteel(this.getMaterial()) ? IafConfig.dragonsteelBaseDurability : this.getMaterial().getDurability();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        this.hurtEnemy(this, stack, target, attacker);
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
        this.appendHoverText(this.tier, stack, worldIn, tooltip, flagIn);
    }
}
