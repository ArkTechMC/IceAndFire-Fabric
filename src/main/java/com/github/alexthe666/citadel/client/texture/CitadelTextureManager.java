package com.github.alexthe666.citadel.client.texture;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class CitadelTextureManager {

    private static final Map<Identifier, Identifier> COLOR_MAPPED_TEXTURES = new HashMap<>();

    public static Identifier getColorMappedTexture(Identifier textureLoc, int[] colors) {
        return getColorMappedTexture(textureLoc, textureLoc, colors);
    }

    public static Identifier getColorMappedTexture(Identifier namespace, Identifier textureLoc, int[] colors) {
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        AbstractTexture abstracttexture = textureManager.getOrDefault(namespace, MissingSprite.getMissingSpriteTexture());
        if (abstracttexture == MissingSprite.getMissingSpriteTexture()) {
            textureManager.registerTexture(namespace, new ColorMappedTexture(textureLoc, colors));
        }
        return namespace;
    }

    public static VideoFrameTexture getVideoTexture(Identifier namespace, int defaultWidth, int defaultHeight) {
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        AbstractTexture abstracttexture = textureManager.getOrDefault(namespace, MissingSprite.getMissingSpriteTexture());
        if (abstracttexture == MissingSprite.getMissingSpriteTexture()) {
            abstracttexture = new VideoFrameTexture(new NativeImage(defaultWidth, defaultHeight, false));
            textureManager.registerTexture(namespace, abstracttexture);
        }
        return abstracttexture instanceof VideoFrameTexture ? (VideoFrameTexture) abstracttexture : null;
    }
}
