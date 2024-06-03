package com.github.alexthe666.citadel.client.model.basic;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

/*
 * @since 1.9.0
 * Duplicate of ModelPart class which is not final
 */
@Environment(EnvType.CLIENT)
public class BasicModelPart {
    public float textureWidth = 64.0F;
    public float textureHeight = 32.0F;
    public int textureOffsetX;
    public int textureOffsetY;
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;
    public boolean mirror;
    public boolean showModel = true;
    private final ObjectList<ModelBox> cubeList = new ObjectArrayList<>();
    private final ObjectList<BasicModelPart> childModels = new ObjectArrayList<>();

    public BasicModelPart(BasicEntityModel model) {
        this.setTextureSize(model.textureWidth, model.textureHeight);
    }

    public BasicModelPart(BasicEntityModel model, int texOffX, int texOffY) {
        this(model.textureWidth, model.textureHeight, texOffX, texOffY);
    }

    public BasicModelPart(int textureWidthIn, int textureHeightIn, int textureOffsetXIn, int textureOffsetYIn) {
        this.setTextureSize(textureWidthIn, textureHeightIn);
        this.setTextureOffset(textureOffsetXIn, textureOffsetYIn);
    }

    private BasicModelPart() {
    }

    public BasicModelPart getModelAngleCopy() {
        BasicModelPart BasicModelPart = new BasicModelPart();
        BasicModelPart.copyModelAngles(this);
        return BasicModelPart;
    }

    public void copyModelAngles(BasicModelPart BasicModelPartIn) {
        this.rotateAngleX = BasicModelPartIn.rotateAngleX;
        this.rotateAngleY = BasicModelPartIn.rotateAngleY;
        this.rotateAngleZ = BasicModelPartIn.rotateAngleZ;
        this.rotationPointX = BasicModelPartIn.rotationPointX;
        this.rotationPointY = BasicModelPartIn.rotationPointY;
        this.rotationPointZ = BasicModelPartIn.rotationPointZ;
    }

    /**
     * Sets the current box's rotation points and rotation angles to another box.
     */
    public void addChild(BasicModelPart renderer) {
        this.childModels.add(renderer);
    }

    public BasicModelPart setTextureOffset(int x, int y) {
        this.textureOffsetX = x;
        this.textureOffsetY = y;
        return this;
    }

