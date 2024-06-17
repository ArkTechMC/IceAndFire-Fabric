package com.iafenvoy.iceandfire.client.render.entity;

import com.iafenvoy.citadel.client.model.AdvancedEntityModel;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.client.render.entity.layer.LayerDragonArmor;
import com.iafenvoy.iceandfire.client.render.entity.layer.LayerDragonBanner;
import com.iafenvoy.iceandfire.client.render.entity.layer.LayerDragonEyes;
import com.iafenvoy.iceandfire.client.render.entity.layer.LayerDragonRider;
import com.iafenvoy.iceandfire.client.texture.ArrayLayeredTexture;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.enums.EnumDragonTextures;
import com.google.common.collect.Maps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RenderDragonBase extends MobEntityRenderer<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> {
    private final Map<String, Identifier> LAYERED_TEXTURE_CACHE = Maps.newHashMap();
    private final int dragonType;

    public RenderDragonBase(EntityRendererFactory.Context context, AdvancedEntityModel model, int dragonType) {
        super(context, model, 0.15F);
        this.addFeature(new LayerDragonEyes(this));
        this.addFeature(new LayerDragonRider(this, false));
        this.addFeature(new LayerDragonBanner(this));
        this.addFeature(new LayerDragonArmor(this, dragonType));
        this.dragonType = dragonType;
    }

    private Vec3d getPosition(LivingEntity LivingEntityIn, double p_177110_2_, float p_177110_4_) {
        double d0 = LivingEntityIn.lastRenderX + (LivingEntityIn.getX() - LivingEntityIn.lastRenderX) * (double) p_177110_4_;
        double d1 = p_177110_2_ + LivingEntityIn.lastRenderY + (LivingEntityIn.getY() - LivingEntityIn.lastRenderY) * (double) p_177110_4_;
        double d2 = LivingEntityIn.lastRenderZ + (LivingEntityIn.getZ() - LivingEntityIn.lastRenderZ) * (double) p_177110_4_;
        return new Vec3d(d0, d1, d2);
    }

    @Override
    protected void scale(EntityDragonBase entity, MatrixStack matrixStackIn, float partialTickTime) {
        this.shadowRadius = entity.getRenderSize() / 3;
        float f7 = entity.prevDragonPitch + (entity.getDragonPitch() - entity.prevDragonPitch) * partialTickTime;
        matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f7));
        matrixStackIn.scale(this.shadowRadius, this.shadowRadius, this.shadowRadius);
    }

    @Override
    public Identifier getTexture(EntityDragonBase entity) {
        String baseTexture = entity.getVariantName(entity.getVariant()) + entity.getDragonStage() + entity.isModelDead() + entity.isMale() + entity.isSkeletal() + entity.isSleeping() + entity.isBlinking();
        Identifier resourcelocation = this.LAYERED_TEXTURE_CACHE.get(baseTexture);
        if (resourcelocation == null) {
            resourcelocation = new Identifier(IceAndFire.MOD_ID, "dragon_texture_" + baseTexture);
            List<String> tex = new ArrayList<>();
            tex.add(EnumDragonTextures.getTextureFromDragon(entity).toString());
            if (entity.isMale() && !entity.isSkeletal()) {
                if (this.dragonType == 0)
                    tex.add(EnumDragonTextures.getDragonEnum(entity).FIRE_MALE_OVERLAY.toString());
                else if (this.dragonType == 1)
                    tex.add(EnumDragonTextures.getDragonEnum(entity).ICE_MALE_OVERLAY.toString());
                else if (this.dragonType == 2)
                    tex.add(EnumDragonTextures.getDragonEnum(entity).LIGHTNING_MALE_OVERLAY.toString());
            } else
                tex.add(EnumDragonTextures.Armor.EMPTY.FIRETEXTURE.toString());
            ArrayLayeredTexture layeredBase = new ArrayLayeredTexture(tex);
            MinecraftClient.getInstance().getTextureManager().registerTexture(resourcelocation, layeredBase);
            this.LAYERED_TEXTURE_CACHE.put(baseTexture, resourcelocation);
        }
        return resourcelocation;
    }
}
