package dev.arktechmc.iafextra.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public interface IArmorTextureProvider {
    Identifier getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type);
}
