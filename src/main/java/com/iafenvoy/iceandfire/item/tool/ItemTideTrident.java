package com.iafenvoy.iceandfire.item.tool;

import com.iafenvoy.iceandfire.entity.EntityTideTrident;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
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
        if (entityLiving instanceof PlayerEntity lvt_5_1_) {
            int lvt_6_1_ = this.getMaxUseTime(stack) - timeLeft;
            if (lvt_6_1_ >= 10) {
                int lvt_7_1_ = EnchantmentHelper.getRiptide(stack);
                if (lvt_7_1_ <= 0 || lvt_5_1_.isTouchingWaterOrRain()) {
                    if (!worldIn.isClient) {
                        stack.damage(1, lvt_5_1_, (player) -> player.sendToolBreakStatus(entityLiving.getActiveHand()));
                        if (lvt_7_1_ == 0) {
                            EntityTideTrident lvt_8_1_ = new EntityTideTrident(worldIn, lvt_5_1_, stack);
                            lvt_8_1_.setVelocity(lvt_5_1_, lvt_5_1_.getPitch(), lvt_5_1_.getYaw(), 0.0F, 2.5F + (float) lvt_7_1_ * 0.5F, 1.0F);
                            if (lvt_5_1_.getAbilities().creativeMode) {
                                lvt_8_1_.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                            }

                            worldIn.spawnEntity(lvt_8_1_);
                            worldIn.playSoundFromEntity(null, lvt_8_1_, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            if (!lvt_5_1_.getAbilities().creativeMode) {
                                lvt_5_1_.getInventory().removeOne(stack);
                            }
                        }
                    }

                    lvt_5_1_.incrementStat(Stats.USED.getOrCreateStat(this));
                    if (lvt_7_1_ > 0) {
                        float lvt_8_2_ = lvt_5_1_.getYaw();
                        float lvt_9_1_ = lvt_5_1_.getPitch();
                        float lvt_10_1_ = -MathHelper.sin(lvt_8_2_ * 0.017453292F) * MathHelper.cos(lvt_9_1_ * 0.017453292F);
                        float lvt_11_1_ = -MathHelper.sin(lvt_9_1_ * 0.017453292F);
                        float lvt_12_1_ = MathHelper.cos(lvt_8_2_ * 0.017453292F) * MathHelper.cos(lvt_9_1_ * 0.017453292F);
                        float lvt_13_1_ = MathHelper.sqrt(lvt_10_1_ * lvt_10_1_ + lvt_11_1_ * lvt_11_1_ + lvt_12_1_ * lvt_12_1_);
                        float lvt_14_1_ = 3.0F * ((1.0F + (float) lvt_7_1_) / 4.0F);
                        lvt_10_1_ *= lvt_14_1_ / lvt_13_1_;
                        lvt_11_1_ *= lvt_14_1_ / lvt_13_1_;
                        lvt_12_1_ *= lvt_14_1_ / lvt_13_1_;
                        lvt_5_1_.addVelocity(lvt_10_1_, lvt_11_1_, lvt_12_1_);
                        lvt_5_1_.useRiptide(20);
                        if (lvt_5_1_.isOnGround()) {
                            float lvt_15_1_ = 1.1999999F;
                            lvt_5_1_.move(MovementType.SELF, new Vec3d(0.0D, 1.1999999284744263D, 0.0D));
                        }

                        SoundEvent lvt_15_4_;
                        if (lvt_7_1_ >= 3) {
                            lvt_15_4_ = SoundEvents.ITEM_TRIDENT_RIPTIDE_3;
                        } else if (lvt_7_1_ == 2) {
                            lvt_15_4_ = SoundEvents.ITEM_TRIDENT_RIPTIDE_2;
                        } else {
                            lvt_15_4_ = SoundEvents.ITEM_TRIDENT_RIPTIDE_1;
                        }

                        worldIn.playSoundFromEntity(null, lvt_5_1_, lvt_15_4_, SoundCategory.PLAYERS, 1.0F, 1.0F);
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
