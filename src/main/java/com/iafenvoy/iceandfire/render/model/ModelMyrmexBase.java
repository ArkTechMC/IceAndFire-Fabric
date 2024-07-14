package com.iafenvoy.iceandfire.render.model;

import com.iafenvoy.iceandfire.entity.EntityMyrmexBase;
import com.iafenvoy.uranus.client.model.basic.BasicModelPart;
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
