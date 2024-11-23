package com.iafenvoy.iceandfire;

import com.iafenvoy.iceandfire.config.IafClientConfig;
import com.iafenvoy.iceandfire.event.ClientEvents;
import com.iafenvoy.iceandfire.network.ClientNetworkHelper;
import com.iafenvoy.iceandfire.registry.*;
import com.iafenvoy.iceandfire.render.model.util.DragonAnimationsLibrary;
import com.iafenvoy.iceandfire.render.model.util.EnumDragonModelTypes;
import com.iafenvoy.iceandfire.render.model.util.EnumDragonPoses;
import com.iafenvoy.iceandfire.render.model.util.EnumSeaSerpentAnimations;
import com.iafenvoy.jupiter.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;

@Environment(EnvType.CLIENT)
public class IceAndFireClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ConfigManager.getInstance().registerConfigHandler(IafClientConfig.INSTANCE);

        IafScreenHandlers.registerGui();
        EnumSeaSerpentAnimations.initializeSerpentModels();
        DragonAnimationsLibrary.register(EnumDragonPoses.values(), EnumDragonModelTypes.values());
        IafRenderers.registerRenderLayers();
        IafRenderers.registerModelPredicates();
        IafKeybindings.init();
        IafRenderers.registerEntityRenderers();
        IafRenderers.registerBlockEntityRenderers();
        IafRenderers.registerParticleRenderers();
        IafRenderers.registerArmorRenderers();
        IafRenderers.registerItemRenderers();

        UseEntityCallback.EVENT.register(ClientEvents::onEntityInteract);

        ClientNetworkHelper.registerReceivers();
    }
}
