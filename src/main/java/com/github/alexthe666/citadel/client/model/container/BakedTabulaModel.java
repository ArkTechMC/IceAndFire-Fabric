package com.github.alexthe666.citadel.client.model.container;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

public class BakedTabulaModel implements BakedModel {
    private final ImmutableList<BakedQuad> quads;
    private final Sprite particle;
    private final ImmutableMap<ModelTransformationMode, AffineTransformation> transforms;

    public BakedTabulaModel(ImmutableList<BakedQuad> quads, Sprite particle, ImmutableMap<ModelTransformationMode, AffineTransformation> transforms) {
        this.quads = quads;
        this.particle = particle;
        this.transforms = transforms;
    }

    @Override
    public List<BakedQuad> getQuads(@org.jetbrains.annotations.Nullable BlockState p_235039_, @org.jetbrains.annotations.Nullable Direction p_235040_, Random p_235041_) {
        return this.quads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getParticleSprite() {
        return this.particle;
    }

    @Override
    public ModelTransformation getTransformation() {
        return ModelTransformation.NONE;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }
}
