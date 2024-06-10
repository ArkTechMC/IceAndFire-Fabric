package com.github.alexthe666.citadel.item;

import dev.arktechmc.iafextra.render.TEISRItemRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;

public class ItemCustomRender extends Item {
    public ItemCustomRender(Settings props) {
        super(props);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            BuiltinItemRendererRegistry.INSTANCE.register(this, new TEISRItemRenderer());
    }
}
