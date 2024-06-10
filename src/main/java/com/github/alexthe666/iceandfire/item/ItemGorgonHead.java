package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.datagen.tags.IafEntityTags;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ItemGorgonHead extends Item {

    public ItemGorgonHead() {
        super(new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/.maxDamage(1));
    }

    @Override
    public void onCraft(ItemStack itemStack, @NotNull World world, @NotNull PlayerEntity player) {
        itemStack.setNbt(new NbtCompound());
    }

    @Override
    public int getMaxUseTime(@NotNull ItemStack stack) {
        return 72000;
    }

    @Override
    public @NotNull UseAction getUseAction(@NotNull ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void onStoppedUsing(@NotNull ItemStack stack, World worldIn, LivingEntity entity, int timeLeft) {
        double dist = 32;
        Vec3d Vector3d = entity.getCameraPosVec(1.0F);
        Vec3d Vector3d1 = entity.getRotationVec(1.0F);
        Vec3d Vector3d2 = Vector3d.add(Vector3d1.x * dist, Vector3d1.y * dist, Vector3d1.z * dist);
        Entity pointedEntity = null;
        List<Entity> list = worldIn.getOtherEntities(entity, entity.getBoundingBox().stretch(Vector3d1.x * dist, Vector3d1.y * dist, Vector3d1.z * dist).expand(1.0D, 1.0D, 1.0D), new Predicate<Entity>() {
            @Override
            public boolean apply(Entity entity) {
                if (entity instanceof LivingEntity livingEntity) {
                    boolean isImmune = livingEntity instanceof IBlacklistedFromStatues blacklisted && !blacklisted.canBeTurnedToStone() || entity.getType().isIn(IafEntityTags.IMMUNE_TO_GORGON_STONE) || livingEntity.hasStatusEffect(StatusEffects.BLINDNESS);
                    return !isImmune && entity.canHit() && !livingEntity.isDead() && (entity instanceof PlayerEntity || DragonUtils.isAlive(livingEntity));
                }

                return false;
            }
        });
        double d2 = dist;
        for (int j = 0; j < list.size(); ++j) {
            Entity entity1 = list.get(j);
            Box axisalignedbb = entity1.getBoundingBox().expand(entity1.getTargetingMargin());
            Optional<Vec3d> optional = axisalignedbb.raycast(Vector3d, Vector3d2);

            if (axisalignedbb.contains(Vector3d)) {
                if (d2 >= 0.0D) {
                    //pointedEntity = entity1;
                    d2 = 0.0D;
                }
            } else if (optional.isPresent()) {
                double d3 = Vector3d.distanceTo(optional.get());

                if (d3 < d2 || d2 == 0.0D) {
                    if (entity1.getRootVehicle() == entity.getRootVehicle() && !entity.canRiderInteract()) {
                        if (d2 == 0.0D) {
                            pointedEntity = entity1;
                        }
                    } else {
                        pointedEntity = entity1;
                        d2 = d3;
                    }
                }
            }
        }
        if (pointedEntity != null) {
            if (pointedEntity instanceof LivingEntity livingEntity) {
                boolean wasSuccesful = true;

                if (pointedEntity instanceof PlayerEntity) {
                    wasSuccesful = pointedEntity.damage(IafDamageRegistry.causeGorgonDamage(pointedEntity), Integer.MAX_VALUE);
                } else {
                    if (!worldIn.isClient)
                        pointedEntity.remove(Entity.RemovalReason.KILLED);
                }

                if (wasSuccesful) {
                    pointedEntity.playSound(IafSoundRegistry.TURN_STONE, 1, 1);
                    EntityStoneStatue statue = EntityStoneStatue.buildStatueEntity(livingEntity);
                    statue.updatePositionAndAngles(pointedEntity.getX(), pointedEntity.getY(), pointedEntity.getZ(), pointedEntity.getYaw(), pointedEntity.getPitch());
                    statue.bodyYaw = pointedEntity.getYaw();
                    if (!worldIn.isClient) {
                        worldIn.spawnEntity(statue);
                    }
                }

                if (entity instanceof PlayerEntity player && !player.isCreative()) {
                    stack.decrement(1);
                }
            }
        }
        stack.getNbt().putBoolean("Active", false);
    }

    @Override
    public @NotNull TypedActionResult<ItemStack> use(@NotNull World worldIn, PlayerEntity playerIn, @NotNull Hand hand) {
        ItemStack itemStackIn = playerIn.getStackInHand(hand);
        playerIn.setCurrentHand(hand);
        itemStackIn.getNbt().putBoolean("Active", true);
        return new TypedActionResult<>(ActionResult.SUCCESS, itemStackIn);
    }

    @Override
    public void usageTick(@NotNull World level, @NotNull LivingEntity player, @NotNull ItemStack stack, int count) {
    }

    @Override
    public void appendTooltip(@NotNull ItemStack stack, World worldIn, List<Text> tooltip, @NotNull TooltipContext flagIn) {
        tooltip.add(Text.translatable("item.iceandfire.legendary_weapon.desc").formatted(Formatting.GRAY));
    }
}
