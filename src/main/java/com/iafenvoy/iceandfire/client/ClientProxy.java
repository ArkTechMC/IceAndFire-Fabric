package com.iafenvoy.iceandfire.client;

import com.iafenvoy.iceandfire.CommonProxy;
import com.iafenvoy.iceandfire.client.render.entity.layer.LayerDragonArmor;
import com.iafenvoy.iceandfire.entity.util.MyrmexHive;

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
