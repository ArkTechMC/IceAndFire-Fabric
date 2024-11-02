package com.iafenvoy.iceandfire.world.structure;

import com.google.common.collect.Lists;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.data.DragonColor;
import com.iafenvoy.iceandfire.data.DragonType;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.entity.util.HomePosition;
import com.iafenvoy.iceandfire.item.block.BlockGoldPile;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.registry.IafStructurePieces;
import com.iafenvoy.iceandfire.registry.IafStructureTypes;
import com.iafenvoy.iceandfire.registry.tag.IafBlockTags;
import com.iafenvoy.iceandfire.world.gen.WorldGenCaveStalactites;
import com.iafenvoy.iceandfire.world.gen.WorldGenDragonCave;
import com.iafenvoy.uranus.util.ShapeBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DragonCaveStructure extends Structure {
    public static final Codec<DragonCaveStructure> ENTRY_CODEC = RecordCodecBuilder.<DragonCaveStructure>mapCodec(instance ->
            instance.group(configCodecBuilder(instance)).apply(instance, DragonCaveStructure::new)).codec();
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    protected boolean male;

    protected DragonCaveStructure(Config config) {
        super(config);
    }

    @Override
    protected Optional<StructurePosition> getStructurePosition(Context context) {
        this.male = context.random().nextBoolean();
        BlockRotation blockRotation = BlockRotation.random(context.random());
        BlockPos blockPos = this.getShiftedPos(context, blockRotation);
        return blockPos.getY() >= 60 ? Optional.empty() : Optional.of(new Structure.StructurePosition(blockPos, (collector) -> {
            this.addPieces(collector, blockPos, blockRotation, context);
        }));
    }

    private void addPieces(StructurePiecesCollector collector, BlockPos pos, BlockRotation rotation, Structure.Context context) {
        List<StructurePiece> list = Lists.newArrayList();
        int r = context.random().nextInt(30);
        long seed = context.random().nextLong();
        for (int i = -2; i <= 2; i++)
            for (int j = -2; j <= 2; j++)
                list.add(new DragonCavePiece(0, new BlockBox(pos.getX() + i * 16, pos.getY(), pos.getZ() + j * 16,
                        pos.getX() + i * 16, pos.getY(), pos.getZ() + j * 16), this.male, new BlockPos(i * 16, 0, j * 16), 20 - r, seed));
        Objects.requireNonNull(collector);
        list.forEach(collector::addPiece);
    }

    @Override
    public StructureType<?> getType() {
        return IafStructureTypes.DRAGON_CAVE;
    }

    public static class DragonCavePiece extends StructurePiece {
        public static final Identifier DRAGON_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/fire_dragon_female_cave");
        public static final Identifier DRAGON_MALE_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/fire_dragon_male_cave");
        public static final BlockState PALETTE_BLOCK1 = IafBlocks.CHARRED_STONE.getDefaultState();
        public static final BlockState PALETTE_BLOCK2 = IafBlocks.CHARRED_COBBLESTONE.getDefaultState();
        public static final WorldGenCaveStalactites CEILING_DECO = new WorldGenCaveStalactites(IafBlocks.CHARRED_STONE, 3);
        public static final BlockState TREASURE_PILE = IafBlocks.GOLD_PILE.getDefaultState();
        private final boolean male;
        private final BlockPos offset;
        private final int y;
        private final long seed;

        protected DragonCavePiece(int length, BlockBox boundingBox, boolean male, BlockPos offset, int y, long seed) {
            super(IafStructurePieces.DRAGON_CAVE, length, boundingBox);
            this.male = male;
            this.offset = offset;
            this.y = y;
            this.seed = seed;
        }

        public DragonCavePiece(StructureContext structureContext, NbtCompound nbt) {
            super(IafStructurePieces.DRAGON_CAVE, nbt);
            this.male = nbt.getBoolean("male");
            this.offset = BlockPos.fromLong(nbt.getLong("offset"));
            this.y = nbt.getInt("down");
            this.seed = nbt.getLong("seed");
        }

        @Override
        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            nbt.putBoolean("male", this.male);
            nbt.putLong("offset", this.offset.asLong());
            nbt.putInt("down", this.y);
            nbt.putLong("seed", this.seed);
        }

        @Override
        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
//            BlockPos position = pivot.add(this.offset);
//            if (random.nextDouble() >= IafCommonConfig.INSTANCE.dragon.generateDenChance.getValue() || !IafFeatures.isFarEnoughFromSpawn(world, position) || !IafFeatures.isFarEnoughFromDangerousGen(world, position, this.getId(), this.getFeatureType()))
//                return;

            int j = 40;
            // Update the position, so it doesn't go above the ocean floor
//            for (int k = 0; k < 20; ++k)
//                for (int l = 0; l < 20; ++l)
//                    j = Math.min(j, world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, position.getX() + k, position.getZ() + l));

            // Offset the position randomly
            random = new CheckedRandom(this.seed);
            j -= 20;
            j -= this.y;

            // If the cave generation point is too low
            if (j < world.getBottomY() + 20)
                return;
            // Center the position at the "middle" of the chunk
            BlockPos position = new BlockPos((chunkPos.x << 4) + 8, this.y, (chunkPos.z << 4) + 8).subtract(this.offset);
            int dragonAge = 75 + random.nextInt(50);
            int radius = (int) (dragonAge * 0.2F) + random.nextInt(4);
            this.generateCave(world, radius, 3, position, random);
            if (this.offset.equals(new BlockPos(0, 0, 0))) {
                EntityDragonBase dragon = this.createDragon(world, random, position, dragonAge);
                world.spawnEntity(dragon);
            }
        }

        private boolean isIn(ChunkPos chunkPos, BlockPos blockPos) {
            return chunkPos.getStartX() <= blockPos.getX() && blockPos.getX() <= chunkPos.getEndX() &&
                    chunkPos.getStartZ() <= blockPos.getZ() && blockPos.getZ() <= chunkPos.getEndZ();
        }

        public void generateCave(WorldAccess worldIn, int radius, int amount, BlockPos center, Random rand) {
            List<WorldGenDragonCave.SphereInfo> sphereList = new ArrayList<>();
            sphereList.add(new WorldGenDragonCave.SphereInfo(radius, center.toImmutable()));
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
                sphereList.add(new WorldGenDragonCave.SphereInfo(r, centerOffset));
            }
            Set<BlockPos> shellBlocksSet = sphereBlocks.map(BlockPos::toImmutable).collect(Collectors.toSet());
            Set<BlockPos> hollowBlocksSet = hollowBlocks.map(BlockPos::toImmutable).collect(Collectors.toSet());
            shellBlocksSet.removeAll(hollowBlocksSet);

            ChunkPos chunkPos = new ChunkPos(center.add(this.offset));
            shellBlocksSet.removeIf(x -> !this.isIn(chunkPos, x));
            hollowBlocksSet.removeIf(x -> !this.isIn(chunkPos, x));

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
            List<Block> dragonTypeOres = this.getBlockList(IafBlockTags.FIRE_DRAGON_CAVE_ORES);//TODO
            positions.forEach(blockPos -> {
                if (!(worldIn.getBlockState(blockPos).getBlock() instanceof BlockWithEntity) && worldIn.getBlockState(blockPos).getHardness(worldIn, blockPos) >= 0) {
                    boolean doOres = rand.nextDouble() < IafCommonConfig.INSTANCE.dragon.generateOreRatio.getValue();
                    if (doOres) {
                        Block toPlace = null;
                        if (rand.nextBoolean())
                            toPlace = !dragonTypeOres.isEmpty() ? dragonTypeOres.get(rand.nextInt(dragonTypeOres.size())) : null;
                        else {
                            double chance = rand.nextDouble();
                            if (!rareOres.isEmpty() && chance <= 0.15)
                                toPlace = rareOres.get(rand.nextInt(rareOres.size()));
                            else if (!uncommonOres.isEmpty() && chance <= 0.45)
                                toPlace = uncommonOres.get(rand.nextInt(uncommonOres.size()));
                            else if (!commonOres.isEmpty())
                                toPlace = commonOres.get(rand.nextInt(commonOres.size()));
                        }
                        if (toPlace != null)
                            worldIn.setBlockState(blockPos, toPlace.getDefaultState(), Block.NOTIFY_LISTENERS);
                        else
                            worldIn.setBlockState(blockPos, rand.nextBoolean() ? PALETTE_BLOCK1 : PALETTE_BLOCK2, Block.NOTIFY_LISTENERS);
                    } else
                        worldIn.setBlockState(blockPos, rand.nextBoolean() ? PALETTE_BLOCK1 : PALETTE_BLOCK2, Block.NOTIFY_LISTENERS);
                }
            });
        }

        private List<Block> getBlockList(final TagKey<Block> tagKey) {
            return Registries.BLOCK.getEntryList(tagKey).map(holders -> holders.stream().map(RegistryEntry::value).toList()).orElse(Collections.emptyList());
        }

        public void hollowOut(WorldAccess worldIn, Set<BlockPos> positions) {
            positions.forEach(blockPos -> {
                if (!(worldIn.getBlockState(blockPos).getBlock() instanceof BlockWithEntity))
                    worldIn.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
            });
        }

        public void decorateCave(WorldAccess worldIn, Random rand, Set<BlockPos> positions, List<WorldGenDragonCave.SphereInfo> spheres, BlockPos center) {
            for (WorldGenDragonCave.SphereInfo sphere : spheres) {
                BlockPos pos = sphere.pos();
                int radius = sphere.radius();
                for (int i = 0; i < 15 + rand.nextInt(10); i++)
                    CEILING_DECO.generate(worldIn, rand, pos.up(radius / 2 - 1).add(rand.nextInt(radius) - radius / 2, 0, rand.nextInt(radius) - radius / 2));
            }

            positions.forEach(blockPos -> {
                if (blockPos.getY() < center.getY()) {
                    BlockState stateBelow = worldIn.getBlockState(blockPos.down());
                    if ((stateBelow.isIn(BlockTags.BASE_STONE_OVERWORLD) || stateBelow.isIn(IafBlockTags.DRAGON_ENVIRONMENT_BLOCKS)) && worldIn.getBlockState(blockPos).isAir())
                        this.setGoldPile(worldIn, blockPos, rand);
                }
            });
        }

        public void setGoldPile(WorldAccess world, BlockPos pos, Random rand) {
            if (!(world.getBlockState(pos).getBlock() instanceof BlockWithEntity)) {
                int chance = rand.nextInt(99) + 1;
                if (chance < 60) {
                    boolean generateGold = rand.nextDouble() < IafCommonConfig.INSTANCE.dragon.generateDenGoldChance.getValue() * (this.male ? 1 : 2);
                    world.setBlockState(pos, generateGold ? TREASURE_PILE.with(BlockGoldPile.LAYERS, 1 + rand.nextInt(7)) : Blocks.AIR.getDefaultState(), 3);
                } else if (chance == 61) {
                    world.setBlockState(pos, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, HORIZONTALS[rand.nextInt(3)]), Block.NOTIFY_LISTENERS);
                    if (world.getBlockState(pos).getBlock() instanceof ChestBlock) {
                        BlockEntity blockEntity = world.getBlockEntity(pos);
                        if (blockEntity instanceof ChestBlockEntity chestBlockEntity)
                            chestBlockEntity.setLootTable(this.male ? DRAGON_MALE_CHEST : DRAGON_CHEST, rand.nextLong());
                    }
                }
            }
        }

        protected EntityType<? extends EntityDragonBase> getDragonType() {
            return IafEntities.FIRE_DRAGON;
        }

        private EntityDragonBase createDragon(final StructureWorldAccess worldGen, final Random random, final BlockPos position, int dragonAge) {
            EntityDragonBase dragon = this.getDragonType().create(worldGen.toServerWorld());
            assert dragon != null;
            dragon.setGender(this.male);
            dragon.growDragon(dragonAge);
            dragon.setAgingDisabled(true);
            dragon.setHealth(dragon.getMaxHealth());
            List<DragonColor> colors = DragonColor.getColorsByType(DragonType.getTypeByEntityType(this.getDragonType()));
            dragon.setVariant(colors.get(random.nextInt(colors.size())).name());
            dragon.updatePositionAndAngles(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, random.nextFloat() * 360, 0);
            dragon.setInSittingPose(true);
            dragon.homePos = new HomePosition(position, worldGen.toServerWorld());
            dragon.setHunger(50);
            return dragon;
        }
    }
}
