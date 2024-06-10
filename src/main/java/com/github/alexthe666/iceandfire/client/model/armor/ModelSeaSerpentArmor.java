package com.github.alexthe666.iceandfire.client.model.armor;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;

public class ModelSeaSerpentArmor extends ArmorModelBase {
    private static final ModelPart INNER_MODEL = getModelData(Dilation.NONE.add(INNER_MODEL_OFFSET), 0.0F).getRoot().createPart(64, 64);
    private static final ModelPart OUTER_MODEL = getModelData(Dilation.NONE.add(OUTER_MODEL_OFFSET), 0.0F).getRoot().createPart(64, 64);

    public ModelSeaSerpentArmor(boolean inner) {
        super(getBakedModel(inner));
    }

    public static ModelData getModelData(Dilation deformation, float offset) {
        ModelData meshdefinition = BipedEntityModel.getModelData(deformation, offset);
        ModelPartData partdefinition = meshdefinition.getRoot();

        partdefinition.getChild("head").addChild("headFin", ModelPartBuilder.create().uv(0, 32).cuboid(-0.5F, -8.4F, -7.9F, 1, 16, 14), ModelTransform.of(-3.5F, -8.8F, 3.5F, 3.141592653589793F, -0.5235987755982988F, 0.0F));
        partdefinition.getChild("head").addChild("headFin2", ModelPartBuilder.create().uv(0, 32).mirrored().cuboid(-0.5F, -8.4F, -7.9F, 1, 16, 14), ModelTransform.of(3.5F, -8.8F, 3.5F, 3.141592653589793F, 0.5235987755982988F, 0.0F));

        partdefinition.getChild("right_arm").addChild("armFinR", ModelPartBuilder.create().uv(30, 32).cuboid(-0.5F, -5.4F, -6.0F, 1, 7, 5), ModelTransform.of(-1.5F, 4.0F, -0.4F, 3.141592653589793F, -1.3089969389957472F, -0.003490658503988659F));
        partdefinition.getChild("left_arm").addChild("armFinL", ModelPartBuilder.create().uv(30, 32).mirrored().cuboid(-0.5F, -5.4F, -6.0F, 1, 7, 5), ModelTransform.of(1.5F, 4.0F, -0.4F, 3.141592653589793F, 1.3089969389957472F, 0.0F));

        partdefinition.getChild("right_leg").addChild("legFinR", ModelPartBuilder.create().uv(45, 31).cuboid(-0.5F, -5.4F, -6.0F, 1, 7, 6), ModelTransform.of(-1.5F, 5.2F, 1.6F, 3.141592653589793F, -1.3089969389957472F, 0.0F));
        partdefinition.getChild("left_leg").addChild("legFinL", ModelPartBuilder.create().uv(45, 31).mirrored().cuboid(-0.5F, -5.4F, -6.0F, 1, 7, 6), ModelTransform.of(1.5F, 5.2F, 1.6F, 3.141592653589793F, 1.3089969389957472F, 0.0F));

        partdefinition.getChild("right_arm").addChild("shoulderR", ModelPartBuilder.create().uv(38, 46).cuboid(-3.5F, -2.0F, -2.5F, 5, 12, 5), ModelTransform.pivot(0.0F, -0.5F, 0.0F));
        partdefinition.getChild("left_arm").addChild("shoulderL", ModelPartBuilder.create().uv(38, 46).mirrored().cuboid(-1.5F, -2.0F, -2.5F, 5, 12, 5), ModelTransform.pivot(0.0F, -0.5F, 0.0F));

        return meshdefinition;
    }

    public static ModelPart getBakedModel(boolean inner) {
        return inner ? INNER_MODEL : OUTER_MODEL;
    }
}
