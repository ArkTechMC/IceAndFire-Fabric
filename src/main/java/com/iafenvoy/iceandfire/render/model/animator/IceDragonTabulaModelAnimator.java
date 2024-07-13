package com.iafenvoy.iceandfire.render.model.animator;

import com.iafenvoy.uranus.client.model.TabulaModel;
import com.iafenvoy.iceandfire.entity.EntityIceDragon;
import com.iafenvoy.iceandfire.render.model.util.DragonAnimationsLibrary;
import com.iafenvoy.iceandfire.render.model.util.EnumDragonModelTypes;
import com.iafenvoy.iceandfire.render.model.util.EnumDragonPoses;

public class IceDragonTabulaModelAnimator extends DragonTabulaModelAnimator<EntityIceDragon> {
    public IceDragonTabulaModelAnimator() {
        super(DragonAnimationsLibrary.getModel(EnumDragonPoses.GROUND_POSE, EnumDragonModelTypes.ICE_DRAGON_MODEL));
        this.walkPoses = new TabulaModel[]{this.getModel(EnumDragonPoses.WALK1), this.getModel(EnumDragonPoses.WALK2), this.getModel(EnumDragonPoses.WALK3), this.getModel(EnumDragonPoses.WALK4)};
        this.flyPoses = new TabulaModel[]{this.getModel(EnumDragonPoses.FLIGHT1), this.getModel(EnumDragonPoses.FLIGHT2), this.getModel(EnumDragonPoses.FLIGHT3), this.getModel(EnumDragonPoses.FLIGHT4), this.getModel(EnumDragonPoses.FLIGHT5), this.getModel(EnumDragonPoses.FLIGHT6)};
        this.swimPoses = new TabulaModel[]{this.getModel(EnumDragonPoses.SWIM1), this.getModel(EnumDragonPoses.SWIM2), this.getModel(EnumDragonPoses.SWIM3), this.getModel(EnumDragonPoses.SWIM4), this.getModel(EnumDragonPoses.SWIM5)};
    }

    @Override
    protected TabulaModel getModel(EnumDragonPoses pose) {
        return DragonAnimationsLibrary.getModel(pose, EnumDragonModelTypes.ICE_DRAGON_MODEL);
    }
}
