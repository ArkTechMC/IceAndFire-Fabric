package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class RenderHydraArrow extends ProjectileEntityRenderer {
    private static final Identifier TEXTURES = new Identifier(IceAndFire.MOD_ID, "textures/models/misc/hydra_arrow.png");

    public RenderHydraArrow(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return TEXTURES;
    }
}