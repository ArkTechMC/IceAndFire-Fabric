package com.iafenvoy.iceandfire.screen.gui;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityHippogryph;
import com.iafenvoy.iceandfire.screen.handler.HippogryphScreenHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

//TODO: We do the same thing here as we do for the other GUI entity screens, that's dumb
public class HippogryphScreen extends HandledScreen<HippogryphScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/gui/hippogryph.png");
    private float mousePosx;
    private float mousePosY;

    public HippogryphScreen(HippogryphScreenHandler dragonInv, PlayerInventory playerInv, Text name) {
        super(dragonInv, playerInv, name);
    }

    @Override
    protected void drawForeground(DrawContext pGuiGraphics, int mouseX, int mouseY) {
        assert MinecraftClient.getInstance().world != null;
        Entity entity = MinecraftClient.getInstance().world.getEntityById(this.handler.getHippogryphId());
        assert this.client != null;
        TextRenderer font = this.client.textRenderer;
        if (entity instanceof EntityHippogryph hippo)
            pGuiGraphics.drawText(font, hippo.getDisplayName().getString(), 8, 6, 4210752, false);
        pGuiGraphics.drawText(font, this.playerInventoryTitle, 8, this.backgroundHeight - 96 + 2, 4210752, false);
    }

    @Override
    public void render(DrawContext pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(pGuiGraphics);
        this.mousePosx = mouseX;
        this.mousePosY = mouseY;
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.drawMouseoverTooltip(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        pGuiGraphics.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        assert MinecraftClient.getInstance().world != null;
        Entity entity = MinecraftClient.getInstance().world.getEntityById(this.handler.getHippogryphId());
        if (entity instanceof EntityHippogryph hippo) {
            if (hippo.isChested())
                pGuiGraphics.drawTexture(TEXTURE, i + 79, j + 17, 0, this.backgroundHeight, 5 * 18, 54);
            InventoryScreen.drawEntity(pGuiGraphics, i + 51, j + 60, 17, i + 51 - this.mousePosx, j + 75 - 50 - this.mousePosY, hippo);
        }
    }
}