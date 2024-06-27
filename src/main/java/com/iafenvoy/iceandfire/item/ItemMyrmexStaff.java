package com.iafenvoy.iceandfire.item;

import com.iafenvoy.iceandfire.entity.util.MyrmexHive;
import com.iafenvoy.iceandfire.network.IafServerNetworkHandler;
import com.iafenvoy.iceandfire.network.message.MessageGetMyrmexHiveS2C;
import com.iafenvoy.iceandfire.network.message.MessageSetMyrmexHiveNull;
import com.iafenvoy.iceandfire.screen.handler.MyrmexAddRoomScreenHandler;
import com.iafenvoy.iceandfire.screen.handler.MyrmexStaffScreenHandler;
import com.iafenvoy.iceandfire.world.MyrmexWorldData;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
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
        if (playerIn.isSneaking())
            return super.use(worldIn, playerIn, hand);
        if (itemStackIn.getNbt() != null && itemStackIn.getNbt().containsUuid("HiveUUID")) {
            UUID id = itemStackIn.getNbt().getUuid("HiveUUID");
            if (!worldIn.isClient) {
                MyrmexHive hive = MyrmexWorldData.get(worldIn).getHiveFromUUID(id);
                MyrmexWorldData.addHive(worldIn, new MyrmexHive());
                if (hive != null) IafServerNetworkHandler.sendToAll(new MessageGetMyrmexHiveS2C(hive.toNBT()));
                else IafServerNetworkHandler.sendToAll(new MessageSetMyrmexHiveNull());
            } else if (id != null && !id.equals(new UUID(0, 0)))
                playerIn.openHandledScreen(new ExtendedScreenHandlerFactory() {
                    @Override
                    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                        NbtCompound compound = new NbtCompound();
                        itemStackIn.writeNbt(compound);
                        buf.writeNbt(compound);
                    }

                    @Override
                    public Text getDisplayName() {
                        return Text.translatable("myrmex_staff_screen");
                    }

                    @Override
                    public @NotNull ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                        return new MyrmexStaffScreenHandler(syncId, playerInventory);
                    }
                });
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
                    if (hive != null) IafServerNetworkHandler.sendToAll(new MessageGetMyrmexHiveS2C(hive.toNBT()));
                    else IafServerNetworkHandler.sendToAll(new MessageSetMyrmexHiveNull());

                } else if (id != null && !id.equals(new UUID(0, 0)))
                    context.getPlayer().openHandledScreen(new ExtendedScreenHandlerFactory() {
                        @Override
                        public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                            ItemStack stack = context.getPlayer().getStackInHand(context.getHand());
                            NbtCompound compound = new NbtCompound();
                            stack.writeNbt(compound);
                            buf.writeNbt(compound);
                            buf.writeLong(context.getBlockPos().asLong());
                            buf.writeEnumConstant(player.getHorizontalFacing());

                        }

                        @Override
                        public Text getDisplayName() {
                            return Text.translatable("myrmex_add_room");
                        }

                        @Override
                        public @NotNull ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                            return new MyrmexAddRoomScreenHandler(syncId, playerInventory);
                        }
                    });
            }
            context.getPlayer().swingHand(context.getHand());
            return ActionResult.SUCCESS;
        }
    }
}
