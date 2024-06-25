package com.iafenvoy.iceandfire.screen.gui;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.screen.handler.PodiumScreenHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PodiumScreen extends HandledScreen<PodiumScreenHandler> {
    public static final Identifier PODUIM_TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/gui/podium.png");

    public PodiumScreen(PodiumScreenHandler container, PlayerInventory inv, Text name) {
        super(container, inv, name);
        this.backgroundHeight = 133;
    }

    @Override
    protected void drawForeground(DrawContext pGuiGraphics, int x, int y) {
        if (this.handler != null) {
            String s = I18n.translate("block.iceandfire.podium");
            assert this.client != null;
            pGuiGraphics.drawText(this.textRenderer, s, this.backgroundWidth / 2 - this.client.textRenderer.getWidth(s) / 2, 6, 4210752, false);
        }
        pGuiGraphics.drawText(this.textRenderer, this.playerInventoryTitle, 8, this.backgroundHeight - 96 + 2, 4210752, false);
    }

    @Override
    public void render(DrawContext pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.drawMouseoverTooltip(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext pGuiGraphics, float partialTicks, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        pGuiGraphics.drawTexture(PODUIM_TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }
}