package com.iafenvoy.iceandfire.compat.jade;

import com.iafenvoy.iceandfire.entity.*;
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
        registration.registerEntityComponent(MultipartComponentProvider.INSTANCE, EntityMutlipartPart.class);
    }
}
