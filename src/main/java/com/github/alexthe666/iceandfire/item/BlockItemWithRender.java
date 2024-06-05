package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.citadel.client.CitadelItemRenderProperties;
import com.github.alexthe666.iceandfire.client.render.tile.IceAndFireTEISR;
import io.github.fabricators_of_create.porting_lib.common.util.NonNullSupplier;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.BlockItem;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class BlockItemWithRender extends BlockItem {
    public BlockItemWithRender(Block p_40565_, Settings p_40566_) {
        super(p_40565_, p_40566_);
    }

    @Override
    public void initializeClient(Consumer<CitadelItemRenderProperties> consumer) {
        consumer.accept(new CitadelItemRenderProperties() {
            static final NonNullSupplier<BuiltinModelItemRenderer> renderer = () -> new IceAndFireTEISR(MinecraftClient.getInstance().getBlockEntityRenderDispatcher(), MinecraftClient.getInstance().getEntityModelLoader());

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return renderer.get();
            }
        });
    }

}
