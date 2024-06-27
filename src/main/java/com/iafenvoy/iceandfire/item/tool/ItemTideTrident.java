package com.iafenvoy.iceandfire.item.tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.iafenvoy.iceandfire.entity.EntityTideTrident;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ItemTideTrident extends TridentItem {
    public ItemTideTrident() {
        super(new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/.maxDamage(400));
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity player) {
            int time = this.getMaxUseTime(stack) - timeLeft;
            if (time >= 10) {
                int riptideLevel = EnchantmentHelper.getRiptide(stack);
                if (riptideLevel <= 0 || player.isTouchingWaterOrRain()) {
                    if (!worldIn.isClient) {
                        stack.damage(1, player, p -> p.sendToolBreakStatus(entityLiving.getActiveHand()));
                        if (riptideLevel == 0) {
                            EntityTideTrident tideTrident = new EntityTideTrident(worldIn, player, stack);
                            tideTrident.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 2.5F + (float) riptideLevel * 0.5F, 1.0F);
                            if (player.getAbilities().creativeMode)
                                tideTrident.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                            worldIn.spawnEntity(tideTrident);
                            worldIn.playSoundFromEntity(null, tideTrident, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            if (!player.getAbilities().creativeMode)
                                player.getInventory().removeOne(stack);
                        }
                    }

                    player.incrementStat(Stats.USED.getOrCreateStat(this));
                    if (riptideLevel > 0) {
                        float yaw = player.getYaw();
                        float pitch = player.getPitch();
                        float velocityX = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
                        float velocityY = -MathHelper.sin(pitch * 0.017453292F);
                        float velocityZ = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
                        float speed = MathHelper.sqrt(velocityX * velocityX + velocityY * velocityY + velocityZ * velocityZ);
                        float targetSpeed = 3.0F * ((1.0F + (float) riptideLevel) / 4.0F);
                        velocityX *= targetSpeed / speed;
                        velocityY *= targetSpeed / speed;
                        velocityZ *= targetSpeed / speed;
                        player.addVelocity(velocityX, velocityY, velocityZ);
                        player.useRiptide(20);
                        if (player.isOnGround())
                            player.move(MovementType.SELF, new Vec3d(0.0D, 1.1999999284744263D, 0.0D));

                        SoundEvent sound;
                        if (riptideLevel >= 3)
                            sound = SoundEvents.ITEM_TRIDENT_RIPTIDE_3;
                        else if (riptideLevel == 2)
                            sound = SoundEvents.ITEM_TRIDENT_RIPTIDE_2;
                        else
                            sound = SoundEvents.ITEM_TRIDENT_RIPTIDE_1;

                        worldIn.playSoundFromEntity(null, player, sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }
    }


    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot) {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 12.0D, EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -2.9F, EntityAttributeModifier.Operation.ADDITION));
        }
        return builder.build();
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        tooltip.add(Text.translatable("item.iceandfire.legendary_weapon.desc").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.iceandfire.tide_trident.desc_0").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.iceandfire.tide_trident.desc_1").formatted(Formatting.GRAY));
    }
}
