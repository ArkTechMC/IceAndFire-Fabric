package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonArrow;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;


public class RenderDragonArrow extends ProjectileEntityRenderer<EntityDragonArrow> {
    private static final Identifier TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/misc/dragonbone_arrow.png");

    public RenderDragonArrow(EntityRendererFactory.Context context) {
        super(context);
    }


    @Override
    public Identifier getTexture(EntityDragonArrow entity) {
        return TEXTURE;
    }
}