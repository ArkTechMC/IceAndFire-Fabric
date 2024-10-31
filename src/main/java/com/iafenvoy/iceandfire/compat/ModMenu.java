package com.iafenvoy.iceandfire.compat;

import com.iafenvoy.iceandfire.config.IafClientConfig;
import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.jupiter.render.screen.ConfigSelectScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigSelectScreen<>(Text.translatable("config.iceandfire.title"), parent, IafCommonConfig.INSTANCE, IafClientConfig.INSTANCE);
    }
}
