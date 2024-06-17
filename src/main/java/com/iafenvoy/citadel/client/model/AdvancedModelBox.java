package com.iafenvoy.citadel.client.model;

import com.iafenvoy.citadel.client.model.basic.BasicModelPart;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * An enhanced RendererModel
 *
 * @author gegy1000
 * @since 1.0.0
 */
@Environment(EnvType.CLIENT)
public class AdvancedModelBox extends BasicModelPart {
    public final String boxName;
    private final AdvancedEntityModel<?> model;
    public float defaultRotationX, defaultRotationY, defaultRotationZ;
    public float defaultPositionX, defaultPositionY, defaultPositionZ;
    public float scaleX = 1.0F, scaleY = 1.0F, scaleZ = 1.0F;
    public int textureOffsetX, textureOffsetY;
    public boolean scaleChildren;
    public ObjectList<TabulaModelRenderUtils.ModelBox> cubeList;
    public ObjectList<BasicModelPart> childModels;
    public float offsetX;
    public float offsetY;
    public float offsetZ;
    private AdvancedModelBox parent;
    private float textureWidth;
    private float textureHeight;

    public AdvancedModelBox(AdvancedEntityModel<?> model, String name) {
        super(model);
        this.textureWidth = model.texWidth;
        this.textureHeight = model.texHeight;
        this.model = model;
        this.cubeList = new ObjectArrayList<>();
        this.childModels = new ObjectArrayList<>();
        this.boxName = name;
    }

    public AdvancedModelBox(AdvancedEntityModel<?> model) {
        this(model, null);
        this.textureWidth = model.texWidth;
        this.textureHeight = model.texHeight;
        this.cubeList = new ObjectArrayList<>();
        this.childModels = new ObjectArrayList<>();
    }

    public AdvancedModelBox(AdvancedEntityModel<?> model, int textureOffsetX, int textureOffsetY) {
        this(model);
        this.textureWidth = model.texWidth;
        this.textureHeight = model.texHeight;
        this.setTextureOffset(textureOffsetX, textureOffsetY);
        this.cubeList = new ObjectArrayList<>();
        this.childModels = new ObjectArrayList<>();
    }

    public BasicModelPart addBox(String p_217178_1_, float p_217178_2_, float p_217178_3_, float p_217178_4_, int p_217178_5_, int p_217178_6_, int p_217178_7_, float p_217178_8_, int p_217178_9_, int p_217178_10_) {
        this.setTextureOffset(p_217178_9_, p_217178_10_);
        this.addBox(this.textureOffsetX, this.textureOffsetY, p_217178_2_, p_217178_3_, p_217178_4_, (float) p_217178_5_, (float) p_217178_6_, (float) p_217178_7_, p_217178_8_, p_217178_8_, p_217178_8_, this.mirror);
        return this;
    }

