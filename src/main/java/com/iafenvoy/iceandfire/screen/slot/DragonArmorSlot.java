package com.iafenvoy.iceandfire.screen.slot;

import com.iafenvoy.iceandfire.enums.EnumDragonArmorPart;
import com.iafenvoy.iceandfire.item.armor.ItemDragonArmor;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class DragonArmorSlot extends Slot {
    private final EnumDragonArmorPart expectedArmor;

    public DragonArmorSlot(Inventory inventory, int index, int x, int y, EnumDragonArmorPart expectedArmor) {
        super(inventory, index, x, y);
        this.expectedArmor = expectedArmor;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return super.canInsert(stack) && !stack.isEmpty() && stack.getItem() instanceof ItemDragonArmor armor && armor.dragonSlot == this.expectedArmor;
    }
}
