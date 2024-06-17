package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.ModelDreadGhoul;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.entity.EntityDreadGhoul;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RenderDreadGhoul extends MobEntityRenderer<EntityDreadGhoul, ModelDreadGhoul> {
    public static final Identifier TEXTURE_EYES = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_ghoul_eyes.png");

    public static final Identifier TEXTURE_0 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_ghoul_closed_1.png");
    public static final Identifier TEXTURE_1 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_ghoul_closed_2.png");
    public static final Identifier TEXTURE_2 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_ghoul_closed_3.png");
    public static final Identifier TEXTURE_0_MID = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_ghoul_mid_1.png");
    public static final Identifier TEXTURE_1_MID = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_ghoul_mid_2.png");
    public static final Identifier TEXTURE_2_MID = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_ghoul_mid_3.png");
    public static final Identifier TEXTURE_0_OPEN = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_ghoul_open_1.png");
    public static final Identifier TEXTURE_1_OPEN = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_ghoul_open_2.png");
    public static final Identifier TEXTURE_2_OPEN = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_ghoul_open_3.png");

    public RenderDreadGhoul(EntityRendererFactory.Context context) {
        super(context, new ModelDreadGhoul(0.0F), 0.5F);
        this.addFeature(new LayerGenericGlowing<>(this, TEXTURE_EYES));
    }

    @Override
    protected void scale(EntityDreadGhoul entity, MatrixStack matrixStackIn, float partialTickTime) {
        float scale = entity.getSize() < 0.01F ? 1F : entity.getSize();
        matrixStackIn.scale(scale, scale, scale);
    }

    @Override
    public Identifier getTexture(EntityDreadGhoul ghoul) {
        return switch (ghoul.getScreamStage()) {
            case 2 -> switch (ghoul.getVariant()) {
                case 1 -> TEXTURE_1_OPEN;
                case 2 -> TEXTURE_2_OPEN;
                default -> TEXTURE_0_OPEN;
            };
            case 1 -> switch (ghoul.getVariant()) {
                case 1 -> TEXTURE_1_MID;
                case 2 -> TEXTURE_2_MID;
                default -> TEXTURE_0_MID;
            };
            default -> switch (ghoul.getVariant()) {
                case 1 -> TEXTURE_1;
                case 2 -> TEXTURE_2;
                default -> TEXTURE_0;
            };
        };
    }
}
