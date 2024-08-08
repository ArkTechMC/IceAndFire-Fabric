package com.iafenvoy.iceandfire.render.entity;

import com.iafenvoy.iceandfire.data.SeaSerpent;
import com.iafenvoy.iceandfire.entity.EntitySeaSerpent;
import com.iafenvoy.iceandfire.render.entity.layer.LayerSeaSerpentAncient;
import com.iafenvoy.uranus.client.model.AdvancedEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RenderSeaSerpent extends MobEntityRenderer<EntitySeaSerpent, AdvancedEntityModel<EntitySeaSerpent>> {
    public RenderSeaSerpent(EntityRendererFactory.Context context, AdvancedEntityModel model) {
        super(context, model, 1.6F);
        this.features.add(new LayerSeaSerpentAncient(this));
    }

    @Override
    protected void scale(EntitySeaSerpent entity, MatrixStack matrixStackIn, float partialTickTime) {
        this.shadowRadius = entity.getSeaSerpentScale();
        matrixStackIn.scale(this.shadowRadius, this.shadowRadius, this.shadowRadius);
    }

    @Override
    public Identifier getTexture(EntitySeaSerpent serpent) {
        return SeaSerpent.getByName(serpent.getVariant()).getTexture(serpent.isBlinking());
    }
}
