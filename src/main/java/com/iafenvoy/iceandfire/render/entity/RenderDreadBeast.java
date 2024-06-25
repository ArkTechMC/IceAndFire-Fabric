package com.iafenvoy.iceandfire.render.entity;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityDreadBeast;
import com.iafenvoy.iceandfire.render.entity.layer.LayerGenericGlowing;
import com.iafenvoy.iceandfire.render.model.ModelDreadBeast;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RenderDreadBeast extends MobEntityRenderer<EntityDreadBeast, ModelDreadBeast> {
    public static final Identifier TEXTURE_EYES = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_beast_eyes.png");
    public static final Identifier TEXTURE_0 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_beast_1.png");
    public static final Identifier TEXTURE_1 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_beast_2.png");

    public RenderDreadBeast(EntityRendererFactory.Context context) {
        super(context, new ModelDreadBeast(), 0.5F);
        this.addFeature(new LayerGenericGlowing<>(this, TEXTURE_EYES));
    }

    @Override
    protected void scale(EntityDreadBeast entity, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(entity.getSize(), entity.getSize(), entity.getSize());
    }

    @Override
    public Identifier getTexture(EntityDreadBeast beast) {
        return beast.getVariant() == 1 ? TEXTURE_1 : TEXTURE_0;
    }
}
