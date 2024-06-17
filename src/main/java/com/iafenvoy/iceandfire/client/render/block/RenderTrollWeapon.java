package com.iafenvoy.iceandfire.client.render.block;

import com.iafenvoy.iceandfire.client.model.ModelTrollWeapon;
import com.iafenvoy.iceandfire.enums.EnumTroll;
import com.iafenvoy.iceandfire.item.tool.ItemTrollWeapon;
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
        EnumTroll.Weapon weapon = EnumTroll.Weapon.AXE;
        if (stack.getItem() instanceof ItemTrollWeapon)
            weapon = ((ItemTrollWeapon) stack.getItem()).weapon;
        stackIn.push();
        stackIn.translate(0.5F, -0.75F, 0.5F);
        MODEL.render(stackIn, bufferIn.getBuffer(RenderLayer.getEntityCutout(weapon.TEXTURE)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        stackIn.pop();
    }
}
