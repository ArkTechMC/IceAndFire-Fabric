package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemDragonEgg extends Item {
    public final EnumDragonEgg type;

    public ItemDragonEgg(EnumDragonEgg type) {
        super(new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/.maxCount(1));
        this.type = type;
    }

    @Override
    public @NotNull String getTranslationKey() {
        return "item.iceandfire.dragonegg";
    }

    @Override
    public void onCraft(ItemStack itemStack, @NotNull World world, @NotNull PlayerEntity player) {
        itemStack.setNbt(new NbtCompound());
    }

    @Override
    public void appendTooltip(@NotNull ItemStack stack, World worldIn, List<Text> tooltip, @NotNull TooltipContext flagIn) {
        tooltip.add(Text.translatable("dragon." + this.type.toString().toLowerCase()).formatted(this.type.color));
    }

    @Override
    public @NotNull ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack itemstack = context.getPlayer().getStackInHand(context.getHand());
        BlockPos offset = context.getBlockPos().offset(context.getSide());
        EntityDragonEgg egg = new EntityDragonEgg(IafEntityRegistry.DRAGON_EGG.get(), context.getWorld());
        egg.setEggType(this.type);
        egg.refreshPositionAndAngles(offset.getX() + 0.5, offset.getY(), offset.getZ() + 0.5, 0, 0);
        egg.onPlayerPlace(context.getPlayer());
        if (itemstack.hasCustomName()) {
            egg.setCustomName(itemstack.getName());
        }
        if (!context.getWorld().isClient) {
            context.getWorld().spawnEntity(egg);
        }
        itemstack.decrement(1);
        return ActionResult.SUCCESS;
    }
}
