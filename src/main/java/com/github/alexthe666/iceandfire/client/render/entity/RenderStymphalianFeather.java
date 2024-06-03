package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class RenderStymphalianFeather extends ProjectileEntityRenderer {
    private static final Identifier TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/stymphalianbird/feather.png");

    public RenderStymphalianFeather(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public @NotNull Identifier getTexture(@NotNull Entity entity) {
        return TEXTURE;
    }
}