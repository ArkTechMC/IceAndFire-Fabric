package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.entity.EntityMyrmexBase;

public class MyrmexQueenAIWander extends MyrmexAIWander {
    public MyrmexQueenAIWander(EntityMyrmexBase myrmex, double speed) {
        super(myrmex, speed);
    }

    @Override
    public boolean canStart() {
        return (this.myrmex.canSeeSky() || this.myrmex.getHive() == null) && super.canStart();
    }
}
