package com.iafenvoy.iceandfire;

import com.iafenvoy.iceandfire.config.IafConfig;
import com.iafenvoy.iceandfire.event.ServerEvents;
import com.iafenvoy.iceandfire.network.ServerNetworkHelper;
import com.iafenvoy.iceandfire.registry.*;
import com.iafenvoy.uranus.event.EntityEvents;
import com.iafenvoy.uranus.event.LivingEntityEvents;
import com.iafenvoy.uranus.event.PlayerEvents;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class IceAndFire implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "iceandfire";
    public static final String MOD_NAME = "Ice And Fire";
    public static final String VERSION;

    static {
        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer(IceAndFire.MOD_ID);
        if (container.isPresent()) VERSION = container.get().getMetadata().getVersion().getFriendlyString();
        else VERSION = "Unknown";
    }

    @Override
    public void onInitialize() {
        AutoConfig.register(IafConfig.class, GsonConfigSerializer::new);

        IafItems.init();
        IafBlocks.init();
        IafEntities.init();
        IafSounds.init();
        IafTrades.init();
        IafRecipes.init();
        IafLoots.init();
        IafFeatures.init();
        IafBlockEntities.init();
        IafBannerPatterns.init();
        IafPlacementFilters.init();
        IafStructureTypes.init();
        IafStructures.init();
        IafScreenHandlers.init();
        IafRecipeSerializers.init();
        IafProcessors.init();
        IafItemGroups.init();
        IafParticles.init();

        PlayerBlockBreakEvents.AFTER.register(ServerEvents::onBreakBlock);
        UseEntityCallback.EVENT.register(ServerEvents::onEntityInteract);
        UseItemCallback.EVENT.register(ServerEvents::onEntityUseItem);
        UseBlockCallback.EVENT.register(ServerEvents::onPlayerRightClick);
        ServerLivingEntityEvents.AFTER_DEATH.register(ServerEvents::onEntityDie);
        AttackEntityCallback.EVENT.register(ServerEvents::onPlayerAttack);
        PlayerEvents.LOGGED_OUT.register(ServerEvents::onPlayerLeaveEvent);
        EntityEvents.ON_JOIN_WORLD.register(ServerEvents::onEntityJoinWorld);
        EntityEvents.START_TRACKING_TAIL.register(ServerEvents::onLivingSetTarget);
        LivingEntityEvents.DAMAGE.register(ServerEvents::onEntityDamage);
        LivingEntityEvents.FALL.register(ServerEvents::onEntityFall);

        ServerNetworkHelper.registerReceivers();
    }
}