package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.ModelStymphalianBird;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class RenderStymphalianBird extends MobEntityRenderer<EntityStymphalianBird, ModelStymphalianBird> {

    public static final Identifier TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/stymphalianbird/stymphalian_bird.png");

    public RenderStymphalianBird(EntityRendererFactory.Context context) {
        super(context, new ModelStymphalianBird(), 0.6F);
    }

    @Override
    public void scale(@NotNull EntityStymphalianBird LivingEntityIn, MatrixStack stack, float partialTickTime) {
        stack.scale(0.75F, 0.75F, 0.75F);
    }

    @Override
    public @NotNull Identifier getTexture(@NotNull EntityStymphalianBird cyclops) {
        return TEXTURE;
    }

}
