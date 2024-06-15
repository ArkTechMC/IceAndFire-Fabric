package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.ModelHydraBody;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerHydraHead;
import com.github.alexthe666.iceandfire.entity.EntityHydra;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RenderHydra extends MobEntityRenderer<EntityHydra, ModelHydraBody> {

    public static final Identifier TEXUTURE_0 = new Identifier(IceAndFire.MOD_ID, "textures/models/hydra/hydra_0.png");
    public static final Identifier TEXUTURE_1 = new Identifier(IceAndFire.MOD_ID, "textures/models/hydra/hydra_1.png");
    public static final Identifier TEXUTURE_2 = new Identifier(IceAndFire.MOD_ID, "textures/models/hydra/hydra_2.png");
    public static final Identifier TEXUTURE_EYES = new Identifier(IceAndFire.MOD_ID, "textures/models/hydra/hydra_eyes.png");

    public RenderHydra(EntityRendererFactory.Context context) {
        super(context, new ModelHydraBody(), 1.2F);
        this.addFeature(new LayerHydraHead(this));
        this.addFeature(new LayerGenericGlowing<>(this, TEXUTURE_EYES));
    }

    @Override
    public void scale(EntityHydra LivingEntityIn, MatrixStack stack, float partialTickTime) {
        stack.scale(1.75F, 1.75F, 1.75F);
    }

    @Override
    public Identifier getTexture(EntityHydra gorgon) {
        return switch (gorgon.getVariant()) {
            default -> TEXUTURE_0;
            case 1 -> TEXUTURE_1;
            case 2 -> TEXUTURE_2;
        };
    }

}
