package com.github.alexthe666.citadel.client.gui;

import com.github.alexthe666.citadel.client.gui.data.EntityLinkData;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;


public class EntityLinkButton extends ButtonWidget {

    private static final Map<String, Entity> renderedEntities = new HashMap<>();
    private static final Quaternionf ENTITY_ROTATION = (new Quaternionf()).rotationXYZ((float) Math.toRadians(30), (float) Math.toRadians(130), (float) Math.PI);
    private final EntityLinkData data;
    private final GuiBasicBook bookGUI;

    public EntityLinkButton(GuiBasicBook bookGUI, EntityLinkData linkData, int x, int y, PressAction o) {
        super(x + linkData.getX() - 12, y + linkData.getY(), (int) (24 * linkData.getScale()), (int) (24 * linkData.getScale()), ScreenTexts.EMPTY, o, DEFAULT_NARRATION_SUPPLIER);
        this.data = linkData;
        this.bookGUI = bookGUI;
    }

    public void renderButton(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int lvt_5_1_ = 0;
        int lvt_6_1_ = 30;
        float f = (float) this.data.getScale();
        guiGraphics.getMatrices().push();
        guiGraphics.getMatrices().translate(this.getX(), this.getY(), 0);
        guiGraphics.getMatrices().scale(f, f, 1);
        this.drawBtn(false, guiGraphics, 0, 0, lvt_5_1_, lvt_6_1_, 24, 24);
        EntityType<?> type = Registries.ENTITY_TYPE.get(new Identifier(this.data.getEntity()));
        Entity model = renderedEntities.putIfAbsent(this.data.getEntity(), type.create(MinecraftClient.getInstance().world));

        guiGraphics.enableScissor(this.getX() + Math.round(f * 4), this.getY() + Math.round(f * 4), this.getX() + Math.round(f * 20), this.getY() + Math.round(f * 20));
        if (model != null) {
            if (MinecraftClient.getInstance().player != null)
                model.age = MinecraftClient.getInstance().player.age;
            float renderScale = (float) (this.data.getEntityScale() * f * 10);
            this.renderEntityInInventory(guiGraphics, 11 + (int) (this.data.getOffsetX() * this.data.getEntityScale()), 22 + (int) (this.data.getOffsetY() * this.data.getEntityScale()), renderScale, ENTITY_ROTATION, model);
        }
        guiGraphics.disableScissor();
        if (this.hovered) {
            this.bookGUI.setEntityTooltip(this.data.getHoverText());
            lvt_5_1_ = 48;
        } else {
            lvt_5_1_ = 24;
        }
        this.drawBtn(!this.hovered, guiGraphics, 0, 0, lvt_5_1_, lvt_6_1_, 24, 24);
        guiGraphics.getMatrices().pop();
    }

    public void drawBtn(boolean color, DrawContext guiGraphics, int x, int y, int u, int v, int width, int height) {
        int r, g, b;
        if (color) {
            int widgetColor = this.bookGUI.getWidgetColor();
            r = (widgetColor & 0xFF0000) >> 16;
            g = (widgetColor & 0xFF00) >> 8;
            b = (widgetColor & 0xFF);
        } else r = g = b = 255;
        guiGraphics.drawTexturedQuad(this.bookGUI.getBookWidgetTexture(), x, x + width, y, y + height, 0, u / 256.0F, (u + width) / 256.0F, v / 256.0F, (v + height) / 256.0F, r, g, b, 255);
    }


    public void renderEntityInInventory(DrawContext guiGraphics, int xPos, int yPos, float scale, Quaternionf rotation, Entity entity) {
        guiGraphics.getMatrices().push();
        guiGraphics.getMatrices().translate(xPos, yPos, 50.0D);
        guiGraphics.getMatrices().multiplyPositionMatrix((new Matrix4f()).scaling(scale, scale, (-scale)));
        guiGraphics.getMatrices().multiply(rotation);

        Vector3f light0 = new Vector3f(1, -1.0F, -1.0F).normalize();
        Vector3f light1 = new Vector3f(-1, 1.0F, 1.0F).normalize();
        RenderSystem.setShaderLights(light0, light1);
        EntityRenderDispatcher entityrenderdispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        entityrenderdispatcher.setRenderShadows(false);
        RenderSystem.runAsFancy(() -> entityrenderdispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, guiGraphics.getMatrices(), guiGraphics.getVertexConsumers(), 15728880));
        guiGraphics.draw();
        entityrenderdispatcher.setRenderShadows(true);
        guiGraphics.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }
}
