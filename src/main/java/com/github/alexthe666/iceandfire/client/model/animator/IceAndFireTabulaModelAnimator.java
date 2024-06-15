package com.github.alexthe666.iceandfire.client.model.animator;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.util.math.MathHelper;

public class IceAndFireTabulaModelAnimator {
    protected final TabulaModel baseModel;

    public IceAndFireTabulaModelAnimator(TabulaModel baseModel) {
        this.baseModel = baseModel;
    }

    public void setRotateAngle(AdvancedModelBox model, float limbSwingAmount, float x, float y, float z) {
        model.rotateAngleX += limbSwingAmount * this.distance(model.rotateAngleX, x);
        model.rotateAngleY += limbSwingAmount * this.distance(model.rotateAngleY, y);
        model.rotateAngleZ += limbSwingAmount * this.distance(model.rotateAngleZ, z);
    }

    public void addToRotateAngle(AdvancedModelBox model, float limbSwingAmount, float x, float y, float z) {
        model.rotateAngleX += Math.min(limbSwingAmount * 2, 1) * this.distance(model.defaultRotationX, x);
        model.rotateAngleY += Math.min(limbSwingAmount * 2, 1) * this.distance(model.defaultRotationY, y);
        model.rotateAngleZ += Math.min(limbSwingAmount * 2, 1) * this.distance(model.defaultRotationZ, z);
    }

    public boolean isRotationEqual(AdvancedModelBox original, AdvancedModelBox pose) {
        return pose != null && pose.rotateAngleX == original.defaultRotationX && pose.rotateAngleY == original.defaultRotationY && pose.rotateAngleZ == original.defaultRotationZ;
    }

    public boolean isPositionEqual(AdvancedModelBox original, AdvancedModelBox pose) {
        return pose.rotationPointX == original.defaultPositionX && pose.rotationPointY == original.defaultPositionY && pose.rotationPointZ == original.defaultPositionZ;
    }

    public void transitionTo(AdvancedModelBox from, AdvancedModelBox to, float timer, float maxTime, boolean oldFashioned) {
        if (oldFashioned) {
            from.rotateAngleX += ((to.rotateAngleX - from.rotateAngleX) / maxTime) * timer;
            from.rotateAngleY += ((to.rotateAngleY - from.rotateAngleY) / maxTime) * timer;
            from.rotateAngleZ += ((to.rotateAngleZ - from.rotateAngleZ) / maxTime) * timer;
        } else
            this.transitionAngles(from, to, timer, maxTime);
        from.rotationPointX += ((to.rotationPointX - from.rotationPointX) / maxTime) * timer;
        from.rotationPointY += ((to.rotationPointY - from.rotationPointY) / maxTime) * timer;
        from.rotationPointZ += ((to.rotationPointZ - from.rotationPointZ) / maxTime) * timer;
    }

    public void transitionAngles(AdvancedModelBox from, AdvancedModelBox to, float timer, float maxTime) {
        from.rotateAngleX += ((this.distance(from.rotateAngleX, to.rotateAngleX)) / maxTime) * timer;
        from.rotateAngleY += ((this.distance(from.rotateAngleY, to.rotateAngleY)) / maxTime) * timer;
        from.rotateAngleZ += ((this.distance(from.rotateAngleZ, to.rotateAngleZ)) / maxTime) * timer;
    }

    public float distance(float rotateAngleFrom, float rotateAngleTo) {
        return (float) IAFMath.atan2_accurate(MathHelper.sin(rotateAngleTo - rotateAngleFrom), MathHelper.cos(rotateAngleTo - rotateAngleFrom));
    }

    public void rotate(ModelAnimator animator, AdvancedModelBox model, float x, float y, float z) {
        animator.rotate(model, (float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
    }

    public void moveToPose(TabulaModel model, TabulaModel modelTo) {
        for (AdvancedModelBox cube : model.getCubes().values()) {
            AdvancedModelBox cubeTo = modelTo.getCube(cube.boxName);
            if (!this.isRotationEqual(this.baseModel.getCube(cube.boxName), cubeTo)) {
                float toX = cubeTo.rotateAngleX;
                float toY = cubeTo.rotateAngleY;
                float toZ = cubeTo.rotateAngleZ;
                model.llibAnimator.rotate(cube, this.distance(cube.rotateAngleX, toX), this.distance(cube.rotateAngleY, toY), this.distance(cube.rotateAngleZ, toZ));
            }
            if (!this.isPositionEqual(this.baseModel.getCube(cube.boxName), cubeTo)) {
                float toX = cubeTo.rotationPointX;
                float toY = cubeTo.rotationPointY;
                float toZ = cubeTo.rotationPointZ;
                model.llibAnimator.move(cube, toX - cube.rotationPointX, toY - cube.rotationPointY, toZ - cube.rotationPointZ);
            }
        }
    }
}
