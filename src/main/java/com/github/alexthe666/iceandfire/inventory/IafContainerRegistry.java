package com.github.alexthe666.iceandfire.inventory;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.gui.*;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class IafContainerRegistry {
    public static final ScreenHandlerType<ContainerLectern> IAF_LECTERN_CONTAINER = register("iaf_lectern", new ScreenHandlerType<>(ContainerLectern::new, FeatureFlags.VANILLA_FEATURES));
    public static final ScreenHandlerType<ContainerPodium> PODIUM_CONTAINER = register("podium", new ScreenHandlerType<>(ContainerPodium::new, FeatureFlags.VANILLA_FEATURES));
    public static final ScreenHandlerType<ContainerDragon> DRAGON_CONTAINER = register("dragon", new ScreenHandlerType<>(ContainerDragon::new, FeatureFlags.VANILLA_FEATURES));
    public static final ScreenHandlerType<ContainerHippogryph> HIPPOGRYPH_CONTAINER = register("hippogryph", new ScreenHandlerType<>(ContainerHippogryph::new, FeatureFlags.VANILLA_FEATURES));
    public static final ScreenHandlerType<HippocampusContainerMenu> HIPPOCAMPUS_CONTAINER = register("hippocampus", new ScreenHandlerType<>(HippocampusContainerMenu::new, FeatureFlags.VANILLA_FEATURES));
    public static final ScreenHandlerType<ContainerDragonForge> DRAGON_FORGE_CONTAINER = register("dragon_forge", new ScreenHandlerType<>(ContainerDragonForge::new, FeatureFlags.VANILLA_FEATURES));

    public static <C extends ScreenHandler> ScreenHandlerType<C> register(String name, ScreenHandlerType<C> type) {
        return Registry.register(Registries.SCREEN_HANDLER, new Identifier(IceAndFire.MOD_ID, name), type);
    }

    public static void init() {
    }

    public static void registerGui() {
        HandledScreens.register(IafContainerRegistry.IAF_LECTERN_CONTAINER, GuiLectern::new);
        HandledScreens.register(IafContainerRegistry.PODIUM_CONTAINER, GuiPodium::new);
        HandledScreens.register(IafContainerRegistry.DRAGON_CONTAINER, GuiDragon::new);
        HandledScreens.register(IafContainerRegistry.HIPPOGRYPH_CONTAINER, GuiHippogryph::new);
        HandledScreens.register(IafContainerRegistry.HIPPOCAMPUS_CONTAINER, GuiHippocampus::new);
        HandledScreens.register(IafContainerRegistry.DRAGON_FORGE_CONTAINER, GuiDragonForge::new);
    }
}
