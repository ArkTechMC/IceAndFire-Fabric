package com.iafenvoy.iceandfire.render;

import com.iafenvoy.citadel.client.model.AdvancedModelBox;
import com.iafenvoy.citadel.client.model.ITabulaModelAnimator;
import com.iafenvoy.citadel.client.model.TabulaModel;
import com.iafenvoy.citadel.client.model.tabula.TabulaModelContainer;

import java.util.List;

public class TabulaModelAccessor extends TabulaModel {
    public TabulaModelAccessor(TabulaModelContainer container, ITabulaModelAnimator tabulaAnimator) {
        super(container, tabulaAnimator);
    }

    public TabulaModelAccessor(TabulaModelContainer container) {
        super(container);
    }

    public List<AdvancedModelBox> getRootBox() {
        return super.rootBoxes;
    }
}
