package com.github.alexthe666.citadel.client.gui;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.client.rewards.CitadelPatreonRenderer;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import io.github.fabricators_of_create.porting_lib.util.client.ForgeSlider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class GuiCitadelPatreonConfig extends GameOptionsScreen {

    private ForgeSlider distSlider;
    private ForgeSlider speedSlider;
    private ForgeSlider heightSlider;
    private ButtonWidget changeButton;
    private float rotateDist;
    private float rotateSpeed;
    private float rotateHeight;
    private String followType;

    public GuiCitadelPatreonConfig(Screen parentScreenIn, GameOptions gameSettingsIn) {
        super(parentScreenIn, gameSettingsIn, Text.translatable("citadel.gui.patreon_customization"));
        NbtCompound tag = CitadelEntityData.getOrCreateCitadelTag(MinecraftClient.getInstance().player);
        float distance = tag.contains("CitadelRotateDistance") ? tag.getFloat("CitadelRotateDistance") : 2F;
        float speed = tag.contains("CitadelRotateSpeed") ? tag.getFloat("CitadelRotateSpeed") : 1;
        float height = tag.contains("CitadelRotateHeight") ? tag.getFloat("CitadelRotateHeight") : 1F;
        this.rotateDist = roundTo(distance, 3);
        this.rotateSpeed = roundTo(speed, 3);
        this.rotateHeight = roundTo(height, 3);
        this.followType = tag.contains("CitadelFollowerType") ? tag.getString("CitadelFollowerType") : "citadel";
    }

    private void setSliderValue(int i, float sliderValue) {
        boolean flag = false;
        NbtCompound tag = CitadelEntityData.getOrCreateCitadelTag(MinecraftClient.getInstance().player);
        if (i == 0) {
            this.rotateDist = roundTo(sliderValue, 3);
            tag.putFloat("CitadelRotateDistance", this.rotateDist);
            //distSlider.isHovered = false;
        } else if (i == 1) {
            this.rotateSpeed = roundTo(sliderValue, 3);
            tag.putFloat("CitadelRotateSpeed", this.rotateSpeed);
            //speedSlider.isHovered = false;
        } else {
            this.rotateHeight = roundTo(sliderValue, 3);
            tag.putFloat("CitadelRotateHeight", this.rotateHeight);
            //heightSlider.isHovered = false;
        }
        CitadelEntityData.setCitadelTag(MinecraftClient.getInstance().player, tag);
        Citadel.sendMSGToServer(new PropertiesMessage("CitadelPatreonConfig", tag, MinecraftClient.getInstance().player.getId()));
    }

    public static float roundTo(float value, int places) {
        return value;
    }

    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 16777215);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    protected void init() {
        super.init();
        int i = this.width / 2;
        int j = this.height / 6;
        ButtonWidget doneButton = ButtonWidget.builder(ScreenTexts.DONE, (p_213079_1_) -> {
            this.client.setScreen(this.parent);
        }).size(200, 20).position(i - 100, j + 120).build();
        this.addDrawableChild(doneButton);
        this.addDrawableChild(this.distSlider = new ForgeSlider(i - 150 / 2 - 25, j + 30, 150, 20, Text.translatable("citadel.gui.orbit_dist").append(Text.translatable(": ")), Text.translatable(""), 0.125F, 5F, this.rotateDist, 0.1D, 1, true) {
            @Override
            protected void applyValue() {
                GuiCitadelPatreonConfig.this.setSliderValue(0, (float) this.getValue());
            }
        });

        ButtonWidget reset1Button = ButtonWidget.builder(Text.translatable("citadel.gui.reset"), (p_213079_1_) -> {
            this.setSliderValue(0, 0.4F);
        }).size(40, 20).position(i - 150 / 2 + 135, j + 30).build();
        this.addDrawableChild(reset1Button);

        this.addDrawableChild(this.speedSlider = new ForgeSlider(i - 150 / 2 - 25, j + 60, 150, 20, Text.translatable("citadel.gui.orbit_speed").append(Text.translatable(": ")), Text.translatable(""), 0.0F, 5F, this.rotateSpeed, 0.1D, 2, true) {
            @Override
            protected void applyValue() {
                GuiCitadelPatreonConfig.this.setSliderValue(1, (float) this.getValue());
            }
        });

        ButtonWidget reset2Button = ButtonWidget.builder(Text.translatable("citadel.gui.reset"), (p_213079_1_) -> {
            this.setSliderValue(1, 1F / 5F);
        }).size(40, 20).position(i - 150 / 2 + 135, j + 60).build();
        this.addDrawableChild(reset2Button);

        this.addDrawableChild(this.heightSlider = new ForgeSlider(i - 150 / 2 - 25, j + 90, 150, 20, Text.translatable("citadel.gui.orbit_height").append(Text.translatable(": ")), Text.translatable(""), 0.0F, 2F, this.rotateHeight, 0.1D, 2, true) {
            @Override
            protected void applyValue() {
                GuiCitadelPatreonConfig.this.setSliderValue(2, (float) this.getValue());
            }
        });

        ButtonWidget reset3Button = ButtonWidget.builder(Text.translatable("citadel.gui.reset"), (p_213079_1_) -> {
            this.setSliderValue(2, 0.5F);
        }).size(40, 20).position(i - 150 / 2 + 135, j + 90).build();
        this.addDrawableChild(reset3Button);

        this.changeButton = ButtonWidget.builder(this.getTypeText(), (p_213079_1_) -> {
            this.followType = CitadelPatreonRenderer.getIdOfNext(this.followType);
            NbtCompound tag = CitadelEntityData.getOrCreateCitadelTag(MinecraftClient.getInstance().player);
            if (tag != null) {
                tag.putString("CitadelFollowerType", this.followType);
                CitadelEntityData.setCitadelTag(MinecraftClient.getInstance().player, tag);
            }
            Citadel.sendMSGToServer(new PropertiesMessage("CitadelPatreonConfig", tag, MinecraftClient.getInstance().player.getId()));
            this.changeButton.setMessage(this.getTypeText());
        }).size(200, 20).position(i - 100, j).build();
        this.addDrawableChild(this.changeButton);
    }

    private Text getTypeText() {
        return Text.translatable("citadel.gui.follower_type").append(Text.translatable("citadel.follower." + this.followType));
    }
}
