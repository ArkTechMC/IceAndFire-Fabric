package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.google.common.collect.Multimap;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;


public interface DragonSteelOverrides<T extends ToolItem> {

    /**
     * Kept for compatibility
     *
     * @deprecated use data pack overrides instead
     */
    @Deprecated
    Multimap<EntityAttribute, EntityAttributeModifier> bakeDragonsteel();

    default float getAttackDamage(T item) {
        if (item instanceof SwordItem) {
            return ((SwordItem) item).getAttackDamage();
        }
        if (item instanceof MiningToolItem) {
            return ((MiningToolItem) item).getAttackDamage();
        }
        return item.getMaterial().getAttackDamage();
        //return item.getDamage(item.asItem().getDefaultInstance())
    }

    default boolean isDragonsteel(ToolMaterial tier) {
        return tier.getTag() == DragonSteelTier.DRAGONSTEEL_TIER_TAG;
    }

    default boolean isDragonsteelFire(ToolMaterial tier) {
        return tier == DragonSteelTier.DRAGONSTEEL_TIER_FIRE;
    }

    default boolean isDragonsteelIce(ToolMaterial tier) {
        return tier == DragonSteelTier.DRAGONSTEEL_TIER_ICE;
    }

    default boolean isDragonsteelLightning(ToolMaterial tier) {
        return tier == DragonSteelTier.DRAGONSTEEL_TIER_LIGHTNING;
    }

    default void hurtEnemy(T item, ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (item.getMaterial() == IafItemRegistry.SILVER_TOOL_MATERIAL) {
            if (target.getGroup() == EntityGroup.UNDEAD) {
                target.damage(attacker.getWorld().getDamageSources().magic(), getAttackDamage(item) + 3.0F);
            }
        }

        if (item.getMaterial() == IafItemRegistry.MYRMEX_CHITIN_TOOL_MATERIAL) {
            if (target.getGroup() != EntityGroup.ARTHROPOD) {
                target.damage(attacker.getWorld().getDamageSources().generic(), getAttackDamage(item) + 5.0F);
            }
            if (target instanceof EntityDeathWorm) {
                target.damage(attacker.getWorld().getDamageSources().generic(), getAttackDamage(item) + 5.0F);
            }
        }
        if (isDragonsteelFire(item.getMaterial()) && IafConfig.dragonWeaponFireAbility) {
            target.setOnFireFor(15);
            target.takeKnockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
        if (isDragonsteelIce(item.getMaterial()) && IafConfig.dragonWeaponIceAbility) {
            EntityDataProvider.getCapability(target).ifPresent(data -> data.frozenData.setFrozen(target, 300));
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 300, 2));
            target.takeKnockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
        if (isDragonsteelLightning(item.getMaterial()) && IafConfig.dragonWeaponLightningAbility) {
            boolean flag = true;
            if (attacker instanceof PlayerEntity) {
                if (attacker.handSwingProgress > 0.2) {
                    flag = false;
                }
            }
            if (!attacker.getWorld().isClient && flag) {
                LightningEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(target.getWorld());
                lightningboltentity.getCommandTags().add(ServerEvents.BOLT_DONT_DESTROY_LOOT);
                lightningboltentity.getCommandTags().add(attacker.getUuidAsString());
                lightningboltentity.refreshPositionAfterTeleport(target.getPos());
                if (!target.getWorld().isClient) {
                    target.getWorld().spawnEntity(lightningboltentity);
                }
            }
            target.takeKnockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }

    }

    default void appendHoverText(ToolMaterial tier, ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        if (tier == IafItemRegistry.SILVER_TOOL_MATERIAL) {
            tooltip.add(Text.translatable("silvertools.hurt").formatted(Formatting.GREEN));
        }
        if (tier == IafItemRegistry.MYRMEX_CHITIN_TOOL_MATERIAL) {
            tooltip.add(Text.translatable("myrmextools.hurt").formatted(Formatting.GREEN));
        }
        if (isDragonsteelFire(tier) && IafConfig.dragonWeaponFireAbility) {
            tooltip.add(Text.translatable("dragon_sword_fire.hurt2").formatted(Formatting.DARK_RED));
        }
        if (isDragonsteelIce(tier) && IafConfig.dragonWeaponIceAbility) {
            tooltip.add(Text.translatable("dragon_sword_ice.hurt2").formatted(Formatting.AQUA));
        }
        if (isDragonsteelLightning(tier) && IafConfig.dragonWeaponLightningAbility) {
            tooltip.add(Text.translatable("dragon_sword_lightning.hurt2").formatted(Formatting.DARK_PURPLE));
        }
    }
}
