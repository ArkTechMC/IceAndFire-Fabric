package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelTideTrident;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;

public class RenderTideTridentItem extends BuiltinModelItemRenderer {
    private static final ModelTideTrident MODEL = new ModelTideTrident();

    public RenderTideTridentItem(BlockEntityRenderDispatcher p_172550_, EntityModelLoader p_172551_) {
        super(p_172550_, p_172551_);
    }

    @Override
    public void render(@NotNull ItemStack stack, @NotNull ModelTransformationMode type, MatrixStack stackIn, @NotNull VertexConsumerProvider bufferIn, int combinedLightIn, int combinedOverlayIn) {
        stackIn.translate(0.5F, 0.5f, 0.5f);
        if (type == ModelTransformationMode.GUI || type == ModelTransformationMode.FIXED || type == ModelTransformationMode.NONE || type == ModelTransformationMode.GROUND) {
            ItemStack tridentInventory = new ItemStack(IafItemRegistry.TIDE_TRIDENT_INVENTORY.get());
            if (stack.hasEnchantments()) {
                NbtList enchantments = stack.getNbt().getList("Enchantments", 10);
                tridentInventory.setSubNbt("Enchantments", enchantments);
            }
            MinecraftClient.getInstance().getItemRenderer().renderItem(tridentInventory, type, type == ModelTransformationMode.GROUND ? combinedLightIn : 240, combinedOverlayIn, stackIn, bufferIn, MinecraftClient.getInstance().world, 0);
        } else {
            stackIn.push();
            stackIn.translate(0, 0.2F, -0.15F);
            if (type.isFirstPerson()) {
                stackIn.translate(type == ModelTransformationMode.FIRST_PERSON_LEFT_HAND ? -0.3F : 0.3F, 0.2F, -0.2F);
            } else {
                stackIn.translate(0, 0.6F, 0.0F);
            }
            stackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(160.0F));
            VertexConsumer glintVertexBuilder = ItemRenderer.getDirectItemGlintConsumer(bufferIn, RenderLayer.getEntityCutoutNoCull(RenderTideTrident.TRIDENT), false, stack.hasGlint());
            MODEL.render(stackIn,
                    glintVertexBuilder, combinedLightIn,
                    combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            stackIn.pop();
        }

    }

}
