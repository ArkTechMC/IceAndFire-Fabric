package com.github.alexthe666.iceandfire.compat.jade;

import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.EntityLightningDragon;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class IceAndFireJadePlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(DragonComponentProvider.INSTANCE, EntityFireDragon.class);
        registration.registerEntityComponent(DragonComponentProvider.INSTANCE, EntityIceDragon.class);
        registration.registerEntityComponent(DragonComponentProvider.INSTANCE, EntityLightningDragon.class);
    }
}
