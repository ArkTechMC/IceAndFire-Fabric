package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityDragonSkull;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemDragonSkull extends Item {
    private final int dragonType;

    public ItemDragonSkull(int dragonType) {
        super(new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/.maxCount(1));
        this.dragonType = dragonType;
    }

    static String getName(int type) {
        return "dragon_skull_%s".formatted(getType(type));
    }

    private static String getType(int type) {
        if (type == 2) {
            return "lightning";
        } else if (type == 1) {
            return "ice";
        } else {
            return "fire";
        }
    }

    @Override
    public void onCraft(ItemStack itemStack, @NotNull World world, @NotNull PlayerEntity player) {
        itemStack.setNbt(new NbtCompound());
    }

    @Override
    public void inventoryTick(ItemStack stack, @NotNull World worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getNbt() == null) {
            stack.setNbt(new NbtCompound());
            stack.getNbt().putInt("Stage", 4);
            stack.getNbt().putInt("DragonAge", 75);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, @NotNull TooltipContext flagIn) {
        String iceorfire = "dragon." + getType(dragonType);
        tooltip.add(Text.translatable(iceorfire).formatted(Formatting.GRAY));
        if (stack.getNbt() != null) {
            tooltip.add(Text.translatable("dragon.stage").formatted(Formatting.GRAY).append(Text.literal(" " + stack.getNbt().getInt("Stage"))));
        }
    }

    @Override
    public @NotNull ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getPlayer().getStackInHand(context.getHand());
        /*
         * EntityDragonEgg egg = new EntityDragonEgg(worldIn);
         * egg.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() +
         * 0.5); if(!worldIn.isRemote){ worldIn.spawnEntityInWorld(egg); }
         */
        if (stack.getNbt() != null) {
            EntityDragonSkull skull = new EntityDragonSkull(IafEntityRegistry.DRAGON_SKULL.get(), context.getWorld());
            skull.setDragonType(dragonType);
            skull.setStage(stack.getNbt().getInt("Stage"));
            skull.setDragonAge(stack.getNbt().getInt("DragonAge"));
            BlockPos offset = context.getBlockPos().offset(context.getSide(), 1);
            skull.refreshPositionAndAngles(offset.getX() + 0.5, offset.getY(), offset.getZ() + 0.5, 0, 0);
            float yaw = context.getPlayer().getYaw();
            if (context.getSide() != Direction.UP) {
                yaw = context.getPlayer().getHorizontalFacing().asRotation();
            }
            skull.setYaw(yaw);
            if (stack.hasCustomName()) {
                skull.setCustomName(stack.getName());
            }
            if (!context.getWorld().isClient) {
                context.getWorld().spawnEntity(skull);
            }
            if (!context.getPlayer().isCreative()) {
                stack.decrement(1);
            }
        }
        return ActionResult.SUCCESS;

    }
}
