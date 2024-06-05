package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.serialization.Codec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class WorldGenDreadRuin extends Feature<DefaultFeatureConfig> {
    private static final Identifier STRUCTURE_0 = new Identifier(IceAndFire.MOD_ID, "dread_ruin_0");
    private static final Identifier STRUCTURE_1 = new Identifier(IceAndFire.MOD_ID, "dread_ruin_1");
    private static final Identifier STRUCTURE_2 = new Identifier(IceAndFire.MOD_ID, "dread_ruin_2");
    private static final Identifier STRUCTURE_3 = new Identifier(IceAndFire.MOD_ID, "dread_ruin_3");
    private static final Identifier STRUCTURE_4 = new Identifier(IceAndFire.MOD_ID, "dread_ruin_4");
    private static final Identifier STRUCTURE_5 = new Identifier(IceAndFire.MOD_ID, "dread_ruin_5");
    private static final Identifier STRUCTURE_6 = new Identifier(IceAndFire.MOD_ID, "dread_ruin_6");
    private static final Identifier STRUCTURE_7 = new Identifier(IceAndFire.MOD_ID, "dread_ruin_7");
    private static final Identifier STRUCTURE_8 = new Identifier(IceAndFire.MOD_ID, "dread_ruin_8");
    private static final Identifier STRUCTURE_9 = new Identifier(IceAndFire.MOD_ID, "dread_ruin_9");
    private static final Identifier STRUCTURE_10 = new Identifier(IceAndFire.MOD_ID, "dread_ruin_10");
    private static final Identifier STRUCTURE_11 = new Identifier(IceAndFire.MOD_ID, "dread_ruin_11");
    private static final Identifier STRUCTURE_12 = new Identifier(IceAndFire.MOD_ID, "dread_ruin_12");
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    public WorldGenDreadRuin(Codec<DefaultFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    public static BlockRotation getRotationFromFacing(Direction facing) {
        return switch (facing) {
            case EAST -> BlockRotation.CLOCKWISE_90;
            case SOUTH -> BlockRotation.CLOCKWISE_180;
            case WEST -> BlockRotation.COUNTERCLOCKWISE_90;
            default -> BlockRotation.NONE;
        };
    }


    private Identifier getRandomStructure(Random rand) {
        return switch (rand.nextInt(11)) {
            case 1 -> STRUCTURE_1;
            case 2 -> STRUCTURE_2;
            case 3 -> STRUCTURE_3;
            case 4 -> STRUCTURE_4;
            case 5 -> STRUCTURE_5;
            case 6 -> STRUCTURE_6;
            case 7 -> STRUCTURE_7;
            case 8 -> STRUCTURE_8;
            case 9 -> STRUCTURE_9;
            case 10 -> STRUCTURE_10;
            case 11 -> STRUCTURE_11;
            case 12 -> STRUCTURE_12;
            default -> STRUCTURE_0;
        };
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess worldIn = context.getWorld();
        Random rand = context.getRandom();
        BlockPos position = context.getOrigin();
        Identifier structure = this.getRandomStructure(rand);
        Direction facing = HORIZONTALS[rand.nextInt(3)];
        MinecraftServer server = worldIn.toServerWorld().getServer();
        Biome biome = worldIn.getBiome(position).value();
        /*TemplateManager templateManager = server.getWorld(worldIn.getDimensionType()).getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(getRotationFromFacing(facing)).addProcessor(new DreadRuinProcessor());
        Template template = templateManager.getTemplate(structure);
        BlockPos genPos = position.offset(facing, template.getSize().getZ() / 2).offset(facing.rotateYCCW(), template.getSize().getX() / 2);
        template.addBlocksToWorld(worldIn, genPos, settings, 2);

         */
        return false;
    }
}