package com.github.alexthe666.iceandfire.item.tool;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.github.alexthe666.iceandfire.registry.IafItems;
import com.google.common.collect.Multimap;
import dev.arktechmc.iafextra.data.EntityDataComponent;
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
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
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
        return tier.getTag() == TagKey.of(RegistryKeys.BLOCK, new Identifier(IceAndFire.MOD_ID, "needs_dragonsteel"));
    }

    default boolean isDragonsteelFire(ToolMaterial tier) {
        return tier.equals(DragonSteelTier.createMaterialWithRepairItem(IafItems.DRAGONSTEEL_FIRE_INGOT, "dragonsteel_tier_fire"));
    }

    default boolean isDragonsteelIce(ToolMaterial tier) {
        return tier.equals(DragonSteelTier.createMaterialWithRepairItem(IafItems.DRAGONSTEEL_ICE_INGOT, "dragonsteel_tier_ice"));
    }

    default boolean isDragonsteelLightning(ToolMaterial tier) {
        return tier.equals(DragonSteelTier.createMaterialWithRepairItem(IafItems.DRAGONSTEEL_LIGHTNING_INGOT, "dragonsteel_tier_lightning"));
    }

    default void hurtEnemy(T item, ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (item.getMaterial() == IafItems.SILVER_TOOL_MATERIAL) {
            if (target.getGroup() == EntityGroup.UNDEAD) {
                target.damage(attacker.getWorld().getDamageSources().magic(), this.getAttackDamage(item) + 3.0F);
            }
        }

        if (item.getMaterial() == IafItems.MYRMEX_CHITIN_TOOL_MATERIAL) {
            if (target.getGroup() != EntityGroup.ARTHROPOD) {
                target.damage(attacker.getWorld().getDamageSources().generic(), this.getAttackDamage(item) + 5.0F);
            }
            if (target instanceof EntityDeathWorm) {
                target.damage(attacker.getWorld().getDamageSources().generic(), this.getAttackDamage(item) + 5.0F);
            }
        }
        if (this.isDragonsteelFire(item.getMaterial()) && IafConfig.dragonWeaponFireAbility) {
            target.setOnFireFor(15);
            target.takeKnockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
        if (this.isDragonsteelIce(item.getMaterial()) && IafConfig.dragonWeaponIceAbility) {
            EntityDataComponent data = EntityDataComponent.ENTITY_DATA_COMPONENT.get(target);
            data.frozenData.setFrozen(target, 300);
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 300, 2));
            target.takeKnockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
        if (this.isDragonsteelLightning(item.getMaterial()) && IafConfig.dragonWeaponLightningAbility) {
            boolean flag = true;
            if (attacker instanceof PlayerEntity) {
                if (attacker.handSwingProgress > 0.2) {
                    flag = false;
                }
            }
            if (!attacker.getWorld().isClient && flag) {
                LightningEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(target.getWorld());
                assert lightningboltentity != null;
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
        if (tier == IafItems.SILVER_TOOL_MATERIAL) {
            tooltip.add(Text.translatable("silvertools.hurt").formatted(Formatting.GREEN));
        }
        if (tier == IafItems.MYRMEX_CHITIN_TOOL_MATERIAL) {
            tooltip.add(Text.translatable("myrmextools.hurt").formatted(Formatting.GREEN));
        }
        if (this.isDragonsteelFire(tier) && IafConfig.dragonWeaponFireAbility) {
            tooltip.add(Text.translatable("dragon_sword_fire.hurt2").formatted(Formatting.DARK_RED));
        }
        if (this.isDragonsteelIce(tier) && IafConfig.dragonWeaponIceAbility) {
            tooltip.add(Text.translatable("dragon_sword_ice.hurt2").formatted(Formatting.AQUA));
        }
        if (this.isDragonsteelLightning(tier) && IafConfig.dragonWeaponLightningAbility) {
            tooltip.add(Text.translatable("dragon_sword_lightning.hurt2").formatted(Formatting.DARK_PURPLE));
        }
    }
}
