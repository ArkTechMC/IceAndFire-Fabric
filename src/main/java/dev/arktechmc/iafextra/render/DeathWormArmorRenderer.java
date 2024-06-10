package dev.arktechmc.iafextra.render;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.armor.ModelDeathWormArmor;
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

public class DeathWormArmorRenderer implements ArmorRenderer {
    public @NotNull BipedEntityModel<?> getHumanoidArmorModel(EquipmentSlot armorSlot) {
        return new ModelDeathWormArmor(ModelDeathWormArmor.getBakedModel(armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD));
    }

    public Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
        ArmorMaterial material = ((ArmorItem) stack.getItem()).getMaterial();
        if (material == IafItemRegistry.DEATHWORM_2_ARMOR_MATERIAL) {
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_deathworm_red" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
        } else if (material == IafItemRegistry.DEATHWORM_1_ARMOR_MATERIAL) {
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_deathworm_white" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
        } else {
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_deathworm_yellow" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, this.getHumanoidArmorModel(slot), this.getArmorTexture(stack, slot));
    }
}
