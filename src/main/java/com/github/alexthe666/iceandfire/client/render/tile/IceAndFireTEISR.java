package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.block.BlockPixieHouse;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDreadPortal;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityGhostChest;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class IceAndFireTEISR extends BuiltinModelItemRenderer {

    private final RenderPixieHouse PIXIE_HOUSE_RENDERER;
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    private final EntityModelLoader entityModelSet;
    private final TileEntityGhostChest chest = new TileEntityGhostChest(BlockPos.ORIGIN, IafBlockRegistry.GHOST_CHEST.get().getDefaultState());
    private final TileEntityDreadPortal portal = new TileEntityDreadPortal(BlockPos.ORIGIN, IafBlockRegistry.DREAD_PORTAL.get().getDefaultState());

    public IceAndFireTEISR() {
        this(MinecraftClient.getInstance().getBlockEntityRenderDispatcher(), MinecraftClient.getInstance().getEntityModelLoader());
    }

    public IceAndFireTEISR(BlockEntityRenderDispatcher dispatcher, EntityModelLoader modelSet) {
        super(dispatcher, modelSet);
        this.blockEntityRenderDispatcher = dispatcher;
        this.entityModelSet = modelSet;
        PIXIE_HOUSE_RENDERER = new RenderPixieHouse(null);

    }

    @Override
    public void render(ItemStack stack, @NotNull ModelTransformationMode type, @NotNull MatrixStack stackIn, @NotNull VertexConsumerProvider bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (stack.getItem() == IafBlockRegistry.GHOST_CHEST.get().asItem()) {
            blockEntityRenderDispatcher.renderEntity(chest, stackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() == IafBlockRegistry.DREAD_PORTAL.get()) {
            blockEntityRenderDispatcher.renderEntity(portal, stackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof BlockPixieHouse) {
            PIXIE_HOUSE_RENDERER.metaOverride = (BlockItem) stack.getItem();
            PIXIE_HOUSE_RENDERER.render(null, 0, stackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }

    }
}
