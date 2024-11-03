package com.iafenvoy.iceandfire.render.model.armor;

import com.iafenvoy.uranus.client.render.armor.ArmorModelBase;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;

public class ModelDragonSteelLightningArmor extends ArmorModelBase {
    private static final ModelPart INNER_MODEL = getModelData(Dilation.NONE.add(INNER_MODEL_OFFSET), 0.0F).getRoot().createPart(64, 64);
    private static final ModelPart OUTER_MODEL = getModelData(Dilation.NONE.add(OUTER_MODEL_OFFSET), 0.0F).getRoot().createPart(64, 64);

    public ModelDragonSteelLightningArmor(boolean inner) {
        super(getBakedModel(inner));
    }

    public static ModelData getModelData(Dilation deformation, float offset) {
        ModelData modelData = BipedEntityModel.getModelData(deformation, offset);
        ModelPartData root = modelData.getRoot();

        root.getChild("head").addChild("HornR", ModelPartBuilder.create().uv(9, 39).cuboid(-1.0F, -0.5F, 0.0F, 2, 2, 4), ModelTransform.of(-2.8F, -7.9F, -4.2F, 0.27314402793711257F, -0.24434609527920614F, 0.0F));
        root.getChild("head").addChild("HornL", ModelPartBuilder.create().uv(9, 39).mirrored().cuboid(-1.0F, -0.5F, 0.0F, 2, 2, 4), ModelTransform.of(2.8F, -7.9F, -4.2F, 0.27314402793711257F, 0.24434609527920614F, 0.0F));

        root.getChild("head").addChild("HornR2", ModelPartBuilder.create().uv(9, 38).cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 5), ModelTransform.of(-3.2F, -5.4F, -4.0F, -0.25185101106278174F, -0.296705972839036F, 0.0F));
        root.getChild("head").addChild("HornL2", ModelPartBuilder.create().uv(9, 38).cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 5), ModelTransform.of(3.2F, -5.4F, -4.0F, -0.25185101106278174F, 0.296705972839036F, 0.0F));

        root.getChild("head").addChild("HornR4", ModelPartBuilder.create().uv(9, 38).cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 5), ModelTransform.of(-3.2F, -6.4F, -3.0F, 0.0F, -0.296705972839036F, 0.0F));
        root.getChild("head").addChild("HornL4", ModelPartBuilder.create().uv(9, 38).cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 5), ModelTransform.of(3.2F, -6.4F, -3.0F, 0.0F, 0.296705972839036F, 0.0F));

        root.getChild("head").addChild("visor1", ModelPartBuilder.create().uv(27, 50).cuboid(-4.7F, -13.3F, -4.9F, 4, 5, 8), ModelTransform.pivot(0.0F, 9.0F, 0.2F));
        root.getChild("head").addChild("visor2", ModelPartBuilder.create().uv(27, 50).mirrored().cuboid(0.8F, -13.3F, -4.9F, 4, 5, 8), ModelTransform.pivot(-0.1F, 9.0F, 0.2F));

        root.getChild("right_arm").addChild("sleeveRight", ModelPartBuilder.create().uv(36, 33).cuboid(-4.5F, -2.1F, -2.4F, 5, 4, 5), ModelTransform.of(0.3F, -0.3F, 0.0F, 0.0F, 0.0F, -0.12217304763960307F));
        root.getChild("left_arm").addChild("sleeveLeft", ModelPartBuilder.create().uv(36, 33).mirrored().cuboid(-0.5F, -2.1F, -2.4F, 5, 4, 5), ModelTransform.of(-0.7F, -0.3F, 0.0F, 0.0F, 0.0F, 0.12217304763960307F));

        root.addChild("right_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), ModelTransform.pivot(-1.9F, 12.0F + offset, 0.0F));
        root.addChild("left_leg", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), ModelTransform.pivot(1.9F, 12.0F + offset, 0.0F));
        root.getChild("right_leg").addChild("robeLowerRight", ModelPartBuilder.create().uv(4, 51).mirrored().cuboid(-2.1F, 0.0F, -2.5F, 4, 7, 5), ModelTransform.pivot(0.0F, -0.2F, 0.0F));
        root.getChild("left_leg").addChild("robeLowerLeft", ModelPartBuilder.create().uv(4, 51).cuboid(-1.9F, 0.0F, -2.5F, 4, 7, 5), ModelTransform.pivot(0.0F, -0.2F, 0.0F));

        root.getChild("head").getChild("HornR").addChild("HornR2_1", ModelPartBuilder.create().uv(9, 38).mirrored().cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 5), ModelTransform.of(0.0F, 0.3F, 3.6F, -0.08726646259971647F, 0.0F, 0.0F));
        root.getChild("head").getChild("HornL").addChild("HornL2_1", ModelPartBuilder.create().uv(9, 38).cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 5), ModelTransform.of(0.0F, 0.3F, 3.6F, -0.08726646259971647F, 0.0F, 0.0F));

        root.getChild("head").getChild("HornR").getChild("HornR2_1").addChild("HornR3", ModelPartBuilder.create().uv(24, 44).mirrored().cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 4), ModelTransform.of(0.0F, 0.0F, 4.3F, 0.17453292519943295F, 0.0F, 0.0F));
        root.getChild("head").getChild("HornL").getChild("HornL2_1").addChild("HornL3", ModelPartBuilder.create().uv(24, 44).cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 4), ModelTransform.of(0.0F, 0.0F, 4.3F, 0.17453292519943295F, 0.0F, 0.0F));

        root.getChild("head").getChild("HornR2").addChild("HornR3_1", ModelPartBuilder.create().uv(25, 45).mirrored().cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 3), ModelTransform.of(0.0F, 0.0F, 4.3F, 0.13962634015954636F, 0.0F, 0.0F));
        root.getChild("head").getChild("HornL2").addChild("HornL3_1", ModelPartBuilder.create().uv(25, 45).cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 3), ModelTransform.of(0.0F, 0.0F, 4.3F, 0.13962634015954636F, 0.0F, 0.0F));

        root.getChild("head").getChild("HornR4").addChild("HornR5", ModelPartBuilder.create().uv(25, 45).mirrored().cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 3), ModelTransform.of(0.0F, 0.0F, 4.3F, 0.13962634015954636F, 0.0F, 0.0F));
        root.getChild("head").getChild("HornL4").addChild("HornL5", ModelPartBuilder.create().uv(25, 45).cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 3), ModelTransform.of(0.0F, 0.0F, 4.3F, 0.13962634015954636F, 0.0F, 0.0F));

        return modelData;
    }

    public static ModelPart getBakedModel(boolean inner) {
        return inner ? INNER_MODEL : OUTER_MODEL;
    }
}