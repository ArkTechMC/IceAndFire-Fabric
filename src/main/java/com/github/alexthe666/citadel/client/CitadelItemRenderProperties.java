package com.github.alexthe666.citadel.client;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class CitadelItemRenderProperties implements IClientItemExtensions {

    private final BuiltinModelItemRenderer renderer = new CitadelItemstackRenderer();

    @Override
    public BuiltinModelItemRenderer getCustomRenderer() {
        return renderer;
    }
}
