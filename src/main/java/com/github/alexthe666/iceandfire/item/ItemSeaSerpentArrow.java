package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpentArrow;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemSeaSerpentArrow extends ArrowItem {

    public ItemSeaSerpentArrow() {
        super(new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
    }

    @Override
    public @NotNull PersistentProjectileEntity createArrow(@NotNull World worldIn, @NotNull ItemStack stack, @NotNull LivingEntity shooter) {
        return new EntitySeaSerpentArrow(IafEntityRegistry.SEA_SERPENT_ARROW.get(), worldIn, shooter);
    }

    @Override
    public void appendTooltip(@NotNull ItemStack stack, World worldIn, List<Text> tooltip, @NotNull TooltipContext flagIn) {
        tooltip.add(Text.translatable("item.iceandfire.sea_serpent_arrow.desc").formatted(Formatting.GRAY));
    }
}
