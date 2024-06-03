package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.ModelGorgon;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGorgonEyes;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class RenderGorgon extends MobEntityRenderer<EntityGorgon, ModelGorgon> {

    public static final Identifier PASSIVE_TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/gorgon/gorgon_passive.png");
    public static final Identifier AGRESSIVE_TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/gorgon/gorgon_active.png");
    public static final Identifier DEAD_TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/gorgon/gorgon_decapitated.png");

    public RenderGorgon(EntityRendererFactory.Context context) {
        super(context, new ModelGorgon(), 0.4F);
        this.features.add(new LayerGorgonEyes(this));
    }

    @Override
    public void scale(@NotNull EntityGorgon LivingEntityIn, MatrixStack stack, float partialTickTime) {
        stack.scale(0.85F, 0.85F, 0.85F);
    }

    @Override
    public @NotNull Identifier getTexture(EntityGorgon gorgon) {
        if (gorgon.getAnimation() == EntityGorgon.ANIMATION_SCARE) {
            return AGRESSIVE_TEXTURE;
        } else if (gorgon.deathTime > 0) {
            return DEAD_TEXTURE;
        } else {
            return PASSIVE_TEXTURE;
        }
    }

}
