package com.github.alexthe666.citadel.client.shader;

import net.minecraft.client.gl.ShaderProgram;

public class CitadelInternalShaders {
    private static ShaderProgram renderTypeRainbowAura;

    public static ShaderProgram getRenderTypeRainbowAura() {
        return renderTypeRainbowAura;
    }

    public static void setRenderTypeRainbowAura(ShaderProgram instance) {
        renderTypeRainbowAura = instance;
    }
}
