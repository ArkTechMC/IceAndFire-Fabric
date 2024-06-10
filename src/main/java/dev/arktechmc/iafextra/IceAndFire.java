package dev.arktechmc.iafextra;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.CitadelEvents;
import com.github.alexthe666.citadel.server.block.LecternBooks;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
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
import dev.arktechmc.iafextra.network.IafServerNetworkHandler;
import io.github.fabricators_of_create.porting_lib.entity.events.EntityEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.PlayerEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingDamageEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IceAndFire implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LecternBooks.init();
        UseBlockCallback.EVENT.register(CitadelEvents::onRightClickBlock);
        ServerPlayerEvents.COPY_FROM.register(CitadelEvents::onPlayerClone);
        Citadel.ITEMS.register();
        Citadel.BLOCKS.register();
        Citadel.BLOCK_ENTITIES.register();

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

        IafItemRegistry.ITEMS.register();
        IafBlockRegistry.BLOCKS.register();
        IafTabRegistry.TAB_REGISTER.register();
        IafEntityRegistry.ENTITIES.register();
        IafTileEntityRegistry.TYPES.register();
        IafPlacementFilterRegistry.PLACEMENT_MODIFIER_TYPES.register();
        IafWorldRegistry.FEATURES.register();
        IafRecipeRegistry.init();
        IafBannerPatterns.BANNERS.register();
        IafStructureTypes.STRUCTURE_TYPES.register();
        IafContainerRegistry.CONTAINERS.register();
        IafRecipeSerializers.SERIALIZERS.register();
        IafProcessors.PROCESSORS.register();
        IafLootRegistry.init();

        IafVillagerRegistry.POI_TYPES.register();
        IafVillagerRegistry.PROFESSIONS.register();

//        BiomeModificationImpl.INSTANCE.addModifier(new Identifier(com.github.alexthe666.iceandfire.IceAndFire.MOD_ID, "biome"),
//                ModificationPhase.ADDITIONS, context -> true, (context, biomeModificationContext) -> {
//
//                    IafWorldRegistry.addFeatures(biome, this.featureMap);
//                });

        IafRecipeRegistry.registerDispenser();
        IafItemRegistry.registerItems();
        IafItemRegistry.setRepairMaterials();
        IafEntityRegistry.bakeAttributes();
        IafEntityRegistry.commonSetup();
        IafEntityRegistry.addSpawners();
        IafSoundRegistry.registerSoundEvents();
        EventRegistration.register();
        IafVillagerRegistry.addScribeTrades();

        IafServerNetworkHandler.register();
    }
}
