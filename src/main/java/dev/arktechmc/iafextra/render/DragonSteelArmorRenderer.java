package dev.arktechmc.iafextra.render;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.armor.ModelDragonsteelFireArmor;
import com.github.alexthe666.iceandfire.client.model.armor.ModelDragonsteelIceArmor;
import com.github.alexthe666.iceandfire.client.model.armor.ModelDragonsteelLightningArmor;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import static com.github.alexthe666.iceandfire.item.IafItemRegistry.*;

public class DragonSteelArmorRenderer implements ArmorRenderer {
    public @NotNull BipedEntityModel<?> getHumanoidArmorModel(ItemStack itemStack, EquipmentSlot armorSlot, BipedEntityModel<?> _default) {
        boolean inner = armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD;
        if (itemStack.getItem() instanceof ArmorItem) {
            ArmorMaterial armorMaterial = ((ArmorItem) itemStack.getItem()).getMaterial();
            if (DRAGONSTEEL_FIRE_ARMOR_MATERIAL.equals(armorMaterial))
                return new ModelDragonsteelFireArmor(inner);
            if (DRAGONSTEEL_ICE_ARMOR_MATERIAL.equals(armorMaterial))
                return new ModelDragonsteelIceArmor(inner);
            if (DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL.equals(armorMaterial))
                return new ModelDragonsteelLightningArmor(inner);
        }
        return _default;

    }

    public Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
        ArmorMaterial material = ((ArmorItem) stack.getItem()).getMaterial();
        if (material == DRAGONSTEEL_FIRE_ARMOR_MATERIAL) {
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_dragonsteel_fire" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
        } else if (material == IafItemRegistry.DRAGONSTEEL_ICE_ARMOR_MATERIAL) {
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_dragonsteel_ice" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
        } else {
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_dragonsteel_lightning" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, this.getHumanoidArmorModel(stack, slot, contextModel), this.getArmorTexture(stack, slot));
    }
}
