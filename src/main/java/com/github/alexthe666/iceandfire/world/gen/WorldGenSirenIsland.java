package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.util.WorldUtil;
import com.github.alexthe666.iceandfire.world.IafWorldData;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class WorldGenSirenIsland extends Feature<DefaultFeatureConfig> implements TypedFeature {

    private final int MAX_ISLAND_RADIUS = 10;

    public WorldGenSirenIsland(final Codec<DefaultFeatureConfig> configuration) {
        super(configuration);
    }

    @Override
    public boolean generate(final FeatureContext<DefaultFeatureConfig> context) {
        if (!WorldUtil.canGenerate(IafConfig.generateSirenChance, context.getWorld(), context.getRandom(), context.getOrigin(), this.getId(), false)) {
            return false;
        }

        int up = context.getRandom().nextInt(4) + 1;
        BlockPos center = context.getOrigin().up(up);
        int layer = 0;
        int sirens = 1 + context.getRandom().nextInt(3);

        while (!context.getWorld().getBlockState(center).isOpaque() && center.getY() >= context.getWorld().getBottomY()) {
            layer++;

            for (float i = 0; i < this.getRadius(layer, up); i += 0.5f) {
                for (float j = 0; j < 2 * Math.PI * i + context.getRandom().nextInt(2); j += 0.5f) {
                    BlockPos stonePos = BlockPos.ofFloored(Math.floor(center.getX() + MathHelper.sin(j) * i + context.getRandom().nextInt(2)), center.getY(), Math.floor(center.getZ() + MathHelper.cos(j) * i + context.getRandom().nextInt(2)));
                    context.getWorld().setBlockState(stonePos, this.getStone(context.getRandom()), Block.NOTIFY_ALL);
                    BlockPos upPos = stonePos.up();

                    if (context.getWorld().isAir(upPos) && context.getWorld().isAir(upPos.east()) && context.getWorld().isAir(upPos.north()) && context.getWorld().isAir(upPos.north().east()) && context.getRandom().nextInt(3) == 0 && sirens > 0) {
                        this.spawnSiren(context.getWorld(), context.getRandom(), upPos.north().east());
                        sirens--;
                    }
                }
            }

            center = center.down();
        }

        layer++;

        for (float i = 0; i < this.getRadius(layer, up); i += 0.5f) {
            for (float j = 0; j < 2 * Math.PI * i + context.getRandom().nextInt(2); j += 0.5f) {
                BlockPos stonePos = BlockPos.ofFloored(Math.floor(center.getX() + MathHelper.sin(j) * i + context.getRandom().nextInt(2)), center.getY(), Math.floor(center.getZ() + MathHelper.cos(j) * i + context.getRandom().nextInt(2)));

                while (!context.getWorld().getBlockState(stonePos).isOpaque() && stonePos.getY() >= 0) {
                    context.getWorld().setBlockState(stonePos, this.getStone(context.getRandom()), Block.NOTIFY_ALL);
                    stonePos = stonePos.down();
                }
            }
        }

        return true;
    }

    private int getRadius(int layer, int up) {
        return layer > up ? (int) (layer * 0.25) + up : Math.min(layer, this.MAX_ISLAND_RADIUS);
    }

    private BlockState getStone(Random random) {
        int chance = random.nextInt(100);
        if (chance > 90) {
            return Blocks.MOSSY_COBBLESTONE.getDefaultState();
        } else if (chance > 70) {
            return Blocks.GRAVEL.getDefaultState();
        } else if (chance > 45) {
            return Blocks.COBBLESTONE.getDefaultState();
        } else {
            return Blocks.STONE.getDefaultState();
        }
    }

    private void spawnSiren(ServerWorldAccess worldIn, Random rand, BlockPos position) {
        EntitySiren siren = new EntitySiren(IafEntityRegistry.SIREN.get(), worldIn.toServerWorld());
        siren.setSinging(true);
        siren.setHairColor(rand.nextInt(2));
        siren.setSingingPose(rand.nextInt(2));
        siren.updatePositionAndAngles(position.getX() + 0.5D, position.getY() + 1, position.getZ() + 0.5D, rand.nextFloat() * 360, 0);
        worldIn.spawnEntity(siren);
    }

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.OCEAN;
    }

    @Override
    public String getId() {
        return "siren_island";
    }
}
