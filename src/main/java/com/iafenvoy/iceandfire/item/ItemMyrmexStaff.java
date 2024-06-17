package com.iafenvoy.iceandfire.item;

import com.iafenvoy.iceandfire.client.gui.GuiMyrmexAddRoom;
import com.iafenvoy.iceandfire.client.gui.GuiMyrmexStaff;
import com.iafenvoy.iceandfire.entity.util.MyrmexHive;
import com.iafenvoy.iceandfire.message.MessageGetMyrmexHive;
import com.iafenvoy.iceandfire.message.MessageSetMyrmexHiveNull;
import com.iafenvoy.iceandfire.world.MyrmexWorldData;
import com.iafenvoy.iceandfire.network.IafServerNetworkHandler;
import net.minecraft.client.MinecraftClient;
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

import java.util.UUID;

public class ItemMyrmexStaff extends Item {

    public ItemMyrmexStaff(boolean jungle) {
        super(new Settings().maxCount(1));
    }

    @Override
    public void onCraft(ItemStack itemStack, World world, PlayerEntity player) {
        itemStack.setNbt(new NbtCompound());
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (stack.getNbt() == null) {
            stack.setNbt(new NbtCompound());
            stack.getNbt().putUuid("HiveUUID", new UUID(0, 0));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand hand) {
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
            } else if (id != null && !id.equals(new UUID(0, 0)))
                MinecraftClient.getInstance().setScreen(new GuiMyrmexStaff(itemStackIn));
        }
        playerIn.swingHand(hand);
        return new TypedActionResult<>(ActionResult.PASS, itemStackIn);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        assert context.getPlayer() != null;
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
                } else if (id != null && !id.equals(new UUID(0, 0)))
                    if (context.getWorld().isClient)
                        MinecraftClient.getInstance().setScreen(new GuiMyrmexAddRoom(context.getPlayer().getStackInHand(context.getHand()), context.getBlockPos(), context.getPlayer().getHorizontalFacing()));
            }
            context.getPlayer().swingHand(context.getHand());
            return ActionResult.SUCCESS;
        }
    }
}
