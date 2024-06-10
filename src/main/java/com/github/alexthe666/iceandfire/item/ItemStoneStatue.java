package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemStoneStatue extends Item {

    public ItemStoneStatue() {
        super(new Settings().maxCount(1));
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, @NotNull List<Text> tooltip, @NotNull TooltipContext flagIn) {
        if (stack.getNbt() != null) {
            boolean isPlayer = stack.getNbt().getBoolean("IAFStoneStatuePlayerEntity");
            String id = stack.getNbt().getString("IAFStoneStatueEntityID");
            if (EntityType.get(id).orElse(null) != null) {
                EntityType type = EntityType.get(id).orElse(null);
                MutableText untranslated = isPlayer ? Text.translatable("entity.minecraft.player") : Text.translatable(type.getTranslationKey());
                tooltip.add(untranslated.formatted(Formatting.GRAY));
            }
        }
    }

    @Override
    public void onCraft(ItemStack itemStack, @NotNull World world, @NotNull PlayerEntity player) {
        itemStack.setNbt(new NbtCompound());
        itemStack.getNbt().putBoolean("IAFStoneStatuePlayerEntity", true);
    }

    @Override
    public @NotNull ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getSide() != Direction.UP) {
            return ActionResult.FAIL;
        } else {
            ItemStack stack = context.getPlayer().getStackInHand(context.getHand());
            if (stack.getNbt() != null) {
                String id = stack.getNbt().getString("IAFStoneStatueEntityID");
                NbtCompound statueNBT = stack.getNbt().getCompound("IAFStoneStatueNBT");
                EntityStoneStatue statue = new EntityStoneStatue(IafEntityRegistry.STONE_STATUE.get(),
                        context.getWorld());
                statue.readCustomDataFromNbt(statueNBT);
                statue.setTrappedEntityTypeString(id);
                double d1 = context.getPlayer().getX() - (context.getBlockPos().getX() + 0.5);
                double d2 = context.getPlayer().getZ() - (context.getBlockPos().getZ() + 0.5);
                float yaw = (float) (MathHelper.atan2(d2, d1) * (180F / (float) Math.PI)) - 90;
                statue.prevYaw = yaw;
                statue.setYaw(yaw);
                statue.headYaw = yaw;
                statue.bodyYaw = yaw;
                statue.prevBodyYaw = yaw;
                statue.updatePositionAndAngles(context.getBlockPos().getX() + 0.5, context.getBlockPos().getY() + 1, context.getBlockPos().getZ() + 0.5, yaw, 0);
                if (!context.getWorld().isClient) {
                    context.getWorld().spawnEntity(statue);
                    statue.readCustomDataFromNbt(stack.getNbt());
                }
                statue.setCrackAmount(0);

                if (!context.getPlayer().isCreative()) {
                    stack.decrement(1);
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.SUCCESS;
    }
}
