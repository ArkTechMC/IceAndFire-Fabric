package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.screen.gui.*;
import com.iafenvoy.iceandfire.screen.gui.bestiary.BestiaryScreen;
import com.iafenvoy.iceandfire.screen.handler.*;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class IafScreenHandlers {
    public static final ScreenHandlerType<DragonScreenHandler> DRAGON_SCREEN = register("dragon", new ScreenHandlerType<>(DragonScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    public static final ScreenHandlerType<HippogryphScreenHandler> HIPPOGRYPH_SCREEN = register("hippogryph", new ScreenHandlerType<>(HippogryphScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    public static final ScreenHandlerType<HippocampusScreenHandler> HIPPOCAMPUS_SCREEN = register("hippocampus", new ScreenHandlerType<>(HippocampusScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    public static final ScreenHandlerType<DragonForgeScreenHandler> DRAGON_FORGE_SCREEN = register("dragon_forge", new ScreenHandlerType<>(DragonForgeScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    public static final ScreenHandlerType<PodiumScreenHandler> PODIUM_SCREEN = register("podium", new ScreenHandlerType<>(PodiumScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    public static final ScreenHandlerType<LecternScreenHandler> IAF_LECTERN_SCREEN = register("iaf_lectern", new ScreenHandlerType<>(LecternScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    public static final ScreenHandlerType<BestiaryScreenHandler> BESTIARY_SCREEN = register("bestiary", new ExtendedScreenHandlerType<>(BestiaryScreenHandler::new));

    private static <C extends ScreenHandler> ScreenHandlerType<C> register(String name, ScreenHandlerType<C> type) {
        return Registry.register(Registries.SCREEN_HANDLER, new Identifier(IceAndFire.MOD_ID, name), type);
    }

    public static void init() {
    }

    public static void registerGui() {
        HandledScreens.register(IafScreenHandlers.IAF_LECTERN_SCREEN, LecternScreen::new);
        HandledScreens.register(IafScreenHandlers.PODIUM_SCREEN, PodiumScreen::new);
        HandledScreens.register(IafScreenHandlers.DRAGON_SCREEN, DragonScreen::new);
        HandledScreens.register(IafScreenHandlers.HIPPOGRYPH_SCREEN, HippogryphScreen::new);
        HandledScreens.register(IafScreenHandlers.HIPPOCAMPUS_SCREEN, HippocampusScreen::new);
        HandledScreens.register(IafScreenHandlers.DRAGON_FORGE_SCREEN, DragonForgeScreen::new);
        HandledScreens.register(IafScreenHandlers.BESTIARY_SCREEN, BestiaryScreen::new);
    }
}
