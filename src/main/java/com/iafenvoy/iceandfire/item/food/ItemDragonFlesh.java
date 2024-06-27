package com.iafenvoy.iceandfire.item.food;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDragonFlesh extends ItemGenericFood {
    final int dragonType;

    public ItemDragonFlesh(int dragonType) {
        super(8, 0.8F, true, false, false);
        this.dragonType = dragonType;
    }

    public static String getNameForType(int dragonType) {
        return switch (dragonType) {
            case 1 -> "ice_dragon_flesh";
            case 2 -> "lightning_dragon_flesh";
            default -> "fire_dragon_flesh";
        };
    }

    @Override
    public void onFoodEaten(ItemStack stack, World worldIn, LivingEntity livingEntity) {
        if (!worldIn.isClient) {
            if (this.dragonType == 0)
                livingEntity.setOnFireFor(5);
            else if (this.dragonType == 1)
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2));
            else {
                if (!livingEntity.getWorld().isClient) {
                    LightningEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(livingEntity.getWorld());
                    assert lightningboltentity != null;
                    lightningboltentity.refreshPositionAfterTeleport(livingEntity.getPos());
                    if (!livingEntity.getWorld().isClient)
                        livingEntity.getWorld().spawnEntity(lightningboltentity);
                }
            }
        }
    }
}
