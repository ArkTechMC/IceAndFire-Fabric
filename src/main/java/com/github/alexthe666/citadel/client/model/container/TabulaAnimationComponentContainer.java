package com.github.alexthe666.citadel.client.model.container;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gegy1000
 * @since 1.0.0
 */
public class TabulaAnimationComponentContainer {
    private String name;
    private String identifier;

    private int startKey;
    private int length;

    private final double[] posChange = new double[3];
    private final double[] rotChange = new double[3];
    private final double[] scaleChange = new double[3];
    private double opacityChange;

    private final double[] posOffset = new double[3];
    private final double[] rotOffset = new double[3];
    private final double[] scaleOffset = new double[3];
    private double opacityOffset;

    private final List<double[]> progressionCoords = new ArrayList<>();

    private boolean hidden;

    public String getName() {
        return this.name;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public int getStartKey() {
        return this.startKey;
    }

    public int getEndKey() {
        return this.startKey + this.length;
    }

    public int getLength() {
        return this.length;
    }

    public double[] getPositionChange() {
        return this.posChange;
    }

    public double[] getRotationChange() {
        return this.rotChange;
    }

    public double[] getScaleChange() {
        return this.scaleChange;
    }

    public double getOpacityChange() {
        return this.opacityChange;
    }

    public double[] getPositionOffset() {
        return this.posOffset;
    }

    public double[] getRotationOffset() {
        return this.rotOffset;
    }

    public double[] getScaleOffset() {
        return this.scaleOffset;
    }

    public double getOpacityOffset() {
        return this.opacityOffset;
    }

    public List<double[]> getProgressionCoords() {
        return this.progressionCoords;
    }

    public boolean isHidden() {
        return this.hidden;
    }
}
