package com.iafenvoy.iceandfire.render.entity;

import com.iafenvoy.uranus.client.model.AdvancedEntityModel;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityCockatrice;
import com.iafenvoy.iceandfire.entity.EntityGorgon;
import com.iafenvoy.iceandfire.particle.CockatriceBeamRender;
import com.iafenvoy.iceandfire.render.model.ModelCockatrice;
import com.iafenvoy.iceandfire.render.model.ModelCockatriceChick;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class RenderCockatrice extends MobEntityRenderer<EntityCockatrice, AdvancedEntityModel<EntityCockatrice>> {
    public static final Identifier TEXTURE_ROOSTER = new Identifier(IceAndFire.MOD_ID, "textures/models/cockatrice/cockatrice_0.png");
    public static final Identifier TEXTURE_HEN = new Identifier(IceAndFire.MOD_ID, "textures/models/cockatrice/cockatrice_1.png");
    public static final Identifier TEXTURE_ROOSTER_CHICK = new Identifier(IceAndFire.MOD_ID, "textures/models/cockatrice/cockatrice_0_chick.png");
    public static final Identifier TEXTURE_HEN_CHICK = new Identifier(IceAndFire.MOD_ID, "textures/models/cockatrice/cockatrice_1_chick.png");
    public static final ModelCockatrice ADULT_MODEL = new ModelCockatrice();
    public static final ModelCockatriceChick BABY_MODEL = new ModelCockatriceChick();

    public RenderCockatrice(EntityRendererFactory.Context context) {
        super(context, new ModelCockatrice(), 0.6F);
    }

    private Vec3d getPosition(LivingEntity LivingEntityIn, double p_177110_2_) {
        double d0 = LivingEntityIn.lastRenderX + (LivingEntityIn.getX() - LivingEntityIn.lastRenderX);
        double d1 = p_177110_2_ + LivingEntityIn.lastRenderY + (LivingEntityIn.getY() - LivingEntityIn.lastRenderY);
        double d2 = LivingEntityIn.lastRenderZ + (LivingEntityIn.getZ() - LivingEntityIn.lastRenderZ);
        return new Vec3d(d0, d1, d2);
    }

    @Override
    public boolean shouldRender(EntityCockatrice livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntityIn, camera, camX, camY, camZ))
            return true;
        else {
            if (livingEntityIn.hasTargetedEntity()) {
                LivingEntity livingentity = livingEntityIn.getTargetedEntity();
                if (livingentity != null) {
                    Vec3d Vector3d = this.getPosition(livingentity, (double) livingentity.getHeight() * 0.5D);
                    Vec3d Vector3d1 = this.getPosition(livingEntityIn, livingEntityIn.getStandingEyeHeight());
                    return camera.isVisible(new Box(Vector3d1.x, Vector3d1.y, Vector3d1.z, Vector3d.x, Vector3d.y, Vector3d.z));
                }
            }
            return false;
        }
    }

    @Override
    public void render(EntityCockatrice entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        this.model = entityIn.isBaby() ? BABY_MODEL : ADULT_MODEL;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        LivingEntity livingentity = entityIn.getTargetedEntity();
        boolean blindness = entityIn.hasStatusEffect(StatusEffects.BLINDNESS) || livingentity != null && livingentity.hasStatusEffect(StatusEffects.BLINDNESS);
        if (!blindness && livingentity != null && EntityGorgon.isEntityLookingAt(entityIn, livingentity, EntityCockatrice.VIEW_RADIUS) && EntityGorgon.isEntityLookingAt(livingentity, entityIn, EntityCockatrice.VIEW_RADIUS))
            CockatriceBeamRender.render(entityIn, livingentity, matrixStackIn, bufferIn, partialTicks);
    }

    @Override
    protected void scale(EntityCockatrice entity, MatrixStack matrixStackIn, float partialTickTime) {
        if (entity.isBaby())
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
    }

    @Override
    public Identifier getTexture(EntityCockatrice cockatrice) {
        return cockatrice.isBaby() ? cockatrice.isHen() ? TEXTURE_HEN_CHICK : TEXTURE_ROOSTER_CHICK : cockatrice.isHen() ? TEXTURE_HEN : TEXTURE_ROOSTER;
    }
}
