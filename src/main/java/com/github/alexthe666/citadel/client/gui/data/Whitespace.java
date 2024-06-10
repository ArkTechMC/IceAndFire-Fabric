package com.github.alexthe666.citadel.client.gui.data;

public class Whitespace {
    private final int page;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private boolean down = false;

    public Whitespace(int page, int x, int y, int width, int height) {
        this.page = page;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Whitespace(int page, int x, int y, int width, int height, boolean down) {
        this.page = page;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.down = down;
    }

    public int getPage() {
        return this.page;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isDown() {
        return this.down;
    }

    public void setDown(boolean downIn) {
        this.down = downIn;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Whitespace ws)
            return ws.x == this.x && ws.y == this.y && ws.height == this.height && ws.width == this.width && ws.down == this.down;
        return false;
    }
}
