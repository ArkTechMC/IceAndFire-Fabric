package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.ModelDreadKnight;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.entity.EntityDreadKnight;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RenderDreadKnight extends MobEntityRenderer<EntityDreadKnight, ModelDreadKnight> {
    public static final Identifier TEXTURE_EYES = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_knight_eyes.png");
    public static final Identifier TEXTURE_0 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_knight_1.png");
    public static final Identifier TEXTURE_1 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_knight_2.png");
    public static final Identifier TEXTURE_2 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_knight_3.png");

    public RenderDreadKnight(EntityRendererFactory.Context context) {
        super(context, new ModelDreadKnight(0.0F), 0.6F);
        this.addFeature(new LayerGenericGlowing<>(this, TEXTURE_EYES));
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
    }

    @Override
    protected void scale(EntityDreadKnight entity, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.95F, 0.95F, 0.95F);
    }

    @Override
    public Identifier getTexture(EntityDreadKnight entity) {
        return switch (entity.getArmorVariant()) {
            case 1 -> TEXTURE_1;
            case 2 -> TEXTURE_2;
            default -> TEXTURE_0;
        };
    }
}
