package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.ModelDreadScuttler;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.entity.EntityDreadScuttler;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class RenderDreadScuttler extends MobEntityRenderer<EntityDreadScuttler, ModelDreadScuttler> {

    public static final Identifier TEXTURE_EYES = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_scuttler_eyes.png");
    public static final Identifier TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_scuttler.png");

    public RenderDreadScuttler(EntityRendererFactory.Context context) {
        super(context, new ModelDreadScuttler(), 0.75F);
        this.addFeature(new LayerGenericGlowing<>(this, TEXTURE_EYES));
    }

    @Override
    public void scale(EntityDreadScuttler LivingEntityIn, MatrixStack stack, float partialTickTime) {
        stack.scale(LivingEntityIn.getSize(), LivingEntityIn.getSize(), LivingEntityIn.getSize());
    }

    @Override
    public @NotNull Identifier getTexture(@NotNull EntityDreadScuttler beast) {
        return TEXTURE;

    }

}
