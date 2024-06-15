package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityMobSkull;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

public class ItemMobSkull extends Item {

    private final EnumSkullType skull;

    public ItemMobSkull(EnumSkullType skull) {
        super(new Settings().maxCount(1));
        this.skull = skull;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        EntityMobSkull skull = new EntityMobSkull(IafEntityRegistry.MOB_SKULL.get(), context.getWorld());
        ItemStack stack = player.getStackInHand(context.getHand());
        BlockPos offset = context.getBlockPos().offset(context.getSide(), 1);
        skull.refreshPositionAndAngles(offset.getX() + 0.5, offset.getY(), offset.getZ() + 0.5, 0, 0);
        float yaw = player.getYaw();
        if (context.getSide() != Direction.UP) {
            yaw = player.getHorizontalFacing().asRotation();
        }
        skull.setYaw(yaw);
        skull.setSkullType(this.skull);
        if (!context.getWorld().isClient) {
            context.getWorld().spawnEntity(skull);
        }
        if (stack.hasCustomName()) {
            skull.setCustomName(stack.getName());
        }
        if (!player.isCreative()) {
            stack.decrement(1);
        }
        return ActionResult.SUCCESS;
    }
}
