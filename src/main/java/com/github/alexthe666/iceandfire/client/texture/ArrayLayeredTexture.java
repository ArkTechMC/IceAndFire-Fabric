package com.github.alexthe666.iceandfire.client.texture;

import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class ArrayLayeredTexture extends AbstractTexture {
    private static final Logger LOGGER = LogManager.getLogger();
    public final List<String> layeredTextureNames;

    public ArrayLayeredTexture(List<String> textureNames) {
        this.layeredTextureNames = textureNames;
    }

    @Override
    public void load(@NotNull ResourceManager manager) {
        Iterator<String> iterator = this.layeredTextureNames.iterator();
        String s = iterator.next();
        Optional<Resource> iresource = manager.getResource(new Identifier(s));
        if (iresource.isPresent()) {
            try {
                NativeImage nativeimage = NativeImage.read(iresource.get().getInputStream());
                while (iterator.hasNext()) {
                    String s1 = iterator.next();
                    if (s1 != null) {
                        Optional<Resource> iresource1 = manager.getResource(new Identifier(s1));
                        NativeImage nativeimage1 = NativeImage.read(iresource1.get().getInputStream());
                        for (int i = 0; i < Math.min(nativeimage1.getHeight(), nativeimage.getHeight()); i++) {
                            for (int j = 0; j < Math.min(nativeimage1.getWidth(), nativeimage.getWidth()); j++) {
                                nativeimage.blend(j, i, nativeimage1.getColor(j, i));
                            }
                        }
                    }
                }

                if (!RenderSystem.isOnRenderThreadOrInit()) {
                    RenderSystem.recordRenderCall(() -> {
                        this.loadImage(nativeimage);
                    });
                } else {
                    this.loadImage(nativeimage);
                }
            } catch (IOException exception) {
                LOGGER.error("Couldn't load layered image", exception);
            }
        } else {
            LOGGER.error("Couldn't load layered image");
        }

    }

    private void loadImage(NativeImage imageIn) {
        TextureUtil.prepareImage(this.getGlId(), imageIn.getWidth(), imageIn.getHeight());
        imageIn.upload(0, 0, 0, true);
    }
}
