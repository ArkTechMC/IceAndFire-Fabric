package com.iafenvoy.iceandfire.render.entity;

import com.iafenvoy.iceandfire.entity.EntityTroll;
import com.iafenvoy.iceandfire.render.entity.layer.LayerTrollEyes;
import com.iafenvoy.iceandfire.render.entity.layer.LayerTrollWeapon;
import com.iafenvoy.iceandfire.render.model.ModelTroll;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class RenderTroll extends MobEntityRenderer<EntityTroll, ModelTroll> {
    public RenderTroll(EntityRendererFactory.Context context) {
        super(context, new ModelTroll(), 0.9F);
        this.features.add(new LayerTrollWeapon(this));
        this.features.add(new LayerTrollEyes(this));
    }

    @Override
    public Identifier getTexture(EntityTroll troll) {
        return troll.getTrollType().getTexture();
    }
}
