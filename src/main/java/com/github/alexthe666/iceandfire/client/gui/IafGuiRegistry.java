package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.inventory.IafContainerRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class IafGuiRegistry {

    public static void register() {
        HandledScreens.register(IafContainerRegistry.IAF_LECTERN_CONTAINER.get(), GuiLectern::new);
        HandledScreens.register(IafContainerRegistry.PODIUM_CONTAINER.get(), GuiPodium::new);
        HandledScreens.register(IafContainerRegistry.DRAGON_CONTAINER.get(), GuiDragon::new);
        HandledScreens.register(IafContainerRegistry.HIPPOGRYPH_CONTAINER.get(), GuiHippogryph::new);
        HandledScreens.register(IafContainerRegistry.HIPPOCAMPUS_CONTAINER.get(), GuiHippocampus::new);
        HandledScreens.register(IafContainerRegistry.DRAGON_FORGE_CONTAINER.get(), GuiDragonForge::new);
    }
}
