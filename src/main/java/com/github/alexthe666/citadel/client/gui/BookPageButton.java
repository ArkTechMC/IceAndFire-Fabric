package com.github.alexthe666.citadel.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundEvents;

@Environment(EnvType.CLIENT)
public class BookPageButton extends ButtonWidget {
    private final boolean isForward;
    private final boolean playTurnSound;
    private final GuiBasicBook bookGUI;

    public BookPageButton(GuiBasicBook bookGUI, int x, int y, boolean isForward, PressAction onPress, boolean playTurnSound) {
        super(x, y, 23, 13, ScreenTexts.EMPTY, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.isForward = isForward;
        this.playTurnSound = playTurnSound;
        this.bookGUI = bookGUI;
    }

    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        int u = 0;
        int v = 0;
        if (this.hovered) u += 23;
        if (!this.isForward) v += 13;
        this.drawNextArrow(context, this.getX(), this.getY(), u, v, 18, 12);
    }

    public void drawNextArrow(DrawContext context, int x, int y, int u, int v, int width, int height) {
        int r, g, b;
        if (this.hovered) {
            int color = this.bookGUI.getWidgetColor();
            r = (color & 0xFF0000) >> 16;
            g = (color & 0xFF00) >> 8;
            b = (color & 0xFF);
        } else r = g = b = 255;
        context.drawTexturedQuad(this.bookGUI.getBookWidgetTexture(), x, x + width, y, y + height, 100, u / 256.0F, (u + width) / 256.0F, (v) / 256.0F, (v + height) / 256.0F, r, g, b, 255);
    }

    public void playDownSound(SoundManager soundManager) {
        if (this.playTurnSound)
            soundManager.play(PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0F));
    }
}
