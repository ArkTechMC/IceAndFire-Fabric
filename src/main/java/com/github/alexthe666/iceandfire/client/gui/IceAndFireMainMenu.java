package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraftforge.client.ForgeHooksClient;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class IceAndFireMainMenu extends TitleScreen {
    public static final int LAYER_COUNT = 2;
    public static final Identifier splash = new Identifier(IceAndFire.MOD_ID, "splashes.txt");
    private static final Identifier MINECRAFT_TITLE_TEXTURES = new Identifier("textures/gui/title/minecraft.png");
    private static final Identifier BESTIARY_TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/gui/main_menu/bestiary_menu.png");
    private static final Identifier TABLE_TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/gui/main_menu/table.png");
    public static Identifier[] pageFlipTextures;
    public static Identifier[] drawingTextures = new Identifier[22];
    private int layerTick;
    private String splashText;
    private boolean isFlippingPage = false;
    private int pageFlip = 0;
    private Picture[] drawnPictures;
    private Enscription[] drawnEnscriptions;
    private float globalAlpha = 1F;

    public IceAndFireMainMenu() {
        pageFlipTextures = new Identifier[]{new Identifier(IceAndFire.MOD_ID, "textures/gui/main_menu/page_1.png"),
            new Identifier(IceAndFire.MOD_ID, "textures/gui/main_menu/page_2.png"),
            new Identifier(IceAndFire.MOD_ID, "textures/gui/main_menu/page_3.png"),
            new Identifier(IceAndFire.MOD_ID, "textures/gui/main_menu/page_4.png"),
            new Identifier(IceAndFire.MOD_ID, "textures/gui/main_menu/page_5.png"),
            new Identifier(IceAndFire.MOD_ID, "textures/gui/main_menu/page_6.png")};
        for (int i = 0; i < drawingTextures.length; i++) {
            drawingTextures[i] = new Identifier(IceAndFire.MOD_ID, "textures/gui/main_menu/drawing_" + (i + 1) + ".png");
        }
        this.resetDrawnImages();
        final String branch = "1.17";
        try (final BufferedReader reader = getURLContents("https://raw.githubusercontent.com/Alex-the-666/Ice_and_Fire/"
            + branch + "/src/main/resources/assets/iceandfire/splashes.txt", "assets/iceandfire/splashes.txt")) {
            List<String> list = IOUtils.readLines(reader);

            if (!list.isEmpty()) {
                do {
                    this.splashText = list.get(ThreadLocalRandom.current().nextInt(list.size()));
                } while (this.splashText.hashCode() == 125780783);
            }
        } catch (IOException e) {
            IceAndFire.LOGGER.error("Exception trying to collect splash screen lines: ", e);
        }
    }

    public static BufferedReader getURLContents(String urlString, String backupFileLoc) {
        BufferedReader reader = null;
        boolean useBackup = false;
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            url = null;
            useBackup = true;
        }
        if (url != null) {
            URLConnection connection = null;
            try {
                connection = url.openConnection();
                connection.setConnectTimeout(200);
                InputStream is = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is));
            } catch (IOException e) {
                IceAndFire.LOGGER.warn("Ice and Fire couldn't download splash texts for main menu");
                useBackup = true;
            }
        }
        if (useBackup) {
            InputStream is = IceAndFireMainMenu.class.getClassLoader().getResourceAsStream(backupFileLoc);
            if (is != null) {
                reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            }
        }
        return reader;
    }


    private void resetDrawnImages() {
        this.globalAlpha = 0;
        Random random = ThreadLocalRandom.current();
        this.drawnPictures = new Picture[1 + random.nextInt(2)];
        boolean left = random.nextBoolean();
        for (int i = 0; i < this.drawnPictures.length; i++) {
            left = !left;
            int x;
            int y = random.nextInt(25);
            if (left) {
                x = -15 - random.nextInt(20) - 128;
            } else {
                x = 30 + random.nextInt(20);
            }
            this.drawnPictures[i] = new Picture(random.nextInt(drawingTextures.length - 1), x, y, 0.5F, random.nextFloat() * 0.5F + 0.5F);
        }
        this.drawnEnscriptions = new Enscription[4 + random.nextInt(8)];
        for (int i = 0; i < this.drawnEnscriptions.length; i++) {
            left = !left;
            int x;
            int y = 10 + random.nextInt(130);
            if (left) {
                x = -30 - random.nextInt(30) - 50;
            } else {
                x = 30 + random.nextInt(30);
            }
            String s1 = "missingno";
            this.drawnEnscriptions[i] = new Enscription(s1, x, y, random.nextFloat() * 0.5F + 0.5F, 0X9C8B7B);
        }
    }

    @Override
    public void tick() {
        super.tick();
        float flipTick = this.layerTick % 40;
        if (this.globalAlpha < 1 && !this.isFlippingPage && flipTick < 30) {
            this.globalAlpha += 0.1F;
        }

        if (this.globalAlpha > 0 && flipTick > 30) {
            this.globalAlpha -= 0.1F;
        }
        if (flipTick == 0 && !this.isFlippingPage) {
            this.isFlippingPage = true;
        }
        if (this.isFlippingPage) {
            if (this.layerTick % 2 == 0) {
                this.pageFlip++;
            }
            if (this.pageFlip == 6) {
                this.pageFlip = 0;
                this.isFlippingPage = false;
                this.resetDrawnImages();
            }
        }

        this.layerTick++;
    }

    @Override
    public void render(@NotNull DrawContext ms, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        int width = this.width;
        int height = this.height;
        ms.drawTexture(TABLE_TEXTURE, 0, 0, 0, 0, width, height, width, height);
        ms.drawTexture(BESTIARY_TEXTURE, 50, 0, 0, 0, width - 100, height, width - 100, height);
        float f11 = 1.0F;
        int l = MathHelper.ceil(f11 * 255.0F) << 24;
        if (this.isFlippingPage) {
            ms.drawTexture(pageFlipTextures[Math.min(5, this.pageFlip)], 50, 0, 0, 0, width - 100, height, width - 100, height);
        } else {
            int middleX = width / 2;
            int middleY = height / 5;
            float widthScale = width / 427F;
            float heightScale = height / 427F;
            float imageScale = Math.min(widthScale, heightScale) * 192;
            for (Picture picture : this.drawnPictures) {
                float alpha = (picture.alpha * this.globalAlpha + 0.01F);
                RenderSystem.enableBlend();
                RenderSystem.setShaderColor(1, 1, 1, 1);
                ms.drawTexture(drawingTextures[picture.image], (int) (picture.x * widthScale) + middleX, (int) ((picture.y * heightScale) + middleY), 0, 0, (int) imageScale, (int) imageScale, (int) imageScale, (int) imageScale);
                RenderSystem.disableBlend();
            }
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager._enableBlend();
        this.client.textRenderer.draw("Ice and Fire " + Formatting.YELLOW + IceAndFire.VERSION, 2, height - 10, 0xFFFFFFFF, false, ms.getMatrices().peek().getPositionMatrix(), ms.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        ms.drawTexture(MINECRAFT_TITLE_TEXTURES, width / 2 - 256 / 2, 10, 0, 0, 256, 64, 256, 64);

        ForgeHooksClient.renderMainMenu(this, ms, this.client.textRenderer, width, height, l);
        if (this.splashText != null) {
            ms.getMatrices().push();
            ms.getMatrices().translate((this.width / 2 + 90), 70.0D, 0.0D);
            ms.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-20.0F));
            float f2 = 1.8F - MathHelper.abs(MathHelper.sin((float) (Util.getMeasuringTimeMs() % 1000L) / 1000.0F * ((float) Math.PI * 2F)) * 0.1F);
            f2 = f2 * 100.0F / (float) (this.textRenderer.getWidth(this.splashText) + 32);
            ms.getMatrices().scale(f2, f2, f2);
            ms.drawCenteredTextWithShadow(this.textRenderer, this.splashText, 0, -8, 16776960 | l);
            ms.getMatrices().pop();
        }


        String s1 = "Copyright Mojang AB. Do not distribute!";
        TextRenderer font = this.client.textRenderer;
        ms.drawTextWithShadow(font, s1, width - this.client.textRenderer.getWidth(s1) - 2,
            height - 10, 0xFFFFFFFF);
        for (int i = 0; i < this.drawables.size(); ++i) {
            this.drawables.get(i).render(ms, mouseX, mouseY, partialTicks);
        }
        for (int i = 0; i < this.drawables.size(); i++) {
            this.drawables.get(i).render(ms, mouseX, mouseY, this.client.getLastFrameDuration());
        }
    }

    private class Picture {
        int image;
        int x;
        int y;
        float alpha;

        public Picture(int image, int x, int y, float alpha, float scale) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.alpha = alpha;
        }
    }

    private class Enscription {
        public Enscription(String text, int x, int y, float alpha, int color) {
        }
    }
}

