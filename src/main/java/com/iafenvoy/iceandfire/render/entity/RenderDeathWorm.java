package com.iafenvoy.iceandfire.render.entity;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityDeathWorm;
import com.iafenvoy.iceandfire.render.model.ModelDeathWorm;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class RenderDeathWorm extends MobEntityRenderer<EntityDeathWorm, ModelDeathWorm> {
    public static final Identifier TEXTURE_RED = new Identifier(IceAndFire.MOD_ID, "textures/models/deathworm/deathworm_red.png");
    public static final Identifier TEXTURE_WHITE = new Identifier(IceAndFire.MOD_ID, "textures/models/deathworm/deathworm_white.png");
    public static final Identifier TEXTURE_YELLOW = new Identifier(IceAndFire.MOD_ID, "textures/models/deathworm/deathworm_yellow.png");

    public RenderDeathWorm(EntityRendererFactory.Context context) {
        super(context, new ModelDeathWorm(), 0);
    }

    @Override
    protected void scale(EntityDeathWorm entity, MatrixStack matrixStackIn, float partialTickTime) {
        this.shadowRadius = entity.getScaleFactor() / 3;
        matrixStackIn.scale(entity.getScaleFactor(), entity.getScaleFactor(), entity.getScaleFactor());
    }

    @Override
    protected int getBlockLight(EntityDeathWorm entityIn, BlockPos partialTicks) {
        return entityIn.isOnFire() ? 15 : entityIn.getWormBrightness(false);
    }

    @Override
    protected int getSkyLight(EntityDeathWorm entity, BlockPos pos) {
        return entity.getWormBrightness(true);
    }

    @Override
    public Identifier getTexture(EntityDeathWorm entity) {
        return entity.getVariant() == 2 ? TEXTURE_WHITE : entity.getVariant() == 1 ? TEXTURE_RED : TEXTURE_YELLOW;
    }
}
