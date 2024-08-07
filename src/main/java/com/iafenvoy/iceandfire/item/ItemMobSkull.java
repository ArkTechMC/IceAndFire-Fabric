package com.iafenvoy.iceandfire.item;

import com.iafenvoy.iceandfire.entity.EntityMobSkull;
import com.iafenvoy.iceandfire.data.IafSkullType;
import com.iafenvoy.iceandfire.registry.IafEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class ItemMobSkull extends Item {

    private final IafSkullType skull;

    public ItemMobSkull(IafSkullType skull) {
        super(new Settings().maxCount(1));
        this.skull = skull;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        EntityMobSkull skull = new EntityMobSkull(IafEntities.MOB_SKULL, context.getWorld());
        assert player != null;
        ItemStack stack = player.getStackInHand(context.getHand());
        BlockPos offset = context.getBlockPos().offset(context.getSide(), 1);
        skull.refreshPositionAndAngles(offset.getX() + 0.5, offset.getY(), offset.getZ() + 0.5, 0, 0);
        float yaw = player.getYaw();
        if (context.getSide() != Direction.UP)
            yaw = player.getHorizontalFacing().asRotation();
        skull.setYaw(yaw);
        skull.setSkullType(this.skull);
        if (!context.getWorld().isClient)
            context.getWorld().spawnEntity(skull);
        if (stack.hasCustomName())
            skull.setCustomName(stack.getName());
        if (!player.isCreative())
            stack.decrement(1);
        return ActionResult.SUCCESS;
    }
}
