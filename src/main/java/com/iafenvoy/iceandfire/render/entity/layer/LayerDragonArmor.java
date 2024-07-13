package com.iafenvoy.iceandfire.render.entity.layer;

import com.iafenvoy.uranus.client.model.AdvancedEntityModel;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.enums.EnumDragonTextures;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class LayerDragonArmor extends FeatureRenderer<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> {
    private static final EquipmentSlot[] ARMOR_SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public LayerDragonArmor(MobEntityRenderer<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> renderIn, int type) {
        super(renderIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int light, EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        AdvancedEntityModel<EntityDragonBase> model = this.getContextModel();
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = dragon.getEquippedStack(slot);
            if (stack.isEmpty()) continue;
            Identifier texture = EnumDragonTextures.getArmorTexture(stack, slot);
            VertexConsumer vertexConsumer = bufferIn.getBuffer(RenderLayer.getEntityCutoutNoCull(texture));
            model.render(matrixStackIn, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}