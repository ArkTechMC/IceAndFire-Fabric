package com.iafenvoy.iceandfire.world.gen;

import com.iafenvoy.iceandfire.block.BlockGoldPile;
import com.iafenvoy.iceandfire.config.IafConfig;
import com.iafenvoy.iceandfire.datagen.tags.IafBlockTags;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.entity.util.HomePosition;
import com.iafenvoy.iceandfire.registry.IafFeatures;
import com.iafenvoy.iceandfire.util.ShapeBuilder;
import com.iafenvoy.iceandfire.world.IafWorldData;
import com.mojang.serialization.Codec;
import io.github.fabricators_of_create.porting_lib.tags.TagHelper;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class WorldGenDragonCave extends Feature<DefaultFeatureConfig> implements TypedFeature {
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    public Identifier DRAGON_CHEST;
    public Identifier DRAGON_MALE_CHEST;
    public WorldGenCaveStalactites CEILING_DECO;
    public BlockState PALETTE_BLOCK1;
    public BlockState PALETTE_BLOCK2;
    public TagKey<Block> dragonTypeOreTag;
    public BlockState TREASURE_PILE;
    public boolean isMale;

    protected WorldGenDragonCave(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess worldIn = context.getWorld();
        Random rand = context.getRandom();
        BlockPos position = context.getOrigin();
        if (rand.nextInt(IafConfig.getInstance().generateDragonDenChance) != 0 || !IafFeatures.isFarEnoughFromSpawn(worldIn, position) || !IafFeatures.isFarEnoughFromDangerousGen(worldIn, position, this.getId(), this.getFeatureType())) {
            return false;
        }
        this.isMale = rand.nextBoolean();
        ChunkPos chunkPos = worldIn.getChunk(position).getPos();


        int j = 40;
        // Update the position so it doesn't go above the ocean floor
        for (int k = 0; k < 20; ++k) {
            for (int l = 0; l < 20; ++l) {
                j = Math.min(j, worldIn.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, position.getX() + k, position.getZ() + l));
            }
        }

        // Offset the position randomly
        j -= 20;
        j -= rand.nextInt(30);

        // If the cave generation point is too low
        if (j < worldIn.getBottomY() + 20) {
            return false;
        }
        // Center the position at the "middle" of the chunk
        position = new BlockPos((chunkPos.x << 4) + 8, j, (chunkPos.z << 4) + 8);
        int dragonAge = 75 + rand.nextInt(50);
        int radius = (int) (dragonAge * 0.2F) + rand.nextInt(4);
        this.generateCave(worldIn, radius, 3, position, rand);
        EntityDragonBase dragon = this.createDragon(worldIn, rand, position, dragonAge);
        worldIn.spawnEntity(dragon);
        return true;
    }

    public void generateCave(WorldAccess worldIn, int radius, int amount, BlockPos center, Random rand) {
        List<SphereInfo> sphereList = new ArrayList<>();
        sphereList.add(new SphereInfo(radius, center.toImmutable()));
        Stream<BlockPos> sphereBlocks = ShapeBuilder.start().getAllInCutOffSphereMutable(radius, radius / 2, center).toStream(false);
        Stream<BlockPos> hollowBlocks = ShapeBuilder.start().getAllInRandomlyDistributedRangeYCutOffSphereMutable(radius - 2, (int) ((radius - 2) * 0.75), (radius - 2) / 2, rand, center).toStream(false);
        //Get shells
        //Get hollows
        for (int i = 0; i < amount + rand.nextInt(2); i++) {
            Direction direction = HORIZONTALS[rand.nextInt(HORIZONTALS.length - 1)];
            int r = 2 * (int) (radius / 3F) + rand.nextInt(8);
            BlockPos centerOffset = center.offset(direction, radius - 2);
            sphereBlocks = Stream.concat(sphereBlocks, ShapeBuilder.start().getAllInCutOffSphereMutable(r, r, centerOffset).toStream(false));
            hollowBlocks = Stream.concat(hollowBlocks, ShapeBuilder.start().getAllInRandomlyDistributedRangeYCutOffSphereMutable(r - 2, (int) ((r - 2) * 0.75), (r - 2) / 2, rand, centerOffset).toStream(false));
            sphereList.add(new SphereInfo(r, centerOffset));
        }
        Set<BlockPos> shellBlocksSet = sphereBlocks.map(BlockPos::toImmutable).collect(Collectors.toSet());
        Set<BlockPos> hollowBlocksSet = hollowBlocks.map(BlockPos::toImmutable).collect(Collectors.toSet());
        shellBlocksSet.removeAll(hollowBlocksSet);

        //setBlocks
        this.createShell(worldIn, rand, shellBlocksSet);
        //removeBlocks
        this.hollowOut(worldIn, hollowBlocksSet);
        //decorate
        this.decorateCave(worldIn, rand, hollowBlocksSet, sphereList, center);
        sphereList.clear();
    }

    public void createShell(WorldAccess worldIn, Random rand, Set<BlockPos> positions) {
        List<Block> rareOres = this.getBlockList(IafBlockTags.DRAGON_CAVE_RARE_ORES);
        List<Block> uncommonOres = this.getBlockList(IafBlockTags.DRAGON_CAVE_UNCOMMON_ORES);
        List<Block> commonOres = this.getBlockList(IafBlockTags.DRAGON_CAVE_COMMON_ORES);
        List<Block> dragonTypeOres = this.getBlockList(this.dragonTypeOreTag);

        positions.forEach(blockPos -> {
            if (!(worldIn.getBlockState(blockPos).getBlock() instanceof BlockWithEntity) && worldIn.getBlockState(blockPos).getHardness(worldIn, blockPos) >= 0) {
                boolean doOres = rand.nextInt(IafConfig.getInstance().oreToStoneRatioForDragonCaves + 1) == 0;

                if (doOres) {
                    Block toPlace = null;

                    if (rand.nextBoolean()) {
                        toPlace = !dragonTypeOres.isEmpty() ? dragonTypeOres.get(rand.nextInt(dragonTypeOres.size())) : null;
                    } else {
                        double chance = rand.nextDouble();

                        if (!rareOres.isEmpty() && chance <= 0.15) {
                            toPlace = rareOres.get(rand.nextInt(rareOres.size()));
                        } else if (!uncommonOres.isEmpty() && chance <= 0.45) {
                            toPlace = uncommonOres.get(rand.nextInt(uncommonOres.size()));
                        } else if (!commonOres.isEmpty()) {
                            toPlace = commonOres.get(rand.nextInt(commonOres.size()));
                        }
                    }

                    if (toPlace != null) {
                        worldIn.setBlockState(blockPos, toPlace.getDefaultState(), Block.NOTIFY_LISTENERS);
                    } else {
                        worldIn.setBlockState(blockPos, rand.nextBoolean() ? this.PALETTE_BLOCK1 : this.PALETTE_BLOCK2, Block.NOTIFY_LISTENERS);
                    }
                } else {
                    worldIn.setBlockState(blockPos, rand.nextBoolean() ? this.PALETTE_BLOCK1 : this.PALETTE_BLOCK2, Block.NOTIFY_LISTENERS);
                }
            }
        });
    }

    private List<Block> getBlockList(final TagKey<Block> tagKey) {
        return TagHelper.getContents(Registries.BLOCK, tagKey);
    }

    public void hollowOut(WorldAccess worldIn, Set<BlockPos> positions) {
        positions.forEach(blockPos -> {
            if (!(worldIn.getBlockState(blockPos).getBlock() instanceof BlockWithEntity)) {
                worldIn.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
            }
        });
    }

    public void decorateCave(WorldAccess worldIn, Random rand, Set<BlockPos> positions, List<SphereInfo> spheres, BlockPos center) {
        for (SphereInfo sphere : spheres) {
            BlockPos pos = sphere.pos;
            int radius = sphere.radius;

            for (int i = 0; i < 15 + rand.nextInt(10); i++) {
                this.CEILING_DECO.generate(worldIn, rand, pos.up(radius / 2 - 1).add(rand.nextInt(radius) - radius / 2, 0, rand.nextInt(radius) - radius / 2));
            }
        }

        positions.forEach(blockPos -> {
            if (blockPos.getY() < center.getY()) {
                BlockState stateBelow = worldIn.getBlockState(blockPos.down());

                if ((stateBelow.isIn(BlockTags.BASE_STONE_OVERWORLD) || stateBelow.isIn(IafBlockTags.DRAGON_ENVIRONMENT_BLOCKS)) && worldIn.getBlockState(blockPos).isAir()) {
                    this.setGoldPile(worldIn, blockPos, rand);
                }
            }
        });
    }

    public void setGoldPile(WorldAccess world, BlockPos pos, Random rand) {
        if (!(world.getBlockState(pos).getBlock() instanceof BlockWithEntity)) {
            int chance = rand.nextInt(99) + 1;
            if (chance < 60) {
                int goldRand = Math.max(1, IafConfig.getInstance().dragonDenGoldAmount) * (this.isMale ? 1 : 2);
                boolean generateGold = rand.nextInt(goldRand) == 0;
                world.setBlockState(pos, generateGold ? this.TREASURE_PILE.with(BlockGoldPile.LAYERS, 1 + rand.nextInt(7)) : Blocks.AIR.getDefaultState(), 3);
            } else if (chance == 61) {
                world.setBlockState(pos, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, HORIZONTALS[rand.nextInt(3)]), Block.NOTIFY_LISTENERS);

                if (world.getBlockState(pos).getBlock() instanceof ChestBlock) {
                    BlockEntity blockEntity = world.getBlockEntity(pos);

                    if (blockEntity instanceof ChestBlockEntity chestBlockEntity) {
                        chestBlockEntity.setLootTable(this.isMale ? this.DRAGON_MALE_CHEST : this.DRAGON_CHEST, rand.nextLong());
                    }
                }
            }
        }
    }

    private EntityDragonBase createDragon(final StructureWorldAccess worldGen, final Random random, final BlockPos position, int dragonAge) {
        EntityDragonBase dragon = this.getDragonType().create(worldGen.toServerWorld());
        assert dragon != null;
        dragon.setGender(this.isMale);
        dragon.growDragon(dragonAge);
        dragon.setAgingDisabled(true);
        dragon.setHealth(dragon.getMaxHealth());
        dragon.setVariant(random.nextInt(4));
        dragon.updatePositionAndAngles(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, random.nextFloat() * 360, 0);
        dragon.setInSittingPose(true);
        dragon.homePos = new HomePosition(position, worldGen.toServerWorld());
        dragon.setHunger(50);
        return dragon;
    }

    public abstract EntityType<? extends EntityDragonBase> getDragonType();

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.UNDERGROUND;
    }

    @Override
    public String getId() {
        return "dragon_cave";
    }

    private record SphereInfo(int radius, BlockPos pos) {
    }
}
