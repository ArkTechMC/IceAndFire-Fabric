package com.iafenvoy.iceandfire.client.render.entity;

import com.iafenvoy.iceandfire.IceAndFire;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class RenderSeaSerpentArrow extends ProjectileEntityRenderer {
    private static final Identifier TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/misc/sea_serpent_arrow.png");

    public RenderSeaSerpentArrow(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return TEXTURE;
    }
}