package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.client.gui.*;
import com.iafenvoy.iceandfire.inventory.*;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class IafScreenHandlers {
    public static <C extends ScreenHandler> ScreenHandlerType<C> register(String name, ScreenHandlerType<C> type) {
        return Registry.register(Registries.SCREEN_HANDLER, new Identifier(IceAndFire.MOD_ID, name), type);
    }    public static final ScreenHandlerType<ContainerLectern> IAF_LECTERN_CONTAINER = register("iaf_lectern", new ScreenHandlerType<>(ContainerLectern::new, FeatureFlags.VANILLA_FEATURES));

    public static void init() {
    }    public static final ScreenHandlerType<ContainerPodium> PODIUM_CONTAINER = register("podium", new ScreenHandlerType<>(ContainerPodium::new, FeatureFlags.VANILLA_FEATURES));

    public static void registerGui() {
        HandledScreens.register(IafScreenHandlers.IAF_LECTERN_CONTAINER, GuiLectern::new);
        HandledScreens.register(IafScreenHandlers.PODIUM_CONTAINER, GuiPodium::new);
        HandledScreens.register(IafScreenHandlers.DRAGON_CONTAINER, GuiDragon::new);
        HandledScreens.register(IafScreenHandlers.HIPPOGRYPH_CONTAINER, GuiHippogryph::new);
        HandledScreens.register(IafScreenHandlers.HIPPOCAMPUS_CONTAINER, GuiHippocampus::new);
        HandledScreens.register(IafScreenHandlers.DRAGON_FORGE_CONTAINER, GuiDragonForge::new);
    }    public static final ScreenHandlerType<ContainerDragon> DRAGON_CONTAINER = register("dragon", new ScreenHandlerType<>(ContainerDragon::new, FeatureFlags.VANILLA_FEATURES));
    public static final ScreenHandlerType<ContainerHippogryph> HIPPOGRYPH_CONTAINER = register("hippogryph", new ScreenHandlerType<>(ContainerHippogryph::new, FeatureFlags.VANILLA_FEATURES));
    public static final ScreenHandlerType<HippocampusContainerMenu> HIPPOCAMPUS_CONTAINER = register("hippocampus", new ScreenHandlerType<>(HippocampusContainerMenu::new, FeatureFlags.VANILLA_FEATURES));
    public static final ScreenHandlerType<ContainerDragonForge> DRAGON_FORGE_CONTAINER = register("dragon_forge", new ScreenHandlerType<>(ContainerDragonForge::new, FeatureFlags.VANILLA_FEATURES));






}
