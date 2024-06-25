package com.iafenvoy.iceandfire.item.armor;

import com.iafenvoy.citadel.server.item.CustomArmorMaterial;
import com.iafenvoy.iceandfire.enums.EnumDragonArmor;
import com.iafenvoy.iceandfire.enums.EnumDragonColor;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemScaleArmor extends ArmorItem implements IProtectAgainstDragonItem {

    public final EnumDragonArmor armor_type;
    public final EnumDragonColor eggType;

    public ItemScaleArmor(EnumDragonColor eggType, EnumDragonArmor armorType, CustomArmorMaterial material, Type slot) {
        super(material, slot, new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
        this.armor_type = armorType;
        this.eggType = eggType;
    }

    @Override
    public String getTranslationKey() {
        return switch (this.type) {
            case HELMET -> "item.iceandfire.dragon_helmet";
            case CHESTPLATE -> "item.iceandfire.dragon_chestplate";
            case LEGGINGS -> "item.iceandfire.dragon_leggings";
            case BOOTS -> "item.iceandfire.dragon_boots";
        };
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        tooltip.add(Text.translatable("dragon." + this.eggType.toString().toLowerCase()).formatted(this.eggType.color));
        tooltip.add(Text.translatable("item.dragonscales_armor.desc").formatted(Formatting.GRAY));
    }
}
