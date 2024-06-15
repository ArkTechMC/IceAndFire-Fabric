package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexEgg;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.util.Identifier;

public class RenderMyrmexEgg extends LivingEntityRenderer<EntityMyrmexEgg, AdvancedEntityModel<EntityMyrmexEgg>> {

    public static final Identifier EGG_JUNGLE = new Identifier(IceAndFire.MOD_ID, "textures/models/myrmex/myrmex_jungle_egg.png");
    public static final Identifier EGG_DESERT = new Identifier(IceAndFire.MOD_ID, "textures/models/myrmex/myrmex_desert_egg.png");

    public RenderMyrmexEgg(EntityRendererFactory.Context context) {
        super(context, new ModelDragonEgg<>(), 0.3F);
    }

    @Override
    protected boolean hasLabel(EntityMyrmexEgg entity) {
        return entity.shouldRenderName() && entity.hasCustomName();
    }

    @Override
    public Identifier getTexture(EntityMyrmexEgg entity) {
        return entity.isJungle() ? EGG_JUNGLE : EGG_DESERT;
    }

}
