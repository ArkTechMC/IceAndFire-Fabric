package com.github.alexthe666.iceandfire.client.model.armor;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;

public class ModelCopperArmor extends ArmorModelBase {
    private static final ModelPart INNER_MODEL = getModelData(Dilation.NONE.add(INNER_MODEL_OFFSET), 0.0F).getRoot().createPart(64, 64);
    private static final ModelPart OUTER_MODEL = getModelData(Dilation.NONE.add(OUTER_MODEL_OFFSET), 0.0F).getRoot().createPart(64, 64);

    public ModelCopperArmor(boolean inner) {
        super(getBakedModel(inner));
    }

    public static ModelData getModelData(Dilation deformation, float offset) {
        ModelData modelData = BipedEntityModel.getModelData(deformation, offset);
        ModelPartData root = modelData.getRoot();

        root.getChild("head").addChild("crest", ModelPartBuilder.create().uv(23, 31).cuboid(0.0F, -7.5F, -9.0F, 0, 16, 14), ModelTransform.pivot(0.0F, -7.6F, 2.6F));
        root.getChild("head").addChild("facePlate", ModelPartBuilder.create().uv(34, 32).cuboid(-4.5F, -8.2F, -4.01F, 9, 10, 1), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        root.getChild("right_leg").addChild("robeLowerRight", ModelPartBuilder.create().uv(0, 51).cuboid(-2.1F, 0.0F, -2.5F, 4, 8, 5).mirrored(), ModelTransform.pivot(0.0F, -0.2F, 0.0F));
        root.getChild("left_leg").addChild("robeLowerLeft", ModelPartBuilder.create().uv(0, 51).cuboid(-1.9F, 0.0F, -2.5F, 4, 8, 5), ModelTransform.pivot(0.0F, -0.2F, 0.0F));

        return modelData;
    }

    public static ModelPart getBakedModel(boolean inner) {
        return inner ? INNER_MODEL : OUTER_MODEL;
    }
}
