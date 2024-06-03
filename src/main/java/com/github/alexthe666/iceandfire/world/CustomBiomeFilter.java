package com.github.alexthe666.iceandfire.world;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.FeaturePlacementContext;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.AbstractConditionalPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;
import org.jetbrains.annotations.NotNull;

/**
    Some worldgen mods / datapacks split biomes between cave and surface<br>
    By default dragon caves (or any underground generation) would only check against the cave biome, not the surface biome)<br>
    Since the passed y position will be the lowest point of the world (e.g. -64)
*/
public class CustomBiomeFilter extends AbstractConditionalPlacementModifier {
    private static final CustomBiomeFilter INSTANCE = new CustomBiomeFilter();
    public static Codec<CustomBiomeFilter> CODEC = Codec.unit(() -> INSTANCE);

    private CustomBiomeFilter() { /* Nothing to do */ }

    public static CustomBiomeFilter biome() {
        return INSTANCE;
    }

    protected boolean shouldPlace(final FeaturePlacementContext context, @NotNull final Random random, @NotNull final BlockPos position) {
        PlacedFeature placedfeature = context.getPlacedFeature().orElseThrow(() -> new IllegalStateException("Tried to biome check an unregistered feature, or a feature that should not restrict the biome"));
        boolean hasFeature = context.getChunkGenerator().getGenerationSettings(context.getWorld().getBiome(position)).isFeatureAllowed(placedfeature);

        if (!hasFeature) {
            // TODO :: In theory this could cause a fire dragon cave to spawn in an Terralith ice cave if said cave spawns below a desert or sth.
            hasFeature = context.getChunkGenerator().getGenerationSettings(context.getWorld().getBiome(context.getWorld().getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, position))).isFeatureAllowed(placedfeature);
        }

        return hasFeature;
    }

    public @NotNull PlacementModifierType<?> getType() {
        return IafPlacementFilterRegistry.CUSTOM_BIOME_FILTER.get();
    }
}
