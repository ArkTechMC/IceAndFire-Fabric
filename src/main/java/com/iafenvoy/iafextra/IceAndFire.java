package com.iafenvoy.iafextra;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.entity.IafVillagerRegistry;
import com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry;
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
import com.iafenvoy.iafextra.network.IafServerNetworkHandler;
import net.fabricmc.api.ModInitializer;

public class IceAndFire implements ModInitializer {
    @Override
    public void onInitialize() {
        IafItemRegistry.ITEMS.register();
        IafBlockRegistry.BLOCKS.register();
        IafTabRegistry.TAB_REGISTER.register();
        IafEntityRegistry.ENTITIES.register();
        IafTileEntityRegistry.TYPES.register();
        IafPlacementFilterRegistry.PLACEMENT_MODIFIER_TYPES.register();
        IafWorldRegistry.FEATURES.register();
        IafRecipeRegistry.RECIPE_TYPE.register();
        IafBannerPatterns.BANNERS.register();
        IafStructureTypes.STRUCTURE_TYPES.register();
        IafContainerRegistry.CONTAINERS.register();
        IafRecipeSerializers.SERIALIZERS.register();
        IafProcessors.PROCESSORS.register();
        IafLootRegistry.init();

        IafVillagerRegistry.POI_TYPES.register();
        IafVillagerRegistry.PROFESSIONS.register();

        IafRecipeRegistry.registerDispenser();
        IafItemRegistry.registerItems();
        IafItemRegistry.setRepairMaterials();
        IafEntityRegistry.bakeAttributes();
        IafEntityRegistry.commonSetup();
        IafEntityRegistry.addSpawners();
        IafSoundRegistry.registerSoundEvents();
        EventRegistration.register();

        IafServerNetworkHandler.register();
    }
}
