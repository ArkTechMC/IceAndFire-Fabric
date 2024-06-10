package com.github.alexthe666.iceandfire.client.model.armor;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;

public class ModelDragonsteelFireArmor extends ArmorModelBase {
    private static final ModelPart INNER_MODEL = getModelData(Dilation.NONE.add(INNER_MODEL_OFFSET), 0.0F).getRoot().createPart(64, 64);
    private static final ModelPart OUTER_MODEL = getModelData(Dilation.NONE.add(OUTER_MODEL_OFFSET), 0.0F).getRoot().createPart(64, 64);

    public ModelDragonsteelFireArmor(boolean inner) {
        super(getBakedModel(inner));
    }

    public static ModelData getModelData(Dilation deformation, float offset) {
        ModelData meshdefinition = BipedEntityModel.getModelData(deformation, offset);
        ModelPartData partdefinition = meshdefinition.getRoot();

        partdefinition.getChild("head").addChild("HornR", ModelPartBuilder.create().uv(9, 39).cuboid(-1.0F, -0.5F, 0.0F, 2, 2, 4), ModelTransform.of(-2.5F, -7.9F, -4.2F, 0.43022366061660217F, -0.15707963267948966F, 0.0F));
        partdefinition.getChild("head").addChild("HornL", ModelPartBuilder.create().uv(9, 39).cuboid(-1.0F, -0.5F, 0.0F, 2, 2, 4), ModelTransform.of(2.5F, -7.9F, -4.2F, 0.43022366061660217F, 0.15707963267948966F, 0.0F));

        partdefinition.getChild("head").addChild("HornL4", ModelPartBuilder.create().uv(9, 38).cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 5), ModelTransform.of(3.2F, -7.4F, -3.0F, -0.14713125594312196F, 0.296705972839036F, 0.0F));
        partdefinition.getChild("head").addChild("HornR4", ModelPartBuilder.create().uv(9, 38).cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 5), ModelTransform.of(-3.2F, -7.4F, -3.0F, -0.14713125594312196F, -0.296705972839036F, 0.0F));

        partdefinition.getChild("head").addChild("visor1", ModelPartBuilder.create().uv(27, 50).cuboid(-4.7F, -13.3F, -4.9F, 4, 5, 8), ModelTransform.pivot(0.0F, 9.0F, 0.2F));
        partdefinition.getChild("head").addChild("visor2", ModelPartBuilder.create().uv(27, 50).mirrored().cuboid(0.8F, -13.3F, -4.9F, 4, 5, 8), ModelTransform.pivot(-0.1F, 9.0F, 0.2F));

        partdefinition.getChild("right_arm").addChild("sleeveRight", ModelPartBuilder.create().uv(36, 33).cuboid(-4.5F, -2.1F, -2.4F, 5, 6, 5), ModelTransform.of(0.3F, -0.3F, 0.0F, 0.0F, 0.0F, -0.12217304763960307F));
        partdefinition.getChild("left_arm").addChild("sleeveLeft", ModelPartBuilder.create().uv(36, 33).mirrored().cuboid(-0.5F, -2.1F, -2.4F, 5, 6, 5), ModelTransform.of(-0.7F, -0.3F, 0.0F, 0.0F, 0.0F, 0.12217304763960307F));

        partdefinition.getChild("right_leg").addChild("robeLowerRight", ModelPartBuilder.create().uv(4, 51).mirrored().cuboid(-2.1F, 0.0F, -2.5F, 4, 7, 5), ModelTransform.pivot(0.0F, -0.2F, 0.0F));
        partdefinition.getChild("left_leg").addChild("robeLowerLeft", ModelPartBuilder.create().uv(4, 51).cuboid(-1.9F, 0.0F, -2.5F, 4, 7, 5), ModelTransform.pivot(0.0F, -0.2F, 0.0F));

        partdefinition.getChild("head").getChild("HornR").addChild("HornR2", ModelPartBuilder.create().uv(9, 38).cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 5), ModelTransform.of(0.0F, 0.3F, 3.6F, -0.3391174736624982F, 0.0F, 0.0F));
        partdefinition.getChild("head").getChild("HornL").addChild("HornL2", ModelPartBuilder.create().uv(9, 38).cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 5), ModelTransform.of(0.0F, 0.3F, 3.6F, -0.3391174736624982F, 0.0F, 0.0F));

        partdefinition.getChild("head").getChild("HornR").getChild("HornR2").addChild("HornR3", ModelPartBuilder.create().uv(24, 44).mirrored().cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 4), ModelTransform.of(0.0F, -0.1F, 4.3F, 0.5918411493512771F, 0.0F, 0.0F));
        partdefinition.getChild("head").getChild("HornL").getChild("HornL2").addChild("HornL3", ModelPartBuilder.create().uv(24, 44).cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 4), ModelTransform.of(0.0F, -0.1F, 4.3F, 0.5918411493512771F, 0.0F, 0.0F));

        partdefinition.getChild("head").getChild("HornR4").addChild("HornR5", ModelPartBuilder.create().uv(25, 45).mirrored().cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 3), ModelTransform.of(0.0F, -0.1F, 4.3F, 0.3649483465920143F, 0.0F, 0.0F));
        partdefinition.getChild("head").getChild("HornL4").addChild("HornL5", ModelPartBuilder.create().uv(25, 45).cuboid(-1.0F, -0.8F, 0.0F, 2, 2, 3), ModelTransform.of(0.0F, -0.1F, 4.3F, 0.3649483465920143F, 0.0F, 0.0F));
        return meshdefinition;
    }

    public static ModelPart getBakedModel(boolean inner) {
        return inner ? INNER_MODEL : OUTER_MODEL;
    }

}
