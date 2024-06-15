package dev.arktechmc.iafextra.render;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.armor.ModelTrollArmor;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.item.ItemTrollArmor;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Locale;

public class TrollArmorRenderer implements ArmorRenderer {
    public BipedEntityModel<?> getHumanoidArmorModel(LivingEntity LivingEntity, ItemStack itemStack, EquipmentSlot armorSlot, BipedEntityModel<?> _default) {
        return new ModelTrollArmor(armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD);
    }

    public Identifier getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        EnumTroll troll = ((ItemTrollArmor) stack.getItem()).troll;
        return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_troll_" + troll.name().toLowerCase(Locale.ROOT) + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {

    }
}
