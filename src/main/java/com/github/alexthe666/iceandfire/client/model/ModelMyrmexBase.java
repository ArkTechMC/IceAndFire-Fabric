package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.client.util.math.MatrixStack;

public abstract class ModelMyrmexBase<T extends EntityMyrmexBase> extends ModelDragonBase<T> {
    private static final ModelMyrmexLarva LARVA_MODEL = new ModelMyrmexLarva();
    private static final ModelMyrmexPupa PUPA_MODEL = new ModelMyrmexPupa();

    public void postRenderArm(float scale, MatrixStack stackIn) {
        for (BasicModelPart renderer : this.getHeadParts())
            renderer.translateRotate(stackIn);
    }

    public abstract BasicModelPart[] getHeadParts();
}
