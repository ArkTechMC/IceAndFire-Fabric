package com.github.alexthe666.citadel.client.gui.data;

public class ItemRenderData {
    private final int page;
    private String item;
    private String item_tag = "";
    private int x;
    private int y;
    private double scale;

    public ItemRenderData(String item, int x, int y, double scale, int page) {
        this.item = item;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.page = page;
    }

    public String getItem() {
        return this.item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItemTag() {
        return this.item_tag;
    }

    public void setItemTag(String item) {
        this.item_tag = this.item_tag;
    }

    public int getPage() {
        return this.page;
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
}
