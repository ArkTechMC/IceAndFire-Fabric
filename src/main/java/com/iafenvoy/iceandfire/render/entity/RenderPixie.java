package com.iafenvoy.iceandfire.render.entity;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityPixie;
import com.iafenvoy.iceandfire.render.entity.layer.LayerPixieGlow;
import com.iafenvoy.iceandfire.render.entity.layer.LayerPixieItem;
import com.iafenvoy.iceandfire.render.model.ModelPixie;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RenderPixie extends MobEntityRenderer<EntityPixie, ModelPixie> {
    public static final Identifier TEXTURE_0 = new Identifier(IceAndFire.MOD_ID, "textures/models/pixie/pixie_0.png");
    public static final Identifier TEXTURE_1 = new Identifier(IceAndFire.MOD_ID, "textures/models/pixie/pixie_1.png");
    public static final Identifier TEXTURE_2 = new Identifier(IceAndFire.MOD_ID, "textures/models/pixie/pixie_2.png");
    public static final Identifier TEXTURE_3 = new Identifier(IceAndFire.MOD_ID, "textures/models/pixie/pixie_3.png");
    public static final Identifier TEXTURE_4 = new Identifier(IceAndFire.MOD_ID, "textures/models/pixie/pixie_4.png");
    public static final Identifier TEXTURE_5 = new Identifier(IceAndFire.MOD_ID, "textures/models/pixie/pixie_5.png");

    public RenderPixie(EntityRendererFactory.Context context) {
        super(context, new ModelPixie(), 0.2F);
        this.features.add(new LayerPixieItem(this));
        this.features.add(new LayerPixieGlow(this));
    }

    @Override
    public void scale(EntityPixie LivingEntityIn, MatrixStack stack, float partialTickTime) {
        stack.scale(0.55F, 0.55F, 0.55F);
        if (LivingEntityIn.isSitting()) {
            stack.translate(0F, 0.5F, 0F);

        }
    }

    @Override
    public Identifier getTexture(EntityPixie pixie) {
        return switch (pixie.getColor()) {
            default -> TEXTURE_0;
            case 1 -> TEXTURE_1;
            case 2 -> TEXTURE_2;
            case 3 -> TEXTURE_3;
            case 4 -> TEXTURE_4;
            case 5 -> TEXTURE_5;
        };
    }
}
