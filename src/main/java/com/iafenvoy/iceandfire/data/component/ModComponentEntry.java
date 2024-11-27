package com.iafenvoy.iceandfire.data.component;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.LivingEntity;

public class ModComponentEntry implements EntityComponentInitializer {
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(LivingEntity.class, EntityDataComponent.COMPONENT, EntityDataComponent::new);
        registry.registerForPlayers(PortalDataComponent.COMPONENT, PortalDataComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
