package com.github.alexthe666.iceandfire.client;

import com.github.alexthe666.iceandfire.CommonProxy;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonArmor;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;

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
