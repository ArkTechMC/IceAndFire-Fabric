package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityGhostSword;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.google.common.collect.Multimap;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemGhostSword extends SwordItem {

    public ItemGhostSword() {
        super(IafItemRegistry.GHOST_SWORD_TOOL_MATERIAL, 5, -1.0F, new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
    }

    public static void spawnGhostSwordEntity(ItemStack stack, PlayerEntity playerEntity) {
        if (playerEntity.getItemCooldownManager().isCoolingDown(stack.getItem()))
            return;
        if (playerEntity.getStackInHand(Hand.MAIN_HAND) != stack)
            return;
        final Multimap<EntityAttribute, EntityAttributeModifier> dmg = stack.getAttributeModifiers(EquipmentSlot.MAINHAND);
        double totalDmg = 0D;
        for (EntityAttributeModifier modifier : dmg.get(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
            totalDmg += modifier.getValue();
        }
        playerEntity.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1, 1);
        EntityGhostSword shot = new EntityGhostSword(IafEntityRegistry.GHOST_SWORD.get(), playerEntity.getWorld(), playerEntity, totalDmg * 0.5F);
        shot.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 1, 0.5f);
        playerEntity.getWorld().spawnEntity(shot);
        stack.damage(1, playerEntity, entity -> entity.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        playerEntity.getItemCooldownManager().set(stack.getItem(), 10);
    }

    @Override
    public boolean postHit(@NotNull ItemStack stack, @NotNull LivingEntity targetEntity, @NotNull LivingEntity attacker) {
        return super.postHit(stack, targetEntity, attacker);
    }

    @Override
    public void appendTooltip(@NotNull ItemStack stack, World worldIn, List<Text> tooltip, @NotNull TooltipContext flagIn) {
        tooltip.add(Text.translatable("item.iceandfire.legendary_weapon.desc").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.iceandfire.ghost_sword.desc_0").formatted(Formatting.GRAY));
    }
}