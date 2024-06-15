package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import static com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry.PIXIE_JAR;

public class BlockJar extends BlockWithEntity {
    protected static final VoxelShape AABB = Block.createCuboidShape(3, 0, 3, 13, 16, 13);
    private final boolean empty;
    private final int pixieType;

    public BlockJar(int pixieType) {
        super(pixieType != -1 ? Settings.create().mapColor(MapColor.CLEAR).instrument(Instrument.HAT).nonOpaque().dynamicBounds().strength(1, 2).sounds(BlockSoundGroup.GLASS).luminance((state) -> 10).dropsLike(IafBlockRegistry.JAR_EMPTY.get()) : Settings.create().mapColor(MapColor.CLEAR).instrument(Instrument.HAT).nonOpaque().dynamicBounds().strength(1, 2).sounds(BlockSoundGroup.GLASS));
        this.empty = pixieType == -1;
        this.pixieType = pixieType;
    }

    static String name(int pixieType) {
        if (pixieType == -1) return "pixie_jar_empty";
        return "pixie_jar_%d".formatted(pixieType);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
        return AABB;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
        return AABB;
    }


    @Override
    public void onStateReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        this.dropPixie(worldIn, pos);
        super.onStateReplaced(state, worldIn, pos, newState, isMoving);
    }

    public void dropPixie(World world, BlockPos pos) {
        if (world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof TileEntityJar jar && jar.hasPixie)
            jar.releasePixie();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockHitResult resultIn) {
        if (!this.empty && world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof TileEntityJar jar && jar.hasPixie && jar.hasProduced) {
            jar.hasProduced = false;
            ItemEntity item = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(IafItemRegistry.PIXIE_DUST.get()));
            if (!world.isClient)
                world.spawnEntity(item);
            world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5, IafSoundRegistry.PIXIE_HURT, SoundCategory.NEUTRAL, 1, 1, false);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }


    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (world.getBlockEntity(pos) instanceof TileEntityJar jar) {
            if (!this.empty) {
                jar.hasPixie = true;
                jar.pixieType = this.pixieType;
            } else
                jar.hasPixie = false;
            jar.markDirty();
        }
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState state, BlockEntityType<T> entityType) {
        return checkType(entityType, PIXIE_JAR.get(), TileEntityJar::tick);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityJar(pos, state, this.empty);
    }
}
