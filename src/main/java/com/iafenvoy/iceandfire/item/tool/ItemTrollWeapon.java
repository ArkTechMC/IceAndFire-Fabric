package com.iafenvoy.iceandfire.item.tool;

import com.iafenvoy.iceandfire.data.TrollType;
import com.iafenvoy.iceandfire.registry.IafItems;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemTrollWeapon extends SwordItem {
    public TrollType.ITrollWeapon weapon;

    public ItemTrollWeapon(TrollType.ITrollWeapon weapon) {
        super(IafItems.TROLL_WEAPON_TOOL_MATERIAL, 15, -3.5F, new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
        this.weapon = weapon;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player)
            return player.getAttackCooldownProgress(0) < 0.95 || player.handSwingProgress != 0;
        else return super.postHit(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player && selected)
            if (player.getAttackCooldownProgress(0) < 0.95 && player.handSwingProgress > 0)
                player.handSwingTicks--;
    }

    public boolean onEntitySwing(LivingEntity LivingEntity, ItemStack stack) {
        if (LivingEntity instanceof PlayerEntity player)
            if (player.getAttackCooldownProgress(0) < 1 && player.handSwingProgress > 0)
                return true;
            else
                player.handSwingTicks = -1;
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        tooltip.add(Text.translatable("item.iceandfire.legendary_weapon.desc").formatted(Formatting.GRAY));
    }
}
