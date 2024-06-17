package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerSeaSerpentAncient;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RenderSeaSerpent extends MobEntityRenderer<EntitySeaSerpent, AdvancedEntityModel<EntitySeaSerpent>> {
    public static final Identifier TEXTURE_BLUE = new Identifier(IceAndFire.MOD_ID, "textures/models/seaserpent/seaserpent_blue.png");
    public static final Identifier TEXTURE_BLUE_BLINK = new Identifier(IceAndFire.MOD_ID, "textures/models/seaserpent/seaserpent_blue_blink.png");
    public static final Identifier TEXTURE_BRONZE = new Identifier(IceAndFire.MOD_ID, "textures/models/seaserpent/seaserpent_bronze.png");
    public static final Identifier TEXTURE_BRONZE_BLINK = new Identifier(IceAndFire.MOD_ID, "textures/models/seaserpent/seaserpent_bronze_blink.png");
    public static final Identifier TEXTURE_DARKBLUE = new Identifier(IceAndFire.MOD_ID, "textures/models/seaserpent/seaserpent_darkblue.png");
    public static final Identifier TEXTURE_DARKBLUE_BLINK = new Identifier(IceAndFire.MOD_ID, "textures/models/seaserpent/seaserpent_darkblue_blink.png");
    public static final Identifier TEXTURE_GREEN = new Identifier(IceAndFire.MOD_ID, "textures/models/seaserpent/seaserpent_green.png");
    public static final Identifier TEXTURE_GREEN_BLINK = new Identifier(IceAndFire.MOD_ID, "textures/models/seaserpent/seaserpent_green_blink.png");
    public static final Identifier TEXTURE_PURPLE = new Identifier(IceAndFire.MOD_ID, "textures/models/seaserpent/seaserpent_purple.png");
    public static final Identifier TEXTURE_PURPLE_BLINK = new Identifier(IceAndFire.MOD_ID, "textures/models/seaserpent/seaserpent_purple_blink.png");
    public static final Identifier TEXTURE_RED = new Identifier(IceAndFire.MOD_ID, "textures/models/seaserpent/seaserpent_red.png");
    public static final Identifier TEXTURE_RED_BLINK = new Identifier(IceAndFire.MOD_ID, "textures/models/seaserpent/seaserpent_red_blink.png");
    public static final Identifier TEXTURE_TEAL = new Identifier(IceAndFire.MOD_ID, "textures/models/seaserpent/seaserpent_teal.png");
    public static final Identifier TEXTURE_TEAL_BLINK = new Identifier(IceAndFire.MOD_ID, "textures/models/seaserpent/seaserpent_teal_blink.png");

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
        return switch (serpent.getVariant()) {
            case 0 -> serpent.isBlinking() ? TEXTURE_BLUE_BLINK : TEXTURE_BLUE;
            case 1 -> serpent.isBlinking() ? TEXTURE_BRONZE_BLINK : TEXTURE_BRONZE;
            case 2 -> serpent.isBlinking() ? TEXTURE_DARKBLUE_BLINK : TEXTURE_DARKBLUE;
            case 3 -> serpent.isBlinking() ? TEXTURE_GREEN_BLINK : TEXTURE_GREEN;
            case 4 -> serpent.isBlinking() ? TEXTURE_PURPLE_BLINK : TEXTURE_PURPLE;
            case 5 -> serpent.isBlinking() ? TEXTURE_RED_BLINK : TEXTURE_RED;
            case 6 -> serpent.isBlinking() ? TEXTURE_TEAL_BLINK : TEXTURE_TEAL;
            default -> TEXTURE_BLUE;
        };
    }
}
