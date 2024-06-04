package com.github.alexthe666.citadel.client.event;

import com.iafenvoy.iafextra.event.Event;
import net.minecraft.client.gui.DrawContext;

public class EventRenderSplashText extends Event {
    private String splashText;

    private final DrawContext guiGraphics;
    private final float partialTicks;

    public EventRenderSplashText(String splashText, DrawContext guiGraphics, float partialTicks) {
        this.splashText = splashText;
        this.guiGraphics = guiGraphics;
        this.partialTicks = partialTicks;
    }

    public String getSplashText() {
        return splashText;
    }

    public void setSplashText(String splashText) {
        this.splashText = splashText;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public DrawContext getGuiGraphics() {
        return guiGraphics;
    }

    @Event.HasResult
    public static class Pre extends EventRenderSplashText {

        private int splashTextColor;

        public Pre(String splashText, DrawContext guiGraphics, float partialTicks, int splashTextColor) {
            super(splashText, guiGraphics, partialTicks);
            this.splashTextColor = splashTextColor;
        }

        public int getSplashTextColor() {
            return splashTextColor;
        }

        public void setSplashTextColor(int splashTextColor) {
            this.splashTextColor = splashTextColor;
        }
    }

    @Event.HasResult
    public static class Post extends EventRenderSplashText {

        public Post(String splashText, DrawContext guiGraphics, float partialTicks) {
            super(splashText, guiGraphics, partialTicks);
        }
    }

}
