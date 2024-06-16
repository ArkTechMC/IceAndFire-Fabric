package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.ModelGorgonHead;
import com.github.alexthe666.iceandfire.client.model.ModelGorgonHeadActive;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class RenderGorgonHead extends BuiltinModelItemRenderer {

    private static final RenderLayer ACTIVE_TEXTURE = RenderLayer.getEntityCutoutNoCull(new Identifier(IceAndFire.MOD_ID, "textures/models/gorgon/head_active.png"), false);
    private static final RenderLayer INACTIVE_TEXTURE = RenderLayer.getEntityCutoutNoCull(new Identifier(IceAndFire.MOD_ID, "textures/models/gorgon/head_inactive.png"), false);
    private static final AdvancedEntityModel ACTIVE_MODEL = new ModelGorgonHeadActive();
    private static final AdvancedEntityModel<Entity> INACTIVE_MODEL = new ModelGorgonHead();

    public RenderGorgonHead(BlockEntityRenderDispatcher dispatcher, EntityModelLoader set) {
        super(dispatcher, set);
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode type, MatrixStack stackIn, VertexConsumerProvider bufferIn, int combinedLightIn, int combinedOverlayIn) {
        boolean active = false;
        if (stack.getItem() == IafItemRegistry.GORGON_HEAD) {
            if (stack.getNbt() != null) {
                if (stack.getNbt().getBoolean("Active"))
                    active = true;
            }
        }
        AdvancedEntityModel model = active ? ACTIVE_MODEL : INACTIVE_MODEL;
        stackIn.push();
        stackIn.translate(0.5F, active ? 1.5F : 1.25F, 0.5F);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(active ? ACTIVE_TEXTURE : INACTIVE_TEXTURE);
        model.render(stackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        stackIn.pop();
    }

}
