package com.github.alexthe666.citadel.client.texture;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.ColorHelper;

import java.io.IOException;
import java.io.InputStream;

public class ColorMappedTexture extends ResourceTexture {

    private final int[] colors;

    public ColorMappedTexture(Identifier resourceLocation, int[] colors) {
        super(resourceLocation);
        this.colors = colors;
    }

    public void load(ResourceManager resourceManager) throws IOException {
        NativeImage nativeimage = this.getNativeImage(resourceManager, this.location);
        if (nativeimage != null) {
            if (resourceManager.getResource(this.location).isPresent()) {
                Resource resource = resourceManager.getResource(this.location).get();
                try {
                    ColorsMetadataSection section = resource.getMetadata().decode(ColorsMetadataSection.SERIALIZER).orElse(new ColorsMetadataSection(null));
                    NativeImage nativeimage2 = this.getNativeImage(resourceManager, section.getColorRamp());
                    if (nativeimage2 != null) {
                        this.processColorMap(nativeimage, nativeimage2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            TextureUtil.prepareImage(this.getGlId(), nativeimage.getWidth(), nativeimage.getHeight());
            this.bindTexture();
            nativeimage.upload(0, 0, 0, false);
        }
    }

    private NativeImage getNativeImage(ResourceManager resourceManager, Identifier resourceLocation) {
        Resource resource = null;
        if (resourceLocation == null) {
            return null;
        }
        try {
            resource = resourceManager.getResourceOrThrow(resourceLocation);
            InputStream inputstream = resource.getInputStream();
            NativeImage nativeimage = NativeImage.read(inputstream);
            if (inputstream != null) {
                inputstream.close();
            }
            return nativeimage;
        } catch (Throwable throwable1) {
            return null;
        }
    }

    private void processColorMap(NativeImage nativeImage, NativeImage colorMap) {
        int[] fromColorMap = new int[colorMap.getHeight()];
        for (int i = 0; i < fromColorMap.length; i++) {
            fromColorMap[i] = colorMap.getColor(0, i);
        }
        for (int i = 0; i < nativeImage.getWidth(); i++) {
            for (int j = 0; j < nativeImage.getHeight(); j++) {
                int colorAt = nativeImage.getColor(i, j);
                if (ColorHelper.Abgr.getAlpha(colorAt) == 0) {
                    continue;
                }
                int replaceIndex = -1;
                for (int k = 0; k < fromColorMap.length; k++) {
                    if (colorAt == fromColorMap[k]) {
                        replaceIndex = k;
                    }
                }
                if (replaceIndex >= 0 && this.colors.length > replaceIndex) {
                    int r = this.colors[replaceIndex] >> 16 & 255;
                    int g = this.colors[replaceIndex] >> 8 & 255;
                    int b = this.colors[replaceIndex] & 255;
                    nativeImage.setColor(i, j, ColorHelper.Abgr.getAbgr(ColorHelper.Abgr.getAlpha(colorAt), b, g, r));
                }
            }
        }
    }

    private static class ColorsMetadataSection {

        public static final ColorsMetadataSectionSerializer SERIALIZER = new ColorsMetadataSectionSerializer();

        private final Identifier colorRamp;

        public ColorsMetadataSection(Identifier colorRamp) {
            this.colorRamp = colorRamp;
        }

        private boolean areColorsEqual(int color1, int color2) {
            int r1 = color1 >> 16 & 255;
            int g1 = color1 >> 8 & 255;
            int b1 = color1 & 255;
            int r2 = color2 >> 16 & 255;
            int g2 = color2 >> 8 & 255;
            int b2 = color2 & 255;
            return r1 == r2 && g1 == g2 && b1 == b2;
        }

        public Identifier getColorRamp() {
            return this.colorRamp;
        }
    }

    private static class ColorsMetadataSectionSerializer implements ResourceMetadataReader<ColorsMetadataSection> {
        private ColorsMetadataSectionSerializer() {
        }

        public ColorsMetadataSection fromJson(JsonObject json) {

            return new ColorsMetadataSection(new Identifier(JsonHelper.getString(json, "color_ramp")));
        }

        public String getKey() {
            return "colors";
        }
    }
}
