package com.github.alexthe666.citadel.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;


public class LinkButton extends ButtonWidget {
    public final ItemStack previewStack;
    public final GuiBasicBook book;

    public LinkButton(GuiBasicBook book, int x, int y, int width, int height, Text message, ItemStack previewStack, PressAction onPress) {
        super(x, y, width + (previewStack.isEmpty() ? 0 : 6), height, message, onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.previewStack = previewStack;
        this.book = book;
    }

    public LinkButton(GuiBasicBook book, int x, int y, int width, int height, Text message, PressAction onPress) {
        this(book, x, y, width, height, message, ItemStack.EMPTY, onPress);
    }

//    @Override
//    public int getFGColor() {
//        return this.hovered ? book.getWidgetColor() : this.active ? 0X94745A : 10526880;
//    }

    public static void drawTextOf(DrawContext guiGraphics, TextRenderer font, Text component, int x, int y, int color) {
        OrderedText text = component.asOrderedText();
        guiGraphics.drawText(font, text, x - font.getWidth(text) / 2, y, color, false);
    }

    @Override
    public int getTextureY() {
        int i = 1;
        if (!this.active) i = 0;
        else if (this.isSelected()) i = 2;
        return 46 + i * 20;
    }

    @Override
    public void renderButton(DrawContext guiGraphics, int guiX, int guiY, float partialTicks) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        TextRenderer font = minecraft.textRenderer;
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, this.book.getBookButtonsTexture());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getTextureY();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        guiGraphics.drawTexture(this.book.getBookButtonsTexture(), this.getX(), this.getY(), 0, 46 + i * 20, this.width / 2, this.height);
        guiGraphics.drawTexture(this.book.getBookButtonsTexture(), this.getX() + this.width / 2, this.getY(), 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        if (this.hovered) {
            int color = this.book.getWidgetColor();
            int r = (color & 0xFF0000) >> 16;
            int g = (color & 0xFF00) >> 8;
            int b = (color & 0xFF);
            i = 3;
            Identifier buttonsTexture = this.book.getBookButtonsTexture();
            int x = this.getX();
            int y = this.getY();
            guiGraphics.drawTexturedQuad(buttonsTexture, x, x + this.width / 2, y, y + this.height, 0, 0, ((float) this.width / 2) / 256.0F, (46 + i * 20) / 256.0F, (46 + i * 20 + this.height) / 256.0F, r, g, b, 255);
            guiGraphics.drawTexturedQuad(buttonsTexture, x + this.width / 2, x + this.width, y, y + this.height, 0, (200 - (float) this.width / 2) / 256.0F, 200 / 256.0F, (46 + i * 20) / 256.0F, (46 + i * 20 + this.height) / 256.0F, r, g, b, 255);
        }

//        int j = getFGColor();
        int j = -1;
        int itemTextOffset = this.previewStack.isEmpty() ? 0 : 8;
        if (!this.previewStack.isEmpty())
            guiGraphics.drawItem(this.previewStack, this.getX() + 2, this.getY() + 1);
        drawTextOf(guiGraphics, font, this.getMessage(), this.getX() + itemTextOffset + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0F));
    }
}
