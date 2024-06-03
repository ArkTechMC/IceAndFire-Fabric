package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.armor.ModelDeathWormArmor;
import com.github.alexthe666.iceandfire.interfaces.IArmorTextureProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ItemDeathwormArmor extends ArmorItem implements IArmorTextureProvider {

    public ItemDeathwormArmor(ArmorMaterial material, Type slot) {
        super(material, slot, new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
    }


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull BipedEntityModel<?> getHumanoidArmorModel(LivingEntity LivingEntity, ItemStack itemStack, EquipmentSlot armorSlot, BipedEntityModel<?> _default) {
                return new ModelDeathWormArmor(ModelDeathWormArmor.getBakedModel(armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD));
            }
        });
    }


    @Override
    public Identifier getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (this.getMaterial() == IafItemRegistry.DEATHWORM_2_ARMOR_MATERIAL) {
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_deathworm_red" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
        } else if (this.getMaterial() == IafItemRegistry.DEATHWORM_1_ARMOR_MATERIAL) {
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_deathworm_white" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
        } else {
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_deathworm_yellow" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
        }
    }
}
