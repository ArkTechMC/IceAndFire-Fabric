package com.github.alexthe666.iceandfire.client.model.armor;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;

public class ModelSilverArmor extends ArmorModelBase {
    private static final ModelPart INNER_MODEL = getModelData(Dilation.NONE.add(INNER_MODEL_OFFSET), 0.0F).getRoot().createPart(64, 64);
    private static final ModelPart OUTER_MODEL = getModelData(Dilation.NONE.add(OUTER_MODEL_OFFSET), 0.0F).getRoot().createPart(64, 64);

    public ModelSilverArmor(boolean inner) {
        super(getBakedModel(inner));
    }

    public static ModelData getModelData(Dilation deformation, float offset) {
        ModelData meshdefinition = BipedEntityModel.getModelData(deformation, offset);
        ModelPartData partdefinition = meshdefinition.getRoot();
        partdefinition.getChild("head").addChild("faceGuard", ModelPartBuilder.create().uv(30, 47).cuboid(-4.5F, -3.0F, -6.1F, 9, 9, 8), ModelTransform.of(0.0F, -6.6F, 1.9F, -0.7285004297824331F, 0.0F, 0.0F));
        partdefinition.getChild("head").addChild("helmWingR", ModelPartBuilder.create().uv(2, 37).cuboid(-0.5F, -1.0F, 0.0F, 1, 4, 6), ModelTransform.of(-3.0F, -6.3F, 1.3F, 0.5235987755982988F, -0.4363323129985824F, -0.05235987755982988F));
        partdefinition.getChild("head").addChild("helmWingL", ModelPartBuilder.create().uv(2, 37).mirrored().cuboid(-0.5F, -1.0F, 0.0F, 1, 4, 6), ModelTransform.of(3.0F, -6.3F, 1.3F, 0.5235987755982988F, 0.4363323129985824F, 0.05235987755982988F));

        partdefinition.getChild("hat").addChild("crest", ModelPartBuilder.create().uv(18, 32).cuboid(0.0F, -0.5F, 0.0F, 1, 9, 9), ModelTransform.of(0.0F, -7.9F, -0.1F, 1.2292353921796064F, 0.0F, 0.0F));

        partdefinition.getChild("body").addChild("robeLowerBack", ModelPartBuilder.create().uv(4, 55).mirrored().cuboid(-4.0F, 0.0F, -2.5F, 8, 8, 1), ModelTransform.of(0.0F, 12.0F, 0.0F, 0.0F, 3.141592653589793F, 0.0F));
        partdefinition.getChild("body").addChild("robeLower", ModelPartBuilder.create().uv(4, 55).cuboid(-4.0F, 0.0F, -2.5F, 8, 8, 1), ModelTransform.pivot(0.0F, 12.0F, 0.0F));
        return meshdefinition;
    }

    public static ModelPart getBakedModel(boolean inner) {
        return inner ? INNER_MODEL : OUTER_MODEL;
    }

}