    public BasicModelPart addBox(String partName, float x, float y, float z, int width, int height, int depth, float delta, int texX, int texY) {
        this.setTextureOffset(texX, texY);
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, (float)width, (float)height, (float)depth, delta, delta, delta, this.mirror, false);
        return this;
    }

    public BasicModelPart addBox(float x, float y, float z, float width, float height, float depth) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, 0.0F, 0.0F, 0.0F, this.mirror, false);
        return this;
    }

    public BasicModelPart addBox(float x, float y, float z, float width, float height, float depth, boolean mirrorIn) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, 0.0F, 0.0F, 0.0F, mirrorIn, false);
        return this;
    }

    public void addBox(float x, float y, float z, float width, float height, float depth, float delta) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, delta, delta, delta, this.mirror, false);
    }

    public void addBox(float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, deltaX, deltaY, deltaZ, this.mirror, false);
    }

    public void addBox(float x, float y, float z, float width, float height, float depth, float delta, boolean mirrorIn) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, delta, delta, delta, mirrorIn, false);
    }

    private void addBox(int texOffX, int texOffY, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirorIn, boolean p_228305_13_) {
        this.cubeList.add(new ModelBox(texOffX, texOffY, x, y, z, width, height, depth, deltaX, deltaY, deltaZ, mirorIn, this.textureWidth, this.textureHeight));
    }

    public void setRotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
        this.rotationPointX = rotationPointXIn;
        this.rotationPointY = rotationPointYIn;
        this.rotationPointZ = rotationPointZIn;
    }

    public void render(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn) {
        this.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void render(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.showModel) {
            if (!this.cubeList.isEmpty() || !this.childModels.isEmpty()) {
                matrixStackIn.push();
                this.translateRotate(matrixStackIn);
                this.doRender(matrixStackIn.peek(), bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);

                for(BasicModelPart BasicModelPart : this.childModels) {
                    BasicModelPart.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
                }

                matrixStackIn.pop();
            }
        }
    }

    public void translateRotate(MatrixStack matrixStackIn) {
        matrixStackIn.translate(this.rotationPointX / 16.0F, this.rotationPointY / 16.0F, (double)(this.rotationPointZ / 16.0F));
        if (this.rotateAngleZ != 0.0F) {
            matrixStackIn.multiply(RotationAxis.POSITIVE_Z.rotation(this.rotateAngleZ));
        }

        if (this.rotateAngleY != 0.0F) {
            matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotation(this.rotateAngleY));
        }

        if (this.rotateAngleX != 0.0F) {
            matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotation(this.rotateAngleX));
        }

    }

    private void doRender(MatrixStack.Entry matrixEntryIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        Matrix4f matrix4f = matrixEntryIn.getPositionMatrix();
        Matrix3f matrix3f = matrixEntryIn.getNormalMatrix();

        for(ModelBox BasicModelPart$modelbox : this.cubeList) {
            for(TexturedQuad BasicModelPart$texturedquad : BasicModelPart$modelbox.quads) {
                Vector3f vector3f = new Vector3f(BasicModelPart$texturedquad.normal);
                vector3f.mul(matrix3f);
                float f = vector3f.x();
                float f1 = vector3f.y();
                float f2 = vector3f.z();

                for(int i = 0; i < 4; ++i) {
                    PositionTextureVertex BasicModelPart$positiontexturevertex = BasicModelPart$texturedquad.vertexPositions[i];
                    float f3 = BasicModelPart$positiontexturevertex.position.x() / 16.0F;
                    float f4 = BasicModelPart$positiontexturevertex.position.y() / 16.0F;
                    float f5 = BasicModelPart$positiontexturevertex.position.z() / 16.0F;
                    Vector4f vector4f = new Vector4f(f3, f4, f5, 1.0F);
                    vector4f.mul(matrix4f);
                    bufferIn.vertex(vector4f.x(), vector4f.y(), vector4f.z(), red, green, blue, alpha, BasicModelPart$positiontexturevertex.textureU, BasicModelPart$positiontexturevertex.textureV, packedOverlayIn, packedLightIn, f, f1, f2);
                }
            }
        }

    }

    /**
     * Returns the model renderer with the new texture parameters.
     */
    public BasicModelPart setTextureSize(int textureWidthIn, int textureHeightIn) {
        this.textureWidth = (float)textureWidthIn;
        this.textureHeight = (float)textureHeightIn;
        return this;
    }

    public ModelBox getRandomCube(net.minecraft.util.math.random.Random randomIn) {
        return this.cubeList.size() > 0 ? this.cubeList.get(randomIn.nextInt(this.cubeList.size())) : null;
    }

    @Environment(EnvType.CLIENT)
    public static class ModelBox {
        private final TexturedQuad[] quads;
        public final float posX1;
        public final float posY1;
        public final float posZ1;
        public final float posX2;
        public final float posY2;
        public final float posZ2;

        public ModelBox(int texOffX, int texOffY, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirorIn, float texWidth, float texHeight) {
            this.posX1 = x;
            this.posY1 = y;
            this.posZ1 = z;
            this.posX2 = x + width;
            this.posY2 = y + height;
            this.posZ2 = z + depth;
            this.quads = new TexturedQuad[6];
            float f = x + width;
            float f1 = y + height;
            float f2 = z + depth;
            x = x - deltaX;
            y = y - deltaY;
            z = z - deltaZ;
            f = f + deltaX;
            f1 = f1 + deltaY;
            f2 = f2 + deltaZ;
            if (mirorIn) {
                float f3 = f;
                f = x;
                x = f3;
            }

            PositionTextureVertex BasicModelPart$positiontexturevertex7 = new PositionTextureVertex(x, y, z, 0.0F, 0.0F);
            PositionTextureVertex BasicModelPart$positiontexturevertex = new PositionTextureVertex(f, y, z, 0.0F, 8.0F);
            PositionTextureVertex BasicModelPart$positiontexturevertex1 = new PositionTextureVertex(f, f1, z, 8.0F, 8.0F);
            PositionTextureVertex BasicModelPart$positiontexturevertex2 = new PositionTextureVertex(x, f1, z, 8.0F, 0.0F);
            PositionTextureVertex BasicModelPart$positiontexturevertex3 = new PositionTextureVertex(x, y, f2, 0.0F, 0.0F);
            PositionTextureVertex BasicModelPart$positiontexturevertex4 = new PositionTextureVertex(f, y, f2, 0.0F, 8.0F);
            PositionTextureVertex BasicModelPart$positiontexturevertex5 = new PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
            PositionTextureVertex BasicModelPart$positiontexturevertex6 = new PositionTextureVertex(x, f1, f2, 8.0F, 0.0F);
            float f4 = (float)texOffX;
            float f5 = (float)texOffX + depth;
            float f6 = (float)texOffX + depth + width;
            float f7 = (float)texOffX + depth + width + width;
            float f8 = (float)texOffX + depth + width + depth;
            float f9 = (float)texOffX + depth + width + depth + width;
            float f10 = (float)texOffY;
            float f11 = (float)texOffY + depth;
            float f12 = (float)texOffY + depth + height;
            this.quads[2] = new TexturedQuad(new PositionTextureVertex[]{BasicModelPart$positiontexturevertex4, BasicModelPart$positiontexturevertex3, BasicModelPart$positiontexturevertex7, BasicModelPart$positiontexturevertex}, f5, f10, f6, f11, texWidth, texHeight, mirorIn, Direction.DOWN);
            this.quads[3] = new TexturedQuad(new PositionTextureVertex[]{BasicModelPart$positiontexturevertex1, BasicModelPart$positiontexturevertex2, BasicModelPart$positiontexturevertex6, BasicModelPart$positiontexturevertex5}, f6, f11, f7, f10, texWidth, texHeight, mirorIn, Direction.UP);
            this.quads[1] = new TexturedQuad(new PositionTextureVertex[]{BasicModelPart$positiontexturevertex7, BasicModelPart$positiontexturevertex3, BasicModelPart$positiontexturevertex6, BasicModelPart$positiontexturevertex2}, f4, f11, f5, f12, texWidth, texHeight, mirorIn, Direction.WEST);
            this.quads[4] = new TexturedQuad(new PositionTextureVertex[]{BasicModelPart$positiontexturevertex, BasicModelPart$positiontexturevertex7, BasicModelPart$positiontexturevertex2, BasicModelPart$positiontexturevertex1}, f5, f11, f6, f12, texWidth, texHeight, mirorIn, Direction.NORTH);
            this.quads[0] = new TexturedQuad(new PositionTextureVertex[]{BasicModelPart$positiontexturevertex4, BasicModelPart$positiontexturevertex, BasicModelPart$positiontexturevertex1, BasicModelPart$positiontexturevertex5}, f6, f11, f8, f12, texWidth, texHeight, mirorIn, Direction.EAST);
            this.quads[5] = new TexturedQuad(new PositionTextureVertex[]{BasicModelPart$positiontexturevertex3, BasicModelPart$positiontexturevertex4, BasicModelPart$positiontexturevertex5, BasicModelPart$positiontexturevertex6}, f8, f11, f9, f12, texWidth, texHeight, mirorIn, Direction.SOUTH);
        }
    }

    @Environment(EnvType.CLIENT)
    static class PositionTextureVertex {
        public final Vector3f position;
        public final float textureU;
        public final float textureV;

        public PositionTextureVertex(float x, float y, float z, float texU, float texV) {
            this(new Vector3f(x, y, z), texU, texV);
        }

        public PositionTextureVertex setTextureUV(float texU, float texV) {
            return new PositionTextureVertex(this.position, texU, texV);
        }

        public PositionTextureVertex(Vector3f posIn, float texU, float texV) {
            this.position = posIn;
            this.textureU = texU;
            this.textureV = texV;
        }
    }

    @Environment(EnvType.CLIENT)
    static class TexturedQuad {
        public final PositionTextureVertex[] vertexPositions;
        public final Vector3f normal;

        public TexturedQuad(PositionTextureVertex[] positionsIn, float u1, float v1, float u2, float v2, float texWidth, float texHeight, boolean mirrorIn, Direction directionIn) {
            this.vertexPositions = positionsIn;
            float f = 0.0F / texWidth;
            float f1 = 0.0F / texHeight;
            positionsIn[0] = positionsIn[0].setTextureUV(u2 / texWidth - f, v1 / texHeight + f1);
            positionsIn[1] = positionsIn[1].setTextureUV(u1 / texWidth + f, v1 / texHeight + f1);
            positionsIn[2] = positionsIn[2].setTextureUV(u1 / texWidth + f, v2 / texHeight - f1);
            positionsIn[3] = positionsIn[3].setTextureUV(u2 / texWidth - f, v2 / texHeight - f1);
            if (mirrorIn) {
                int i = positionsIn.length;

                for(int j = 0; j < i / 2; ++j) {
                    PositionTextureVertex BasicModelPart$positiontexturevertex = positionsIn[j];
                    positionsIn[j] = positionsIn[i - 1 - j];
                    positionsIn[i - 1 - j] = BasicModelPart$positiontexturevertex;
                }
            }

            this.normal = directionIn.getUnitVector();
            if (mirrorIn) {
                this.normal.mul(-1.0F, 1.0F, 1.0F);
            }

        }
    }
}