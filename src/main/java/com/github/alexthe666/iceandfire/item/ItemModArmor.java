package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import dev.arktechmc.iafextra.interfaces.IArmorTextureProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ItemModArmor extends ArmorItem implements IArmorTextureProvider {

    public ItemModArmor(ArmorMaterial material, Type slot) {
        super(material, slot, new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        if (this == IafItemRegistry.EARPLUGS.get()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            if (calendar.get(Calendar.MONTH) + 1 == 4 && calendar.get(Calendar.DATE) == 1) {
                return "item.iceandfire.air_pods";
            }
        }
        return super.getTranslationKey(stack);
    }

    @Override
    public Identifier getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (this.material == IafItemRegistry.MYRMEX_DESERT_ARMOR_MATERIAL) {
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/" + (slot == EquipmentSlot.LEGS ? "myrmex_desert_layer_2" : "myrmex_desert_layer_1") + ".png");
        }
        if (this.material == IafItemRegistry.MYRMEX_JUNGLE_ARMOR_MATERIAL) {
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/" + (slot == EquipmentSlot.LEGS ? "myrmex_jungle_layer_2" : "myrmex_jungle_layer_1") + ".png");
        }
        if (this.material == IafItemRegistry.SHEEP_ARMOR_MATERIAL) {
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/" + (slot == EquipmentSlot.LEGS ? "sheep_disguise_layer_2" : "sheep_disguise_layer_1") + ".png");
        }
        if (this.material == IafItemRegistry.EARPLUGS_ARMOR_MATERIAL) {
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/earplugs_layer_1.png");
        }
        return null;
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        if (this == IafItemRegistry.EARPLUGS.get()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            if (calendar.get(Calendar.MONTH) + 1 == 4 && calendar.get(Calendar.DATE) == 1) {
                tooltip.add(Text.translatable("item.iceandfire.air_pods.desc").formatted(Formatting.GREEN));
            }
        }
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
    }
}
