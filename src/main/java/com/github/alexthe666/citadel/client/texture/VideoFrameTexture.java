package com.github.alexthe666.citadel.client.texture;

import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.math.ColorHelper;

import java.awt.image.BufferedImage;

public class VideoFrameTexture extends NativeImageBackedTexture {

    public VideoFrameTexture(NativeImage image) {
        super(image);
    }


    @Override
    public void setImage(NativeImage nativeImage) {
        super.setImage(nativeImage);
        if (this.getImage() != null) {
            TextureUtil.prepareImage(this.getGlId(), this.getImage().getWidth(), this.getImage().getHeight());
            this.upload();
        }
    }

    public void setPixelsFromBufferedImage(BufferedImage bufferedImage) {
        for (int i = 0; i < Math.min(this.getImage().getWidth(), bufferedImage.getWidth()); i++) {
            for (int j = 0; j < Math.min(this.getImage().getHeight(), bufferedImage.getHeight()); j++) {
                int color = bufferedImage.getRGB(i, j);
                int r = color >> 16 & 255;
                int g = color >> 8 & 255;
                int b = color & 255;
                this.getImage().setColor(i, j, ColorHelper.Abgr.getAbgr(0XFF, b, g, r));
            }
        }
        this.upload();
    }
}
