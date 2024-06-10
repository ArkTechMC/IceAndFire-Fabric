package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.inventory.ContainerDragon;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class GuiDragon extends HandledScreen<ContainerDragon> {
    private static final Identifier texture = new Identifier(IceAndFire.MOD_ID, "textures/gui/dragon.png");

    public GuiDragon(ContainerDragon dragonInv, PlayerInventory playerInv, Text name) {
        super(dragonInv, playerInv, name);
        this.backgroundHeight = 214;
    }

    @Override
    protected void drawForeground(@NotNull DrawContext matrixStack, int mouseX, int mouseY) {

    }

    @Override
    public void render(@NotNull DrawContext matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.drawMouseoverTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(@NotNull DrawContext matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int k = (this.width - this.backgroundWidth) / 2;
        int l = (this.height - this.backgroundHeight) / 2;
        matrixStack.drawTexture(texture, k, l, 0, 0, this.backgroundWidth, this.backgroundHeight);
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        if (entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;
            float dragonScale = 1F / Math.max(0.0001F, dragon.getScaleFactor());
            Quaternionf quaternionf = (new Quaternionf()).rotateY((float) MathHelper.lerp((float) mouseX / this.width, 0, Math.PI)).rotateZ((float) MathHelper.lerp((float) mouseY / this.width, Math.PI, Math.PI + 0.2));
            InventoryScreen.drawEntity(matrixStack, k + 88, l + (int) (0.5F * (dragon.flyProgress)) + 55, (int) (dragonScale * 23F), quaternionf, null, dragon);
        }
        if (entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;

            TextRenderer font = this.client.textRenderer;
            String s3 = dragon.getCustomName() == null ? StatCollector.translateToLocal("dragon.unnamed") : StatCollector.translateToLocal("dragon.name") + " " + dragon.getCustomName().getString();
            font.draw(s3, k + this.backgroundWidth / 2 - font.getWidth(s3) / 2, l + 75, 0XFFFFFF, false, matrixStack.getMatrices().peek().getPositionMatrix(), matrixStack.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            String s2 = StatCollector.translateToLocal("dragon.health") + " " + Math.floor(Math.min(dragon.getHealth(), dragon.getMaxHealth())) + " / " + dragon.getMaxHealth();
            font.draw(s2, k + this.backgroundWidth / 2 - font.getWidth(s2) / 2, l + 84, 0XFFFFFF, false, matrixStack.getMatrices().peek().getPositionMatrix(), matrixStack.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            String s5 = StatCollector.translateToLocal("dragon.gender") + StatCollector.translateToLocal((dragon.isMale() ? "dragon.gender.male" : "dragon.gender.female"));
            font.draw(s5, k + this.backgroundWidth / 2 - font.getWidth(s5) / 2, l + 93, 0XFFFFFF, false, matrixStack.getMatrices().peek().getPositionMatrix(), matrixStack.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            String s6 = StatCollector.translateToLocal("dragon.hunger") + dragon.getHunger() + "/100";
            font.draw(s6, k + this.backgroundWidth / 2 - font.getWidth(s6) / 2, l + 102, 0XFFFFFF, false, matrixStack.getMatrices().peek().getPositionMatrix(), matrixStack.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            String s4 = StatCollector.translateToLocal("dragon.stage") + " " + dragon.getDragonStage() + " " + StatCollector.translateToLocal("dragon.days.front") + dragon.getAgeInDays() + " " + StatCollector.translateToLocal("dragon.days.back");
            font.draw(s4, k + this.backgroundWidth / 2 - font.getWidth(s4) / 2, l + 111, 0XFFFFFF, false, matrixStack.getMatrices().peek().getPositionMatrix(), matrixStack.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            String s7 = dragon.getOwner() != null ? StatCollector.translateToLocal("dragon.owner") + dragon.getOwner().getName().getString() : StatCollector.translateToLocal("dragon.untamed");
            font.draw(s7, k + this.backgroundWidth / 2 - font.getWidth(s7) / 2, l + 120, 0XFFFFFF, false, matrixStack.getMatrices().peek().getPositionMatrix(), matrixStack.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
        }
    }


}