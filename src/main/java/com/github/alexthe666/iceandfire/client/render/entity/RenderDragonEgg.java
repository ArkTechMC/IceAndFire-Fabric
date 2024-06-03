package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;


public class RenderDragonEgg extends LivingEntityRenderer<EntityDragonEgg, ModelDragonEgg<EntityDragonEgg>> {

    public static final Identifier EGG_RED = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/egg_red.png");
    public static final Identifier EGG_GREEN = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/egg_green.png");
    public static final Identifier EGG_BRONZE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/egg_bronze.png");
    public static final Identifier EGG_GREY = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/egg_gray.png");
    public static final Identifier EGG_BLUE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/egg_blue.png");
    public static final Identifier EGG_WHITE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/egg_white.png");
    public static final Identifier EGG_SAPPHIRE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/egg_sapphire.png");
    public static final Identifier EGG_SILVER = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/egg_silver.png");
    public static final Identifier EGG_ELECTRIC = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/egg_electric.png");
    public static final Identifier EGG_AMYTHEST = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/egg_amythest.png");
    public static final Identifier EGG_BLACK = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/egg_black.png");
    public static final Identifier EGG_COPPER = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/egg_copper.png");

    public RenderDragonEgg(EntityRendererFactory.Context context) {
        super(context, new ModelDragonEgg(), 0.3F);
    }

    @Override
    public @NotNull Identifier getTexture(EntityDragonEgg entity) {
        switch (entity.getEggType()) {
            default:
                return EGG_RED;
            case GREEN:
                return EGG_GREEN;
            case BRONZE:
                return EGG_BRONZE;
            case GRAY:
                return EGG_GREY;
            case BLUE:
                return EGG_BLUE;
            case WHITE:
                return EGG_WHITE;
            case SAPPHIRE:
                return EGG_SAPPHIRE;
            case SILVER:
                return EGG_SILVER;
            case ELECTRIC:
                return EGG_ELECTRIC;
            case AMYTHEST:
                return EGG_AMYTHEST;
            case COPPER:
                return EGG_COPPER;
            case BLACK:
                return EGG_BLACK;

        }
    }

}
