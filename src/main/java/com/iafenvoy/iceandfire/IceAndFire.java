package com.iafenvoy.iceandfire;

import com.iafenvoy.iceandfire.event.AttackEntityEvent;
import com.iafenvoy.iceandfire.event.EventBus;
import com.iafenvoy.iceandfire.event.ProjectileImpactEvent;
import com.iafenvoy.iceandfire.event.ServerEvents;
import com.iafenvoy.iceandfire.network.IafServerNetworkHandler;
import com.iafenvoy.iceandfire.registry.*;
import io.github.fabricators_of_create.porting_lib.entity.events.EntityEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.PlayerEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingDamageEvent;
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
    public static final CommonProxy PROXY = new CommonProxy();
    public static final String VERSION;

    static {
        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer(IceAndFire.MOD_ID);
        if (container.isPresent()) VERSION = container.get().getMetadata().getVersion().getFriendlyString();
        else VERSION = "Unknown";
    }

    @Override
    public void onInitialize() {
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
        IafScreenHandlers.init();
        IafRecipeSerializers.init();
        IafProcessors.init();
        IafItemGroups.init();
        IafParticles.init();

        EventBus.register(ProjectileImpactEvent.class, ServerEvents::onArrowCollide);
        EventBus.register(AttackEntityEvent.class, ServerEvents::onPlayerAttackMob);
        PlayerBlockBreakEvents.AFTER.register(ServerEvents::onBreakBlock);
        UseEntityCallback.EVENT.register(ServerEvents::onEntityInteract);
        UseItemCallback.EVENT.register(ServerEvents::onEntityUseItem);
        UseBlockCallback.EVENT.register(ServerEvents::onPlayerRightClick);
        ServerLivingEntityEvents.AFTER_DEATH.register(ServerEvents::onEntityDie);
        AttackEntityCallback.EVENT.register(ServerEvents::onPlayerAttack);
        PlayerEvents.LOGGED_OUT.register(ServerEvents::onPlayerLeaveEvent);
        EntityEvents.ON_JOIN_WORLD.register(ServerEvents::onEntityJoinWorld);
        EntityEvents.START_TRACKING_TAIL.register(ServerEvents::onLivingSetTarget);
        EventBus.register(AttackEntityEvent.class, ServerEvents::onLivingAttacked);
        LivingDamageEvent.DAMAGE.register(ServerEvents::onEntityDamage);
        LivingEntityEvents.DROPS.register(ServerEvents::onEntityDrop);
        LivingEntityEvents.FALL.register(ServerEvents::onEntityFall);

        IafServerNetworkHandler.register();
    }
}