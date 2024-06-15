package com.github.alexthe666.iceandfire.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemHippogryphSword extends SwordItem {

    public ItemHippogryphSword() {
        super(IafItemRegistry.HIPPOGRYPH_SWORD_TOOL_MATERIAL, 3, -2.4F, new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity targetEntity, LivingEntity attacker) {
        float f = (float) attacker.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue();
        float f3 = 1.0F + EnchantmentHelper.getSweepingMultiplier(attacker) * f;
        if (attacker instanceof PlayerEntity player) {
            for (LivingEntity LivingEntity : attacker.getWorld().getNonSpectatingEntities(LivingEntity.class, targetEntity.getBoundingBox().expand(1.0D, 0.25D, 1.0D))) {
                if (LivingEntity != player && LivingEntity != targetEntity && !attacker.isTeammate(LivingEntity) && attacker.squaredDistanceTo(LivingEntity) < 9.0D) {
                    LivingEntity.takeKnockback(0.4F, MathHelper.sin(attacker.getYaw() * 0.017453292F), -MathHelper.cos(attacker.getYaw() * 0.017453292F));
                    LivingEntity.damage(attacker.getWorld().getDamageSources().playerAttack(player), f3);
                }
            }
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
            player.spawnSweepAttackParticles();
        }
        return super.postHit(stack, targetEntity, attacker);
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        tooltip.add(Text.translatable("item.iceandfire.legendary_weapon.desc").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.iceandfire.hippogryph_sword.desc_0").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.iceandfire.hippogryph_sword.desc_1").formatted(Formatting.GRAY));
    }
}
