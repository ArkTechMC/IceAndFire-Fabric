package com.iafenvoy.iceandfire.item.tool;

import com.iafenvoy.iceandfire.entity.EntitySeaSerpentArrow;
import com.iafenvoy.iceandfire.registry.IafEntities;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemSeaSerpentArrow extends ArrowItem {

    public ItemSeaSerpentArrow() {
        super(new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
    }

    @Override
    public PersistentProjectileEntity createArrow(World worldIn, ItemStack stack, LivingEntity shooter) {
        return new EntitySeaSerpentArrow(IafEntities.SEA_SERPENT_ARROW, worldIn, shooter);
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        tooltip.add(Text.translatable("item.iceandfire.sea_serpent_arrow.desc").formatted(Formatting.GRAY));
    }
}
