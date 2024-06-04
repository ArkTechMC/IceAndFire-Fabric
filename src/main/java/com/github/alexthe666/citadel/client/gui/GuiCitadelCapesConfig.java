package com.github.alexthe666.citadel.client.gui;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.ClientProxy;
import com.github.alexthe666.citadel.client.rewards.CitadelCapes;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class GuiCitadelCapesConfig extends GameOptionsScreen {
    private String capeType;
    private ButtonWidget button;


    public GuiCitadelCapesConfig(Screen parentScreenIn, GameOptions gameSettingsIn) {
        super(parentScreenIn, gameSettingsIn, Text.translatable("citadel.gui.capes"));
        NbtCompound tag = CitadelEntityData.getOrCreateCitadelTag(MinecraftClient.getInstance().player);
        capeType = tag.contains("CitadelCapeType") && !tag.getString("CitadelCapeType").isEmpty() ? tag.getString("CitadelCapeType") : null;
    }


    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 16777215);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        int i = this.width / 2;
        int j = this.height / 6;
        guiGraphics.getMatrices().push();
        ClientProxy.hideFollower = true;
        renderBackwardsEntity(i, j + 144, 60, 0, 0, MinecraftClient.getInstance().player);
        ClientProxy.hideFollower = false;
        guiGraphics.getMatrices().pop();
    }

    public static void renderBackwardsEntity(int x, int y, int size, float angleXComponent, float angleYComponent, LivingEntity entity) {
        float f = angleXComponent;
        float f1 = angleYComponent;
        MatrixStack posestack = RenderSystem.getModelViewStack();
        posestack.push();
        posestack.translate(x, y, 1050.0D);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        MatrixStack posestack1 = new MatrixStack();
        posestack1.translate(0.0D, 0.0D, 1000.0D);
        posestack1.scale((float) size, (float) size, (float) size);
        Quaternionf quaternion = RotationAxis.POSITIVE_Z.rotationDegrees(180.0F);
        Quaternionf quaternion1 = RotationAxis.POSITIVE_X.rotationDegrees(f1 * 20.0F);
        quaternion.mul(quaternion1);
        quaternion.mul(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
        posestack1.multiply(quaternion);
        float f2 = entity.bodyYaw;
        float f3 = entity.getYaw();
        float f4 = entity.getPitch();
        float f5 = entity.prevHeadYaw;
        float f6 = entity.headYaw;
        entity.bodyYaw = 180.0F + f * 20.0F;
        entity.setYaw(180.0F + f * 40.0F);
        entity.setPitch(-f1 * 20.0F);
        entity.headYaw = entity.getYaw();
        entity.prevHeadYaw = entity.getYaw();
        DiffuseLighting.method_34742();
        EntityRenderDispatcher entityrenderdispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        quaternion1.conjugate();
        entityrenderdispatcher.setRotation(quaternion1);
        entityrenderdispatcher.setRenderShadows(false);
        VertexConsumerProvider.Immediate multibuffersource$buffersource = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880);
        });
        multibuffersource$buffersource.draw();
        entityrenderdispatcher.setRenderShadows(true);
        entity.bodyYaw = f2;
        entity.setYaw(f3);
        entity.setPitch(f4);
        entity.prevHeadYaw = f5;
        entity.headYaw = f6;
        posestack.pop();
        RenderSystem.applyModelViewMatrix();
        DiffuseLighting.enableGuiDepthLighting();
    }


    protected void init() {
        super.init();
        int i = this.width / 2;
        int j = this.height / 6;
        ButtonWidget doneButton = ButtonWidget.builder(ScreenTexts.DONE, (p_213079_1_) -> {
            this.client.setScreen(this.parent);
        }).size(200, 20).position(i - 100, j + 160).build();
        this.addDrawableChild(doneButton);
        button = ButtonWidget.builder(getTypeText(), (p_213079_1_) -> {
            CitadelCapes.Cape nextCape = CitadelCapes.getNextCape(capeType, MinecraftClient.getInstance().player.getUuid());
            this.capeType = nextCape == null ? null : nextCape.getIdentifier();
            NbtCompound tag = CitadelEntityData.getOrCreateCitadelTag(MinecraftClient.getInstance().player);
            if (capeType == null) {
                tag.putString("CitadelCapeType", "");
                tag.putBoolean("CitadelCapeDisabled", true);
            } else {
                tag.putString("CitadelCapeType", capeType);
                tag.putBoolean("CitadelCapeDisabled", false);
            }
            CitadelEntityData.setCitadelTag(MinecraftClient.getInstance().player, tag);
            Citadel.sendMSGToServer(new PropertiesMessage("CitadelTagUpdate", tag, MinecraftClient.getInstance().player.getId()));
            button.setMessage(getTypeText());
        }).size(200, 20).position(i - 100, j).build();
        this.addDrawableChild(button);

    }

    private Text getTypeText() {
        Text suffix;

        if (capeType == null) {
            suffix = Text.translatable("citadel.gui.no_cape");
        } else {
            CitadelCapes.Cape cape = CitadelCapes.getById(capeType);
            if (cape == null) {
                suffix = Text.translatable("citadel.gui.no_cape");
            } else {
                suffix = Text.translatable("cape." + cape.getIdentifier());
            }
        }
        return Text.translatable("citadel.gui.cape_type").append(" ").append(suffix);
    }
}
