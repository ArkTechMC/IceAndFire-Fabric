package com.iafenvoy.iceandfire.item;

import com.iafenvoy.iceandfire.entity.EntityHippogryphEgg;
import com.iafenvoy.iceandfire.enums.EnumHippogryphTypes;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.registry.IafItems;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class ItemHippogryphEgg extends Item {

    public ItemHippogryphEgg() {
        super(new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/.maxCount(1));
    }

    public static ItemStack createEggStack(EnumHippogryphTypes parent1, EnumHippogryphTypes parent2) {
        EnumHippogryphTypes eggType = ThreadLocalRandom.current().nextBoolean() ? parent1 : parent2;
        ItemStack stack = new ItemStack(IafItems.HIPPOGRYPH_EGG);
        NbtCompound tag = new NbtCompound();
        tag.putInt("EggOrdinal", eggType.ordinal());
        stack.setNbt(tag);
        return stack;
    }


/*    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            for (EnumHippogryphTypes type : EnumHippogryphTypes.values()) {
                ItemStack stack = new ItemStack(this);
                CompoundTag tag = new CompoundTag();
                tag.putInt("EggOrdinal", type.ordinal());
                stack.setTag(tag);
                items.add(stack);

            }
        }

    }*/

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getStackInHand(handIn);

        if (!playerIn.isCreative())
            itemstack.decrement(1);

        worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (worldIn.random.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isClient) {
            EntityHippogryphEgg entityegg = new EntityHippogryphEgg(IafEntities.HIPPOGRYPH_EGG, worldIn,
                    playerIn, itemstack);
            entityegg.setVelocity(playerIn, playerIn.getPitch(), playerIn.getYaw(), 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntity(entityegg);
        }

        return new TypedActionResult<>(ActionResult.SUCCESS, itemstack);
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        NbtCompound tag = stack.getNbt();
        int eggOrdinal = 0;
        if (tag != null)
            eggOrdinal = tag.getInt("EggOrdinal");

        String type = EnumHippogryphTypes.values()[MathHelper.clamp(eggOrdinal, 0, EnumHippogryphTypes.values().length - 1)].name().toLowerCase(Locale.ROOT);
        tooltip.add(Text.translatable("entity.iceandfire.hippogryph." + type).formatted(Formatting.GRAY));
    }
}
