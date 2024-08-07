package com.iafenvoy.iceandfire.render.block;

import com.iafenvoy.iceandfire.enums.TrollType;
import com.iafenvoy.iceandfire.item.tool.ItemTrollWeapon;
import com.iafenvoy.iceandfire.render.model.ModelTrollWeapon;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class RenderTrollWeapon extends BuiltinModelItemRenderer {
    private static final ModelTrollWeapon MODEL = new ModelTrollWeapon();

    public RenderTrollWeapon(BlockEntityRenderDispatcher dispatcher, EntityModelLoader set) {
        super(dispatcher, set);
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode type, MatrixStack stackIn, VertexConsumerProvider bufferIn, int combinedLightIn, int combinedOverlayIn) {
        TrollType.ITrollWeapon weapon = TrollType.BuiltinWeapon.AXE;
        if (stack.getItem() instanceof ItemTrollWeapon trollWeapon)
            weapon = trollWeapon.weapon;
        stackIn.push();
        stackIn.translate(0.5F, -0.75F, 0.5F);
        MODEL.render(stackIn, bufferIn.getBuffer(RenderLayer.getEntityCutout(weapon.getTexture())), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        stackIn.pop();
    }
}
