package com.github.alexthe666.iceandfire.item;

import dev.arktechmc.iafextra.render.TEISRItemRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class BlockItemWithRender extends BlockItem {
    public BlockItemWithRender(Block p_40565_, Settings p_40566_) {
        super(p_40565_, p_40566_);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            BuiltinItemRendererRegistry.INSTANCE.register(this, new TEISRItemRenderer());
    }
}
