package com.iafenvoy.iceandfire.item.block;

import com.iafenvoy.iceandfire.entity.block.BlockEntityDreadPortal;
import com.iafenvoy.iceandfire.entity.util.dragon.DragonUtils;
import com.iafenvoy.iceandfire.item.block.util.IDreadBlock;
import com.iafenvoy.iceandfire.registry.IafBlockEntities;
import com.iafenvoy.iceandfire.registry.IafParticles;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BlockDreadPortal extends BlockWithEntity implements IDreadBlock {

    public BlockDreadPortal() {
        super(Settings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.BLOCK).nonOpaque().dynamicBounds().strength(-1, 100000).luminance((state) -> 1).ticksRandomly());
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
//        if (!entity.getWorld().getRegistryKey().getValue().equals(new Identifier(IceAndFire.MOD_ID, "dread_land"))) {
//            MiscEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, MiscEntityProperties.class);
//            if (properties != null) {
//                properties.lastEnteredDreadPortalX = pos.getX();
//                properties.lastEnteredDreadPortalY = pos.getY();
//                properties.lastEnteredDreadPortalZ = pos.getZ();
//            }
//        }
//        if ((!entity.isBeingRidden()) && (entity.getPassengers().isEmpty()) && (entity instanceof PlayerEntityMP)) {
//            CriteriaTriggers.ENTER_BLOCK.trigger((PlayerEntityMP) entity, world.getBlockState(pos));
//            PlayerEntityMP thePlayer = (PlayerEntityMP) entity;
//            if (thePlayer.timeUntilPortal > 0) {
//                thePlayer.timeUntilPortal = 10;
//            } else if (thePlayer.dimension != IafConfig.dreadlandsDimensionId) {
//                thePlayer.timeUntilPortal = 10;
//                thePlayer.changeDimension(IafConfig.dreadlandsDimensionId, new TeleporterDreadLands(thePlayer.server.getWorld(IafConfig.dreadlandsDimensionId), false));
//            } else {
//                MiscEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(thePlayer, MiscEntityProperties.class);
//                BlockPos setPos = BlockPos.ORIGIN;
//                if (properties != null) {
//                    setPos = new BlockPos(properties.lastEnteredDreadPortalX, properties.lastEnteredDreadPortalY, properties.lastEnteredDreadPortalZ);
//                }
//                thePlayer.timeUntilPortal = 10;
//                thePlayer.changeDimension(0, new TeleporterDreadLands(thePlayer.server.getWorld(0), true));
//                thePlayer.setPositionAndRotation(setPos.getX(), setPos.getY() + 0.5D, setPos.getZ(), 0, 0);
//
//            }
//        }
    }

    public void updateTick(World world, BlockPos pos, BlockState state, Random rand) {
        if (!this.canSurviveAt(world, pos))
            world.breakBlock(pos, true);
    }

    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (!this.canSurviveAt(world, pos))
            world.breakBlock(pos, true);
    }

    public boolean canSurviveAt(World world, BlockPos pos) {
        return DragonUtils.isDreadBlock(world.getBlockState(pos.up())) && DragonUtils.isDreadBlock(world.getBlockState(pos.down()));
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
        BlockEntity tileentity = world.getBlockEntity(pos);

        if (tileentity instanceof BlockEntityDreadPortal) {
            int i = 3;
            for (int j = 0; j < i; ++j) {
                double d0 = (float) pos.getX() + rand.nextFloat();
                double d1 = (float) pos.getY() + rand.nextFloat();
                double d2 = (float) pos.getZ() + rand.nextFloat();
                double d3 = ((double) rand.nextFloat() - 0.5D) * 0.25D;
                double d4 = ((double) rand.nextFloat()) * -0.25D;
                double d5 = ((double) rand.nextFloat() - 0.5D) * 0.25D;
                int k = rand.nextInt(2) * 2 - 1;
                world.addParticle(IafParticles.DREAD_PORTAL, d0, d1, d2, d3, d4, d5);
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0, 0, 0, 0, 0, 0);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> entityType) {
        return checkType(entityType, IafBlockEntities.DREAD_PORTAL, BlockEntityDreadPortal::tick);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityDreadPortal(pos, state);
    }
}