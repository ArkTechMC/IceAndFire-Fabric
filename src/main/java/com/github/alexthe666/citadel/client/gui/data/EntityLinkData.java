package com.github.alexthe666.citadel.client.gui.data;

public class EntityLinkData {
    private String entity;
    private int x;
    private int y;
    private float offsetX;
    private float offsetY;
    private double entityScale;
    private double scale;
    private int page;
    private String linkedPage;
    private String hoverText;

    public EntityLinkData(String entity, int x, int y, double scale, double entityScale, int page, String linkedPage, String hoverText, float offsetX, float offsetY) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.entityScale = entityScale;
        this.page = page;
        this.linkedPage = linkedPage;
        this.hoverText = hoverText;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
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

    public double getEntityScale() {
        return this.entityScale;
    }

    public void setEntityScale(double scale) {
        this.entityScale = scale;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getLinkedPage() {
        return this.linkedPage;
    }

    public void setLinkedPage(String linkedPage) {
        this.linkedPage = linkedPage;
    }

    public String getHoverText() {
        return this.hoverText;
    }

    public void setHoverText(String titleText) {
        this.hoverText = titleText;
    }

    public float getOffsetY() {
        return this.offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public float getOffsetX() {
        return this.offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }
}
