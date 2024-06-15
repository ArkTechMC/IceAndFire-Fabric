package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityDeathWormEgg;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ItemDeathwormEgg extends Item {
    private final boolean gigantic;

    public ItemDeathwormEgg(boolean gigantic) {
        super(new Settings().maxCount(1));
        this.gigantic = gigantic;
    }


    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getStackInHand(handIn);

        if (!playerIn.isCreative()) {
            itemstack.decrement(1);
        }

        worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (worldIn.random.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isClient) {
            EntityDeathWormEgg entityegg = new EntityDeathWormEgg(IafEntityRegistry.DEATH_WORM_EGG.get(), playerIn,
                    worldIn, this.gigantic);
            entityegg.setVelocity(playerIn, playerIn.getPitch(), playerIn.getYaw(), 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntity(entityegg);
        }

        return new TypedActionResult<>(ActionResult.SUCCESS, itemstack);
    }
}
