package com.iafenvoy.iceandfire.world.gen;

import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.entity.*;
import com.iafenvoy.iceandfire.entity.util.MyrmexHive;
import com.iafenvoy.iceandfire.item.block.BlockMyrmexBiolight;
import com.iafenvoy.iceandfire.item.block.BlockMyrmexConnectedResin;
import com.iafenvoy.iceandfire.item.block.BlockMyrmexResin;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.registry.IafFeatures;
import com.iafenvoy.iceandfire.world.IafWorldData;
import com.iafenvoy.iceandfire.world.MyrmexWorldData;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WorldGenMyrmexHive extends Feature<DefaultFeatureConfig> implements TypedFeature {

    private static final BlockState DESERT_RESIN = IafBlocks.MYRMEX_DESERT_RESIN.getDefaultState();
    private static final BlockState STICKY_DESERT_RESIN = IafBlocks.MYRMEX_DESERT_RESIN_STICKY.getDefaultState();
    private static final BlockState JUNGLE_RESIN = IafBlocks.MYRMEX_JUNGLE_RESIN.getDefaultState();
    private static final BlockState STICKY_JUNGLE_RESIN = IafBlocks.MYRMEX_JUNGLE_RESIN_STICKY.getDefaultState();
    private final boolean jungle;
    public MyrmexHive hive;
    private int entrances = 0;
    private int totalRooms;
    private boolean hasFoodRoom;
    private boolean hasNursery;
    private boolean small;
    private BlockPos centerOfHive;

    public WorldGenMyrmexHive(boolean small, boolean jungle, Codec<DefaultFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
        this.small = small;
        this.jungle = jungle;
    }

    public void placeSmallGen(StructureWorldAccess worldIn, Random rand, BlockPos pos) {
        this.hasFoodRoom = false;
        this.hasNursery = false;
        this.totalRooms = 0;
        this.entrances = 0;
        this.centerOfHive = pos;
        this.generateMainRoom(worldIn, rand, pos);
        this.small = false;
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess worldIn = context.getWorld();
        Random rand = context.getRandom();
        BlockPos pos = context.getOrigin();
        if (!this.small) {
            if (rand.nextDouble() < IafCommonConfig.INSTANCE.myrmex.colonyGenChance.getDoubleValue() || !IafFeatures.isFarEnoughFromSpawn(worldIn, pos) || !IafFeatures.isFarEnoughFromDangerousGen(worldIn, pos, this.getId())) {
                return false;
            }
            if (MyrmexWorldData.get(worldIn.toServerWorld()) != null && MyrmexWorldData.get(worldIn.toServerWorld()).getNearestHive(pos, 200) != null) {
                return false;
            }
        }
        if (!this.small && !worldIn.getFluidState(pos.down()).isEmpty()) {
            return false;
        }
        this.hasFoodRoom = false;
        this.hasNursery = false;
        this.totalRooms = 0;
        int down = Math.max(15, pos.getY() - 20 + rand.nextInt(10));
        BlockPos undergroundPos = new BlockPos(pos.getX(), down, pos.getZ());
        this.entrances = 0;
        this.centerOfHive = undergroundPos;
        this.generateMainRoom(worldIn, rand, undergroundPos);
        this.small = false;
        return true;
    }

    private void generateMainRoom(ServerWorldAccess world, Random rand, BlockPos position) {
        this.hive = new MyrmexHive(world.toServerWorld(), position, 100);
        MyrmexWorldData.addHive(world.toServerWorld(), this.hive);
        BlockState resin = this.jungle ? JUNGLE_RESIN : DESERT_RESIN;
        BlockState sticky_resin = this.jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        this.generateSphere(world, rand, position, 14, 7, resin, sticky_resin);
        this.generateSphere(world, rand, position, 12, 5, Blocks.AIR.getDefaultState());
        this.decorateSphere(world, rand, position, 12, 5, RoomType.QUEEN);
        this.generatePath(world, rand, position.offset(Direction.NORTH, 9).down(), 15 + rand.nextInt(10), Direction.NORTH, 100);
        this.generatePath(world, rand, position.offset(Direction.SOUTH, 9).down(), 15 + rand.nextInt(10), Direction.SOUTH, 100);
        this.generatePath(world, rand, position.offset(Direction.WEST, 9).down(), 15 + rand.nextInt(10), Direction.WEST, 100);
        this.generatePath(world, rand, position.offset(Direction.EAST, 9).down(), 15 + rand.nextInt(10), Direction.EAST, 100);
        if (!this.small) {
            EntityMyrmexQueen queen = new EntityMyrmexQueen(IafEntities.MYRMEX_QUEEN, world.toServerWorld());
            BlockPos ground = MyrmexHive.getGroundedPos(world, position);
            queen.initialize(world, world.getLocalDifficulty(ground), SpawnReason.CHUNK_GENERATION, null, null);
            queen.setHive(this.hive);
            queen.setJungleVariant(this.jungle);
            queen.updatePositionAndAngles(ground.getX() + 0.5D, ground.getY() + 1D, ground.getZ() + 0.5D, 0, 0);
            world.spawnEntity(queen);

            for (int i = 0; i < 4 + rand.nextInt(3); i++) {
                EntityMyrmexBase myrmex = new EntityMyrmexWorker(IafEntities.MYRMEX_WORKER,
                        world.toServerWorld());
                myrmex.initialize(world, world.getLocalDifficulty(ground), SpawnReason.CHUNK_GENERATION, null, null);
                myrmex.setHive(this.hive);
                myrmex.updatePositionAndAngles(ground.getX() + 0.5D, ground.getY() + 1D, ground.getZ() + 0.5D, 0, 0);
                myrmex.setJungleVariant(this.jungle);
                world.spawnEntity(myrmex);
            }
            for (int i = 0; i < 2 + rand.nextInt(2); i++) {
                EntityMyrmexBase myrmex = new EntityMyrmexSoldier(IafEntities.MYRMEX_SOLDIER,
                        world.toServerWorld());
                myrmex.initialize(world, world.getLocalDifficulty(ground), SpawnReason.CHUNK_GENERATION, null, null);
                myrmex.setHive(this.hive);
                myrmex.updatePositionAndAngles(ground.getX() + 0.5D, ground.getY() + 1D, ground.getZ() + 0.5D, 0, 0);
                myrmex.setJungleVariant(this.jungle);
                world.spawnEntity(myrmex);
            }
            for (int i = 0; i < rand.nextInt(2); i++) {
                EntityMyrmexBase myrmex = new EntityMyrmexSentinel(IafEntities.MYRMEX_SENTINEL,
                        world.toServerWorld());
                myrmex.initialize(world, world.getLocalDifficulty(ground), SpawnReason.CHUNK_GENERATION, null, null);
                myrmex.setHive(this.hive);
                myrmex.updatePositionAndAngles(ground.getX() + 0.5D, ground.getY() + 1D, ground.getZ() + 0.5D, 0, 0);
                myrmex.setJungleVariant(this.jungle);
                world.spawnEntity(myrmex);
            }
        }
    }

    private void generatePath(WorldAccess world, Random rand, BlockPos offset, int length, Direction direction, int roomChance) {
        if (roomChance == 0) {
            return;
        }
        if (this.small) {
            length /= 2;
            if (this.entrances < 1) {
                for (int i = 0; i < length; i++) {
                    this.generateCircle(world, rand, offset.offset(direction, i), direction);
                }
                this.generateEntrance(world, rand, offset.offset(direction, length), direction);
            } else if (this.totalRooms < 2) {
                for (int i = 0; i < length; i++) {
                    this.generateCircle(world, rand, offset.offset(direction, i), direction);
                }
                this.generateRoom(world, rand, offset.offset(direction, length), 6, roomChance / 2, direction);
                for (int i = -3; i < 3; i++) {
                    this.generateCircleAir(world, rand, offset.offset(direction, i), direction);
                    this.generateCircleAir(world, rand, offset.offset(direction, length + i), direction);
                }
                this.totalRooms++;
            }
        } else {
            if (rand.nextInt(100) < roomChance) {
                if (this.entrances < 3 && rand.nextInt(1 + this.entrances * 2) == 0 && this.hasFoodRoom && this.hasNursery && this.totalRooms > 3 || this.entrances == 0) {
                    this.generateEntrance(world, rand, offset.offset(direction, 1), direction);
                } else {
                    for (int i = 0; i < length; i++) {
                        this.generateCircle(world, rand, offset.offset(direction, i), direction);
                    }
                    for (int i = -3; i < 3; i++) {
                        this.generateCircleAir(world, rand, offset.offset(direction, length + i), direction);
                    }
                    this.totalRooms++;
                    this.generateRoom(world, rand, offset.offset(direction, length), 7, roomChance / 2, direction);
                }
            }
        }
    }

    private void generateRoom(WorldAccess world, Random rand, BlockPos position, int size, int roomChance, Direction direction) {
        BlockState resin = this.jungle ? JUNGLE_RESIN : DESERT_RESIN;
        BlockState sticky_resin = this.jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        RoomType type = RoomType.random(rand);
        if (!this.hasFoodRoom) {
            type = RoomType.FOOD;
            this.hasFoodRoom = true;
        } else if (!this.hasNursery) {
            type = RoomType.NURSERY;
            this.hasNursery = true;
        }
        this.generateSphereRespectResin(world, rand, position, size + 2, 4 + 2, resin, sticky_resin);
        this.generateSphere(world, rand, position, size, 4 - 1, Blocks.AIR.getDefaultState());
        this.decorateSphere(world, rand, position, size, 4 - 1, type);
        this.hive.addRoom(position, type);
        if (!this.small) {
            if (rand.nextInt(3) == 0 && direction.getOpposite() != Direction.NORTH) {
                this.generatePath(world, rand, position.offset(Direction.NORTH, size - 2), 5 + rand.nextInt(20), Direction.NORTH, roomChance);
            }
            if (rand.nextInt(3) == 0 && direction.getOpposite() != Direction.SOUTH) {
                this.generatePath(world, rand, position.offset(Direction.SOUTH, size - 2), 5 + rand.nextInt(20), Direction.SOUTH, roomChance);
            }
            if (rand.nextInt(3) == 0 && direction.getOpposite() != Direction.WEST) {
                this.generatePath(world, rand, position.offset(Direction.WEST, size - 2), 5 + rand.nextInt(20), Direction.WEST, roomChance);
            }
            if (rand.nextInt(3) == 0 && direction.getOpposite() != Direction.EAST) {
                this.generatePath(world, rand, position.offset(Direction.EAST, size - 2), 5 + rand.nextInt(20), Direction.EAST, roomChance);
            }
        }
    }

    private void generateEntrance(WorldAccess world, Random rand, BlockPos position, Direction direction) {
        BlockPos up = position.up();
        this.hive.getEntranceBottoms().put(up, direction);
        while (up.getY() < world.getTopPosition(this.small ? Heightmap.Type.MOTION_BLOCKING_NO_LEAVES : Heightmap.Type.WORLD_SURFACE_WG, up).getY()
                && !world.getBlockState(up).isIn(BlockTags.LOGS)) {
            this.generateCircleRespectSky(world, rand, up, direction);
            up = up.up().offset(direction);
        }
        BlockState resin = this.jungle ? JUNGLE_RESIN : DESERT_RESIN;
        BlockState sticky_resin = this.jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        this.generateSphereRespectAir(world, rand, up, 4 + 4, 4 + 2, resin, sticky_resin);
        this.generateSphere(world, rand, up.up(), 4, 4, Blocks.AIR.getDefaultState());
        this.decorateSphere(world, rand, up.up(), 4, 4 - 1, RoomType.ENTERANCE);
        this.hive.getEntrances().put(up, direction);
        this.entrances++;
    }

    private void generateCircle(WorldAccess world, Random rand, BlockPos position, Direction direction) {
        BlockState resin = this.jungle ? JUNGLE_RESIN : DESERT_RESIN;
        BlockState sticky_resin = this.jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        int radius = 3 + 2;
        {
            for (float i = 0; i < radius; i += 0.5F) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5F) {
                    int x = (int) Math.floor(MathHelper.sin(j) * i);
                    int z = (int) Math.floor(MathHelper.cos(j) * i);
                    if (direction == Direction.WEST || direction == Direction.EAST) {
                        world.setBlockState(position.add(0, x, z), rand.nextInt(3) == 0 ? sticky_resin : resin, 2);

                    } else {
                        world.setBlockState(position.add(x, z, 0), rand.nextInt(3) == 0 ? sticky_resin : resin, 2);
                    }
                }
            }
        }
        radius -= 2;
        {
            for (float i = 0; i < radius; i += 0.5F) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5F) {
                    int x = (int) Math.floor(MathHelper.sin(j) * i * MathHelper.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    int z = (int) Math.floor(MathHelper.cos(j) * i * MathHelper.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    if (direction == Direction.WEST || direction == Direction.EAST) {
                        world.setBlockState(position.add(0, x, z), Blocks.AIR.getDefaultState(), 2);
                    } else {
                        world.setBlockState(position.add(x, z, 0), Blocks.AIR.getDefaultState(), 2);
                    }
                }
            }
        }

        this.decorateCircle(world, rand, position, 3, 5, direction);
    }

    private void generateCircleRespectSky(WorldAccess world, Random rand, BlockPos position, Direction direction) {
        BlockState resin = this.jungle ? JUNGLE_RESIN : DESERT_RESIN;
        BlockState sticky_resin = this.jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        int radius = 4 + 2;
        {
            for (float i = 0; i < radius; i += 0.5F) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5F) {
                    int x = (int) Math.floor(MathHelper.sin(j) * i);
                    int z = (int) Math.floor(MathHelper.cos(j) * i);
                    if (direction == Direction.WEST || direction == Direction.EAST) {
                        if (!world.isSkyVisibleAllowingSea(position.add(0, x, z))) {
                            world.setBlockState(position.add(0, x, z), rand.nextInt(3) == 0 ? sticky_resin : resin, 3);
                        }

                    } else {
                        if (!world.isSkyVisibleAllowingSea(position.add(x, z, 0))) {
                            world.setBlockState(position.add(x, z, 0), rand.nextInt(3) == 0 ? sticky_resin : resin, 3);
                        }
                    }
                }
            }
        }
        radius -= 2;
        {
            for (float i = 0; i < radius; i += 0.5F) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5F) {
                    int x = (int) Math.floor(MathHelper.sin(j) * i * MathHelper.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    int z = (int) Math.floor(MathHelper.cos(j) * i * MathHelper.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    if (direction == Direction.WEST || direction == Direction.EAST) {
                        world.setBlockState(position.add(0, x, z), Blocks.AIR.getDefaultState(), 3);
                    } else {
                        world.setBlockState(position.add(x, z, 0), Blocks.AIR.getDefaultState(), 3);
                    }
                }
            }
        }

        this.decorateCircle(world, rand, position, 4, 4, direction);
    }


    private void generateCircleAir(WorldAccess world, Random rand, BlockPos position, Direction direction) {
        {
            for (float i = 0; i < 3; i += 0.5F) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5F) {
                    int x = (int) Math.floor(MathHelper.sin(j) * i * MathHelper.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    int z = (int) Math.floor(MathHelper.cos(j) * i * MathHelper.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    if (direction == Direction.WEST || direction == Direction.EAST) {
                        world.setBlockState(position.add(0, x, z), Blocks.AIR.getDefaultState(), 2);
                    } else {
                        world.setBlockState(position.add(x, z, 0), Blocks.AIR.getDefaultState(), 2);
                    }
                }
            }
        }

        this.decorateCircle(world, rand, position, 3, 5, direction);
    }

    public void generateSphere(WorldAccess world, Random rand, BlockPos position, int size, int height, BlockState fill) {
        int ySize = rand.nextInt(2);
        int j = size + rand.nextInt(2);
        int k = height + ySize;
        int l = size + rand.nextInt(2);
        float f = (j + k + l) * 0.333F;
        for (BlockPos blockpos : BlockPos.stream(position.add(-j, -k, -l), position.add(j, k, l)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
            if (blockpos.getSquaredDistance(position) <= f * f * MathHelper.clamp(rand.nextFloat(), 0.75F, 1.0F) && !world.isAir(blockpos)) {
                world.setBlockState(blockpos, fill, 3);
            }
        }
    }

    public void generateSphere(WorldAccess world, Random rand, BlockPos position, int size, int height, BlockState fill, BlockState fill2) {
        int ySize = rand.nextInt(2);
        int j = size + rand.nextInt(2);
        int k = height + ySize;
        int l = size + rand.nextInt(2);
        float f = (j + k + l) * 0.333F;
        for (BlockPos blockpos : BlockPos.stream(position.add(-j, -k, -l), position.add(j, k, l)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
            if (blockpos.getSquaredDistance(position) <= f * f * MathHelper.clamp(rand.nextFloat(), 0.75F, 1.0F)) {
                world.setBlockState(blockpos, rand.nextInt(3) == 0 ? fill2 : fill, 2);
            }
        }
    }

    public void generateSphereRespectResin(WorldAccess world, Random rand, BlockPos position, int size, int height, BlockState fill, BlockState fill2) {
        int ySize = rand.nextInt(2);
        int j = size + rand.nextInt(2);
        int k = height + ySize;
        int l = size + rand.nextInt(2);
        float f = (j + k + l) * 0.333F;
        for (BlockPos blockpos : BlockPos.stream(position.add(-j, -k, -l), position.add(j, k, l)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
            if (blockpos.getSquaredDistance(position) <= f * f * MathHelper.clamp(rand.nextFloat(), 0.75F, 1.0F)
                    && (!world.isAir(blockpos) || world.isAir(blockpos) && !this.hasResinUnder(blockpos, world))) {
                world.setBlockState(blockpos, rand.nextInt(3) == 0 ? fill2 : fill, 2);
            }
        }
    }

    public void generateSphereRespectAir(WorldAccess world, Random rand, BlockPos position, int size, int height, BlockState fill, BlockState fill2) {
        int ySize = rand.nextInt(2);
        int j = size + rand.nextInt(2);
        int k = height + ySize;
        int l = size + rand.nextInt(2);
        float f = (j + k + l) * 0.333F;
        for (BlockPos blockpos : BlockPos.stream(position.add(-j, -k, -l), position.add(j, k, l)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
            if (blockpos.getSquaredDistance(position) <= f * f * MathHelper.clamp(rand.nextFloat(), 0.75F, 1.0F)
                    && !world.isAir(blockpos)) {
                world.setBlockState(blockpos, rand.nextInt(3) == 0 ? fill2 : fill, 2);
            }
        }
    }

    private boolean hasResinUnder(BlockPos pos, WorldAccess world) {
        BlockPos copy = pos.down();
        while (world.isAir(copy) && copy.getY() > 1) {
            copy = copy.down();
        }
        return world.getBlockState(copy).getBlock() instanceof BlockMyrmexResin || world.getBlockState(copy).getBlock() instanceof BlockMyrmexConnectedResin;
    }

    private void decorateCircle(WorldAccess world, Random rand, BlockPos position, int size, int height, Direction direction) {
        int radius = size + 2;
        {
            for (float i = 0; i < radius; i += 0.5F) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5F) {
                    int x = (int) Math.floor(MathHelper.sin(j) * i);
                    int z = (int) Math.floor(MathHelper.cos(j) * i);
                    if (direction == Direction.WEST || direction == Direction.EAST) {
                        if (world.isAir(position.add(0, x, z))) {
                            this.decorate(world, position.add(0, x, z), position, size, rand, RoomType.TUNNEL);
                        }
                    } else {
                        if (world.isAir(position.add(x, z, 0))) {
                            this.decorate(world, position.add(x, z, 0), position, size, rand, RoomType.TUNNEL);
                        }
                    }
                    if (world.isAir(position.add(0, x, z))) {
                        this.decorateTubers(world, position.add(0, x, z), rand, RoomType.TUNNEL);
                    }
                }
            }
        }
    }

    private void decorateSphere(WorldAccess world, Random rand, BlockPos position, int size, int height, RoomType roomType) {
        int ySize = rand.nextInt(2);
        int j = size + rand.nextInt(2);
        int k = height + ySize;
        int l = size + rand.nextInt(2);
        float f = (j + k + l) * 0.333F;
        for (BlockPos blockpos : BlockPos.stream(position.add(-j, -k, -l), position.add(j, k + 1, l)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
            if (blockpos.getSquaredDistance(position) <= f * f) {
                if (world.getBlockState(blockpos.down()).isOpaque() && world.isAir(blockpos)) {
                    this.decorate(world, blockpos, position, size, rand, roomType);
                }
                if (world.isAir(blockpos)) {
                    this.decorateTubers(world, blockpos, rand, roomType);
                }
            }
        }
    }

    private void decorate(WorldAccess world, BlockPos blockpos, BlockPos center, int size, Random random, RoomType roomType) {
        switch (roomType) {
            case FOOD -> {
                if (random.nextInt(45) == 0 && world.getBlockState(blockpos.down()).getBlock() instanceof BlockMyrmexResin) {
                    WorldGenMyrmexDecoration.generateSkeleton(world, blockpos, center, size, random);
                }
                if (random.nextInt(13) == 0) {
                    WorldGenMyrmexDecoration.generateLeaves(world, blockpos, center, size, random, this.jungle);
                }
                if (random.nextInt(12) == 0) {
                    WorldGenMyrmexDecoration.generatePumpkins(world, blockpos, center, size, random, this.jungle);
                }
                if (random.nextInt(6) == 0) {
                    WorldGenMyrmexDecoration.generateMushrooms(world, blockpos, center, size, random);
                }
                if (random.nextInt(12) == 0) {
                    WorldGenMyrmexDecoration.generateCocoon(world, blockpos, random, this.jungle, this.jungle ? WorldGenMyrmexDecoration.JUNGLE_MYRMEX_FOOD_CHEST : WorldGenMyrmexDecoration.DESERT_MYRMEX_FOOD_CHEST);
                }
            }
            case SHINY -> {
                if (random.nextInt(12) == 0) {
                    WorldGenMyrmexDecoration.generateGold(world, blockpos, center, size, random);
                }
            }
            case TRASH -> {
                if (random.nextInt(24) == 0) {
                    WorldGenMyrmexDecoration.generateTrashHeap(world, blockpos, center, size, random);
                }
                if (random.nextBoolean()) {
                    WorldGenMyrmexDecoration.generateTrashOre(world, blockpos, center, size, random);
                }
                if (random.nextInt(12) == 0) {
                    WorldGenMyrmexDecoration.generateCocoon(world, blockpos, random, this.jungle, WorldGenMyrmexDecoration.MYRMEX_TRASH_CHEST);
                }
            }
            default -> {
            }
        }

    }

    private void decorateTubers(WorldAccess world, BlockPos blockpos, Random random, RoomType roomType) {
        if (world.getBlockState(blockpos.up()).isOpaque() && random.nextInt(roomType == RoomType.ENTERANCE || roomType == RoomType.TUNNEL ? 20 : 6) == 0) {
            int tuberLength = roomType == RoomType.ENTERANCE || roomType == RoomType.TUNNEL ? 1 : roomType == RoomType.QUEEN ? 1 + random.nextInt(5) : 1 + random.nextInt(3);
            for (int i = 0; i < tuberLength; i++) {
                if (world.isAir(blockpos.down(i))) {
                    boolean connected = i != tuberLength - 1;
                    world.setBlockState(blockpos.down(i), this.jungle ? IafBlocks.MYRMEX_JUNGLE_BIOLIGHT.getDefaultState().with(BlockMyrmexBiolight.CONNECTED_DOWN, connected) : IafBlocks.MYRMEX_DESERT_BIOLIGHT.getDefaultState().with(BlockMyrmexBiolight.CONNECTED_DOWN, connected), 2);
                }
            }
        }
    }

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.SURFACE;
    }

    @Override
    public String getId() {
        return "myrmex_hive";
    }

    public enum RoomType {
        DEFAULT(false),
        TUNNEL(false),
        ENTERANCE(false),
        QUEEN(false),
        FOOD(true),
        EMPTY(true),
        NURSERY(true),
        SHINY(true),
        TRASH(true);
        final boolean random;

        RoomType(boolean random) {
            this.random = random;
        }

        public static RoomType random(Random rand) {
            List<RoomType> list = new ArrayList<>();
            for (RoomType type : RoomType.values()) {
                if (type.random) {
                    list.add(type);
                }
            }
            return list.get(rand.nextInt(list.size()));
        }
    }
}
