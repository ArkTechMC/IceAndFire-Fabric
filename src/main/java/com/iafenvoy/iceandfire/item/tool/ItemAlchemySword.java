package com.iafenvoy.iceandfire.item.tool;

import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.data.EntityDataComponent;
import com.iafenvoy.iceandfire.entity.EntityFireDragon;
import com.iafenvoy.iceandfire.entity.EntityIceDragon;
import com.iafenvoy.iceandfire.event.ServerEvents;
import com.iafenvoy.iceandfire.registry.IafItems;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemAlchemySword extends SwordItem {
    public ItemAlchemySword(ToolMaterial toolmaterial) {
        super(toolmaterial, 3, -2.4F, new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this == IafItems.DRAGONBONE_SWORD_FIRE) {
            if (IafCommonConfig.INSTANCE.armors.dragonFireAbility.getBooleanValue()) {
                if (target instanceof EntityIceDragon)
                    target.damage(attacker.getWorld().getDamageSources().inFire(), 13.5F);
                target.setOnFireFor(5);
                target.takeKnockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
            }
        }
        if (this == IafItems.DRAGONBONE_SWORD_ICE) {
            if (IafCommonConfig.INSTANCE.armors.dragonIceAbility.getBooleanValue()) {
                if (target instanceof EntityFireDragon)
                    target.damage(attacker.getWorld().getDamageSources().drown(), 13.5F);
                EntityDataComponent data = EntityDataComponent.get(target);
                data.frozenData.setFrozen(target, 200);
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2));
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 100, 2));
                target.takeKnockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
            }
        }
        if (this == IafItems.DRAGONBONE_SWORD_LIGHTNING) {
            if (IafCommonConfig.INSTANCE.armors.dragonLightningAbility.getBooleanValue()) {
                boolean flag = true;
                if (attacker instanceof PlayerEntity)
                    if (attacker.handSwingProgress > 0.2)
                        flag = false;
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
                if (target instanceof EntityFireDragon || target instanceof EntityIceDragon)
                    target.damage(attacker.getWorld().getDamageSources().lightningBolt(), 9.5F);
                target.takeKnockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        tooltip.add(Text.translatable("item.iceandfire.legendary_weapon.desc").formatted(Formatting.GRAY));
        if (this == IafItems.DRAGONBONE_SWORD_FIRE) {
            tooltip.add(Text.translatable("dragon_sword_fire.hurt1").formatted(Formatting.GREEN));
            if (IafCommonConfig.INSTANCE.armors.dragonFireAbility.getBooleanValue())
                tooltip.add(Text.translatable("dragon_sword_fire.hurt2").formatted(Formatting.DARK_RED));
        }
        if (this == IafItems.DRAGONBONE_SWORD_ICE) {
            tooltip.add(Text.translatable("dragon_sword_ice.hurt1").formatted(Formatting.GREEN));
            if (IafCommonConfig.INSTANCE.armors.dragonIceAbility.getBooleanValue())
                tooltip.add(Text.translatable("dragon_sword_ice.hurt2").formatted(Formatting.AQUA));
        }
        if (this == IafItems.DRAGONBONE_SWORD_LIGHTNING) {
            tooltip.add(Text.translatable("dragon_sword_lightning.hurt1").formatted(Formatting.GREEN));
            if (IafCommonConfig.INSTANCE.armors.dragonLightningAbility.getBooleanValue())
                tooltip.add(Text.translatable("dragon_sword_lightning.hurt2").formatted(Formatting.DARK_PURPLE));
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
