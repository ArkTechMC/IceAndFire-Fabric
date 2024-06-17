package com.iafenvoy.iceandfire.client.render.entity;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.client.model.ModelStymphalianBird;
import com.iafenvoy.iceandfire.entity.EntityStymphalianBird;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RenderStymphalianBird extends MobEntityRenderer<EntityStymphalianBird, ModelStymphalianBird> {
    public static final Identifier TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/stymphalianbird/stymphalian_bird.png");

    public RenderStymphalianBird(EntityRendererFactory.Context context) {
        super(context, new ModelStymphalianBird(), 0.6F);
    }

    @Override
    public void scale(EntityStymphalianBird LivingEntityIn, MatrixStack stack, float partialTickTime) {
        stack.scale(0.75F, 0.75F, 0.75F);
    }

    @Override
    public Identifier getTexture(EntityStymphalianBird cyclops) {
        return TEXTURE;
    }
}