    public BasicModelPart addBox(float p_228300_1_, float p_228300_2_, float p_228300_3_, float p_228300_4_, float p_228300_5_, float p_228300_6_) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, p_228300_1_, p_228300_2_, p_228300_3_, p_228300_4_, p_228300_5_, p_228300_6_, 0.0F, 0.0F, 0.0F, this.mirror);
        return this;
    }

    public BasicModelPart addBox(float p_228304_1_, float p_228304_2_, float p_228304_3_, float p_228304_4_, float p_228304_5_, float p_228304_6_, boolean p_228304_7_) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, p_228304_1_, p_228304_2_, p_228304_3_, p_228304_4_, p_228304_5_, p_228304_6_, 0.0F, 0.0F, 0.0F, p_228304_7_);
        return this;
    }

    public void addBox(float p_228301_1_, float p_228301_2_, float p_228301_3_, float p_228301_4_, float p_228301_5_, float p_228301_6_, float p_228301_7_) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, p_228301_1_, p_228301_2_, p_228301_3_, p_228301_4_, p_228301_5_, p_228301_6_, p_228301_7_, p_228301_7_, p_228301_7_, this.mirror);
    }

    public void addBox(float p_228302_1_, float p_228302_2_, float p_228302_3_, float p_228302_4_, float p_228302_5_, float p_228302_6_, float p_228302_7_, float p_228302_8_, float p_228302_9_) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, p_228302_1_, p_228302_2_, p_228302_3_, p_228302_4_, p_228302_5_, p_228302_6_, p_228302_7_, p_228302_8_, p_228302_9_, this.mirror);
    }

    public void addBox(float p_228303_1_, float p_228303_2_, float p_228303_3_, float p_228303_4_, float p_228303_5_, float p_228303_6_, float p_228303_7_, boolean p_228303_8_) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, p_228303_1_, p_228303_2_, p_228303_3_, p_228303_4_, p_228303_5_, p_228303_6_, p_228303_7_, p_228303_7_, p_228303_7_, p_228303_8_);
    }

    private void addBox(int p_228305_1_, int p_228305_2_, float p_228305_3_, float p_228305_4_, float p_228305_5_, float p_228305_6_, float p_228305_7_, float p_228305_8_, float p_228305_9_, float p_228305_10_, float p_228305_11_, boolean p_228305_12_) {
        this.cubeList.add(new TabulaModelRenderUtils.ModelBox(p_228305_1_, p_228305_2_, p_228305_3_, p_228305_4_, p_228305_5_, p_228305_6_, p_228305_7_, p_228305_8_, p_228305_9_, p_228305_10_, p_228305_11_, p_228305_12_, this.textureWidth, this.textureHeight));
    }

    /**
     * If true, when using setScale, the children of this model part will be scaled as well as just this part. If false, just this part will be scaled.
     *
     * @param scaleChildren true if this parent should scale the children
     * @since 1.1.0
     */
    public void setShouldScaleChildren(boolean scaleChildren) {
        this.scaleChildren = scaleChildren;
    }

    /**
     * Sets the scale for this AdvancedModelBox to be rendered at. (Performs a call to GLStateManager.scale()).
     *
     * @param scaleX the x scale
     * @param scaleY the y scale
     * @param scaleZ the z scale
     * @since 1.1.0
     */
    public void setScale(float scaleX, float scaleY, float scaleZ) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    /**
     * Sets this RendererModel's default pose to the current pose.
     */
    public void updateDefaultPose() {
        this.defaultRotationX = this.rotateAngleX;
        this.defaultRotationY = this.rotateAngleY;
        this.defaultRotationZ = this.rotateAngleZ;

        // this.defaultOffsetX = this.offsetX;
        // this.defaultOffsetY = this.offsetY;
        // this.defaultOffsetZ = this.offsetZ;

        this.defaultPositionX = this.rotationPointX;
        this.defaultPositionY = this.rotationPointY;
        this.defaultPositionZ = this.rotationPointZ;
    }

    public void setPos(float xIn, float yIn, float zIn) {
        this.rotationPointX = xIn;
        this.rotationPointY = yIn;
        this.rotationPointZ = zIn;
    }

    /**
     * Sets the current pose to the previously set default pose.
     */
    public void resetToDefaultPose() {
        this.rotateAngleX = this.defaultRotationX;
        this.rotateAngleY = this.defaultRotationY;
        this.rotateAngleZ = this.defaultRotationZ;

        // this.offsetX = this.defaultOffsetX;
        // this.offsetY = this.defaultOffsetY;
        // this.offsetZ = this.defaultOffsetZ;

        this.rotationPointX = this.defaultPositionX;
        this.rotationPointY = this.defaultPositionY;
        this.rotationPointZ = this.defaultPositionZ;
    }

    @Override
    public void addChild(BasicModelPart child) {
        super.addChild(child);
        this.childModels.add(child);
        if (child instanceof AdvancedModelBox advancedChild)
            advancedChild.setParent(this);
    }

    /**
     * @return the parent of this box
     */
    public AdvancedModelBox getParent() {
        return this.parent;
    }

    /**
     * Sets the parent of this box
     *
     * @param parent the new parent
     */
    public void setParent(AdvancedModelBox parent) {
        this.parent = parent;
    }

    public void translateAndRotate(MatrixStack matrixStackIn) {
        matrixStackIn.translate(this.rotationPointX / 16.0F, this.rotationPointY / 16.0F, (double) (this.rotationPointZ / 16.0F));
        if (this.rotateAngleZ != 0.0F)
            matrixStackIn.multiply(RotationAxis.POSITIVE_Z.rotation(this.rotateAngleZ));
        if (this.rotateAngleY != 0.0F)
            matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotation(this.rotateAngleY));
        if (this.rotateAngleX != 0.0F)
            matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotation(this.rotateAngleX));
        matrixStackIn.scale(this.scaleX, this.scaleY, this.scaleZ);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float alpha) {
        if (this.showModel)
            if (!this.cubeList.isEmpty() || !this.childModels.isEmpty()) {
                matrixStack.push();
                this.translateAndRotate(matrixStack);
                this.doRender(matrixStack.peek(), consumer, light, overlay, red, green, blue, alpha);
                ObjectListIterator<BasicModelPart> var9 = this.childModels.iterator();
                if (!this.scaleChildren)
                    matrixStack.scale(1F / Math.max(this.scaleX, 0.0001F), 1F / Math.max(this.scaleY, 0.0001F), 1F / Math.max(this.scaleZ, 0.0001F));
                while (var9.hasNext()) {
                    BasicModelPart lvt_10_1_ = var9.next();
                    lvt_10_1_.render(matrixStack, consumer, light, overlay, red, green, blue, alpha);
                }
                matrixStack.pop();
            }
    }

    private void doRender(MatrixStack.Entry entry, VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float alpha) {
        Matrix4f lvt_9_1_ = entry.getPositionMatrix();
        Matrix3f lvt_10_1_ = entry.getNormalMatrix();

        for (TabulaModelRenderUtils.ModelBox modelBox : this.cubeList) {
            TabulaModelRenderUtils.TexturedQuad[] quads = modelBox.quads;
            for (TabulaModelRenderUtils.TexturedQuad texturedQuad : quads) {
                Vector3f vector3f = new Vector3f(texturedQuad.normal);
                vector3f.mul(lvt_10_1_);
                float normalX = vector3f.x();
                float normalY = vector3f.y();
                float normalZ = vector3f.z();
                for (int i = 0; i < 4; ++i) {
                    TabulaModelRenderUtils.PositionTextureVertex vertexPosition = texturedQuad.vertexPositions[i];
                    float x = vertexPosition.position.x() / 16.0F;
                    float y = vertexPosition.position.y() / 16.0F;
                    float z = vertexPosition.position.z() / 16.0F;
                    Vector4f vector4f = new Vector4f(x, y, z, 1.0F);
                    vector4f.mul(lvt_9_1_);
                    consumer.vertex(vector4f.x(), vector4f.y(), vector4f.z(), red, green, blue, alpha, vertexPosition.textureU, vertexPosition.textureV, overlay, light, normalX, normalY, normalZ);
                }
            }
        }
    }

    public AdvancedEntityModel<?> getModel() {
        return this.model;
    }

    private float calculateRotation(float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
        float movementScale = this.model.getMovementScale();
        float rotation = (MathHelper.cos(f * (speed * movementScale) + offset) * (degree * movementScale) * f1) + (weight * f1);
        return invert ? -rotation : rotation;
    }

    /**
     * Rotates this box back and forth (rotateAngleX). Useful for arms and legs.
     *
     * @param speed      is how fast the model runs
     * @param degree     is how far the box will rotate;
     * @param invert     will invert the rotation
     * @param offset     will offset the timing of the model
     * @param weight     will make the model favor one direction more based on how fast the mob is moving
     * @param walk       is the walked distance
     * @param walkAmount is the walk speed
     */
    public void walk(float speed, float degree, boolean invert, float offset, float weight, float walk, float walkAmount) {
        this.rotateAngleX += this.calculateRotation(speed, degree, invert, offset, weight, walk, walkAmount);
    }

    /**
     * Rotates this box up and down (rotateAngleZ). Useful for wing and ears.
     *
     * @param speed      is how fast the model runs
     * @param degree     is how far the box will rotate;
     * @param invert     will invert the rotation
     * @param offset     will offset the timing of the model
     * @param weight     will make the model favor one direction more based on how fast the mob is moving
     * @param flap       is the flapped distance
     * @param flapAmount is the flap speed
     */
    public void flap(float speed, float degree, boolean invert, float offset, float weight, float flap, float flapAmount) {
        this.rotateAngleZ += this.calculateRotation(speed, degree, invert, offset, weight, flap, flapAmount);
    }

    /**
     * Rotates this box side to side (rotateAngleY).
     *
     * @param speed       is how fast the model runs
     * @param degree      is how far the box will rotate;
     * @param invert      will invert the rotation
     * @param offset      will offset the timing of the model
     * @param weight      will make the model favor one direction more based on how fast the mob is moving
     * @param swing       is the swung distance
     * @param swingAmount is the swing speed
     */
    public void swing(float speed, float degree, boolean invert, float offset, float weight, float swing, float swingAmount) {
        this.rotateAngleY += this.calculateRotation(speed, degree, invert, offset, weight, swing, swingAmount);
    }

    /**
     * Moves this box up and down (rotationPointY). Useful for bodies.
     *
     * @param speed  is how fast the model runs;
     * @param degree is how far the box will move;
     * @param bounce will make the box bounce;
     * @param f      is the walked distance;
     * @param f1     is the walk speed.
     */
    public void bob(float speed, float degree, boolean bounce, float f, float f1) {
        float movementScale = this.model.getMovementScale();
        degree *= movementScale;
        speed *= movementScale;
        double v = Math.sin(f * speed) * f1 * degree;
        float bob = (float) (v - f1 * degree);
        if (bounce) {
            bob = (float) -Math.abs(v);
        }
        this.rotationPointY += bob;
    }

    @Override
    public void setTextureOffset(int textureOffsetX, int textureOffsetY) {
        this.textureOffsetX = textureOffsetX;
        this.textureOffsetY = textureOffsetY;
    }
}