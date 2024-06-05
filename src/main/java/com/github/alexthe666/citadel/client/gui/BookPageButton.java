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

    public BookPageButton(GuiBasicBook bookGUI, int p_i51079_1_, int p_i51079_2_, boolean p_i51079_3_, PressAction p_i51079_4_, boolean p_i51079_5_) {
        super(p_i51079_1_, p_i51079_2_, 23, 13, ScreenTexts.EMPTY, p_i51079_4_, DEFAULT_NARRATION_SUPPLIER);
        this.isForward = p_i51079_3_;
        this.playTurnSound = p_i51079_5_;
        this.bookGUI = bookGUI;
    }

    public void renderButton(DrawContext p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
        int lvt_5_1_ = 0;
        int lvt_6_1_ = 0;
        if (this.hovered) {
            lvt_5_1_ += 23;
        }
        if (!this.isForward) {
            lvt_6_1_ += 13;
        }
        this.drawNextArrow(p_230431_1_, this.getX(), this.getY(), lvt_5_1_, lvt_6_1_, 18, 12);
    }

    public void drawNextArrow(DrawContext p_238474_1_, int p_238474_2_, int p_238474_3_, int p_238474_4_, int p_238474_5_, int p_238474_6_, int p_238474_7_) {
        if (this.hovered) {
            int color = this.bookGUI.getWidgetColor();
            int r = (color & 0xFF0000) >> 16;
            int g = (color & 0xFF00) >> 8;
            int b = (color & 0xFF);
            BookBlit.blitWithColor(p_238474_1_, this.bookGUI.getBookWidgetTexture(), p_238474_2_, p_238474_3_, 100, p_238474_4_, p_238474_5_, p_238474_6_, p_238474_7_, 256, 256, r, g, b, 255);
        } else {
            BookBlit.blitWithColor(p_238474_1_, this.bookGUI.getBookWidgetTexture(), p_238474_2_, p_238474_3_, 100, p_238474_4_, p_238474_5_, p_238474_6_, p_238474_7_, 256, 256, 255, 255, 255, 255);
        }
    }

    public void playDownSound(SoundManager p_230988_1_) {
        if (this.playTurnSound) {
            p_230988_1_.play(PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0F));
        }

    }
}
