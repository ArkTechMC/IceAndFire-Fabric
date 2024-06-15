package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.ModelDeathWormGauntlet;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDeathWorm;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class RenderDeathWormGauntlet extends BuiltinModelItemRenderer {
    private static final ModelDeathWormGauntlet MODEL = new ModelDeathWormGauntlet();

    public RenderDeathWormGauntlet(BlockEntityRenderDispatcher p_172550_, EntityModelLoader p_172551_) {
        super(p_172550_, p_172551_);
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode type, MatrixStack stackIn, VertexConsumerProvider bufferIn, int combinedLightIn, int combinedOverlayIn) {
        RenderLayer texture;

        if (stack.getItem() == IafItemRegistry.DEATHWORM_GAUNTLET_RED.get()) {
            texture = RenderLayer.getEntityCutout(RenderDeathWorm.TEXTURE_RED);
        } else if (stack.getItem() == IafItemRegistry.DEATHWORM_GAUNTLET_WHITE.get()) {
            texture = RenderLayer.getEntityCutout(RenderDeathWorm.TEXTURE_WHITE);
        } else {
            texture = RenderLayer.getEntityCutout(RenderDeathWorm.TEXTURE_YELLOW);
        }
        stackIn.push();
        stackIn.translate(0.5F, 0.5F, 0.5F);
        stackIn.push();
        stackIn.push();
        MODEL.animate(stack, MinecraftClient.getInstance().getTickDelta());
        MODEL.render(stackIn, bufferIn.getBuffer(texture), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        stackIn.pop();
        stackIn.pop();
        stackIn.pop();
    }
}
