package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.client.render.tile.RenderDeathWormGauntlet;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class ItemDeathwormGauntlet extends Item {

    private boolean deathwormReceded = true;
    private boolean deathwormLaunched = false;
    private int specialDamage = 0;

    public ItemDeathwormGauntlet() {
        super(new Settings().maxDamage(500)/*.tab(IceAndFire.TAB_ITEMS)*/);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {

        consumer.accept(new IClientItemExtensions() {
            static final NonNullLazy<BuiltinModelItemRenderer> renderer = NonNullLazy.of(() -> new RenderDeathWormGauntlet(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return renderer.get();
            }
        });
    }

    @Override
    public int getMaxUseTime(@NotNull ItemStack stack) {
        return 1;
    }

    @Override
    public @NotNull UseAction getUseAction(@NotNull ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public @NotNull TypedActionResult<ItemStack> use(@NotNull World worldIn, PlayerEntity playerIn, @NotNull Hand hand) {
        ItemStack itemStackIn = playerIn.getStackInHand(hand);
        playerIn.setCurrentHand(hand);
        return new TypedActionResult<>(ActionResult.PASS, itemStackIn);
    }

    @Override
    public void usageTick(@NotNull World level, @NotNull LivingEntity entity, @NotNull ItemStack stack, int count) {
        if (!deathwormReceded && !deathwormLaunched) {
            if (entity instanceof PlayerEntity player) {
                NbtCompound tag = stack.getOrCreateNbt();

                if (tag.getInt("HolderID") != player.getId()) {
                    tag.putInt("HolderID", player.getId());
                }

                if (player.getItemCooldownManager().getCooldownProgress(this, 0.0F) == 0) {
                    player.getItemCooldownManager().set(this, 10);
                    player.playSound(IafSoundRegistry.DEATHWORM_ATTACK, 1F, 1F);
                    deathwormReceded = false;
                    deathwormLaunched = true;
                }
            }
        }
    }

    @Override
    public void onStoppedUsing(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull LivingEntity LivingEntity, int timeLeft) {
        if (specialDamage > 0) {
            stack.damage(specialDamage, LivingEntity, player -> player.sendToolBreakStatus(LivingEntity.getActiveHand()));
            specialDamage = 0;
        }

        NbtCompound tag = stack.getOrCreateNbt();

        if (tag.getInt("HolderID") != -1) {
            tag.putInt("HolderID", -1);
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull World world, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        if (!(entity instanceof LivingEntity)) {
            return;
        }

        EntityDataProvider.getCapability(entity).ifPresent(data -> {
            int tempLungeTicks = data.miscData.lungeTicks;

            if (deathwormReceded) {
                if (tempLungeTicks > 0) {
                    tempLungeTicks = tempLungeTicks - 4;
                }

                if (tempLungeTicks <= 0) {
                    tempLungeTicks = 0;
                    deathwormReceded = false;
                    deathwormLaunched = false;
                }
            } else if (deathwormLaunched) {
                tempLungeTicks = 4 + tempLungeTicks;

                if (tempLungeTicks > 20) {
                    deathwormReceded = true;
                }
            }

            if (data.miscData.lungeTicks == 20) {
                if (entity instanceof PlayerEntity player) {
                    Vec3d Vector3d = player.getRotationVec(1.0F).normalize();
                    double range = 5;

                    for (LivingEntity livingEntity : world.getEntitiesByClass(LivingEntity.class, new Box(player.getX() - range, player.getY() - range, player.getZ() - range, player.getX() + range, player.getY() + range, player.getZ() + range), livingEntity -> true)) {
                        //Let's not pull/hit ourselves
                        if (livingEntity == entity) {
                            continue;
                        }

                        Vec3d Vector3d1 = new Vec3d(livingEntity.getX() - player.getX(), livingEntity.getY() - player.getY(), livingEntity.getZ() - player.getZ());
                        double d0 = Vector3d1.length();
                        Vector3d1 = Vector3d1.normalize();
                        double d1 = Vector3d.dotProduct(Vector3d1);
                        boolean canSee = d1 > 1.0D - 0.5D / d0 && player.canSee(livingEntity);

                        if (canSee) {
                            specialDamage++;
                            livingEntity.damage(entity.getWorld().damageSources.playerAttack((PlayerEntity) entity), 3F);
                            livingEntity.takeKnockback(0.5F, livingEntity.getX() - player.getX(), livingEntity.getZ() - player.getZ());
                        }
                    }
                }
            }

            data.miscData.setLungeTicks(tempLungeTicks);
        });
    }

    @Override
    public void appendTooltip(@NotNull ItemStack stack, World worldIn, List<Text> tooltip, @NotNull TooltipContext flagIn) {
        tooltip.add(Text.translatable("item.iceandfire.legendary_weapon.desc").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.iceandfire.deathworm_gauntlet.desc_0").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.iceandfire.deathworm_gauntlet.desc_1").formatted(Formatting.GRAY));
    }
}
