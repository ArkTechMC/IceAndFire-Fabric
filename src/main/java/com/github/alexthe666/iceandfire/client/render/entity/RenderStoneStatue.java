package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.ICustomStatueModel;
import com.github.alexthe666.iceandfire.client.model.ModelHydraBody;
import com.github.alexthe666.iceandfire.client.model.ModelStonePlayer;
import com.github.alexthe666.iceandfire.client.render.IafRenderType;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerHydraHead;
import com.github.alexthe666.iceandfire.entity.EntityHydra;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RenderStoneStatue extends EntityRenderer<EntityStoneStatue> {

    protected static final Identifier[] DESTROY_STAGES = new Identifier[]{new Identifier("textures/block/destroy_stage_0.png"), new Identifier("textures/block/destroy_stage_1.png"), new Identifier("textures/block/destroy_stage_2.png"), new Identifier("textures/block/destroy_stage_3.png"), new Identifier("textures/block/destroy_stage_4.png"), new Identifier("textures/block/destroy_stage_5.png"), new Identifier("textures/block/destroy_stage_6.png"), new Identifier("textures/block/destroy_stage_7.png"), new Identifier("textures/block/destroy_stage_8.png"), new Identifier("textures/block/destroy_stage_9.png")};
    private final Map<String, EntityModel> modelMap = new HashMap<>();
    private final Map<String, Entity> hollowEntityMap = new HashMap<>();
    private final EntityRendererFactory.Context context;

    public RenderStoneStatue(EntityRendererFactory.Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public @NotNull Identifier getTexture(@NotNull EntityStoneStatue entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }

    protected void preRenderCallback(EntityStoneStatue entity, MatrixStack matrixStackIn, float partialTickTime) {
        float scale = entity.getScaleFactor() < 0.01F ? 1F : entity.getScaleFactor();
        matrixStackIn.scale(scale, scale, scale);
    }

    @Override
    public void render(EntityStoneStatue entityIn, float entityYaw, float partialTicks, @NotNull MatrixStack matrixStackIn, @NotNull VertexConsumerProvider bufferIn, int packedLightIn) {
        EntityModel model = new PigEntityModel(this.context.getPart(EntityModelLayers.PIG));

        // Get the correct model
        if (this.modelMap.get(entityIn.getTrappedEntityTypeString()) != null) {
            model = this.modelMap.get(entityIn.getTrappedEntityTypeString());
        } else {
            EntityRenderer renderer = MinecraftClient.getInstance().getEntityRenderDispatcher().renderers.get(entityIn.getTrappedEntityType());

            if (renderer instanceof FeatureRendererContext) {
                model = ((FeatureRendererContext<?, ?>) renderer).getModel();
            } else if (entityIn.getTrappedEntityType() == EntityType.PLAYER) {
                model = new ModelStonePlayer(this.context.getPart(EntityModelLayers.PLAYER));
            }
            this.modelMap.put(entityIn.getTrappedEntityTypeString(), model);
        }
        if (model == null)
            return;

        Entity fakeEntity = null;
        if (this.hollowEntityMap.get(entityIn.getTrappedEntityTypeString()) == null) {
            Entity build = entityIn.getTrappedEntityType().create(MinecraftClient.getInstance().world);
            if (build != null) {
                try {
                    build.readNbt(entityIn.getTrappedTag());
                } catch (Exception e) {
                    IceAndFire.LOGGER.warn("Mob {} could not build statue NBT", entityIn.getTrappedEntityTypeString());
                }
                fakeEntity = this.hollowEntityMap.putIfAbsent(entityIn.getTrappedEntityTypeString(), build);
            }
        } else {
            fakeEntity = this.hollowEntityMap.get(entityIn.getTrappedEntityTypeString());
        }
        RenderLayer tex = IafRenderType.getStoneMobRenderType(200, 200);
        if (fakeEntity instanceof EntityTroll) {
            tex = RenderLayer.getEntityCutout(((EntityTroll) fakeEntity).getTrollType().TEXTURE_STONE);
        }

        VertexConsumer ivertexbuilder = bufferIn.getBuffer(tex);


        matrixStackIn.push();
        float yaw = entityIn.prevYaw + (entityIn.getYaw() - entityIn.prevYaw) * partialTicks;
        boolean shouldSit = entityIn.hasVehicle() && (entityIn.getVehicle() != null && entityIn.getVehicle().canRiderInteract());
        model.child = entityIn.isBaby();
        model.riding = shouldSit;
        model.handSwingProgress = entityIn.getHandSwingProgress(partialTicks);
        if (model instanceof AdvancedEntityModel) {
            ((AdvancedEntityModel<?>) model).resetToDefaultPose();
        } else if (fakeEntity != null) {
            model.setAngles(fakeEntity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
        }
        this.preRenderCallback(entityIn, matrixStackIn, partialTicks);
        matrixStackIn.translate(0, 1.5F, 0);
        matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
        matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
        if (model instanceof ICustomStatueModel && fakeEntity != null) {
            ((ICustomStatueModel) model).renderStatue(matrixStackIn, ivertexbuilder, packedLightIn, fakeEntity);
            if (model instanceof ModelHydraBody && fakeEntity instanceof EntityHydra) {
                LayerHydraHead.renderHydraHeads((ModelHydraBody) model, true, matrixStackIn, bufferIn, packedLightIn, (EntityHydra) fakeEntity, 0, 0, partialTicks, 0, 0, 0);
            }
        } else {
            model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        matrixStackIn.pop();

        if (entityIn.getCrackAmount() >= 1) {
            int i = MathHelper.clamp(entityIn.getCrackAmount() - 1, 0, DESTROY_STAGES.length - 1);
            RenderLayer crackTex = IafRenderType.getStoneCrackRenderType(DESTROY_STAGES[i]);
            VertexConsumer ivertexbuilder2 = bufferIn.getBuffer(crackTex);
            matrixStackIn.push();
            matrixStackIn.push();
            this.preRenderCallback(entityIn, matrixStackIn, partialTicks);
            matrixStackIn.translate(0, 1.5F, 0);
            matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
            matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
            if (model instanceof ICustomStatueModel) {
                ((ICustomStatueModel) model).renderStatue(matrixStackIn, ivertexbuilder2, packedLightIn, fakeEntity);
            } else {
                model.render(matrixStackIn, ivertexbuilder2, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            matrixStackIn.pop();
            matrixStackIn.pop();
        }
        //super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}