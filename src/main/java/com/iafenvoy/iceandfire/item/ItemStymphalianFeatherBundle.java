package com.iafenvoy.iceandfire.item;

import com.iafenvoy.iceandfire.entity.EntityStymphalianFeather;
import com.iafenvoy.iceandfire.registry.IafEntities;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class ItemStymphalianFeatherBundle extends Item {
    public ItemStymphalianFeatherBundle() {
        super(new Settings());
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity player, Hand hand) {
        ItemStack itemStackIn = player.getStackInHand(hand);
        player.setCurrentHand(hand);
        player.getItemCooldownManager().set(this, 15);
        player.playSound(SoundEvents.ENTITY_EGG_THROW, 1, 1);
        float rotation = player.headYaw;
        for (int i = 0; i < 8; i++) {
            EntityStymphalianFeather feather = new EntityStymphalianFeather(IafEntities.STYMPHALIAN_FEATHER, worldIn, player);
            rotation += 45;
            feather.setVelocity(player, 0, rotation, 0.0F, 1.5F, 1.0F);
            if (!worldIn.isClient)
                worldIn.spawnEntity(feather);
        }
        if (!player.isCreative())
            itemStackIn.decrement(1);
        return new TypedActionResult<>(ActionResult.PASS, itemStackIn);
    }


    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        tooltip.add(Text.translatable("item.iceandfire.legendary_weapon.desc").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.iceandfire.stymphalian_feather_bundle.desc_0").formatted(Formatting.GRAY));
    }
}