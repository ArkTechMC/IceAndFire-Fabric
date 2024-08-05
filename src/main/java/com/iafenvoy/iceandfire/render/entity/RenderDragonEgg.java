package com.iafenvoy.iceandfire.render.entity;

import com.iafenvoy.iceandfire.entity.EntityDragonEgg;
import com.iafenvoy.iceandfire.render.model.ModelDragonEgg;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.util.Identifier;

public class RenderDragonEgg extends LivingEntityRenderer<EntityDragonEgg, ModelDragonEgg<EntityDragonEgg>> {
    //TODO: Unused
//    public static final Identifier EGG_RED = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/egg_red.png");
//    public static final Identifier EGG_GREEN = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/egg_green.png");
//    public static final Identifier EGG_BRONZE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/egg_bronze.png");
//    public static final Identifier EGG_GREY = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/egg_gray.png");
//    public static final Identifier EGG_BLUE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/egg_blue.png");
//    public static final Identifier EGG_WHITE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/egg_white.png");
//    public static final Identifier EGG_SAPPHIRE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/egg_sapphire.png");
//    public static final Identifier EGG_SILVER = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/egg_silver.png");
//    public static final Identifier EGG_ELECTRIC = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/egg_electric.png");
//    public static final Identifier EGG_amethyst = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/egg_amethyst.png");
//    public static final Identifier EGG_BLACK = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/egg_black.png");
//    public static final Identifier EGG_COPPER = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/egg_copper.png");

    public RenderDragonEgg(EntityRendererFactory.Context context) {
        super(context, new ModelDragonEgg<>(), 0.3F);
    }

    @Override
    public Identifier getTexture(EntityDragonEgg entity) {
        return entity.getEggType().getEggTexture();
    }
}
