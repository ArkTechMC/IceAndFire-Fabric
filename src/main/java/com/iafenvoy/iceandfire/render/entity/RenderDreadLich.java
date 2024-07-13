package com.iafenvoy.iceandfire.render.entity;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityDreadLich;
import com.iafenvoy.iceandfire.render.entity.layer.LayerGenericGlowing;
import com.iafenvoy.iceandfire.render.model.ModelDreadLich;
import com.iafenvoy.uranus.client.model.util.HideableLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RenderDreadLich extends MobEntityRenderer<EntityDreadLich, ModelDreadLich> {
    public static final Identifier TEXTURE_EYES = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_lich_eyes.png");
    public static final Identifier TEXTURE_0 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_lich_0.png");
    public static final Identifier TEXTURE_1 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_lich_1.png");
    public static final Identifier TEXTURE_2 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_lich_2.png");
    public static final Identifier TEXTURE_3 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_lich_3.png");
    public static final Identifier TEXTURE_4 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_lich_4.png");
    public final HideableLayer<EntityDreadLich, ModelDreadLich, HeldItemFeatureRenderer<EntityDreadLich, ModelDreadLich>> itemLayer;

    public RenderDreadLich(EntityRendererFactory.Context context) {
        super(context, new ModelDreadLich(0.0F), 0.6F);
        this.addFeature(new LayerGenericGlowing<>(this, TEXTURE_EYES));
        this.itemLayer = new HideableLayer<>(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()), this);
        this.addFeature(this.itemLayer);
    }

    @Override
    protected void scale(EntityDreadLich entity, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.95F, 0.95F, 0.95F);
        if (entity.getAnimation() == this.getModel().getSpawnAnimation()) {
            this.itemLayer.hidden = entity.getAnimationTick() <= this.getModel().getSpawnAnimation().getDuration() - 10;
            return;
        }
        this.itemLayer.hidden = false;
    }

    @Override
    public Identifier getTexture(EntityDreadLich entity) {
        return switch (entity.getVariant()) {
            case 1 -> TEXTURE_1;
            case 2 -> TEXTURE_2;
            case 3 -> TEXTURE_3;
            case 4 -> TEXTURE_4;
            default -> TEXTURE_0;
        };
    }
}
