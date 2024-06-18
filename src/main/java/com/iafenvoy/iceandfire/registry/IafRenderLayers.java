package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.client.render.block.RenderDreadPortal;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class IafRenderLayers extends RenderLayer {
    protected static final Transparency GHOST_TRANSPARANCY = new Transparency("translucent_ghost_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });
    private static final Identifier STONE_TEXTURE = new Identifier("textures/block/stone.png");
    private static final RenderLayer DREADLANDS_PORTAL = of("dreadlands_portal", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS, 256, false, false, MultiPhaseParameters.builder().texture(Textures.create().add(RenderDreadPortal.DREAD_PORTAL_BACKGROUND, false, false).add(RenderDreadPortal.DREAD_PORTAL, false, false).build()).build(false));

    public IafRenderLayers(String nameIn, VertexFormat formatIn, VertexFormat.DrawMode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static RenderLayer getGhost(Identifier locationIn) {
        Texture lvt_1_1_ = new Texture(locationIn, false, false);
        return of("ghost_iaf", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder().program(ENTITY_CUTOUT_NONULL_PROGRAM).texture(lvt_1_1_).transparency(GHOST_TRANSPARANCY).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(true));
    }

    public static RenderLayer getGhostDaytime(Identifier locationIn) {
        Texture lvt_1_1_ = new Texture(locationIn, false, false);
        return of("ghost_iaf_day", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder().program(ENTITY_CUTOUT_NONULL_PROGRAM).texture(lvt_1_1_).transparency(TRANSLUCENT_TRANSPARENCY).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(true));
    }

    public static RenderLayer getDreadlandsPortal() {
        return DREADLANDS_PORTAL;
    }

    public static RenderLayer getStoneMobRenderType(float x, float y) {
        Texture textureState = new Texture(STONE_TEXTURE, false, false);
        MultiPhaseParameters rendertype = MultiPhaseParameters.builder().program(RenderLayer.ENTITY_CUTOUT_PROGRAM).texture(textureState).transparency(NO_TRANSPARENCY).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(true);
        return of("stone_entity_type", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, rendertype);
    }

    public static RenderLayer getIce(Identifier locationIn) {
        Texture lvt_1_1_ = new Texture(locationIn, false, false);
        return of("ice_texture", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder().program(RenderLayer.BEACON_BEAM_PROGRAM).texture(lvt_1_1_).transparency(TRANSLUCENT_TRANSPARENCY).cull(ENABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(true));
    }

    public static RenderLayer getStoneCrackRenderType(Identifier crackTex) {
        Texture textureState = new Texture(crackTex, false, false);
        MultiPhaseParameters rendertype$state = MultiPhaseParameters.builder().texture(textureState).program(RenderLayer.ENTITY_CUTOUT_PROGRAM).transparency(TRANSLUCENT_TRANSPARENCY).depthTest(EQUAL_DEPTH_TEST).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(false);
        return of("stone_entity_type_crack", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, rendertype$state);
    }
}
