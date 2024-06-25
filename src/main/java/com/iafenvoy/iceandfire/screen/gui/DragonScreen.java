package com.iafenvoy.iceandfire.screen.gui;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.screen.handler.DragonScreenHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;

public class DragonScreen extends HandledScreen<DragonScreenHandler> {
    private static final Identifier texture = new Identifier(IceAndFire.MOD_ID, "textures/gui/dragon.png");

    public DragonScreen(DragonScreenHandler dragonInv, PlayerInventory playerInv, Text name) {
        super(dragonInv, playerInv, name);
        this.backgroundHeight = 214;
    }

    @Override
    protected void drawForeground(DrawContext matrixStack, int mouseX, int mouseY) {
    }

    @Override
    public void render(DrawContext matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.drawMouseoverTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int k = (this.width - this.backgroundWidth) / 2;
        int l = (this.height - this.backgroundHeight) / 2;
        matrixStack.drawTexture(texture, k, l, 0, 0, this.backgroundWidth, this.backgroundHeight);
        assert MinecraftClient.getInstance().world != null;
        Entity entity = MinecraftClient.getInstance().world.getEntityById(this.handler.getDragonId());
        if (entity instanceof EntityDragonBase dragon) {
            float dragonScale = 1F / Math.max(0.0001F, dragon.getScaleFactor());
            Quaternionf quaternionf = (new Quaternionf()).rotateY((float) MathHelper.lerp((float) mouseX / this.width, 0, Math.PI)).rotateZ((float) MathHelper.lerp((float) mouseY / this.width, Math.PI, Math.PI + 0.2));
            InventoryScreen.drawEntity(matrixStack, k + 88, l + (int) (0.5F * (dragon.flyProgress)) + 55, (int) (dragonScale * 23F), quaternionf, null, dragon);
        }
        if (entity instanceof EntityDragonBase dragon) {
            assert this.client != null;
            TextRenderer textRenderer = this.client.textRenderer;
            String s3 = dragon.getCustomName() == null ? I18n.translate("dragon.unnamed") : I18n.translate("dragon.name") + " " + dragon.getCustomName().getString();
            textRenderer.draw(s3, k + (float) this.backgroundWidth / 2 - (float) textRenderer.getWidth(s3) / 2, l + 75, 0XFFFFFF, false, matrixStack.getMatrices().peek().getPositionMatrix(), matrixStack.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            String s2 = I18n.translate("dragon.health") + " " + Math.floor(Math.min(dragon.getHealth(), dragon.getMaxHealth())) + " / " + dragon.getMaxHealth();
            textRenderer.draw(s2, k + (float) this.backgroundWidth / 2 - (float) textRenderer.getWidth(s2) / 2, l + 84, 0XFFFFFF, false, matrixStack.getMatrices().peek().getPositionMatrix(), matrixStack.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            String s = (dragon.isMale() ? "dragon.gender.male" : "dragon.gender.female");
            String s5 = I18n.translate("dragon.gender") + I18n.translate(s);
            textRenderer.draw(s5, k + (float) this.backgroundWidth / 2 - (float) textRenderer.getWidth(s5) / 2, l + 93, 0XFFFFFF, false, matrixStack.getMatrices().peek().getPositionMatrix(), matrixStack.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            String s6 = I18n.translate("dragon.hunger") + dragon.getHunger() + "/100";
            textRenderer.draw(s6, k + (float) this.backgroundWidth / 2 - (float) textRenderer.getWidth(s6) / 2, l + 102, 0XFFFFFF, false, matrixStack.getMatrices().peek().getPositionMatrix(), matrixStack.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            String s4 = I18n.translate("dragon.stage") + " " + dragon.getDragonStage() + " " + I18n.translate("dragon.days.front") + dragon.getAgeInDays() + " " + I18n.translate("dragon.days.back");
            textRenderer.draw(s4, k + (float) this.backgroundWidth / 2 - (float) textRenderer.getWidth(s4) / 2, l + 111, 0XFFFFFF, false, matrixStack.getMatrices().peek().getPositionMatrix(), matrixStack.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            String s7 = dragon.getOwner() != null ? I18n.translate("dragon.owner") + dragon.getOwner().getName().getString() : I18n.translate("dragon.untamed");
            textRenderer.draw(s7, k + (float) this.backgroundWidth / 2 - (float) textRenderer.getWidth(s7) / 2, l + 120, 0XFFFFFF, false, matrixStack.getMatrices().peek().getPositionMatrix(), matrixStack.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
        }
    }
}