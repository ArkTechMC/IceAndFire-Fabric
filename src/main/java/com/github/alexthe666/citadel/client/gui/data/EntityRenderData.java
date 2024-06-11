package com.github.alexthe666.citadel.client.gui.data;

public class EntityRenderData {
    private final String entityData;
    private String entity;
    private int x;
    private int y;
    private double scale;
    private int page;
    private double rotX;
    private double rotY;
    private double rotZ;
    private boolean followCursor;

    public EntityRenderData(String entity, int x, int y, double scale, int page, double rotX, double rotY, double rotZ, boolean followCursor, String entityData) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.page = page;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.followCursor = followCursor;
        this.entityData = entityData;
    }

    public String getEntity() {
        return this.entity;
    }

    public void setEntity(String model) {
        this.entity = model;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getScale() {
        return this.scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public double getRotX() {
        return this.rotX;
    }

    public void setRotX(double rotX) {
        this.rotX = rotX;
    }

    public double getRotY() {
        return this.rotY;
    }

    public void setRotY(double rotY) {
        this.rotY = rotY;
    }

    public double getRotZ() {
        return this.rotZ;
    }

    public void setRotZ(double rotZ) {
        this.rotZ = rotZ;
    }

    public boolean isFollowCursor() {
        return this.followCursor;
    }

    public void setFollowCursor(boolean followCursor) {
        this.followCursor = followCursor;
    }

    public String getEntityData() {
        return this.entityData;
    }

}
