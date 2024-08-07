package com.iafenvoy.iceandfire.render.armor;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.data.DragonArmor;
import com.iafenvoy.iceandfire.data.DragonType;
import com.iafenvoy.iceandfire.item.armor.ItemScaleArmor;
import com.iafenvoy.iceandfire.render.model.armor.ModelFireDragonScaleArmor;
import com.iafenvoy.iceandfire.render.model.armor.ModelIceDragonScaleArmor;
import com.iafenvoy.iceandfire.render.model.armor.ModelLightningDragonScaleArmor;
import com.iafenvoy.uranus.client.render.armor.ArmorModelBase;
import com.iafenvoy.uranus.client.render.armor.ArmorRendererImpl;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ScaleArmorRenderer extends ArmorRendererImpl {
    @Override
    public ArmorModelBase getHumanoidArmorModel(ItemStack itemStack, EquipmentSlot armorSlot) {
        boolean inner = armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD;
        if (itemStack.getItem() instanceof ItemScaleArmor scaleArmor) {
            DragonType dragonType = scaleArmor.armor_type.getColor().dragonType();
            if (DragonType.FIRE == dragonType) return new ModelFireDragonScaleArmor(inner);
            if (DragonType.ICE == dragonType) return new ModelIceDragonScaleArmor(inner);
            if (DragonType.LIGHTNING == dragonType) return new ModelLightningDragonScaleArmor(inner);
        }
        return null;
    }

    @Override
    public Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
        DragonArmor armor_type = ((ItemScaleArmor) stack.getItem()).armor_type;
        return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_" + armor_type.getColor().name() + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
    }
}
