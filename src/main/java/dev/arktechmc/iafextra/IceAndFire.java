package dev.arktechmc.iafextra;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.client.particle.IafParticleRegistry;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.entity.IafVillagerRegistry;
import com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.github.alexthe666.iceandfire.inventory.IafContainerRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.IafTabRegistry;
import com.github.alexthe666.iceandfire.loot.IafLootRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.recipe.IafBannerPatterns;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import com.github.alexthe666.iceandfire.recipe.IafRecipeSerializers;
import com.github.alexthe666.iceandfire.world.IafPlacementFilterRegistry;
import com.github.alexthe666.iceandfire.world.IafProcessors;
import com.github.alexthe666.iceandfire.world.IafStructureTypes;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import dev.arktechmc.iafextra.event.AttackEntityEvent;
import dev.arktechmc.iafextra.event.EventBus;
import dev.arktechmc.iafextra.event.ProjectileImpactEvent;
import dev.arktechmc.iafextra.network.IafServerNetworkHandler;
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
    public static final String VERSION;

    static {
        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer(com.github.alexthe666.iceandfire.IceAndFire.MOD_ID);
        if (container.isPresent()) VERSION = container.get().getMetadata().getVersion().getFriendlyString();
        else VERSION = "Unknown";
    }

    @Override
    public void onInitialize() {
        IafItemRegistry.init();
        IafBlockRegistry.init();
        IafEntityRegistry.init();
        IafSoundRegistry.init();
        IafVillagerRegistry.init();
        IafRecipeRegistry.init();
        IafLootRegistry.init();
        IafWorldRegistry.init();
        IafTileEntityRegistry.init();
        IafBannerPatterns.init();
        IafPlacementFilterRegistry.init();
        IafStructureTypes.init();
        IafContainerRegistry.init();
        IafRecipeSerializers.init();
        IafProcessors.init();
        IafTabRegistry.init();
        IafParticleRegistry.init();

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
