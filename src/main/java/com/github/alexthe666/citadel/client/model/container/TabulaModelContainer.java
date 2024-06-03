package com.github.alexthe666.citadel.client.model.container;


import java.util.ArrayList;
import java.util.List;

/**
 * @author gegy1000
 * @since 1.0.0
 */
public class TabulaModelContainer {
    private final String modelName;
    private final String authorName;

    private final int projVersion;
    private String[] metadata;

    private final double[] scale = new double[]{1.0, 1.0, 1.0};

    private final int textureWidth;
    private final int textureHeight;

    private final List<TabulaCubeGroupContainer> cubeGroups = new ArrayList<>();
    private List<TabulaCubeContainer> cubes = new ArrayList<>();
    private final List<TabulaAnimationContainer> anims = new ArrayList<>();
    private int cubeCount;

    public TabulaModelContainer(String name, String author, int textureWidth, int textureHeight, List<TabulaCubeContainer> cubes, int projectVersion) {
        this.modelName = name;
        this.authorName = author;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.cubes = cubes;
        this.cubes.forEach(this::incrementCubeCount);
        this.projVersion = projectVersion;
    }

    private void incrementCubeCount(TabulaCubeContainer cube) {
        this.cubeCount++;
        cube.getChildren().forEach(this::incrementCubeCount);
    }

    public String getName() {
        return this.modelName;
    }

    public String getAuthor() {
        return this.authorName;
    }

    public int getProjectVersion() {
        return this.projVersion;
    }

    public String[] getMetadata() {
        return this.metadata;
    }

    public int getTextureWidth() {
        return this.textureWidth;
    }

    public int getTextureHeight() {
        return this.textureHeight;
    }

    public double[] getScale() {
        return this.scale;
    }

    public List<TabulaCubeGroupContainer> getCubeGroups() {
        return this.cubeGroups;
    }

    public List<TabulaCubeContainer> getCubes() {
        return this.cubes;
    }

    public List<TabulaAnimationContainer> getAnimations() {
        return this.anims;
    }

    public int getCubeCount() {
        return this.cubeCount;
    }
}
