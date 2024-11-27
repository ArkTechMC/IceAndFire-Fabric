package com.iafenvoy.iceandfire.render.block;

import com.iafenvoy.iceandfire.entity.block.BlockEntityDreadPortal;
import com.iafenvoy.iceandfire.entity.block.BlockEntityGhostChest;
import com.iafenvoy.iceandfire.item.block.BlockPixieHouse;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class IceAndFireTEISR extends BuiltinModelItemRenderer {
    private final RenderPixieHouse<?> PIXIE_HOUSE_RENDERER;
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    private final BlockEntityGhostChest chest = new BlockEntityGhostChest(BlockPos.ORIGIN, IafBlocks.GHOST_CHEST.getDefaultState());
    private final BlockEntityDreadPortal portal = new BlockEntityDreadPortal(BlockPos.ORIGIN, IafBlocks.DREAD_PORTAL.getDefaultState());

    public IceAndFireTEISR(BlockEntityRenderDispatcher dispatcher, EntityModelLoader modelSet) {
        super(dispatcher, modelSet);
        this.blockEntityRenderDispatcher = dispatcher;
        this.PIXIE_HOUSE_RENDERER = new RenderPixieHouse<>(null);
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode type, MatrixStack stackIn, VertexConsumerProvider bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (stack.getItem() == IafBlocks.GHOST_CHEST.asItem())
            this.blockEntityRenderDispatcher.renderEntity(this.chest, stackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        if (stack.getItem() instanceof BlockItem blockItem)
            blockItem.getBlock();
        if (stack.getItem() == IafBlocks.DREAD_PORTAL.asItem())
            this.blockEntityRenderDispatcher.renderEntity(this.portal, stackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof BlockPixieHouse) {
            this.PIXIE_HOUSE_RENDERER.metaOverride = (BlockItem) stack.getItem();
            this.PIXIE_HOUSE_RENDERER.render(null, 0, stackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
    }
}
