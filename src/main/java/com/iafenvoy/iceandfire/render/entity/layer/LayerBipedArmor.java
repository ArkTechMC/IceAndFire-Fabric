package com.iafenvoy.iceandfire.render.entity.layer;

import com.iafenvoy.uranus.animation.IAnimatedEntity;
import com.iafenvoy.iceandfire.render.model.ModelBipedBase;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

//TODO: Consider support for default minecraft armors/ dynamically selecting custom armors
//Base code from minecraft's ArmorBipedLayer
public class LayerBipedArmor<T extends LivingEntity & IAnimatedEntity, M extends ModelBipedBase<T>, A extends ModelBipedBase<T>> extends FeatureRenderer<T, M> {
    private final A modelLeggings;
    private final A modelArmor;
    private final Identifier defaultLegArmor;
    private final Identifier defaultArmor;

    public LayerBipedArmor(FeatureRendererContext<T, M> mobRenderer, A modelLeggings, A modelArmor, Identifier defaultArmor, Identifier defaultLegArmor) {
        super(mobRenderer);
        this.modelLeggings = modelLeggings;
        this.modelArmor = modelArmor;
        this.defaultLegArmor = defaultLegArmor;
        this.defaultArmor = defaultArmor;
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.renderEquipment(matrixStackIn, bufferIn, entitylivingbaseIn, EquipmentSlot.CHEST, packedLightIn, this.getSlotModel(EquipmentSlot.CHEST));
        this.renderEquipment(matrixStackIn, bufferIn, entitylivingbaseIn, EquipmentSlot.LEGS, packedLightIn, this.getSlotModel(EquipmentSlot.LEGS));
        this.renderEquipment(matrixStackIn, bufferIn, entitylivingbaseIn, EquipmentSlot.FEET, packedLightIn, this.getSlotModel(EquipmentSlot.FEET));
        this.renderEquipment(matrixStackIn, bufferIn, entitylivingbaseIn, EquipmentSlot.HEAD, packedLightIn, this.getSlotModel(EquipmentSlot.HEAD));
    }

    private void renderEquipment(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, T entityIn, EquipmentSlot slotType, int packedLightIn, A modelIn) {
        ItemStack itemstack = entityIn.getEquippedStack(slotType);
        if (itemstack.getItem() instanceof ArmorItem armoritem)
            if (armoritem.getSlotType() == slotType) {
                this.getContextModel().setModelAttributes(modelIn);
                this.setModelSlotVisible(modelIn, slotType);
                boolean flag1 = itemstack.hasGlint();
                this.renderArmorItem(matrixStackIn, bufferIn, packedLightIn, flag1, modelIn, this.getArmorResource(entityIn, itemstack, slotType, null));
            }
    }

    protected void setModelSlotVisible(A modelIn, EquipmentSlot slotIn) {
        modelIn.setVisible(false);
        switch (slotIn) {
            case HEAD -> {
                modelIn.head.invisible = false;
                modelIn.headware.invisible = false;
            }
            case CHEST -> {
                modelIn.body.invisible = false;
                modelIn.armRight.invisible = false;
                modelIn.armLeft.invisible = false;
            }
            case LEGS -> {
                modelIn.body.invisible = false;
                modelIn.legRight.invisible = false;
                modelIn.legLeft.invisible = false;
            }
            case FEET -> {
                modelIn.legRight.invisible = false;
                modelIn.legLeft.invisible = false;
            }
        }
    }

    private void renderArmorItem(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, boolean p_241738_5_, A modelIn, Identifier armorResource) {
        VertexConsumer ivertexbuilder = ItemRenderer.getArmorGlintConsumer(bufferIn, RenderLayer.getArmorCutoutNoCull(armorResource), false, p_241738_5_);
        modelIn.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, (float) 1.0, (float) 1.0, (float) 1.0, 1.0F);
    }

    private A getSlotModel(EquipmentSlot equipmentSlotType) {
        return this.isLegSlot(equipmentSlotType) ? this.modelLeggings : this.modelArmor;
    }

    protected boolean isLegSlot(EquipmentSlot slotIn) {
        return slotIn == EquipmentSlot.LEGS;
    }

    public Identifier getArmorResource(T entity, ItemStack stack, EquipmentSlot slot, String type) {
        if (this.isLegSlot(slot)) return this.defaultLegArmor;
        return this.defaultArmor;
    }
}
