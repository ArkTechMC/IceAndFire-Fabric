package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.citadel.client.CitadelItemRenderProperties;
import com.github.alexthe666.iceandfire.client.render.tile.IceAndFireTEISR;
import net.minecraft.block.Block;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.BlockItem;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;

import java.util.function.Consumer;

public class BlockItemWithRender extends BlockItem {
    public BlockItemWithRender(Block p_40565_, Settings p_40566_) {
        super(p_40565_, p_40566_);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new CitadelItemRenderProperties() {
            static final NonNullLazy<BuiltinModelItemRenderer> renderer = NonNullLazy.of(() -> new IceAndFireTEISR(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return renderer.get();
            }
        });
    }

}
