package com.iafenvoy.iceandfire.item;

import com.iafenvoy.iceandfire.entity.EntityDragonEgg;
import com.iafenvoy.iceandfire.enums.DragonColor;
import com.iafenvoy.iceandfire.registry.IafEntities;
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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ItemDragonEgg extends Item {
    public static final Map<DragonColor, Item> EGGS = new HashMap<>();
    public final DragonColor type;

    public ItemDragonEgg(DragonColor type) {
        super(new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/.maxCount(1));
        this.type = type;
        EGGS.put(type, this);
    }

    @Override
    public String getTranslationKey() {
        return "item.iceandfire.dragonegg";
    }

    @Override
    public void onCraft(ItemStack itemStack, World world, PlayerEntity player) {
        itemStack.setNbt(new NbtCompound());
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        tooltip.add(Text.translatable("dragon." + this.type.name().toLowerCase(Locale.ROOT)).formatted(this.type.color()));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack itemstack = context.getPlayer().getStackInHand(context.getHand());
        BlockPos offset = context.getBlockPos().offset(context.getSide());
        EntityDragonEgg egg = new EntityDragonEgg(IafEntities.DRAGON_EGG, context.getWorld());
        egg.setEggType(this.type);
        egg.refreshPositionAndAngles(offset.getX() + 0.5, offset.getY(), offset.getZ() + 0.5, 0, 0);
        egg.onPlayerPlace(context.getPlayer());
        if (itemstack.hasCustomName())
            egg.setCustomName(itemstack.getName());
        if (!context.getWorld().isClient)
            context.getWorld().spawnEntity(egg);
        itemstack.decrement(1);
        return ActionResult.SUCCESS;
    }
}
