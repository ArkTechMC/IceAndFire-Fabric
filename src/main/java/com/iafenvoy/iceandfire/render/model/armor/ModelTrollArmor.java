package com.iafenvoy.iceandfire.render.model.armor;

import com.iafenvoy.uranus.client.render.armor.ArmorModelBase;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;

public class ModelTrollArmor extends ArmorModelBase {
    private static final ModelPart INNER_MODEL = getModelData(Dilation.NONE.add(INNER_MODEL_OFFSET), 0.0F).getRoot().createPart(64, 64);
    private static final ModelPart OUTER_MODEL = getModelData(Dilation.NONE.add(OUTER_MODEL_OFFSET), 0.0F).getRoot().createPart(64, 64);

    public ModelTrollArmor(boolean inner) {
        super(getBakedModel(inner));
    }

    public static ModelData getModelData(Dilation deformation, float offset) {
        ModelData modelData = BipedEntityModel.getModelData(deformation, offset);
        ModelPartData root = modelData.getRoot();

        root.getChild("head").addChild("hornL", ModelPartBuilder.create().uv(3, 41).mirrored().cuboid(-1.0F, -0.5F, 0.0F, 1, 2, 5), ModelTransform.of(3.0F, -2.2F, -3.0F, -0.7740535232594852F, 2.9595548126067843F, -0.27314402793711257F));
        root.getChild("head").getChild("hornL").addChild("hornL2", ModelPartBuilder.create().uv(15, 50).mirrored().cuboid(-0.51F, -0.8F, 0.0F, 1, 2, 7), ModelTransform.of(-0.4F, 1.3F, 4.5F, 1.2747884856566583F, 0.0F, 0.0F));

        root.getChild("head").addChild("hornR", ModelPartBuilder.create().uv(4, 41).mirrored().cuboid(-0.5F, -0.5F, 0.0F, 1, 2, 5), ModelTransform.of(-3.3F, -2.2F, -3.0F, -0.7740535232594852F, -2.9595548126067843F, 0.27314402793711257F));
        root.getChild("head").getChild("hornR").addChild("hornR2", ModelPartBuilder.create().uv(15, 50).mirrored().cuboid(-0.01F, -0.8F, 0.0F, 1, 2, 7), ModelTransform.of(-0.6F, 1.3F, 4.5F, 1.2747884856566583F, 0.0F, 0.0F));
        return modelData;
    }

    public static ModelPart getBakedModel(boolean inner) {
        return inner ? INNER_MODEL : OUTER_MODEL;
    }
}
