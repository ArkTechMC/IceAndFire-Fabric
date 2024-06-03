package com.github.alexthe666.citadel.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;


public class LinkButton extends ButtonWidget {

    public ItemStack previewStack;
    public GuiBasicBook book;

    public LinkButton(GuiBasicBook book, int x, int y, int width, int height, Text component, ItemStack previewStack, PressAction onPress) {
        super(x, y, width + (previewStack.isEmpty() ? 0 : 6), height, component, onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.previewStack = previewStack;
        this.book = book;
    }

    public LinkButton(GuiBasicBook book, int x, int y, int width, int height, Text component, PressAction onPress) {
        this(book, x, y, width, height, component, ItemStack.EMPTY, onPress);
    }

    @Override
    public int getFGColor() {
        return this.hovered ? book.getWidgetColor() : this.active ? 0X94745A : 10526880;
    }

    private int getTextureY() {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (this.isSelected()) {
            i = 2;
        }

        return 46 + i * 20;
    }


    @Override
    public void renderButton(DrawContext guiGraphics, int guiX, int guiY, float partialTicks) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        TextRenderer font = minecraft.textRenderer;
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, book.getBookButtonsTexture());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getTextureY();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();



        guiGraphics.drawTexture(book.getBookButtonsTexture(), this.getX(), this.getY(), 0, 46 + i * 20, this.width / 2, this.height);
        guiGraphics.drawTexture(book.getBookButtonsTexture(), this.getX() + this.width / 2, this.getY(), 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        if(this.hovered){
            int color = book.getWidgetColor();
            int r = (color & 0xFF0000) >> 16;
            int g = (color & 0xFF00) >> 8;
            int b = (color & 0xFF);
            i = 3;
            BookBlit.blitWithColor(guiGraphics, book.getBookButtonsTexture(), this.getX(), this.getY(), 0, 46 + i * 20, this.width / 2, this.height, 256, 256, r, g, b, 255);
            BookBlit.blitWithColor(guiGraphics, book.getBookButtonsTexture(), this.getX() + this.width / 2, this.getY(), 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height, 256, 256, r, g, b, 255);
        }

        int j = getFGColor();
        int itemTextOffset = previewStack.isEmpty() ? 0 : 8;
        if(!previewStack.isEmpty()){
            ItemRenderer itemRenderer =  MinecraftClient.getInstance().getItemRenderer();
            guiGraphics.drawItem(previewStack, this.getX() + 2, this.getY() + 1);
        }
        drawTextOf(guiGraphics, font, this.getMessage(), this.getX() + itemTextOffset + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }

    public static void drawTextOf(DrawContext guiGraphics, TextRenderer font, Text component, int x, int y, int color) {
        OrderedText formattedcharsequence = component.asOrderedText();
        guiGraphics.drawText(font, formattedcharsequence, (float)(x - font.getWidth(formattedcharsequence) / 2), (float)y, color, false);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0F));
    }

}
