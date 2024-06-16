package com.github.alexthe666.iceandfire.item.armor;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.registry.IafItems;
import dev.arktechmc.iafextra.interfaces.IArmorTextureProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ItemBlindfold extends ArmorItem implements IArmorTextureProvider {

    public ItemBlindfold() {
        super(IafItems.BLINDFOLD_ARMOR_MATERIAL, Type.HELMET, new Settings());
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player && stack.isOf(this))
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 0, false, false));
    }

    @Override
    public Identifier getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/blindfold_layer_1.png");
    }
}
