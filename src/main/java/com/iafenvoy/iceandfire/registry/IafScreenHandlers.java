package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.screen.gui.*;
import com.iafenvoy.iceandfire.screen.handler.*;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class IafScreenHandlers {
    private static <C extends ScreenHandler> ScreenHandlerType<C> register(String name, ScreenHandlerType<C> type) {
        return Registry.register(Registries.SCREEN_HANDLER, new Identifier(IceAndFire.MOD_ID, name), type);
    }    public static final ScreenHandlerType<PodiumScreenHandler> PODIUM_CONTAINER = register("podium", new ScreenHandlerType<>(PodiumScreenHandler::new, FeatureFlags.VANILLA_FEATURES));

    public static void init() {
    }    public static final ScreenHandlerType<LecternScreenHandler> IAF_LECTERN_CONTAINER = register("iaf_lectern", new ScreenHandlerType<>(LecternScreenHandler::new, FeatureFlags.VANILLA_FEATURES));

    public static void registerGui() {
        HandledScreens.register(IafScreenHandlers.IAF_LECTERN_CONTAINER, LecternScreen::new);
        HandledScreens.register(IafScreenHandlers.PODIUM_CONTAINER, PodiumScreen::new);
        HandledScreens.register(IafScreenHandlers.DRAGON_CONTAINER, DragonScreen::new);
        HandledScreens.register(IafScreenHandlers.HIPPOGRYPH_CONTAINER, HippogryphScreen::new);
        HandledScreens.register(IafScreenHandlers.HIPPOCAMPUS_CONTAINER, HippocampusScreen::new);
        HandledScreens.register(IafScreenHandlers.DRAGON_FORGE_CONTAINER, DragonForgeScreen::new);
    }    public static final ScreenHandlerType<DragonScreenHandler> DRAGON_CONTAINER = register("dragon", new ScreenHandlerType<>(DragonScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    public static final ScreenHandlerType<HippogryphScreenHandler> HIPPOGRYPH_CONTAINER = register("hippogryph", new ScreenHandlerType<>(HippogryphScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    public static final ScreenHandlerType<HippocampusContainerMenu> HIPPOCAMPUS_CONTAINER = register("hippocampus", new ScreenHandlerType<>(HippocampusContainerMenu::new, FeatureFlags.VANILLA_FEATURES));
    public static final ScreenHandlerType<DragonForgeScreenHandler> DRAGON_FORGE_CONTAINER = register("dragon_forge", new ScreenHandlerType<>(DragonForgeScreenHandler::new, FeatureFlags.VANILLA_FEATURES));






}
