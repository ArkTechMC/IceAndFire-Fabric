package com.github.alexthe666.citadel.client.gui.data;

public class LineData {
    private int xIndex;
    private int yIndex;
    private String text;
    private int page;

    public LineData(int xIndex, int yIndex, String text, int page) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.text = text;
        this.page = page;
    }

    public int getXIndex() {
        return this.xIndex;
    }

    public void setXIndex(int xIndex) {
        this.xIndex = xIndex;
    }

    public int getYIndex() {
        return this.yIndex;
    }

    public void setYIndex(int yIndex) {
        this.yIndex = yIndex;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
