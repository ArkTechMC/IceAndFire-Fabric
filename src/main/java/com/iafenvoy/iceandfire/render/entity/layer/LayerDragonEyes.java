package com.iafenvoy.iceandfire.render.entity.layer;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.entity.EntityIceDragon;
import com.iafenvoy.iceandfire.entity.EntityLightningDragon;
import com.iafenvoy.iceandfire.enums.EnumDragonColor;
import com.iafenvoy.uranus.client.model.AdvancedEntityModel;
import com.iafenvoy.uranus.client.model.AdvancedModelBox;
import com.iafenvoy.uranus.client.model.TabulaModel;
import com.iafenvoy.uranus.client.model.util.TabulaModelHandlerHelper;
import com.iafenvoy.uranus.client.render.TabulaModelAccessor;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LayerDragonEyes extends FeatureRenderer<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> {
    private TabulaModel fireHead;
    private TabulaModel iceHead;
    private TabulaModel lightningHead;

    public LayerDragonEyes(MobEntityRenderer<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> renderIn) {
        super(renderIn);
        try {
            this.fireHead = this.onlyKeepCubes(new TabulaModelAccessor(TabulaModelHandlerHelper.loadTabulaModel("assets/iceandfire/models/tabula/firedragon/firedragon_ground"), null), Collections.singletonList("HeadFront"));
            this.iceHead = this.onlyKeepCubes(new TabulaModelAccessor(TabulaModelHandlerHelper.loadTabulaModel("assets/iceandfire/models/tabula/icedragon/icedragon_ground"), null), Collections.singletonList("HeadFront"));
            this.lightningHead = this.onlyKeepCubes(new TabulaModelAccessor(TabulaModelHandlerHelper.loadTabulaModel("assets/iceandfire/models/tabula/lightningdragon/lightningdragon_ground"), null), Collections.singletonList("HeadFront"));
        } catch (Exception e) {
            IceAndFire.LOGGER.error(e);
        }
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (dragon.shouldRenderEyes()) {
            RenderLayer eyes = RenderLayer.getEyes(EnumDragonColor.getById(dragon.getVariant()).getEyesTexture(dragon.getDragonStage()));
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(eyes);
            if (dragon instanceof EntityLightningDragon && this.lightningHead != null) {
                this.copyPositions(this.lightningHead, (TabulaModel) this.getContextModel());
                this.lightningHead.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            } else if (dragon instanceof EntityIceDragon && this.iceHead != null) {
                this.copyPositions(this.iceHead, (TabulaModel) this.getContextModel());
                this.iceHead.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            } else if (this.fireHead != null) {
                this.copyPositions(this.fireHead, (TabulaModel) this.getContextModel());
                this.fireHead.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            //Fallback method
            else
                this.getContextModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    protected Identifier getTexture(EntityDragonBase entityIn) {
        return null;
    }

    //TODO: do this with hideable/visble/showModel stuff instead
    //Removes all cubes except the cube names specified by the string list and their parents
    //We need to keep the parents to correctly render the head position
    private TabulaModel onlyKeepCubes(TabulaModelAccessor model, List<String> strings) {
        List<AdvancedModelBox> keepCubes = new ArrayList<>();
        for (String str : strings) {
            AdvancedModelBox cube = model.getCube(str);
            keepCubes.add(cube);
            while (cube.getParent() != null) {
                keepCubes.add(cube.getParent());
                cube = cube.getParent();
            }
        }
        this.removeChildren(model, keepCubes);
        model.getCubes().values().removeIf(advancedModelBox -> !keepCubes.contains(advancedModelBox));
        return model;
    }

    private void removeChildren(TabulaModelAccessor model, List<AdvancedModelBox> keepCubes) {
        model.getRootBox().forEach(modelRenderer -> {
            modelRenderer.childModels.removeIf(child -> !keepCubes.contains(child));
            modelRenderer.childModels.forEach(childModel -> this.removeChildren((AdvancedModelBox) childModel, keepCubes));
        });
    }

    private void removeChildren(AdvancedModelBox modelBox, List<AdvancedModelBox> keepCubes) {
        modelBox.childModels.removeIf(modelRenderer -> !keepCubes.contains(modelRenderer));
        modelBox.childModels.forEach(modelRenderer -> this.removeChildren((AdvancedModelBox) modelRenderer, keepCubes));
    }

    public boolean isAngleEqual(AdvancedModelBox original, AdvancedModelBox pose) {
        return pose != null && pose.rotateAngleX == original.rotateAngleX && pose.rotateAngleY == original.rotateAngleY && pose.rotateAngleZ == original.rotateAngleZ;
    }

    public boolean isPositionEqual(AdvancedModelBox original, AdvancedModelBox pose) {
        return pose.rotationPointX == original.rotationPointX && pose.rotationPointY == original.rotationPointY && pose.rotationPointZ == original.rotationPointZ;
    }

    public void copyPositions(TabulaModel model, TabulaModel modelTo) {
        for (AdvancedModelBox cube : model.getCubes().values()) {
            AdvancedModelBox modelToCube = modelTo.getCube(cube.boxName);
            if (!this.isAngleEqual(cube, modelToCube)) {
                cube.rotateAngleX = modelToCube.rotateAngleX;
                cube.rotateAngleY = modelToCube.rotateAngleY;
                cube.rotateAngleZ = modelToCube.rotateAngleZ;
            }
            if (!this.isPositionEqual(cube, modelToCube)) {
                cube.rotationPointX = modelToCube.rotationPointX;
                cube.rotationPointY = modelToCube.rotationPointY;
                cube.rotationPointZ = modelToCube.rotationPointZ;
            }
        }
    }
}