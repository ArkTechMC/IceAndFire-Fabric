package com.iafenvoy.iceandfire;

import com.iafenvoy.iceandfire.entity.util.MyrmexHive;
import com.iafenvoy.iceandfire.render.entity.layer.LayerDragonArmor;

public class ClientProxy extends CommonProxy {
    private static MyrmexHive referedClientHive = null;

    public static MyrmexHive getReferedClientHive() {
        return referedClientHive;
    }

    @Override
    public void setReferencedHive(MyrmexHive hive) {
        referedClientHive = hive;
    }

    @Override
    public void updateDragonArmorRender(String clear) {
        LayerDragonArmor.clearCache(clear);
    }
}
