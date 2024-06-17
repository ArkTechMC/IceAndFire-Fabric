package com.iafenvoy.iceandfire.client.render.entity.layer;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public interface IHasArmorVariantResource {
    Identifier getArmorResource(int variant, EquipmentSlot slotType);
}
