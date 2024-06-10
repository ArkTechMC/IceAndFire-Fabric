package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import dev.arktechmc.iafextra.util.RandomHelper;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TitleScreenRenderManager {
    public static final int LAYER_COUNT = 2;
    public static final Identifier splash = new Identifier(IceAndFire.MOD_ID, "splashes.txt");
    public static final String VERSION;
    private static final Identifier MINECRAFT_TITLE_TEXTURES = new Identifier("textures/gui/title/minecraft.png");
    private static final Identifier BESTIARY_TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/gui/main_menu/bestiary_menu.png");
    private static final Identifier TABLE_TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/gui/main_menu/table.png");
    private static final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    public static Identifier[] pageFlipTextures;
    public static Identifier[] drawingTextures = new Identifier[22];
    private static int layerTick;
    private static List<String> splashText;
    private static boolean isFlippingPage = false;
    private static int pageFlip = 0;
    private static Picture[] drawnPictures;
    private static Enscription[] drawnEnscriptions;
    private static float globalAlpha = 1F;

    static {
        pageFlipTextures = new Identifier[]{new Identifier(IceAndFire.MOD_ID, "textures/gui/main_menu/page_1.png"),
                new Identifier(IceAndFire.MOD_ID, "textures/gui/main_menu/page_2.png"),
                new Identifier(IceAndFire.MOD_ID, "textures/gui/main_menu/page_3.png"),
                new Identifier(IceAndFire.MOD_ID, "textures/gui/main_menu/page_4.png"),
                new Identifier(IceAndFire.MOD_ID, "textures/gui/main_menu/page_5.png"),
                new Identifier(IceAndFire.MOD_ID, "textures/gui/main_menu/page_6.png")};
        for (int i = 0; i < drawingTextures.length; i++) {
            drawingTextures[i] = new Identifier(IceAndFire.MOD_ID, "textures/gui/main_menu/drawing_" + (i + 1) + ".png");
        }
        resetDrawnImages();
        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer(IceAndFire.MOD_ID);
        if (container.isPresent()) VERSION = container.get().getMetadata().getVersion().getFriendlyString();
        else VERSION = "Unknown";
    }

    public static SplashTextRenderer getSplash() {
        if (splashText == null) {
            try {
                BufferedReader bufferedReader = MinecraftClient.getInstance().getResourceManager().openAsReader(splash);
                splashText = bufferedReader.lines().map(String::trim).filter((splashText) -> splashText.hashCode() != 125780783).toList();
                bufferedReader.close();
            } catch (IOException var8) {
                splashText = new ArrayList<>();
            }
        }
        if (splashText.size() == 0) return null;
        return new SplashTextRenderer(splashText.get(RandomHelper.nextInt(0, splashText.size() - 1)));
    }

    private static void resetDrawnImages() {
        globalAlpha = 0;
        Random random = ThreadLocalRandom.current();
        drawnPictures = new Picture[1 + random.nextInt(2)];
        boolean left = random.nextBoolean();
        for (int i = 0; i < drawnPictures.length; i++) {
            left = !left;
            int x;
            int y = random.nextInt(25);
            if (left) {
                x = -15 - random.nextInt(20) - 128;
            } else {
                x = 30 + random.nextInt(20);
            }
            drawnPictures[i] = new Picture(random.nextInt(drawingTextures.length - 1), x, y, 0.5F, random.nextFloat() * 0.5F + 0.5F);
        }
        drawnEnscriptions = new Enscription[4 + random.nextInt(8)];
        for (int i = 0; i < drawnEnscriptions.length; i++) {
            left = !left;
            int x;
            int y = 10 + random.nextInt(130);
            if (left) {
                x = -30 - random.nextInt(30) - 50;
            } else {
                x = 30 + random.nextInt(30);
            }
            drawnEnscriptions[i] = new Enscription("missingno", x, y, random.nextFloat() * 0.5F + 0.5F, 0X9C8B7B);
        }
    }

    public static void tick() {
        float flipTick = layerTick % 40;
        if (globalAlpha < 1 && !isFlippingPage && flipTick < 30) {
            globalAlpha += 0.1F;
        }

        if (globalAlpha > 0 && flipTick > 30) {
            globalAlpha -= 0.1F;
        }
        if (flipTick == 0 && !isFlippingPage) {
            isFlippingPage = true;
        }
        if (isFlippingPage) {
            if (layerTick % 2 == 0) {
                pageFlip++;
            }
            if (pageFlip == 6) {
                pageFlip = 0;
                isFlippingPage = false;
                resetDrawnImages();
            }
        }

        layerTick++;
    }

    public static void renderBackground(DrawContext ms, int width, int height) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        ms.drawTexture(TABLE_TEXTURE, 0, 0, 0, 0, width, height, width, height);
        ms.drawTexture(BESTIARY_TEXTURE, 50, 0, 0, 0, width - 100, height, width - 100, height);
        if (isFlippingPage) {
            ms.drawTexture(pageFlipTextures[Math.min(5, pageFlip)], 50, 0, 0, 0, width - 100, height, width - 100, height);
        } else {
            int middleX = width / 2;
            int middleY = height / 5;
            float widthScale = width / 427F;
            float heightScale = height / 427F;
            float imageScale = Math.min(widthScale, heightScale) * 192;
            for (Picture picture : drawnPictures) {
                RenderSystem.enableBlend();
                RenderSystem.setShaderColor(1, 1, 1, 1);
                ms.drawTexture(drawingTextures[picture.image], (int) (picture.x * widthScale) + middleX, (int) ((picture.y * heightScale) + middleY), 0, 0, (int) imageScale, (int) imageScale, (int) imageScale, (int) imageScale);
                RenderSystem.disableBlend();
            }
        }
    }

    public static void drawModName(DrawContext ms, int width, int height) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager._enableBlend();
        textRenderer.draw("Ice and Fire " + Formatting.YELLOW + VERSION, 2, height - 20, 0xFFFFFFFF, false, ms.getMatrices().peek().getPositionMatrix(), ms.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
    }

    private static class Picture {
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

    private static class Enscription {
        public Enscription(String text, int x, int y, float alpha, int color) {
        }
    }
}

