package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.message.MessageGetMyrmexHive;
import com.github.alexthe666.iceandfire.message.MessageSetMyrmexHiveNull;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import com.iafenvoy.iafextra.network.IafServerNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ItemMyrmexStaff extends Item {

    public ItemMyrmexStaff(boolean jungle) {
        super(new Settings().maxCount(1));
    }

    @Override
    public void onCraft(ItemStack itemStack, @NotNull World world, @NotNull PlayerEntity player) {
        itemStack.setNbt(new NbtCompound());
    }

    @Override
    public void inventoryTick(ItemStack stack, @NotNull World world, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        if (stack.getNbt() == null) {
            stack.setNbt(new NbtCompound());
            stack.getNbt().putUuid("HiveUUID", new UUID(0, 0));
        }
    }

    @Override
    public @NotNull TypedActionResult<ItemStack> use(@NotNull World worldIn, PlayerEntity playerIn, @NotNull Hand hand) {
        ItemStack itemStackIn = playerIn.getStackInHand(hand);
        if (playerIn.isSneaking()) {
            return super.use(worldIn, playerIn, hand);
        }
        if (itemStackIn.getNbt() != null && itemStackIn.getNbt().containsUuid("HiveUUID")) {
            UUID id = itemStackIn.getNbt().getUuid("HiveUUID");
            if (!worldIn.isClient) {
                MyrmexHive hive = MyrmexWorldData.get(worldIn).getHiveFromUUID(id);
                MyrmexWorldData.addHive(worldIn, new MyrmexHive());
                if (hive != null) {
                    IafServerNetworkHandler.sendToAll(new MessageGetMyrmexHive(hive.toNBT()));
                } else {
                    IafServerNetworkHandler.sendToAll(new MessageSetMyrmexHiveNull());
                }
            } else if (id != null && !id.equals(new UUID(0, 0))) {
                IceAndFire.PROXY.openMyrmexStaffGui(itemStackIn);
            }
        }
        playerIn.swingHand(hand);
        return new TypedActionResult<ItemStack>(ActionResult.PASS, itemStackIn);
    }

    @Override
    public @NotNull ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getPlayer().isSneaking()) {
            return super.useOnBlock(context);
        } else {
            NbtCompound tag = context.getPlayer().getStackInHand(context.getHand()).getNbt();
            if (tag != null && tag.containsUuid("HiveUUID")) {
                UUID id = tag.getUuid("HiveUUID");
                if (!context.getWorld().isClient) {
                    MyrmexHive hive = MyrmexWorldData.get(context.getWorld()).getHiveFromUUID(id);
                    if (hive != null) {
                        IafServerNetworkHandler.sendToAll(new MessageGetMyrmexHive(hive.toNBT()));
                    } else {
                        IafServerNetworkHandler.sendToAll(new MessageSetMyrmexHiveNull());
                    }
                } else if (id != null && !id.equals(new UUID(0, 0))) {
                    IceAndFire.PROXY.openMyrmexAddRoomGui(context.getPlayer().getStackInHand(context.getHand()), context.getBlockPos(), context.getPlayer().getHorizontalFacing());
                }
            }
            context.getPlayer().swingHand(context.getHand());
            return ActionResult.SUCCESS;
        }
    }
}
